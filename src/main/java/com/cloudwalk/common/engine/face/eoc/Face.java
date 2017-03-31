package com.cloudwalk.common.engine.face.eoc;
/**
 * 人脸对象
 * @author 何春节
 * @version 1.0
 */
public class Face implements EOC {
	private static final long serialVersionUID = 1847766475822258737L;

	/**
	 * 人脸x坐标
	 */
	private int x;
	
	/**
	 * 人脸y坐标
	 */
	private int y;
	
	/**
	 * 人脸宽度
	 */
	private int width;
	
	/**
	 * 人脸高度
	 */
	private int heigh;
	
	/**
	 * Base64 编码的人脸图片数据
	 */
	private String img;
	
	/**
	 * 年龄
	 */
	private String age;
	
	/**
	 * 性别 -1表示女，1表示男 
	 */
	private String gender;
	
	/**
	 * 和第一张人脸的相似度 
	 */
	private float score;
	
	/**
	 * 注册的人脸编号
	 */
	private String faceId;
	

	public String getFaceId() {
		return faceId;
	}

	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeigh() {
		return heigh;
	}

	public void setHeigh(int heigh) {
		this.heigh = heigh;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
}
