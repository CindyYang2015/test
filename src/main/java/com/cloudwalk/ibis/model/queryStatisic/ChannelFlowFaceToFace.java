package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * Project Name: ibis-v1.2
 * File Name: ChannelFlowFaceToFace.java
 * Package Name:com.cloudwalk.ibis.model.queryStatisic
 * Description: 脸脸对比流水表对应实体类
 * @date : 2017年2月23日 下午2:05:41 
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
public class ChannelFlowFaceToFace implements Serializable{
	
    /** 
	 * @Fields serialVersionUID : 序列化ID
	*/ 
	private static final long serialVersionUID = 271204072003979963L;

	/**
	 * FLOW_ID 流水id
	 */
	private String flowId;
	
	/**
	 * FILEONE_PATH 注册生物文件1路径
	 */
    private String fileonePath;
    
    /**
     * FILETWO_PATH 注册生物文件2路径
     */
    private String filetwoPath;

    /**
     * CMPSCORE 比对分数0-1,生物文件1与生物文件2比对
     */
    private BigDecimal cmpscore;
    
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

    public String getFileonePath() {
        return fileonePath;
    }

    public void setFileonePath(String fileonePath) {
        this.fileonePath = fileonePath == null ? null : fileonePath.trim();
    }

    public String getFiletwoPath() {
        return filetwoPath;
    }

    public void setFiletwoPath(String filetwoPath) {
        this.filetwoPath = filetwoPath == null ? null : filetwoPath.trim();
    }

    public BigDecimal getCmpscore() {
        return cmpscore;
    }

    public void setCmpscore(BigDecimal cmpscore) {
        this.cmpscore = cmpscore;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	@Override
	public String toString() {
		return "ChannelFlowFaceToFace [flowId=" + flowId + ", fileonePath="
				+ fileonePath + ", filetwoPath=" + filetwoPath + ", cmpscore="
				+ cmpscore + ", remark=" + remark + "]";
	}
    
}