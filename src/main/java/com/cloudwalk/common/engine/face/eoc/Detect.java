package com.cloudwalk.common.engine.face.eoc;

import java.util.List;

/**
 * 人脸检测返回结果对象
 * @author 何春节
 * @version 1.0
 */
public class Detect extends EOCResult {
	private static final long serialVersionUID = 2706311477059717564L;

	/**
	 * 返回检测到的人脸，其中的数据包括 
	 */
	private List<Face> faces;

	public List<Face> getFaces() {
		return faces;
	}

	public void setFaces(List<Face> faces) {
		this.faces = faces;
	}
}
