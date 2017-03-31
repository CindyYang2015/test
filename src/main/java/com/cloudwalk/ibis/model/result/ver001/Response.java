package com.cloudwalk.ibis.model.result.ver001;

import com.cloudwalk.ibis.model.result.ver001.Result;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 接口交易结果
 * @author zhuyf
 *
 */
@XStreamAlias("response")
public class Response implements java.io.Serializable{
	
	private static final long serialVersionUID = 2660476988061083483L;
	
	//响应码 1成功 0失败
	private int code;
	//响应消息
	private String message;
	//响应结果
	private Result result;	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	
	/**
	 * 获取成功的返回结果对象
	 * @param result
	 * @return
	 */
	public static Response getSuccessResponse(Result result) {
		Response response = new Response();
		response.setCode(1);
		response.setMessage("交易成功");
		response.setResult(result);
		return response;
	}
	
	/**
	 * 获取失败的返回结果对象
	 * @param result
	 * @return
	 */
	public static Response getFailResponse(Result result) {
		Response response = new Response();
		response.setCode(0);
		response.setMessage("交易失败");
		response.setResult(result);
		return response;
	}

}
