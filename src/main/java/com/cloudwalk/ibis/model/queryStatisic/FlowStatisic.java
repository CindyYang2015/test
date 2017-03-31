package com.cloudwalk.ibis.model.queryStatisic;

/**
 * 流水成功，失败统计对象
 * @author zhuyf
 *
 */
public class FlowStatisic {

	/**
	 * 1成功 0失败
	 */
	private int status;
	
	/**
	 * 统计的次数
	 */
	private int counts;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}
	
	
}
