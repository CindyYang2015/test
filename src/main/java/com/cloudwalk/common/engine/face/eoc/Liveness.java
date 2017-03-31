package com.cloudwalk.common.engine.face.eoc;

import com.cloudwalk.common.engine.face.eoc.EOCResult;

/**
 * 活体信息
 * @author 罗磊
 *
 */
public class Liveness extends EOCResult
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4373580622077332779L;
	//活体最佳人脸
	private String img;
	
	//是否活体
	private String param;
		
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
