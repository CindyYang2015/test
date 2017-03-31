package com.cloudwalk.ibis.socket;
/**
 * Socket server 常量定义
 * @author hechu
 */
public interface SSConst {
	
	/*
	 * 默认字符集
	 */
	public static final String DEFAULT_CHARSET = "utf-8"; 
//	public static final String DEFAULT_CHARSET = "gbk"; 
	
	/*
	 * 默认报头长度
	 */
	public static final int MESSAGE_HEAD_LENGTH = 8;

	/* 
	 * 单包写入的字节数，将大的数据包分多次写，默认2048 
	 */
	public final  static int CFG_SINGLE_BUFF_SIZE_WRITE = 2048;
	
	/* 
	 * 统计数据流量的单位时间(计量单位:毫秒)，默认5*60*1000 
	 */
	public final  static int CFG_COLLECT_DATA_STREAM_SIZE_TIME = 5*60*1000;
	
	/* 
	 * 不需要压缩响应报文的最大字节数
	 */
	public static int CFG_NOT_ZIP_MAX_SIZE = 500;
	
	
	/*
	 * Socket心跳包发送的间隔时间(秒) 
	 */
	public static int CFG_SOCKET_TEST_INV_TIME  = 5;
	
	/*
	 * Socket尝试连接的时间间隔（秒） 
	 */
	public static int CFG_SOCKET_LINK_INV_TIME = 3;

	/*
	 * Socket主动断开连接的超时时间（秒） 
	 */
	public static int CFG_SOCKET_TIMEOUT_TIME = 99999999;

	
	/*
	 * 报文接收超时时间（秒）
	 */
	public static int CFG_RECV_REQ_MSG_OVER_TIME = 20;
	
	/*
	 * 行情入数据库的间隔时间（分）
	 */
	public static int CFG_QUOTATION_UPDATE_TIME = 20;
}
