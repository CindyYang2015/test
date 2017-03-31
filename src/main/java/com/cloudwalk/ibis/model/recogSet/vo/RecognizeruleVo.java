package com.cloudwalk.ibis.model.recogSet.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class RecognizeruleVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String inputRecogstepId;

    private String inputEngineCode;

    private Short inputStatus;

    private BigDecimal inputScore;

    private String inputRemark;

	public String getInputRecogstepId() {
		return inputRecogstepId;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public void setInputRecogstepId(String inputRecogstepId) {
		this.inputRecogstepId = inputRecogstepId;
	}

	public String getInputEngineCode() {
		return inputEngineCode;
	}

	public void setInputEngineCode(String inputEngineCode) {
		this.inputEngineCode = inputEngineCode;
	}

	public Short getInputStatus() {
		return inputStatus;
	}

	public void setInputStatus(Short inputStatus) {
		this.inputStatus = inputStatus;
	}

	public BigDecimal getInputScore() {
		return inputScore;
	}

	public void setInputScore(BigDecimal inputScore) {
		this.inputScore = inputScore;
	}

	public String getInputRemark() {
		return inputRemark;
	}

	public void setInputRemark(String inputRemark) {
		this.inputRemark = inputRemark;
	}

	@Override
	public String toString() {
		return "RecognizeruleVo [id=" + id + ", inputRecogstepId="
				+ inputRecogstepId + ", inputEngineCode=" + inputEngineCode
				+ ", inputStatus=" + inputStatus + ", inputScore=" + inputScore
				+ ", inputRemark=" + inputRemark + "]";
	}
    
    
    
}
