package com.cloudwalk.ibis.socket;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

/**
 *  根据黄金二级系统平台的通信报文规范，
 *  将JDK的SocketChannel进行封装，
 *  对报文的接收和发送进行异步处理
 *  
 * @author Chunjie He
 * @since 1.6
 * @version 1.0
 * 
 */
public final class GessSocketChannel {
	private static final Logger logger = Logger.getLogger(GessSocketChannel.class);
	
	/** 
	 * 报文头长度
	 */
	private int mHeadLen = SSConst.MESSAGE_HEAD_LENGTH;
	
	/** 
	 * 选择器
	 */
	private Selector mSelector = null;
	
	/** 
	 * SelectionKey 
	 */
	private SelectionKey mSelectionKey = null;
	
	/** 
	 * 套接字通道 
	 */
	private SocketChannel mSocketChannel = null;
	
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
	 * 读取的通信报文头8个字节长度
	 */
	private ByteBuffer mHeadByteBuffer = null;
	
	/**
	 * 读取的通信报文缓冲
	 */
	private ByteBuffer mReadByteBuffer = null;
	
	/**
	 * 读取的总字节数
	 */
	private long mReadBytes = 0;
	
	/**
	 * 写入的总字节数
	 */
	private long mWriteBytes = 0;
	
	/**
	 * 开始计量的时间
	 */
	private long mStartTime = 0;
	
	/**
	 * 是否对Socket的流量进行统计
	 */
	private boolean bIsStatisticsDataStream = false;
	
	/**
	 * 当前正在发送的报文缓冲
	 */
	private ByteBuffer mWriteBuffer = null;
	
	/**
	 * 是否是使用selector 事件进行写操作 
	 */
	private boolean bIsWriteByEvent = false;
	
	
	/**
	 * 当前的事件类型 
	 */
	private int iCurrOps = -1;
	
	/**
	 * 是否是短连接，即只接收一个单包的请求，只发送一个单包
	 */
	private boolean bIsShortLink = false;
	
	/**
	 * 附加的对象
	 */
	private Object mAttachObj = null; 
	
	/** 
	 * 最后发送数据时间
	 */
	private long lastSendDataTime = 0;
	
	/** 
	 * 最后接收数据时间
	 */
	private long lastRecvDataTime = 0;
	
	/** 
	 * 报文通讯头的长度是否使用字节表示
	 */
	private boolean bIsUseByteHeadLen = false;
	
	/**
	 * 待进行写操作的队列
	 */
	private CycleQueue<ByteBuffer> cqWriteMsg = new CycleQueue<ByteBuffer>(10, 4);
	
	/** 
	 * 待进行事件变更的SocketChannel，
	 * 需要外部程序传入，
	 * 	  第一个元素：SocketChannel 
	 * 	  第二个元素：ops
	 */
	private ConcurrentLinkedQueue<Object[]> mSckChlEventChangeQueue = null;
	
	/**
	 * 设置该SocketChannel的数据写入是否使用事件触发模式
	 * @param flag  true:使用事件  flase:不使用事件
	 * @param vSckChlEventChangeQueue  用于接收Socket事件改变的队列
	 */
	public void setWriteByEvent(boolean flag, ConcurrentLinkedQueue<Object[]> vSckChlEventChangeQueue) {
		this.bIsWriteByEvent = flag;
		this.mSckChlEventChangeQueue = vSckChlEventChangeQueue;
	}
	
	
	
	/**
	 * 构造函数
	 * @param vSelector      Selector
	 * @param vSocketChannel Socket通道
	 */
	public GessSocketChannel(Selector vSelector, SocketChannel vSocketChannel, int vHeadLen) {
		this.mSelector = vSelector;
		this.mHeadLen  = vHeadLen;
		this.mSocketChannel  = vSocketChannel;
		this.mHeadByteBuffer = ByteBuffer.allocate(this.mHeadLen);
		this.mStartTime = System.currentTimeMillis();
	}
	
