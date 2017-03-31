package com.cloudwalk.ibis.socket;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
/**
 * 
 * Socket端口监听服务线程
 * @author Chunjie He
 * @version 2.0
 * @date 2015-02-02
 * 
 */
public final class SocketServerThread extends AbstractNetThread {
	private static final Logger logger = Logger.getLogger(SocketServerThread.class);
	
	/** 
	 * 服务端套接字通道 
	 */
	private ServerSocketChannel ssc = null;	
	
	/**
	 * 监听端口
	 */
	private int iListenPort = 0;
	
	/**
	 * 监听IP地址
	 */
	private String sListenIp = "";

	/**
	 * 是否初始化
	 */
	private boolean bIsInit = false;
	
	/**
	 * 构造函数
	 */
	public SocketServerThread() {
	}
	
	/**
	 * 构造函数
	 * @param iListenPort  监听端口
	 */	
	public SocketServerThread(int iListenPort) {
		setListenAddress("", iListenPort);
	}
	
	/**
	 * 构造函数
	 * @param sListenIp    监听的本地IP地址
	 * @param iListenPort  监听端口
	 */
	public SocketServerThread(String sListenIp, int iListenPort) {
		setListenAddress(sListenIp, iListenPort);
	}
	
	/**
	 * 设置服务监听端口
	 * @param iListenPort 监听端口
	 */
	public void setListenAddress(int iListenPort) {
		this.iListenPort = iListenPort;
	}
	
	/**
	 * 设置服务监听的本地IP地址和监听端口
	 * @param sListenIp     本地监听的IP地址
	 * @param iListenPort   监听端口
	 */
	public void setListenAddress(String sListenIp, int iListenPort) {
		this.sListenIp = sListenIp;
		this.iListenPort = iListenPort;
	}

	/**
	 * 初始化，起动监听服务
	 */
	public synchronized void init() throws Exception {
		
		if (this.bIsInit == true) {
			return ;
		}
			
		InetSocketAddress address = null ;
		try {
			
			// 初始化服务端套接字通道
			this.mSelector = Selector.open();
			this.ssc = ServerSocketChannel.open();
			
			if (StringUtils.hasText(this.sListenIp)) {
				address = new InetSocketAddress(this.sListenIp, this.iListenPort);
			} else {
				address = new InetSocketAddress(this.iListenPort);
			}
			
			this.ssc.socket().setReuseAddress(true);
			this.ssc.socket().bind(address, 1000);

			// 配置非阻塞方式
			this.ssc.configureBlocking(false);
			this.ssc.register(this.mSelector, SelectionKey.OP_ACCEPT);
			this.bIsInit = true;
		} catch(Exception e) {
			logger.error("初始化Socket服务失败, 原因：" + e.getMessage(), e);
			throw e;
		}
	}
	

	/**
	 * 关闭监听的相关服务
	 */
	public void close() {
		this.bIsRun = false;
		
		try {
			if (this.mSelector != null) {
				this.mSelector.close();
			}
			
			if (this.ssc != null) {
				this.ssc.close();
			}
		} catch(Exception e) {
			logger.error("关闭监听的相关服务异常, 原因：" + e.getMessage(), e);
		}
	}
}