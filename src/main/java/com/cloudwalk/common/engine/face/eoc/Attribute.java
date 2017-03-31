package com.cloudwalk.common.engine.face.eoc;

import java.util.List;

/**
 * 人脸属性分析返回对象
 * @author 何春节
 * @version 1.0
 */
public class Attribute extends EOCResult {
	private static final long serialVersionUID = 3534397642306871704L;

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
