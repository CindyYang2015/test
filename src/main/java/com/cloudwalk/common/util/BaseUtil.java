package com.cloudwalk.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cloudwalk.common.common.BaseConstants;
import com.cloudwalk.ibis.model.system.User;

public class BaseUtil {

	/**
	 * getCurrentUser:获取系统当前登录用户信息 <br/>
	 * 适用条件：获取当前用户信息<br/>
	 * 执行流程：类调用<br/>
	 * 适用条件：获取当前用户信息<br/>
	 *
	 * @param request http请求
	 * @return User 用户信息
	 */

	public static final User getCurrentUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(BaseConstants.USER);
	}

	/**
	 * setCurrentUser:将当前登录用户信息放入session中 <br/>
	 * 适用条件：将当前用户信息放入session中<br/>
	 * 执行流程：类调用<br/>
	 * 适用条件：存入当前用户信息<br/>
	 *
	 * @param request
	 *            http请求
	 * @return User 用户信息
	 */
	public static final void setCurrentUser(HttpServletRequest request, User user) {
		request.getSession().setAttribute(BaseConstants.USER, user);
	}
	
	/**
	 * 读取session的属性值
	 * @param request
	 * @param attrName
	 * @return
	 */
	public static final String getSessionAttr(HttpServletRequest request,String attrName) {
		HttpSession session = request.getSession();
		String attrValue = (String)session.getAttribute(attrName);
		return attrValue;
	}
}
