/**
 * Project Name:fssc
 * File Name:BaseController.java
 * Package Name:com.linc.usermanager.base.controller
 * Date:2014年11月20日 上午11:30:14
 * Copyright @ 2010-2014 上海企垠信息科技有限公司  All Rights Reserved.
 *
 */

package com.cloudwalk.common.base.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.BaseConstants;
import com.cloudwalk.common.exception.DataException;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.system.User;

/**
 * ClassName:BaseController <br/>
 * Description: 公共控制类，所有的控制类都要继承此类 <br/>
 * Date: 2014年11月20日 上午11:30:14 <br/>
 *
 * @author 焦少平
 * @version 1.0
 * @since JDK 1.7
 * @see
 */

public class BaseController {
	protected final Logger log = Logger.getLogger(this.getClass());

	/** 基于@ExceptionHandler异常处理 */
	@ExceptionHandler
	public String exp(HttpServletRequest request, Exception ex) {
		request.setAttribute("ex", ex);
		// 根据不同错误转向不同页面
		if (ex instanceof DataException) {
			return "data_error";
		} else {
			return "error";
		}
	}

	/**
	 * <p>
	 * getPrintWriter:将对象传到前台 <br/>
	 * 适用条件：AJAX请求<br/>
	 * 执行流程：AJAX调用的方法里调用该方法<br/>
	 * 使用方法：传递response和object参数<br/>
	 * 注意事项:只有需要向前台传递参数的方法里才可以调用该方法<br/>
	 * </p>
	 *
	 * @author:焦少平 Date: 2014年11月9日 下午7:11:05
	 * @param response
	 *            响应
	 * @param object
	 *            对象
	 * @since JDK 1.7
	 */
	public void getPrintWriter(HttpServletResponse response, Object object) {
		PrintWriter out = null;
		try {
			// 得到输出流
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();

			// 输出对象
			out.print(object);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != out) {
				// 刷新流
				out.flush();
				// 关闭流
				out.close();
			}
		}
	}

	/*
	 * 返回空记录集 Author: Jackson He
	 */
	public void responseEmptyRecord(HttpServletResponse response,
			String otherRowsName, String message) {
		StringBuilder jsonResult = new StringBuilder();
		if (message != null && message.length() > 0) {
			jsonResult.append("{ \"rows\":[], \"total\":0, \"message\": \""
					+ message + "\" ");
		} else {
			jsonResult.append("{ \"rows\":[], \"total\":0 ");
		}

		if (otherRowsName != null && otherRowsName.length() > 0) {
			jsonResult.append(", \" " + otherRowsName + "\":[]");
		}

		jsonResult.append(" }");

		getPrintWriter(response, jsonResult.toString());
	}

	/*
	 * 返回操作结果 Author: Jackson He
	 */
	public void responseOperateResult(HttpServletResponse response,
			Integer result, String message) {
		StringBuilder jsonResult = new StringBuilder();
		if (message != null && message.length() > 0) {
			jsonResult.append("{ \"value\":" + result.toString()
					+ ",  \"message\": \"" + message + "\" }");
		} else {
			jsonResult.append("{ \"value\":" + result.toString() + "}");
		}

		getPrintWriter(response, jsonResult.toString());
	}
	
	

	/**
	 *
	 * setPageInfo:设置分页的数据. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午2:14:10
	 * @param request
	 * @param pageInfo
	 * @since JDK 1.7
	 */
	public void setPageInfo(HttpServletRequest request, PageInfo pageInfo) {
		if (pageInfo == null) {
			pageInfo = new PageInfo();
		}
		int page = ObjectUtils.objToInt(request.getParameter("page"), 1);
		int rows = ObjectUtils.objToInt(request.getParameter("rows"), 20);
		pageInfo.setPageSize(rows);
		pageInfo.setCurrentPage(page);
	}
	
	/**
	 * getCurrentUser:获取系统当前登录用户信息 <br/>
	 * 适用条件：获取当前用户信息<br/>
	 * 执行流程：类调用<br/>
	 * 适用条件：获取当前用户信息<br/>
	 *
	 * @param request http请求
	 * @return User 用户信息
	 */

	public User getCurrentUser(HttpServletRequest request) {
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
	public void setCurrentUser(HttpServletRequest request, User user) {
		request.getSession().setAttribute(BaseConstants.USER, user);
	}
	
	/**
	 * 读取session的属性值
	 * @param request
	 * @param attrName
	 * @return
	 */
	public String getSessionAttr(HttpServletRequest request,String attrName) {
		HttpSession session = request.getSession();
		String attrValue = (String)session.getAttribute(attrName);
		return attrValue;
	}

}
