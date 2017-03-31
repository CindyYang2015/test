package com.cloudwalk.ibis.model.result.ver001;

import com.cloudwalk.common.common.InterfaceConst;
import com.cloudwalk.ibis.model.RecogRequest;

/**
 * 调用接口响应结果类
 * @author zhuyf
 *
 */
public class Result implements java.io.Serializable {
	private static final long serialVersionUID = -6963503022738848863L;

	//响应码 0000成功 其他 错误
	private String code;
	//响应消息
	private String message;	
	
	/**
	 * 请求报文实体
	 */
	private RecogRequest request;
	
	/**
	 * 业务数据对象
	 */	
	private Data data;
	
	public Result() {
	}
	
	public Result(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
		
	/**
	 * 获取属性：返回码
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 设置属性： 返回码
	 * 
	 * @param
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 获取属性：返回信息
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}	
	
	public RecogRequest getRequest() {
		return request;
	}

	public void setRequest(RecogRequest request) {
		this.request = request;
	}

	/**
	 * 设置属性：返回信息
	 * 
	 * @param
	 */
	public void setMessage(String message) {
		this.message = message;
	}		

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * 初始化基本的成功实体对象
	 */
	public static Result initSuccessResult() {
		Result result = new Result(InterfaceConst.SUCCESS,"识别认证成功");
		return result;
	}
	
	/**
	 * 初始化基本的失败实体对象
	 */
	public static Result initFailResult() {
		Result result = new Result(InterfaceConst.SERVICE_EXCEPTIONE,"识别认证失败");
		return result;
	}
	
	/**
	 * 初始化基本的识别认证不通过的实体对象
	 */
	public static Result initRecogFailResult() {
		Result result = new Result(InterfaceConst.RECOG_FAIL,"识别分数小于阈值");
		return result;
	}
}
