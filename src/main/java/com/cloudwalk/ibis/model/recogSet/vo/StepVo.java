package com.cloudwalk.ibis.model.recogSet.vo;

import java.io.Serializable;

public class StepVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7340858490395975256L;
	
	private String id;
	private String stepName;
	private String remark;
	private String engineIds;
	private String engineNum;//引擎数量
	private String engineNames;//引擎名称多个以,隔开
	private String legalOrgCode;//机构代码（法人机构）全路径
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEngineIds() {
		return engineIds;
	}
	public void setEngineIds(String engineIds) {
		this.engineIds = engineIds;
	}
	public String getEngineNum() {
		return engineNum;
	}
	public void setEngineNum(String engineNum) {
		this.engineNum = engineNum;
	}
	public String getEngineNames() {
		return engineNames;
	}
	public void setEngineNames(String engineNames) {
		this.engineNames = engineNames;
	}
	public String getLegalOrgCode() {
		return legalOrgCode;
	}
	public void setLegalOrgCode(String legalOrgCode) {
		this.legalOrgCode = legalOrgCode;
	}

	
	
	
}
