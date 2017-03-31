package com.cloudwalk.ibis.socket;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.ibis.service.base.recog.RecogManageService;

/**
 * 启动socket service服务监听器
 * @author 何春节
 * @version 1.0
 * @since 1.6
 */
public class StartupSocketServiceListener extends ContextLoaderListener {
	private static Logger logger = Logger.getLogger(StartupSocketServiceListener.class);
	
	/**
	 * 云丛IBISsocket服务器对象
	 */
	private CWSocketServer socketServer;

	/**
	 * 初始化系统资源
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		
		ServletContext context = event.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		this.initSocketServer(ctx);
	}
	
	/**
	 * 初始化socket server
	 * @param ctx
	 */
	private void initSocketServer(ApplicationContext ctx) {
		RecogManageService recogManageService = (RecogManageService)ctx.getBean("recogManageService");
		this.socketServer = new CWSocketServer();
		
		// 获取IBIS配置的socket IP地址和端口号
		String port = Constants.Config.IBIS_SOCKET_PORT;
		try {
			socketServer.init("", Integer.valueOf(port), recogManageService);
			logger.info("启动人脸socket server 比对服务成功！");
		} catch (Exception e) {
			logger.error("启动人脸socket server比对服务失败，原因：" + e.getMessage(), e);
		}
	}

	/**
	 * 销毁相应资源
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
		if(this.socketServer == null) return;
		this.socketServer.close();
	}
}
