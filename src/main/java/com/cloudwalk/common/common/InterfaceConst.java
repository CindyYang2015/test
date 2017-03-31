package com.cloudwalk.common.common;

/**
 * 接口相关常量配置
 * @author zhuyf
 *
 */
public class InterfaceConst {
	
	/**
	 * 编码
	 */
	public static final String ENCODING = "UTF-8";	
	
	/**
	 * 接口返回成功编码
	 */
	public static final String SUCCESS = "0000";
	
	/**
	 * 系统服务异常
	 */
	public static final String SERVICE_EXCEPTIONE = "5001";
	
	/**
	 * 系统异常
	 */
	public static final String EXCEPTIONE = "5002";
	
	/**
	 * 空指针异常
	 */
	public static final String NULL_EXCE = "5000";
	
	/**
	 * 算法引擎异常
	 */
	public static final String ENGINE_EXCE = "5003";
	
	/**
	 * 识别认证不通过,针对于识别分数小于阈值
	 */
	public static final String RECOG_FAIL = "5004";

	/**
	 * 数据解析异常json
	 */
	public static final String PARSE_JSON_EXCEPTION = "{\"code\":\"5002\",\"message\":\"系统异常,请与管理员联系\"}";
	
	/**
	 * 数据解析异常XML
	 */
	public static final String PARSE_XML_EXCEPTION = "<response><code></code>5002<message>系统异常,请与管理员联系</message></response>";
		
	
}
