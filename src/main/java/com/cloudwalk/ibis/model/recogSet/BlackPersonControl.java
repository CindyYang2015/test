package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;

import com.cloudwalk.common.util.ObjectUtils;

public class BlackPersonControl implements Serializable {
	public BlackPersonControl() {
    	String uuid = ObjectUtils.createUUID();
    	this.id = uuid;
    }
	/**
	 * 主键
	 */
    private String id;
    /**
	 * 黑名单ID
	 */
    private String blackId;
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
    
    /**
	 * 核心客户号
	 */
    private String customerId;
    /**
	 * 证件类型
	 */
    private String ctftype;
    /**
	 * 证件号码
	 */
    private String ctfno;    

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBlackId() {
        return blackId;
    }

    public void setBlackId(String blackId) {
        this.blackId = blackId == null ? null : blackId.trim();
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

    public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCtftype() {
		return ctftype;
	}

	public void setCtftype(String ctftype) {
		this.ctftype = ctftype;
	}

	public String getCtfno() {
		return ctfno;
	}

	public void setCtfno(String ctfno) {
		this.ctfno = ctfno;
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
        BlackPersonControl other = (BlackPersonControl) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBlackId() == null ? other.getBlackId() == null : this.getBlackId().equals(other.getBlackId()))
            && (this.getChannel() == null ? other.getChannel() == null : this.getChannel().equals(other.getChannel()))
            && (this.getTradingCode() == null ? other.getTradingCode() == null : this.getTradingCode().equals(other.getTradingCode()))
            && (this.getLegalOrgCode() == null ? other.getLegalOrgCode() == null : this.getLegalOrgCode().equals(other.getLegalOrgCode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBlackId() == null) ? 0 : getBlackId().hashCode());
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
        sb.append(", blackId=").append(blackId);
        sb.append(", channel=").append(channel);
        sb.append(", tradingCode=").append(tradingCode);
        sb.append(", legalOrgCode=").append(legalOrgCode);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}