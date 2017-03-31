package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;
import java.util.Date;

import com.cloudwalk.common.util.ObjectUtils;

public class StepGroup implements Serializable {
    private String id;

    private String legalOrgCode;

    private String stepgroupName;

    private Short status;

    private String remark;

    private String creator;

    private Date createTime;

    private String updator;

    private Date updateTime;

    private String steps;
    
    private String stepsTemp;
    
    private static final long serialVersionUID = 1L;
    
    public StepGroup(){
    	String uuid = ObjectUtils.createUUID();
		this.id = uuid; 
    }
    
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

    public String getStepgroupName() {
        return stepgroupName;
    }

    public void setStepgroupName(String stepgroupName) {
        this.stepgroupName = stepgroupName == null ? null : stepgroupName.trim();
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
    
    public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}
	
	
	public String getStepsTemp() {
		return stepsTemp;
	}

	public void setStepsTemp(String stepsTemp) {
		this.stepsTemp = stepsTemp;
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
        StepGroup other = (StepGroup) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLegalOrgCode() == null ? other.getLegalOrgCode() == null : this.getLegalOrgCode().equals(other.getLegalOrgCode()))
            && (this.getStepgroupName() == null ? other.getStepgroupName() == null : this.getStepgroupName().equals(other.getStepgroupName()))
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
        result = prime * result + ((getStepgroupName() == null) ? 0 : getStepgroupName().hashCode());
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
        sb.append(", stepgroupName=").append(stepgroupName);
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