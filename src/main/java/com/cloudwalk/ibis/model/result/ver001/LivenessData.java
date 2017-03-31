package com.cloudwalk.ibis.model.result.ver001;

/**
 * 身份证信息
 * @author zhuyf
 *
 */
public class LivenessData extends Data {
	
	
	private static final long serialVersionUID = -1023986344100451666L;
	
	//活体最佳人脸
	private String img;

	public String getImg()
	{
		return img;
	}

	public void setImg(String img)
	{
		this.img = img;
	}
	
	

}
