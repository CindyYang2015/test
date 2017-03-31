package com.cloudwalk.ibis.socket;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * 使用Java.nio进行异步通信的线程基类
 * @author 何春节
 * @version 1.0
 * @since 1.6
 */
public abstract class AbstractNetThread extends Thread {
	private static Logger logger = Logger.getLogger(AbstractNetThread.class);
	
	/**
	 * 是否运行
	 */
	protected boolean bIsRun = true;
	
	/**
	 * 选择器
	 */
	protected Selector mSelector = null;
	
	/**
	 * SocketChannel有可读取数据时的回调方法
	 */
	protected Method callReadMethod = null;
	
	/**
	 * SocketChannel有可读取数据时的回调对象
	 */
	protected Object callReadObject = null;
	
	/**
	 * SocketChannel有可写数据时的回调方法
	 */
	protected Method callWriteMethod = null;
	
	/**
	 * SocketChannel有可写数据时的回调对象
	 */
	protected Object callWriteObject = null;
	
	/**
	 * 接收到新Socket的回调方法
	 */
	protected Method callAcceptMethod = null;
	
	/**
	 * 接收到新Socket的回调对象
	 */
	protected Object callAcceptObject = null;
	
	/**
	 * SocketChannel有可读取数据时的回调对象
	 */
	protected Object callConnectObject = null;

	/**
	 * SocketChannel连接建立或断开的回调方法
	 */
	protected Method callConnectMethod = null;
	
	/**
	 * 读取到完整报文的回调方法
	 */
	protected Method callReadFullMsgMethod = null;
	
	/**
	 * 读取到完整报文的回调对象
	 */
	protected Object callReadFullMsgObject = null;
	
	/**
	 * 构造函数
	 */
	public AbstractNetThread() {
	}
	
	/**
	 * 设置接收到一个新Socket的回调方法。回调方法应尽可能的快速处理 
	 * 回调方法原形为：method(Selector selector,SocketChannel sc);
	 * @param objCall      接收Socket的实例
	 * @param sMethodName  接收的类的方法名称
	 */
	public void regAcceptMethod(Object objCall, String sMethodName)
		throws NoSuchMethodException, SecurityException {
		this.callAcceptMethod = objCall.getClass().getMethod(sMethodName, new Class[] {
			Selector.class, SocketChannel.class
		});
		this.callAcceptObject = objCall;
	}
	
	/**
	 * 设置接收到Socket建立或断开的回调方法。回调方法应尽可能的快速处理 
	 * 回调方法原形为：method(Selector selector,SocketChannel sc);
	 * @param objCall      接收Socket的实例
	 * @param sMethodName  接收的类的方法名称
	 */
	public void regConnectMethod(Object objCall, String sMethodName)
		throws NoSuchMethodException,SecurityException {
		this.callConnectMethod = objCall.getClass().getMethod(sMethodName, new Class[] {
			Selector.class, SocketChannel.class
		});
		this.callConnectObject = objCall;
	}
	
	/**
	 * 设置SocketChannel有可读取数据时回调方法
	 * 回调方法原形为：method(Selector selector,SocketChannel sc);
	 * @param objCall     读取SocketChannel数据的实例
	 * @param sMethodName 读取SocketChannel数据的方法
	 */
	public void regReadMethod(Object objCall,String sMethodName)
		throws NoSuchMethodException,SecurityException {
		this.callReadMethod = objCall.getClass().getMethod(sMethodName, new Class[] {
			Selector.class, SocketChannel.class
		});
		this.callReadObject = objCall;
	}
	
	/**
	 * 设置SocketChannel有可写取数据时回调方法
	 * 回调方法原形为：method(Selector selector,SocketChannel sc);
	 * @param objCall     写入SocketChannel数据的实例
	 * @param sMethodName 写入SocketChannel数据的方法
	 */
	public void regWriteMethod(Object objCall,String sMethodName)
		throws NoSuchMethodException, SecurityException {
		this.callWriteMethod = objCall.getClass().getMethod(sMethodName, new Class[] {
			Selector.class,SocketChannel.class
		});
		this.callReadObject = objCall;
	}

	/**
	 * 设置读取到完整报文时回调方法
	 * 回调方法原形为：method(SocketChannel vSocketChannel, ByteBuffer sFullMsgBuff);
	 * @param objCall     写入SocketChannel数据的实例
	 * @param sMethodName 写入SocketChannel数据的方法
	 */
	public void regReadFullMsgMethod(Object objCall,String sMethodName) 
		throws NoSuchMethodException, SecurityException {
		this.callReadFullMsgMethod = objCall.getClass().getMethod(sMethodName, new Class[] {
			SocketChannel.class, ByteBuffer.class
		});
		this.callReadFullMsgObject = objCall;
	}
	
	/**
	 * 初始化
	 */
	protected abstract void init() throws Exception ;
	
	/**
	 * 关闭处理
	 */
	protected abstract void close();
	
	/** 
	 * 线程空闲时调用 
	 */
	protected void idlesse() {}
	
	/**
	 * 线程方法
	 */
	public void run() {
		int iIdleNum = 0;
		
		try {
			
			this.init(); // 初始化
			
			// 循环接收事件并处理
			while (this.bIsRun) {
				try {
					
					if (this.mSelector != null && this.mSelector.isOpen()) {
						int num = this.mSelector.select(2000);
							
						if (num > 0) {
							Iterator<SelectionKey> it = this.mSelector.selectedKeys().iterator();
							while (it.hasNext()) {
								SelectionKey s = (SelectionKey) it.next();
								it.remove();
								this.doEvent(s);
							}
							
							iIdleNum = 0;
						} else {
							if (++iIdleNum % 5 == 0) {
								iIdleNum = 0;
								this.idlesse();
							}
						}
					} else {
						Thread.sleep(10);
					}
				} catch(Exception e) {
					logger.error("循环接收事件并处理异常，原因：" + e.getMessage(), e);
				}
			}
		} catch(Exception e) {
			logger.error("循环接收事件并处理异常，原因：" + e.getMessage(), e);
		} finally {
			this.close();
		}
	}
	
	/**
	 * 处理事件
	 */
	private void doEvent(SelectionKey s) {
		try {

			// 读取事件
			if (s.isValid() && s.isReadable() && this.callReadMethod != null && this.callReadObject != null) {
				this.callReadMethod.invoke(this.callReadObject, new Object[] {
					this.mSelector, (SocketChannel) s.channel()
				});
			}
			
			// 写入事件
			if (s.isValid() && s.isWritable() && this.callWriteMethod != null && this.callWriteObject != null) {
				this.callWriteMethod.invoke(this.callWriteObject, new Object[] {
					this.mSelector, (SocketChannel) s.channel()
				});
			}
			
			//连接事件
			if (s.isValid() && s.isAcceptable() && this.callAcceptMethod != null && this.callAcceptObject != null) {
				this.callAcceptMethod.invoke(this.callAcceptObject, new Object[] {
					this.mSelector, ((ServerSocketChannel) s.channel()).accept()
				});
			}

			//连接建立或者出错事件
			if (s.isValid() && s.isConnectable() && this.callConnectMethod != null && this.callConnectObject != null) {
				this.callConnectMethod.invoke(this.callConnectObject, new Object[] {
					this.mSelector, (SocketChannel) s.channel()
				});
			}
			
		} catch(Exception e) {
			logger.error("事件处理异常，原因：" + e.getMessage(), e);
		}
	}
}