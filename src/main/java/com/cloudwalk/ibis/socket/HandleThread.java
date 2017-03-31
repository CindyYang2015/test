package com.cloudwalk.ibis.socket;

import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.cloudwalk.ibis.service.base.recog.RecogManageService;

/**
 * 支持socket并发使用
 * @author zhuyf
 *
 */
public class HandleThread implements Runnable {
	
	private static final Logger logger = Logger.getLogger(HandleThread.class);
	/**
	 * 业务处理接口
	 */
	private RecogManageService recogManageService;
	/**
	 * socket 管理线程
	 */
	private SocketManagerThread managerThread = null;	
	
	/**
	 * 接受的消息
	 */
	private String msg;
	
	/**
	 * socket通道
	 */
	private SocketChannel socketChannel;
	
	
	public HandleThread(RecogManageService recogManageService,SocketManagerThread managerThread,SocketChannel socketChannel,String msg){
		this.recogManageService = recogManageService;
		this.managerThread = managerThread;
		this.socketChannel = socketChannel;
		this.msg = msg;
	}

	@Override
	public void run() {
		this.doService(msg);
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void doService(String msg) {
		
		String writesMessage = "";
		try {			
			//识别认证
			writesMessage = this.recogManageService.handleRequest(msg);	
			int length = writesMessage.getBytes().length; // 报文长度
			String reportHead =  String.format("%08d", length);		
			//应答报文
			this.managerThread.addWriteMsg(socketChannel, new StringBuffer(reportHead+writesMessage));
		} catch (Exception e) {			
			logger.error("IBIS接收并返回报文失败，原因：" + e.getMessage(), e);
		}
	}

	

}
