package com.cloudwalk.common.engine.face.eoc;

/**
 * 人脸质量分析结果
 * @author 何春节
 * @version 1.0
 *
 */
public class Quality extends EOCResult {
	private static final long serialVersionUID = 4488868893067474724L;
	
	/**
	 * 质量评分
	 */
	public double score;

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
}
