package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;

/**
 * 
 * Project Name: ibis-v1.2
 * File Name: ChannelFlowBank.java
 * Package Name:com.cloudwalk.ibis.model.queryStatisic
 * Description: 银行卡OCR
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
public class ChannelFlowBank implements Serializable{
	
    /** 
	 * @Fields serialVersionUID : 序列化ID
	 */ 
	private static final long serialVersionUID = -4617820617681890646L;
	
	/**
	 * FLOW_ID 流水ID
	 */
	private String flowId;
	
	/**
	 * IMGPATH 银行卡图片路径
	 */
    private String imgpath;
    
	/**
	 * BANK_CARD_TYPE 银行卡种类（借记卡、贷记卡）
	 */
    private String bankCardType;
    
	/**
	 * BANK_NAME 发卡行名称
	 */
    private String bankName;
    
	/**
	 * CARD_NAME 卡名
	 */
    private String cardName;
    
	/**
	 * BANK_CARD_NO 银行卡卡号
	 */
    private String bankCardNo;
    
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

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType == null ? null : bankCardType.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName == null ? null : cardName.trim();
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo == null ? null : bankCardNo.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	@Override
	public String toString() {
		return "ChannelFlowBank [flowId=" + flowId + ", imgpath=" + imgpath
				+ ", bankCardType=" + bankCardType + ", bankName=" + bankName
				+ ", cardName=" + cardName + ", bankCardNo=" + bankCardNo
				+ ", remark=" + remark + "]";
	}
    
}