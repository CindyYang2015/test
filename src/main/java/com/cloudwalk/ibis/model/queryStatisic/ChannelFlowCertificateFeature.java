package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * Project Name: ibis-v1.2
 * File Name: ChannelFlowCertificateFeature.java
 * Package Name:com.cloudwalk.ibis.model.queryStatisic
 * Description: 证脸对比
 * @date : 2017年2月23日 下午2:18:21 
 * @author: Hu Yuxin
 * @version: V1.0 
 * @since: 1.6
 * *******************************************************************************
 * 序号  修改时间  修改人  修改内容
 *  1
 *  2
 * *******************************************************************************
 * @Copyright: @ 2010-2016 重庆中科云丛科技有限公司  All Rights Reserved.
 */
public class ChannelFlowCertificateFeature implements Serializable{
    /** 
	 * @Fields serialVersionUID : 序列化ID
	*/ 
	private static final long serialVersionUID = 3226413066874996129L;
	
	/**
	 * FLOW_ID 流水id
	 */
	private String flowId;

	/**
	 * PERSON_ID  客户ID
	 */
    private String personId;
    
    /**
     * FEATURE_ID  客户特征ID
     */
    private String featureId;

    /**
     * CMPSCORE 比对分数0-1,生物文件与特征库比对
     */
    private BigDecimal cmpscore;

    /**
     * FEATURE_FILE_PATH 特征库生物文件路径
     */
    private String featureFilePath;

    /**
     * FILE_PATH 生物文件路径
     */
    private String filePath;

    /**
     * REMARK 备注
     */
    private String remark;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId == null ? null : flowId.trim();
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId == null ? null : personId.trim();
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId == null ? null : featureId.trim();
    }

    public BigDecimal getCmpscore() {
        return cmpscore;
    }

    public void setCmpscore(BigDecimal cmpscore) {
        this.cmpscore = cmpscore;
    }

    public String getFeatureFilePath() {
        return featureFilePath;
    }

    public void setFeatureFilePath(String featureFilePath) {
        this.featureFilePath = featureFilePath == null ? null : featureFilePath.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public String getRemark() {
        return remark;
    }

	public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	@Override
	public String toString() {
		return "ChannelFlowCertificateFeature [flowId=" + flowId
				+ ", personId=" + personId + ", featureId=" + featureId
				+ ", cmpscore=" + cmpscore + ", featureFilePath="
				+ featureFilePath + ", filePath=" + filePath + ", remark="
				+ remark + "]";
	}
	
}