package com.cloudwalk.common.engine.face.eoc;
/**
 * 去掉网纹返回的结果
 * @author 何春节
 * @version 1.0
 *
 */
public class Reticulate extends EOCResult {
	private static final long serialVersionUID = 7815819006792419631L;
	
	/**
	 * 去掉网纹的图片
	 */
	private String img;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}
