package com.cloudwalk.ibis.model.result.ver001;

/**
 * 两个文件比对的数据结果
 * @author zhuyf
 *
 */
public class CmpData extends Data {

	private static final long serialVersionUID = -3289486761628363515L;
	
	/**
	 * 比对分数
	 */
	private Double simScore;
	
	public CmpData(){}

	public CmpData(Double simScore) {
		super();
		this.simScore = simScore;
	}

	public Double getSimScore() {
		return simScore;
	}

	public void setSimScore(Double simScore) {
		this.simScore = simScore;
	}
	
	

}
