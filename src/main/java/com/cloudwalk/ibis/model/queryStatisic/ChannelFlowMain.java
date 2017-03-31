package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;

public class ChannelFlowMain implements Serializable {
	
	/**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = 4269416158052337256L;
    
    private String flowId;

    private String legalOrgCode;

    private String channel;

    private String busCode;

    private String interVerCode;

    private String tradingCode;

    private String engineType;

    private String engineCode;

    private String engineVerCode;

    private String recogstepId;

    private Short result;

    private String tradingFlowNo;

    private String tradingDate;

    private String tradingTime;

    private String equipmentNo;

    private String organizationNo;

    private String tellerNo;

    private Long flowNum;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId == null ? null : flowId.trim();
    }

    public String getLegalOrgCode() {
        return legalOrgCode;
    }

    public void setLegalOrgCode(String legalOrgCode) {
        this.legalOrgCode = legalOrgCode == null ? null : legalOrgCode.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

   

    public String getTradingCode() {
        return tradingCode;
    }

    public void setTradingCode(String tradingCode) {
        this.tradingCode = tradingCode == null ? null : tradingCode.trim();
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType == null ? null : engineType.trim();
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode == null ? null : engineCode.trim();
    }


    public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	public String getInterVerCode() {
		return interVerCode;
	}

	public void setInterVerCode(String interVerCode) {
		this.interVerCode = interVerCode;
	}

	public String getEngineVerCode() {
		return engineVerCode;
	}

	public void setEngineVerCode(String engineVerCode) {
		this.engineVerCode = engineVerCode;
	}

	public String getRecogstepId() {
        return recogstepId;
    }

    public void setRecogstepId(String recogstepId) {
        this.recogstepId = recogstepId == null ? null : recogstepId.trim();
    }

    public Short getResult() {
        return result;
    }

    public void setResult(Short result) {
        this.result = result;
    }

    public String getTradingFlowNo() {
        return tradingFlowNo;
    }

    public void setTradingFlowNo(String tradingFlowNo) {
        this.tradingFlowNo = tradingFlowNo == null ? null : tradingFlowNo.trim();
    }

    public String getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(String tradingDate) {
        this.tradingDate = tradingDate == null ? null : tradingDate.trim();
    }

    public String getTradingTime() {
        return tradingTime;
    }

    public void setTradingTime(String tradingTime) {
        this.tradingTime = tradingTime == null ? null : tradingTime.trim();
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo == null ? null : equipmentNo.trim();
    }

    public String getOrganizationNo() {
        return organizationNo;
    }

    public void setOrganizationNo(String organizationNo) {
        this.organizationNo = organizationNo == null ? null : organizationNo.trim();
    }

    public String getTellerNo() {
        return tellerNo;
    }

    public void setTellerNo(String tellerNo) {
        this.tellerNo = tellerNo == null ? null : tellerNo.trim();
    }

    public Long getFlowNum() {
        return flowNum;
    }

    public void setFlowNum(Long flowNum) {
        this.flowNum = flowNum;
    }
}