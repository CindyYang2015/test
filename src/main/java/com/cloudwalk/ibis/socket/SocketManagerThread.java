package com.cloudwalk.ibis.socket;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

/**
 * 批量SocketChannel管理线程
 * @author Chunjie He
 * @version 2.0
 * @date 2015-02-02
 * 
 */
public final class SocketManagerThread extends Thread {
	private static final Logger logger = Logger.getLogger(SocketManagerThread.class);
	
	/** 
	 * 报文通讯头的长度
	 */
	private int mHeadLen = SSConst.MESSAGE_HEAD_LENGTH;
	
	/**
	 * 当前线程管理的SocketChannel集合
	 */
	private Map<SocketChannel, GessSocketChannel> mapGessSocketChannel =
		new ConcurrentHashMap<SocketChannel,GessSocketChannel>(200);
	
	/** 
	 * 待进行事件变更的SocketChannel
	 * 第一个元素：SocketChannel 
	 * 第二个元素：ops
	 */
	private ConcurrentLinkedQueue<Object[]> cqSckChlEventChange = 
			new  ConcurrentLinkedQueue<Object[]>();
	
	/** 
	 * 待进行添加的Socket队列
	 */
	private ConcurrentLinkedQueue<SocketChannel> cqSckChlWaitAdd = 
			new ConcurrentLinkedQueue<SocketChannel>();
	
	/**
	 * 读取一条完整报文后的回调方法
	 */
	private Method callReadFullMsgMethod = null;
	
	/**
	 * 读取一条完整报文后的回调对象
	 */
	private Object callReadFullMsgObject = null;
	
	/**
	 * Socket关闭事件的回调方法
	 */
	private Method callSocketClosedMethod = null;
	
	/**
	 * Socket关闭事件的回调对象
	 */
	private Object callSocketClosedObject = null;
	
	/**
	 * 选择器
	 */
	private Selector mSelector = null;
	
	/**
	 * 读取的总字节数
	 */
	private long mReadBytes = 0;
	
	/**
	 * 写入的总字节数
	 */
	private long mWriteBytes = 0;
	
	/**
	 * 计量的起启时间
	 */
	private long mStartTime = 0;
	
	/**
	 * 是否是短连接，即只接收一个单包的请求，只发送一个单包
	 */
	private boolean bIsShortLink = false;
	
	/** 
	 * 报文通讯头的长度是否使用字节表示 
	 */
	private boolean bIsUseByteHeadLen = false;
	
	/** 
	 * 是否需要添加Socket 
	 */
	private volatile boolean bIsNeedAddSck = false; 
	
	/**
	 * 是否运行当前线程
	 */
	private boolean bIsRun = true;
	
	/**
	 * 构造方法
	 * @throws IOException
	 */
	public SocketManagerThread() throws IOException {
		this.bIsRun = true;
		this.mSelector = Selector.open();
	}
	
	/**
	 * 构造方法
	 * 
	 * @param vHeadLen
	 * @throws IOException
	 */
	public SocketManagerThread(int vHeadLen) throws IOException {
		this.bIsRun = true;
		this.mSelector = Selector.open();
		this.mHeadLen = vHeadLen;
	}
	
	/**
	 * 设置是否是短连接
	 * @param flag 
	 *   true : 短连接 
	 *   false: 长连接
	 */
	public void setIsShortLink(boolean flag) {
		this.bIsShortLink = flag;
	}
	
	/**
	 * 设置报文的通讯长度是否使用字节表示
	 * @param flag
	 */
	public void setIsUseByteHeadLen(boolean flag) {
		this.bIsUseByteHeadLen = flag;
	}
	
	/**
	 * 设置读取一条完整报文后的回调方法。回调方法应尽可能的快速处理 
	 * @param objCall      接收一条完整报文的回调方法的实例
	 * @param sMethodName  接收一条完整报文的回调方法的名称
	 */
	public void regReadFullMsgMethod(Object objCall,String sMethodName) 
		throws NoSuchMethodException, SecurityException {
		this.callReadFullMsgMethod = objCall.getClass().getMethod(sMethodName, new Class[] {
			SocketChannel.class, ByteBuffer.class
		});
		this.callReadFullMsgObject = objCall;
	}
	
