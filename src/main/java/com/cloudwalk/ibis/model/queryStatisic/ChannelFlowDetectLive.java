package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;
/**
 * ClassName: ChannelFlowDetectLive <br/>
 * Description: TODO Description. <br/>
 * date: Feb 27, 2017 11:33:46 AM <br/>
 *
 * @author 杨维龙
 * @version 
 * @since JDK 1.7
 */
public class ChannelFlowDetectLive implements Serializable {
	/**
	 * @Fields serialVersionUID : 序列化ID
	 */
	private static final long serialVersionUID = 3226413066874996129L;
	/**
	 * FLOW_ID 流水id
	 */
	private String flowId;
	/**
	 * IMGPATH 最佳人脸图片地址
	 */
	private String imgpath;
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

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath == null ? null : imgpath.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}
}