package com.cloudwalk.common.engine.face.eoc;
/**
 * 人脸特征提取返回结果
 * @author 何春节
 * @version 1.0
 *
 */
public class MultimodelFeature extends EOCResult {

	private static final long serialVersionUID = -1274356804051970788L;
	
	/**
	 * 人脸特征数据
	 */
	private String feature;
	
	/**
	 * 图片类型  1-高清, 2-水印
	 */
	private int type;

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
