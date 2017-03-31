package com.cloudwalk.common.engine.face.eoc;

import java.util.List;

/**
 * 相识度结果分数
 * @author 何春节
 * @version 1.0
 */
public class Compare extends EOCResult {
	private static final long serialVersionUID = -4944844848302925326L;
	
	/**
	 * 相识度分数
	 */
	protected double score;
	
	/**
	 * 相识的人脸列表
	 */
	private List<Face> faces;

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public List<Face> getFaces() {
		return faces;
	}

	public void setFaces(List<Face> faces) {
		this.faces = faces;
	}
}
