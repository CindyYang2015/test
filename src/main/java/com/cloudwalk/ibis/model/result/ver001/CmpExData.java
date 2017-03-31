package com.cloudwalk.ibis.model.result.ver001;

/**
 * 三个文件比对的数据结果
 * @author zhuyf
 *
 */
public class CmpExData extends Data {

	private static final long serialVersionUID = 6033558491199009014L;
	
	/**
	 * 比对分数1
	 */
	private Double sim1Score;
	
	/**
	 * 比对分数2
	 */
	private Double sim2Score;
	
	public CmpExData() {}

	public CmpExData(Double sim1Score, Double sim2Score) {
		super();
		this.sim1Score = sim1Score;
		this.sim2Score = sim2Score;
	}

	public Double getSim1Score() {
		return sim1Score;
	}

	public void setSim1Score(Double sim1Score) {
		this.sim1Score = sim1Score;
	}

	public Double getSim2Score() {
		return sim2Score;
	}

	public void setSim2Score(Double sim2Score) {
		this.sim2Score = sim2Score;
	}
	
}
