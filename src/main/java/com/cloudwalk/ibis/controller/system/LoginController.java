/**
 * Project Name:ibis
 * File LoginController.java
 * Package Name:com.cloudwalk.ibis.controller.identity
 * Date:2015年08月15日
 * Copyright @ 2015 重庆中科云丛科技有限公司  All Rights Reserved.
 *
 */
package com.cloudwalk.ibis.controller.system;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.common.BaseConstants;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.Md5;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.StringUtil;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.system.LogOperService;
import com.cloudwalk.ibis.service.system.UserService;

/**
 * ClassName:LoginController <br/>
 * Description: 用户登录处理模块. <br/>
 * Date: Date:2015年08月15日 <br/>
 *
 * @author Jackson He
 * @version
 *
 */

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "logOperService")
	private LogOperService logOperService;

	private final static String COOKIE_JSESSIONID_KEY = "JSESSIONID";

	/**
	 *
	 * changePwd:修改密码. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月26日 下午6:57:56
	 * @param request
	 * @param model
	 * @param response
	 * @param user
	 * @throws IOException
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/changePwd")
	public void changePwd(HttpServletRequest request, Model model,
			HttpServletResponse response, User user) {

		//返回结果
		String retJson = "";
		String oldPassword = request.getParameter("oldPassword");
		if (ObjectUtils.isEmpty(oldPassword)) {			
			retJson = ControllerUtil.getFailRetJson("原密码不能为空");
			getPrintWriter(response, retJson);
			return;
		}

		String newPassword = request.getParameter("newPassword");
		if (ObjectUtils.isEmpty(newPassword)) {			
			retJson = ControllerUtil.getFailRetJson("新密码不能为空");
			getPrintWriter(response, retJson);
			return;
		}

		String reNewPassword = request.getParameter("confirmNewPassword");
		if (ObjectUtils.isEmpty(reNewPassword)) {			
			retJson = ControllerUtil.getFailRetJson("确认密码不能为空");
			getPrintWriter(response, retJson);
			return;
		}

		if (!newPassword.equals(reNewPassword)) {			
			retJson = ControllerUtil.getFailRetJson("新密码与确认密码不一致");
			getPrintWriter(response, retJson);
			return;
		}

		try {
			
			User u = BaseUtil.getCurrentUser(request);
			// 验证原密码是否正确
			if (!Md5.md5(oldPassword).toLowerCase().equals(u.getUserPwd())) {			
				retJson = ControllerUtil.getFailRetJson("原密码不正确");
				getPrintWriter(response, retJson);
				return;
			}
			
			// 更新密码
			user.setUserId(u.getUserId());
			user.setUserPwd(Md5.md5(newPassword).toLowerCase());
			int ret = this.userService.updateByPrimaryKeySelective(user);
			if(ret > 0) {
				retJson = ControllerUtil.getSuccessRetJson("修改密码成功");
				logOperService.insertLogOper(request, 1, "修改密码成功");
			} else {
				retJson = ControllerUtil.getFailRetJson("修改密码失败");
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, retJson);
		
	}

	/**
	 * index:跳转至主页面方法 <br/>
	 * 适用条件：登录成功，跳转至主页面<br/>
	 * 执行流程：AJAX调用<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return BaseConstants.LOGIN_INDEX;
	}

	/**
	 * login:登录方法 <br/>
	 * 适用条件：用户登录系统<br/>
	 * 执行流程：AJAX调用<br/>
	 * 使用方法：登录系统时调用<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @param user
	 *            前台登录用户信息
	 * @param validateCode
	 *            验证码
	 * @throws IOException
	 * @throws Exception
	 *             异常
	 */
	@RequestMapping(value = "/login")
	public void login(HttpServletRequest request, Model model,
			HttpServletResponse response, User user) throws IOException {
		String randCheckCode = user.getRandCheckCode().toUpperCase();
		String sessionRandCheckCode = BaseUtil.getSessionAttr(request, Constants.RAND_CHECK_CODE);
		//返回结果
		String retJson = "";
		//判断验证码是否正确
		if(StringUtil.isBlank(randCheckCode) || StringUtil.isBlank(sessionRandCheckCode) || !randCheckCode.equals(sessionRandCheckCode)) {
			//验证码错误
			retJson = ControllerUtil.getFailRetJson("验证码错误");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			userService.login(request, user);
			retJson = ControllerUtil.getSuccessRetJson("登录成功");
			logOperService.insertLogOper(request, 1, "登录成功");
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, retJson);
		
	}

	/**
	 * loginOut:退出系统 <br/>
	 * 适用条件：退出当前系统<br/>
	 * 执行流程：前台AJAX调用<br/>
	 *
	 * @author:焦少平 Date: 2015年4月27日 下午7:53:08
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 登录界面
	 * @throws IOException
	 */
	@RequestMapping(value = "/loginOut")
	public void loginOut(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 删除cookie
		Cookie cookies = new Cookie(COOKIE_JSESSIONID_KEY, null);
		cookies.setPath("/");
		cookies.setMaxAge(0);// 0代表立即删除，-1代表关闭浏览器删除
		response.addCookie(cookies);

		// 移除session
		HttpSession session = request.getSession();
		session.removeAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		session.removeAttribute(BaseConstants.USER);

		// 删除response缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.sendRedirect("toLogin");
	}

	@RequestMapping(value = "/north")
	public ModelAndView north(HttpServletRequest request,
			HttpServletResponse response, ModelAndView modelVeiw)
			throws IOException {

		User u = null;
		try {
			u = BaseUtil.getCurrentUser(request);
			modelVeiw.addObject("userName", u.getUserName());
			modelVeiw.addObject("deptName", u.getDeptName());
			modelVeiw.setViewName("platform/layout/north");
		} catch (Exception e) {
			// 删除response缓存
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.sendRedirect("toLogin");
		}
		return modelVeiw;

	}

	@RequestMapping(value = "/toLogin")
	public String toLogin(HttpServletRequest request,
			HttpServletResponse response) {
		return BaseConstants.LOGIN_HTML;
	}

	/**
	 * getUserInfo:得到用户登录信息 <br/>
	 * 适用条件：主页面显示<br/>
	 * 执行流程：AJAX调用<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 */
	/*
	 * @RequestMapping(value = "/getUserInfo") public void
	 * getUserInfo(HttpServletRequest request, HttpServletResponse response){
	 * //当前时间 String time = DateUtil.sysTime(); //得到当前用户登录名 String name =
	 * BaseUtil.getCurrentUser(request).getUserLoginName(); //返回前台
	 * getPrintWriter(response,
	 * "{\"time\":\""+time+"\",\"name\":\""+name+"\"}"); }
	 */
}
