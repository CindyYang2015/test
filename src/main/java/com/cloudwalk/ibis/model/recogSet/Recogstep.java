package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;
import java.util.Date;

public class Recogstep implements Serializable {
    private String id;

    private String legalOrgCode;

    private String recogstepName;

    private String channel;

    private String tradingCode;

    private String stepgroupId;

    private Short status;

    private String remark;

    private String creator;

    private Date createTime;

    private String updator;

    private Date updateTime;

    private String bankOrgName;
    
    
    
    private static final long serialVersionUID = 1L;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getLegalOrgCode() {
        return legalOrgCode;
    }

    public void setLegalOrgCode(String legalOrgCode) {
        this.legalOrgCode = legalOrgCode == null ? null : legalOrgCode.trim();
    }

    public String getRecogstepName() {
        return recogstepName;
    }

    public void setRecogstepName(String recogstepName) {
        this.recogstepName = recogstepName == null ? null : recogstepName.trim();
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

    public String getStepgroupId() {
        return stepgroupId;
    }

    public void setStepgroupId(String stepgroupId) {
        this.stepgroupId = stepgroupId == null ? null : stepgroupId.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getBankOrgName() {
		return bankOrgName;
	}

	public void setBankOrgName(String bankOrgName) {
		this.bankOrgName = bankOrgName;
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
        Recogstep other = (Recogstep) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLegalOrgCode() == null ? other.getLegalOrgCode() == null : this.getLegalOrgCode().equals(other.getLegalOrgCode()))
            && (this.getRecogstepName() == null ? other.getRecogstepName() == null : this.getRecogstepName().equals(other.getRecogstepName()))
            && (this.getChannel() == null ? other.getChannel() == null : this.getChannel().equals(other.getChannel()))
            && (this.getTradingCode() == null ? other.getTradingCode() == null : this.getTradingCode().equals(other.getTradingCode()))
            && (this.getStepgroupId() == null ? other.getStepgroupId() == null : this.getStepgroupId().equals(other.getStepgroupId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdator() == null ? other.getUpdator() == null : this.getUpdator().equals(other.getUpdator()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLegalOrgCode() == null) ? 0 : getLegalOrgCode().hashCode());
        result = prime * result + ((getRecogstepName() == null) ? 0 : getRecogstepName().hashCode());
        result = prime * result + ((getChannel() == null) ? 0 : getChannel().hashCode());
        result = prime * result + ((getTradingCode() == null) ? 0 : getTradingCode().hashCode());
        result = prime * result + ((getStepgroupId() == null) ? 0 : getStepgroupId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdator() == null) ? 0 : getUpdator().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", legalOrgCode=").append(legalOrgCode);
        sb.append(", recogstepName=").append(recogstepName);
        sb.append(", channel=").append(channel);
        sb.append(", tradingCode=").append(tradingCode);
        sb.append(", stepgroupId=").append(stepgroupId);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", creator=").append(creator);
        sb.append(", createTime=").append(createTime);
        sb.append(", updator=").append(updator);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}