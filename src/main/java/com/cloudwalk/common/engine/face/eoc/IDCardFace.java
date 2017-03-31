package com.cloudwalk.common.engine.face.eoc;

/**
 * ocr人脸信息
 * @author zhuyf
 *
 */
public class IDCardFace {

	//人脸 在身份证中 在身份证中 在身份证中 在身份证中 x坐
	private int left;
	
	//人脸宽度
	private int width;
	
	//人脸高度
	private int height;
	
	//人脸 在身份证中 在身份证中 在身份证中 在身份证中 y
	private int top;
	
	//人脸 图片 (base64编码 )
	private String image;

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	
}
