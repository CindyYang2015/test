package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;
import java.math.BigDecimal;

public class ChannelFlowCompareStrengthen implements Serializable{
	/**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = 4269416158052337256L;
    /**
     * FLOW_ID
     * 流水id 主键
     */
    private String flowId;
    /**
     * PERSON_ID	
     * 客户ID
     */
    private String personId;
    /**
     * FEATURE_ID
     * 客户特征ID
     */
    private String featureId;
    /**
     * FPONE_CMPSCORE
     * 比对分数0-1,生物文件1与特征库比对
     */
    private BigDecimal fpOneCmpscore;
    /**
     * FPTWO_CMPSCORE
     * 比对分数0-1,生物文件2与特征库比对
     */
    private BigDecimal fpTwoCmpscore;
    /**
     * FEATURE_FILE_PATH
     * 特征库生物文件路径
     */
    private String featureFilePath;
    /**
     * FILEONE_PATH
     * 生物文件1路径
     */
    private String fileOnePath;
    /**
     * FILETWO_PATH
     * 生物文件2路径
     */
    private String fileTwoPath;
    /**
     * REMARK
     * 备注
     */
    private String remark;
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public BigDecimal getFpOneCmpscore() {
		return fpOneCmpscore;
	}
	public void setFpOneCmpscore(BigDecimal fpOneCmpscore) {
		this.fpOneCmpscore = fpOneCmpscore;
	}

	public BigDecimal getFpTwoCmpscore() {
		return fpTwoCmpscore;
	}
	public void setFpTwoCmpscore(BigDecimal fpTwoCmpscore) {
		this.fpTwoCmpscore = fpTwoCmpscore;
	}
	public String getFeatureFilePath() {
		return featureFilePath;
	}
	public void setFeatureFilePath(String featureFilePath) {
		this.featureFilePath = featureFilePath;
	}
	public String getFileOnePath() {
		return fileOnePath;
	}
	public void setFileOnePath(String fileOnePath) {
		this.fileOnePath = fileOnePath;
	}
	public String getFileTwoPath() {
		return fileTwoPath;
	}
	public void setFileTwoPath(String fileTwoPath) {
		this.fileTwoPath = fileTwoPath;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

   
}