	/**
	 * 设置Socket关闭后的回调方法
	 * @param objCall      接收Socket关闭事件的回调方法实例
	 * @param sMethodName  接收Socket关闭事件的回调方法名称
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void regSocketClosed(Object objCall, String sMethodName) 
		throws NoSuchMethodException, SecurityException {
		this.callSocketClosedMethod = objCall.getClass().getMethod(sMethodName,new Class[] {
			SocketChannel.class
		});
		this.callSocketClosedObject = objCall;
	}
	
	/**
	 * 添加待管理的SocketChannel对象
	 * @param vSocketChannel
	 * @return 
	 */
	public GessSocketChannel addSocketChannel(SocketChannel vSocketChannel) throws Exception {
		GessSocketChannel gsc = null;
		
		try {
			// 配置非阻塞方式
			vSocketChannel.configureBlocking(false);	
			
			//生成二级系统的SocketChannel
			gsc = new GessSocketChannel(this.mSelector, vSocketChannel, this.mHeadLen);
			
			gsc.setIsShortLink(this.bIsShortLink);
			gsc.setIsUseByteHeadLen(this.bIsUseByteHeadLen);
			gsc.regSocketClosedMethod(this, "closeSocketChannel");
			gsc.regReadFullMsgMethod(this, "readFullMsg");
			gsc.setWriteByEvent(true, this.cqSckChlEventChange);
			
			// 添加到集合中
			mapGessSocketChannel.put(vSocketChannel, gsc);			
			// 添加到队列中，注册读事件
			this.cqSckChlWaitAdd.add(vSocketChannel);
			
			this.bIsNeedAddSck = true;
			this.mSelector.wakeup();
		} catch(Exception e){
			this.closeSocketChannel(vSocketChannel);
			logger.error("添加待管理的SocketChannel对象异常，原因：" + e.getMessage(), e);
		}
		
		return gsc;
	}

	/**
	 * 添加Socket
	 */
	private void loadSckChlAdd() {
		SocketChannel sck = null;
		
		while (true) {
			sck = this.cqSckChlWaitAdd.poll();
			if (sck != null) {
				try {
					sck.register(this.mSelector, SelectionKey.OP_READ);
				} catch(Exception e) {
					logger.error("添加Socket异常，原因：" + e.getMessage(), e);
					if (sck != null) {
						this.closeSocketChannel(sck);
					}
				}
			} else {
				break;
			}
		}
		
		this.bIsNeedAddSck = false;
	}
	
