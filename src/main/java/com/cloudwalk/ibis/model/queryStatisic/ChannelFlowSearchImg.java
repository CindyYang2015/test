package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;
/**
 * 
 * Project Name: ibis-v1.2
 * File Name: ChannelFlowSearchImg.java
 * Package Name:com.cloudwalk.ibis.model.queryStatisic
 * Description: 按脸检索
 * @date : 2017年2月23日 下午2:13:42 
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
public class ChannelFlowSearchImg implements Serializable{
    /** 
	 * @Fields serialVersionUID : 序列化ID
	*/ 
	private static final long serialVersionUID = -3928000130614828864L;

	/**
	 * FLOW_ID 流水id
	 */
	private String flowId;
	
	/**
	 * FILE_PATH 检索文件路径
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
		this.flowId = flowId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "ChannelFlowSearchImg [flowId=" + flowId + ", filePath="
				+ filePath + ", remark=" + remark + "]";
	}
    
}