package com.cloudwalk.common.engine.face.eoc;

import java.util.List;

/**
 * 人脸识别返回结果
 * @author 何春节
 * @version 1.0
 */
public class Identify extends EOCResult {
	private static final long serialVersionUID = 1553635088142123028L;

	/**
	 * 返回识别的人脸列表
	 */
	private List<Face> faces;

	public List<Face> getFaces() {
		return faces;
	}

	public void setFaces(List<Face> faces) {
		this.faces = faces;
	}
}
