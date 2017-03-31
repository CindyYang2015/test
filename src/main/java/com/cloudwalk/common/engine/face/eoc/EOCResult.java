package com.cloudwalk.common.engine.face.eoc;

/**
 * 云之眼返回公共结果
 * @author 何春节
 * @version 1.0
 */
public class EOCResult implements EOC{
	private static final long serialVersionUID = -4759334298625915666L;

	/**
	 * 返回结果，0表示成功，非0为对应错误号
	 */
	protected int result;
	
	/**
	 * 消息描述
	 */
	protected String info;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
