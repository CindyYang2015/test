package com.cloudwalk.common.engine.face.eoc;

/**
 * 查询组中人脸数
 * @author 何春节
 * @version 1.0
 *
 */
public class Query extends EOCResult {
	private static final long serialVersionUID = 1317965299487724872L;

	/**
	 * 组中人脸数
	 */
	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
