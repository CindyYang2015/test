package com.cloudwalk.ibis.service.base.engine;

/**
 * 检索特征信息
 * @author zhuyf
 *
 */
public class SearchFeature {

	/**
	 * 检索特征ID
	 */
	private String featureId;
	
	/**
	 * 检索特征比对的分数
	 */
	private float score;

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	
}
