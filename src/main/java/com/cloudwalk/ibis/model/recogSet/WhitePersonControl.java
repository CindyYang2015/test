package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;

import com.cloudwalk.common.util.ObjectUtils;

public class WhitePersonControl implements Serializable {
	public WhitePersonControl() {
    	String uuid = ObjectUtils.createUUID();
    	this.id = uuid;
    }
	/**
	 * 主键
	 */
    private String id;
    /**
	 * 白名单ID
	 */
    private String whiteId;
    /**
	 * 渠道（对应字典）
	 */
    private String channel;
    /**
	 * 交易代码（对应字典）
	 */
    private String tradingCode;
    /**
	 * 机构代码（法人机构）全路径
	 */
    private String legalOrgCode;    
    

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getWhiteId() {
        return whiteId;
    }

    public void setWhiteId(String whiteId) {
        this.whiteId = whiteId == null ? null : whiteId.trim();
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

    public String getLegalOrgCode() {
        return legalOrgCode;
    }

    public void setLegalOrgCode(String legalOrgCode) {
        this.legalOrgCode = legalOrgCode == null ? null : legalOrgCode.trim();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        WhitePersonControl other = (WhitePersonControl) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getWhiteId() == null ? other.getWhiteId() == null : this.getWhiteId().equals(other.getWhiteId()))
            && (this.getChannel() == null ? other.getChannel() == null : this.getChannel().equals(other.getChannel()))
            && (this.getTradingCode() == null ? other.getTradingCode() == null : this.getTradingCode().equals(other.getTradingCode()))
            && (this.getLegalOrgCode() == null ? other.getLegalOrgCode() == null : this.getLegalOrgCode().equals(other.getLegalOrgCode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWhiteId() == null) ? 0 : getWhiteId().hashCode());
        result = prime * result + ((getChannel() == null) ? 0 : getChannel().hashCode());
        result = prime * result + ((getTradingCode() == null) ? 0 : getTradingCode().hashCode());
        result = prime * result + ((getLegalOrgCode() == null) ? 0 : getLegalOrgCode().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", whiteId=").append(whiteId);
        sb.append(", channel=").append(channel);
        sb.append(", tradingCode=").append(tradingCode);
        sb.append(", legalOrgCode=").append(legalOrgCode);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}