package com.cloudwalk.common.base.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cloudwalk.common.common.BaseConstants;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.system.User;

/**
 * ClassName:LoginInteceptor <br/>
 * Description: 登录拦截器 <br/>
 * Date: 2015年4月21日 下午3:55:40 <br/>
 */
public class LoginInteceptor extends HandlerInterceptorAdapter {

	protected final Logger log = Logger.getLogger(this.getClass());	
	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 *
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 *
	 * @throws Exception
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		HttpSession session = request.getSession();
		if (session != null) {
			User user = (User) session.getAttribute(BaseConstants.USER);
			if (null == user) {// 未登录
				this.redirectUrl(request, response);
				return false;
			} else {
				// 验证权限控制
				if ("/".equals(request.getServletPath())) {
					this.redirectPlt(request, response);
					return false;
				} else {
//					return true;
					return this.validateUrlPerm(request,response, user);
				}
			}
		} else {
			// session超时
			this.redirectUrl(request, response);
			return false;
		}
	}

	/**
	 * redirectUrl:重定向页面，解决IFRAME问题 <br/>
	 * @author:焦少平 Date: 2015年6月12日 上午10:46:20
	 */
	private void redirectUrl(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getContextPath();
		response.setContentType("text/html");
		
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.print("<script>window.parent.location.href= '" + path + BaseConstants.TO_LOGIN + "';</script>");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
	
	/**
	 * 跳转进入平台主页面
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void redirectPlt(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getContextPath();
		response.setContentType("text/html");
		
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.print("<script>window.parent.location.href='" + path + "/login/index';</script>");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
	
	/**
	 * 自定义错误页面跳转
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void redirectErrorPageUrl(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String str = request.getRequestURL().toString();
		String[] arr = str.split(request.getContextPath());
		String url = arr[0] + request.getContextPath() + Constants.PAGE_ERROR;
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		pw.print("<script>window.parent.location.href= '" + url + "' "
				+ ";</script>");
	}

	/**
	 *
	 * validateUrlPerm:验证该url是否合法. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月31日 上午9:40:30
	 * @param request
	 * @return true 权限认证通过 false 不通过
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	private boolean validateUrlPerm(HttpServletRequest request,HttpServletResponse response, User user) throws Exception {

		String reqUrlString = request.getRequestURI().toString();
		String ContextPath = request.getContextPath().toString();
		if (ObjectUtils.isEmpty(reqUrlString)) {
			log.info("permfailure，requestUrl为NULL");
			return false;
		}
		
		reqUrlString = reqUrlString.replace(ContextPath, "");
		// 获取无需权限拦截的url
//		String authExcludeUrls = BasePropsUtil
//				.getProperty("base.perm.exclude.urls");
		String authExcludeUrls = Constants.Config.PERM_EXCLUDE_URLS;

		if (authExcludeUrls.indexOf(reqUrlString) != -1) {			
			return true;
		}
		
		// 权限认证
		List<String> authUrllist = user.getAuthUrllist();
		for (String authUrl : authUrllist) {
			if (authUrl.indexOf(reqUrlString) != -1) {
				return true;
			}
		}
		
		log.info("permfailure，requestUrl=="+reqUrlString+"无权限访问");
		redirectErrorPageUrl(request, response);
		return false;
	}
}
