package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;

/**
 * 
 * Project Name: ibis-v1.2
 * File Name: ChannelFlowSearchPerson.java
 * Package Name:com.cloudwalk.ibis.model.queryStatisic
 * Description: 按证检索
 * @date : 2017年2月23日 下午2:13:42 
 * @author: 张宇超
 * @version: V1.0 
 * @since: 1.6
 * *******************************************************************************
 * 序号  修改时间  修改人  修改内容
 *  1
 *  2
 * *******************************************************************************
 * @Copyright: @ 2010-2016 重庆中科云丛科技有限公司  All Rights Reserved.
 */
public class ChannelFlowSearchPerson implements Serializable{
	
    /** 
	 * @Fields serialVersionUID : 序列化ID
	*/ 
	private static final long serialVersionUID = 274416389677375231L;
	
	/**
	 * FLOW_ID 流水ID
	 */
	private String flowId;
	
	/**
	 * PERSON_ID 客户ID
	 */
    private String personId;
    
	/**
	 * FEATURE_ID 客户特征ID
	 */
    private String featureId;
    
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	@Override
	public String toString() {
		return "ChannelFlowSearchPerson [flowId=" + flowId + ", personId="
				+ personId + ", featureId=" + featureId + ", remark=" + remark
				+ "]";
	}
    
}