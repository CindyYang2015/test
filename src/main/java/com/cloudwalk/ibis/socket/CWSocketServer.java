package com.cloudwalk.ibis.socket;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.cloudwalk.ibis.service.base.recog.RecogManageService;

/**
 * socket server
 * @author 何春节
 * @version 1.0
 * @since 1.6
 */
public class CWSocketServer {
	private static Logger logger = Logger.getLogger(CWSocketServer.class);
	
	/**
	 * 生物识别认证接口
	 */
	private RecogManageService recogManageService;
	
	/**
	 * socket 监听线程
	 */
	private SocketServerThread serverThread = null;
	
	/**
	 * socket 管理线程
	 */
	private SocketManagerThread managerThread = null;	
	
	/**
	 * 线程池服务
	 */
	private ThreadpoolService threadpoolService = null;
	
	
	/**
	 * 初始化socket server
	 * @param ip
	 * @param port
	 * @param faceManage
	 * @throws Exception
	 */
	public void init(String ip, int port, RecogManageService recogManageService) throws Exception {
		//识别认证服务
		this.recogManageService = recogManageService;
		
		// 启动socket管理线程
		this.managerThread = new SocketManagerThread();
		this.managerThread.setName("CW-SMT-" + this.hashCode());
		this.managerThread.setIsShortLink(true);
		this.managerThread.regReadFullMsgMethod(this, "doReadFullMsg");
		this.managerThread.regSocketClosed(this, "doClientSocketClose");
		this.managerThread.start();
		
		// 启动socket监听线程
		this.serverThread = new SocketServerThread();
		this.serverThread.setName("CW-SST-" + this.hashCode());
		this.serverThread.setListenAddress(ip, port);
		this.serverThread.regAcceptMethod(this, "doAcceptNewSocket");
		this.serverThread.start();
		
		//初始化线程池
		threadpoolService = new ThreadpoolService();
		threadpoolService.init();
	}
	
	/**
	 * 关闭监听端口
	 */
	public void close() {
		if (null != this.serverThread) {
			this.serverThread.close();
		}
		if(null != this.managerThread) {
			this.managerThread.close();
		}
		if(null != this.threadpoolService) {
			this.threadpoolService.close();
		}
	}
	
	/**
	 * 接收新的访问线程，并加入到线程管理堆栈中进行管理
	 * @param oSelector
	 * @param socketChannel
	 */
	public void doAcceptNewSocket(Selector oSelector, SocketChannel socketChannel) {
		try {
			if (this.serverThread != null) {
				this.managerThread.addSocketChannel(socketChannel);
			}
		} catch(Exception e) {
			logger.error("接收新的连接异常：" + e.getMessage(), e);
		}
	}
	
	/**
	 *报文传输完成后调用方法
	 * @param socketChannel
	 * @param byteBuffer
	 * @throws UnsupportedEncodingException
	 */
	public void doReadFullMsg(SocketChannel socketChannel, ByteBuffer byteBuffer)
			throws UnsupportedEncodingException {
		
		String reqMsg = new String(byteBuffer.array(), SSConst.DEFAULT_CHARSET);
		HandleThread handleThread = new HandleThread(this.recogManageService,this.managerThread,socketChannel,reqMsg);
		this.threadpoolService.execute(handleThread);
	}
	
	/**
	 * socket关闭方法
	 * @param oSocketChannel
	 */
	public void doClientSocketClose(SocketChannel socketChannel) {
		socketChannel.isBlocking();
	}	
	
}