	/**
	 * 装载SocketChannel 事件改变
	 */
	private void loadSckChlEvtChange() {
		
		SocketChannel sc = null;
		while (true) {
			
			Object[] objEvt = this.cqSckChlEventChange.poll();
			if (objEvt != null) {
				
				try {
					sc = (SocketChannel) objEvt[0];
					if (sc != null && sc.isOpen()) {
						sc.register(this.mSelector, ((Integer) objEvt[1]).intValue());
					}
				} catch(Exception e) {
					if (sc != null) {
						this.closeSocketChannel(sc);
					}
					
					logger.error("装载SocketChannel 事件改变异常，原因：" + e.getMessage(), e);
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * 线程方法
	 */
	public void run() {
		this.mStartTime = System.currentTimeMillis();
		while (this.bIsRun) {
			try {
				int num = this.mSelector.select(2000);
				if (num > 0) {
					
					Iterator<SelectionKey> it = this.mSelector.selectedKeys().iterator();
					while (it.hasNext()) {
						if (this.bIsNeedAddSck) {
							this.loadSckChlAdd();
						}
						
						//处理事件
						SelectionKey s = (SelectionKey) it.next();
						it.remove();
						doEvent(s);
					}
				}
				
				// 装载SocketChannel事件改变消息
				if (this.cqSckChlEventChange.peek() != null) {
					this.loadSckChlEvtChange();
				}
				
				// 添加Socket
				if (this.cqSckChlWaitAdd.peek() != null) {
					this.loadSckChlAdd();
				}
			} catch(Exception e) {
				logger.error("启动socket管理线程失败，原因：" + e.getMessage(), e);
			}
		}
	}

	/**
	 * 处理事件
	 */
	private void doEvent(SelectionKey s) {
		try {

			// 读取事件
			if (s.isValid() && s.isReadable()) {
				GessSocketChannel gsc = this.mapGessSocketChannel.get(s.channel());
				if (gsc != null) {
					statisticsReadDataStream(gsc.doRead());
				}
			}
			
			// 写入事件
			if (s.isValid() && s.isWritable()) {
				GessSocketChannel gsc = this.mapGessSocketChannel.get(s.channel());
				if (gsc != null) {
					statisticsWriteDataStream(gsc.doWrite());
				}
			}
		} catch(Exception e) {
			logger.error("处理事件异常，原因：" + e.getMessage(), e);
		}
	}
	
	/**
	 * 添加待发送报文
	 */
	public void addWriteMsg(SocketChannel vSocketChannel ,StringBuffer sbMsg) {
		GessSocketChannel gsc = this.mapGessSocketChannel.get(vSocketChannel);
		if (gsc != null) {
			gsc.addWriteMsg(sbMsg);
		}
	}
	
	/**
	 * 重载
	 * @param vSocketChannel
	 * @param msgBuff
	 * @throws Exception
	 */
	public void addWriteMsg(SocketChannel vSocketChannel, ByteBuffer msgBuff) 
			throws Exception {
		GessSocketChannel gsc = this.mapGessSocketChannel.get(vSocketChannel);
		if (gsc != null) {
			gsc.addWriteMsg(msgBuff);
		}
	}
	
	/**
	 * 添加待发送报文
	 */
	public void addWriteMsg(StringBuffer sbMsg) {
		SocketChannel scIdlesse = this.getIdlesseSocketChannel();
		if (scIdlesse != null) {
			this.addWriteMsg(scIdlesse, sbMsg);
		}
	}
	
	/**
	 * 添加待发送报文
	 * @param msgBuff
	 * @throws Exception
	 */
	public void addWriteMsg(ByteBuffer msgBuff) throws Exception {
		SocketChannel scIdlesse = this.getIdlesseSocketChannel();
		if (scIdlesse != null) {
			this.addWriteMsg(scIdlesse, msgBuff);
		}
	}
	
	
	/**
	 * 读取完整报文
	 * @param sMsg
	 */
	public void readFullMsg(SocketChannel vSocketChannel, ByteBuffer msgBuff) {
		try {
			if (this.callReadFullMsgObject != null && this.callReadFullMsgMethod != null) {
				this.callReadFullMsgMethod.invoke(this.callReadFullMsgObject, new Object[] {
					vSocketChannel, msgBuff
				});
			}
		} catch(Exception e) {
			logger.error("读取报文异常，原因：" + e.getMessage(), e);
		}
	}
	
	/**
	 * 关闭Socket管理服务
	 */
	public void close() {
		Iterator<SocketChannel> its = null;
		
		this.bIsRun = false;
		try {
			
			its = this.mapGessSocketChannel.keySet().iterator();
			while (its.hasNext()) {
				SocketChannel sc = (SocketChannel) its.next();
				sc.close();
			}
			
			if (this.mSelector != null) {
				this.mSelector.close();
			}
		} catch(Exception e) {
			logger.error("关闭Socket管理服务异常，原因：" + e.getMessage(), e);
		}
	}
	
	/**
	 * 关闭SocketChannel
	 */
	public void closeSocketChannel(SocketChannel vSocketChannel) {
		try {
			
			this.mapGessSocketChannel.remove(vSocketChannel);
			if (this.callSocketClosedObject != null && this.callSocketClosedMethod != null) {	
				this.callSocketClosedMethod.invoke(this.callSocketClosedObject, new Object[] {
					vSocketChannel
				});
			}
		} catch(Exception e) {
			logger.error("关闭SocketChannel异常，原因：" + e.getMessage(), e);
		}
	}
	
	/**
	 * 获得当前比较空闲的SocketChannel
	 * @return
	 */
	public SocketChannel getIdlesseSocketChannel() {
		
		GessSocketChannel ret = null;
		for (GessSocketChannel gsc : this.mapGessSocketChannel.values()) {
			if (ret == null || ret.getUseRate() > gsc.getUseRate()) {
				ret = gsc;
			}
		}
		
		return (ret == null) ? null : ret.getSocketChannel();
	}
	
	/** 
	 * 获得正在写的Socket数量
	 * @return
	 */
	public synchronized int getWriteSocketChannelNum() {
		int num = 0 ;
		for (GessSocketChannel gsc : this.mapGessSocketChannel.values()) {
			if (gsc.isSending()) {
				num++;
			}
		}
		
		return num;
	}
	
	/**
	 * 获得管理的SocketChannel个数
	 * @return
	 */
	public int getSocketChannelCount() {
		return this.mapGessSocketChannel.size();
	}
	
	/**获得管理的Socket集合*/
	public Map<SocketChannel,GessSocketChannel> getManagerSocket() {
		return this.mapGessSocketChannel;
	}
	
	/**
	 * 根据API的SocketChannel返回二级系统对应的GessSocketChannel
	 * @param sc
	 * @return
	 */
	public GessSocketChannel getGessSocketChannel(SocketChannel sc) {
		return this.mapGessSocketChannel.get(sc);
	}
	
	/**
	 * 获得当前SocketChannel读取的总字节数
	 * @return
	 */
	public long getSumReadBytes() {
		return this.mReadBytes;
	}

	/**
	 * 获得当前SocketChannel写入的总字节数
	 * @return
	 */
	public long getSumWriteBytes() {
		return this.mWriteBytes;
	}
	
	/**
	 * 获得当前Socket的利用率 （读入总字节＋写入总字节）/套接字连接时间（秒）
	 * @return
	 */
	public double getUseRate() {
		long currTime = System.currentTimeMillis();
		long invTime = currTime - this.mStartTime;
		
		if (invTime > SSConst.CFG_COLLECT_DATA_STREAM_SIZE_TIME) {
			this.mStartTime = currTime;
			this.mWriteBytes = 0;
			this.mReadBytes  = 0;
		}
		
		invTime = invTime / 1000;

		long bytes = this.mReadBytes + this.mWriteBytes;
		if (invTime <= 0) {
			return bytes;
		} else {
			return bytes * 1.0 / (invTime * 1.0);
		}
	}
	
	/**
	 * 统计输入流量
	 * @param size     当前流大小
	 */
	private void statisticsReadDataStream(int size) {
		long currTime = System.currentTimeMillis();
		long invTime = currTime - this.mStartTime;
		
		if (invTime > SSConst.CFG_COLLECT_DATA_STREAM_SIZE_TIME) {
			this.mStartTime  = currTime;
			this.mWriteBytes = 0;
			this.mReadBytes  = 0;
		}
		
		this.mReadBytes += size;
	}
	
	/**
	 * 统计输出流量
	 * @param size     当前流大小
	 */
	private void statisticsWriteDataStream(int size) {
		long currTime = System.currentTimeMillis();
		long invTime = currTime - this.mStartTime;
		
		if (invTime > SSConst.CFG_COLLECT_DATA_STREAM_SIZE_TIME) {
			this.mStartTime  = currTime;
			this.mWriteBytes = 0;
			this.mReadBytes  = 0;
		}
		
		this.mWriteBytes += size;
	}
}