	/**
	 * 构造函数
	 * @param vSelector      Selector
	 * @param vSocketChannel Socket通道
	 */
	public GessSocketChannel(Selector vSelector, SocketChannel vSocketChannel, 
			SelectionKey vSelectionKey, int vHeadLen) {
		this.mSelector = vSelector;
		this.mHeadLen  = vHeadLen;
		this.mSocketChannel = vSocketChannel;
		this.mSelectionKey  = vSelectionKey;
		
		this.mHeadByteBuffer = ByteBuffer.allocate(this.mHeadLen);;
		this.mStartTime = System.currentTimeMillis();
	}
	
	/**
	 * 设置是否是短连接
	 * @param flag true:短连接 false:长连接
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
	 * 回调方法原形为：method(SocketChannel vSocketChannel, ByteBuffer sFullMsgBuff);
	 * 特殊注意：报文包括6字节长度的通讯控制报文
	 * @param objCall     接收一条完整报文的回调方法的实例
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
	 * 回调方法原形为：method(SocketChannel vSocketChannel);
	 * @param objCall      接收Socket关闭事件的回调方法实例
	 * @param sMethodName  接收Socket关闭事件的回调方法名称
	 */
	public void regSocketClosedMethod(Object objCall,String sMethodName) 
		throws NoSuchMethodException, SecurityException {
		this.callSocketClosedMethod = objCall.getClass().getMethod(sMethodName, new Class[] {
			SocketChannel.class
		});
		this.callSocketClosedObject = objCall;
	}
	
	/**
	 * 读取数据
	 */
	public int doRead() throws Exception {
		int iReadBytes = 0;

		/*
		 * 判断是否应该读取通信报文头, 这里通过判断记录报文头的缓冲区位置。
		 * 如果缓冲区没有读满，说明还需要继续读取；否则说明已经读取完成。
		 */
		if (this.mHeadByteBuffer.position() < this.mHeadByteBuffer.limit()) {
			try {
				
				// 开始读取，并获得读取的长度；如果读取失败，发出关闭socket事件；
				// 否则记录读取数据的字节数.
				int ret = this.mSocketChannel.read(this.mHeadByteBuffer);
				if (ret == -1) {
					this.sendSocketClosedEvent();
				} else {
					iReadBytes += ret;
				}
			} catch(Exception e) {
				this.sendSocketClosedEvent();
				logger.error("读取报文长度信息失败，原因：" + e.getMessage(), e);
			}
		}
		
		
		/*
		 * 读满了报文头长度信息之后，对报文长度信息进行判断处理：
		 * 1： 判断报文长度信息是否合法；
		 * 2：为报文分配相应长度的缓冲区.
		 */
		if (this.mReadByteBuffer == null && this.mHeadByteBuffer.position() == this.mHeadByteBuffer.limit()) {
			
			// 获得报文长度
			int iMsgLen = 0;
			try {
				
				// 对获取到的报文长度进行处理
				if (this.mHeadLen > 0) {
					// 增加对通讯长度为字节数组的支持
					if (this.bIsUseByteHeadLen == false) {
						iMsgLen = Integer.parseInt(new String(this.mHeadByteBuffer.array()));
					} else {
						iMsgLen = this.mHeadByteBuffer.getInt(0);
					}
				} else {
					throw new Exception("接口目前尚未开通对无报文长度结构的报文支持！");
				}
				
				
				// 校验报文长度是否合法
				if (iMsgLen < 0) {
					throw new NumberFormatException("接收到的的报文长度非法！");
				}
				
				// 生成报文缓冲区
				this.mReadByteBuffer = ByteBuffer.allocate(iMsgLen);
				this.mReadByteBuffer.clear();
			} catch (NumberFormatException e) {
				this.sendSocketClosedEvent();
				logger.error("解析通讯头长度发生异常，原因：" + e.getMessage(), e);
			}
		}
		
		
		/*
		 * 读取报文信息，而报文头部的长度表示报文体实际长度（排除报文头）
		 */
		if (this.mReadByteBuffer != null && this.mHeadByteBuffer.position() == this.mHeadByteBuffer.limit()) {
			
			int ret = -1 ;
			try {
				// 读取内容
				ret = this.mSocketChannel.read(this.mReadByteBuffer);
				if (ret <= -1) {
					this.sendSocketClosedEvent();
				} else {
					iReadBytes += ret;
				}
			} catch(Exception e) {
				this.sendSocketClosedEvent();
				logger.error("读取报文信息发生异常, 原因：" + e.getMessage(), e);
			}
			
			
			// 判断报文是否读取完毕，读取完毕后进行后续业务处理
			if (this.mReadByteBuffer.position() == this.mReadByteBuffer.limit()) {
				
				// 如果是短连接，则不再继续接收, 而且有通讯头的报文才不继续接收
				if (this.bIsShortLink == true && this.mHeadLen > 0) {
					this.registerEvent(0);
				}
				
				// 接着处理回调函数
				if (this.callReadFullMsgMethod != null && this.callReadFullMsgObject != null) {
					ByteBuffer unzipBuff = this.mReadByteBuffer;
					
					try {
						this.callReadFullMsgMethod.invoke(this.callReadFullMsgObject, new Object[] {
							this.mSocketChannel, unzipBuff
						});
					} catch(Exception e) {
						this.sendSocketClosedEvent();
						logger.error("读取报文信息完成时处理回调方法异常，原因：" + e.getMessage(), e);
					}
				}
				
				// 清除
				this.mHeadByteBuffer.clear();
				this.mReadByteBuffer = null;
				
				this.lastRecvDataTime = System.currentTimeMillis();
			}
		}
		
		/*
		 * 统计:本应该对本段代码进行同步，但考虑到同步对性能影响比较大，
		 * 同时对统计的值也不是要求非常精确，因此可以不同步
		 */
		if (this.bIsStatisticsDataStream) {
	
			long currTime = System.currentTimeMillis();
			long invTime = currTime - this.mStartTime;
			
			if (invTime > SSConst.CFG_COLLECT_DATA_STREAM_SIZE_TIME) {
				this.mStartTime = currTime;
				this.mWriteBytes = 0;
				this.mReadBytes  = 0;
			}
			
			this.mReadBytes += iReadBytes;	
		}

		return iReadBytes;
	}

