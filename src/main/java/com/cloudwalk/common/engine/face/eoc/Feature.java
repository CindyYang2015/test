package com.cloudwalk.common.engine.face.eoc;
/**
 * 人脸特征提取返回结果
 * @author 何春节
 * @version 1.0
 *
 */
public class Feature extends EOCResult {
	private static final long serialVersionUID = -6741346935642902487L;

	/**
	 * 人脸特征数据
	 */
	private String feature;

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}
}