	/**
	 * 添加待发送的报文，包括通信部分的4位长度
	 */
	public void addWriteMsg(StringBuffer sbMsg) {
		try {
			ByteBuffer msgBuff = ByteBuffer.wrap(sbMsg.toString().getBytes());
			addWriteMsg(msgBuff);
		} catch(Exception e) {
			this.sendSocketClosedEvent();
			logger.info("添加待发送的报文失败，yua" + e.getMessage(), e);
		}
	}
	
	/**
	 * 添加待发送的报文
	 */	
	public synchronized void addWriteMsg(ByteBuffer msgBuff) {
		ByteBuffer zipBuff = msgBuff;
		
		try {
			zipBuff.position(0);
			if (this.bIsWriteByEvent == true && this.bIsShortLink == false) {
				this.cqWriteMsg.add(zipBuff);
				this.registerEvent(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			} else {
				this.directWrite(zipBuff);
			}
			
			this.lastSendDataTime = System.currentTimeMillis();
		} catch(Exception cce) {
			this.sendSocketClosedEvent();
			logger.info("添加待发送的报文异常,原因：" + cce.getMessage(), cce);
		}
	}
	
	/**
	 * 直接输出，不使用事件
	 */
	private int directWrite(ByteBuffer vMsgBuff) throws Exception {		

		synchronized(this) {
			while (vMsgBuff.position() < vMsgBuff.limit()) {
				this.mSocketChannel.write(vMsgBuff);
			}
			
			this.sendSocketClosedEvent();
			
			if (this.bIsStatisticsDataStream) {
				long currTime = System.currentTimeMillis();
				long invTime = currTime - this.mStartTime;
				
				if (invTime > SSConst.CFG_COLLECT_DATA_STREAM_SIZE_TIME) {
					this.mStartTime = currTime;
					this.mWriteBytes = 0;
					this.mReadBytes  = 0;
				}
				
				this.mWriteBytes  += vMsgBuff.limit();
			}
		}
		
		return vMsgBuff.limit();
	}
	

	/**
	 * 将待发送的报文进行单包写操作,
	 * 一个大报文可能会分很多次写操作完成。
	 */
	public int doWrite() throws ClosedChannelException, Exception {	
		int iWriteBytes = 0;

		// 如果当前包已写完，则从队列中取出待写入的数据
		if (this.mWriteBuffer == null || (this.mWriteBuffer != null &&
			this.mWriteBuffer.position() >= this.mWriteBuffer.limit())) {
			this.mWriteBuffer = this.cqWriteMsg.get();
		} 
		
		// 还有待发送的数据，继续发送
		if (this.mWriteBuffer != null) {
			try {
				int ret = this.mSocketChannel.write(this.mWriteBuffer);
				if (ret == -1) {
					this.sendSocketClosedEvent();
				} else if ( ret == 0 ) {
					logger.error("写入队列缓冲区满:" + this.mSocketChannel.socket().toString());
				} else {
					iWriteBytes += ret;
				}
			} catch(Exception e) {
				logger.info("将待发送的报文进行单包写操作异常，原因:" + e.getMessage(), e);
			} finally {
				this.sendSocketClosedEvent();
			}
		} else {
			this.registerEvent(SelectionKey.OP_READ);
		}
		
		if (this.bIsStatisticsDataStream) {
			mWriteBytes += iWriteBytes;
		}
		
		return iWriteBytes;
	}
	
	/**
	 * 发送Socket关闭事件
	 */
	public void sendSocketClosedEvent() {
		try {
			this.registerEvent(0);
			this.mSocketChannel.close();
			
			if (this.callSocketClosedObject != null && this.callSocketClosedMethod != null) {	
				this.callSocketClosedMethod.invoke(this.callSocketClosedObject, new Object[] {
					this.mSocketChannel
				});
			}
		} catch(Exception e){
			logger.error("发送Socket关闭事件" + e.getMessage(), e);
		}
	}
		
	/**
	 * 获得选择器
	 * @return
	 */
	public Selector getSelector() {
		return this.mSelector;
	}
	
	/**
	 * 获得SocketChannel
	 * @return
	 */
	public SocketChannel getSocketChannel() {
		return this.mSocketChannel;
	}
	
	/** 
	 * 设置是否对Socket的通讯流量进行统计 
	 */
	public void setStatisticsDataStream(boolean b) {
		this.bIsStatisticsDataStream = b;
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
	 * 获得当前Socket的最近X分钟平均每秒读取的字节数（默认5分钟）
	 * @return
	 */
	public double getReadUseRate() {
		long currTime = System.currentTimeMillis();
		long invTime = currTime - this.mStartTime;
		if (invTime > SSConst.CFG_COLLECT_DATA_STREAM_SIZE_TIME) {
			this.mStartTime = currTime;
			this.mWriteBytes = 0;
			this.mReadBytes  = 0;
		}
		
		invTime = invTime/1000;
		
		if (invTime <= 0) {
			return this.mReadBytes;
		} else {
			return this.mReadBytes * 1.0 / (invTime * 1.0);
		}
	}
	
	/**
	 * 获得当前Socket的最近X分钟平均每秒输出的字节数（默认5分钟）
	 * @return
	 */
	public double getWriteUseRate() {
		long currTime = System.currentTimeMillis();
		long invTime = currTime - this.mStartTime;
		if (invTime > SSConst.CFG_COLLECT_DATA_STREAM_SIZE_TIME) {
			this.mStartTime = currTime;
			this.mWriteBytes = 0;
			this.mReadBytes  = 0;
		}
		
		invTime = invTime/1000;
		
		if (invTime <= 0) {
			return this.mWriteBytes;
		} else {
			return this.mWriteBytes * 1.0 / (invTime * 1.0);
		}
	}
	
	/**
	 * 获得当前Socket的利用率 （读入总字节＋写入总字节）/ 计量时间（秒）
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
	 * 写入数据使用事件触发完成
	 * @param ops
	 * @throws Exception
	 */
	public synchronized void registerEvent(int ops) throws Exception {
		
		if (this.iCurrOps == ops) {
			return;
		} else {
			this.iCurrOps = ops;
		}
		
		if (this.bIsWriteByEvent == true) {
			if (this.mSckChlEventChangeQueue == null) {
				throw new Exception("对SocketChannel注册事件时.");
			}
				
			if (!this.mSocketChannel.isOpen() && ops != 0) {
				this.sendSocketClosedEvent();
			} else {
				this.mSckChlEventChangeQueue.add(new Object[] {
					this.mSocketChannel,Integer.parseInt("" + ops)
				});
				
				if (this.mSckChlEventChangeQueue.peek() != null) {
					this.mSelector.wakeup();
				}
			}
		} else {
			this.mSocketChannel.register(this.mSelector, ops);
		}
	}

	/**
	 * 添加附加到当前Socket上的对象，以便区分不同类型的Socket
	 * @param obj
	 */
	public void setAttachObject(Object obj) {
		this.mAttachObj = obj;
	}
	
	/**
	 * 获得附加到当前Socket上的对象
	 * @return 如果返回null，代表没有
	 */
	public Object getAttachObject() {
		return this.mAttachObj;
	}
	
	public SelectionKey getSelectionKey() {
		return this.mSelectionKey;
	}
	
	/** 
	 * 最后发送数据时间
	 * @return
	 */
	public long getLastSendDataTime() {
		return this.lastSendDataTime;
	}
	
	/**
	 * 最后接收数据时间
	 * @return
	 */
	public long getLastRecvDataTime() {
		return this.lastRecvDataTime;
	}
	
	/**
	 * 检查是否可发送心跳包报文
	 * @multiple 默认心跳包超时时间的倍数，如：默认心跳包时间为5秒，若输入参数为2，则实际判断是否可发送心跳包的时间为10秒。
	 * @return
	 */
	public boolean checkCanSendTestMsg(int multiple) {
		long currTime = System.currentTimeMillis();
		
		if (this.lastRecvDataTime <= 0) {
			this.lastRecvDataTime = currTime;
		}
		
		if (this.lastSendDataTime <= 0) {
			this.lastSendDataTime = currTime;
		}
		
		if ((currTime - this.lastRecvDataTime) >= (SSConst.CFG_SOCKET_TEST_INV_TIME * multiple * 1000 - 100) 
			&& (currTime - this.lastSendDataTime) >= (SSConst.CFG_SOCKET_TEST_INV_TIME * multiple * 1000 - 100)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 根据最后接收数据的时间，检查Socket是否已超时
	 * @multiple 默认心跳包超时时间的倍数，如：默认心跳包时间为5秒，若输入参数为30，则如果超过150秒没有接收到数据，则判为超时
	 */
	public boolean checkIsRecvDataTimeOut(int multiple) {
		long currTime = System.currentTimeMillis();
		
		if (this.lastRecvDataTime <= 0) {
			this.lastRecvDataTime = currTime;
		}
		
		if (this.lastSendDataTime <= 0) {
			this.lastSendDataTime = currTime;
		}
		
		if ((currTime - this.lastRecvDataTime) >= (SSConst.CFG_SOCKET_TEST_INV_TIME * multiple * 1000 - 100)) { 
			return true;
		} else { 
			return false;
		}
	}
	
	public String toString() {
		return this.mSocketChannel.socket().toString() 
			+ ",平均流量 = " + this.getUseRate() 
			+ " 字节/秒,最后接收数据时间=" + this.lastRecvDataTime 
			+ ",最后发送数据时间=" + this.lastSendDataTime ;
	}
	
	/**
	 * 是否正在发送
	 */	
	public boolean isSending() {
		return mWriteBuffer!=null;
	}
}

