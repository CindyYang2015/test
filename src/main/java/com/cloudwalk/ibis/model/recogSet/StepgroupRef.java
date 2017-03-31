package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;

import com.cloudwalk.common.util.ObjectUtils;

public class StepgroupRef implements Serializable {
    private String id;

    private String stepgroupId;

    private String stepId;

    private static final long serialVersionUID = 1L;

    
    public StepgroupRef(){
    	String uuid = ObjectUtils.createUUID();
		this.id = uuid;     	
    }
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getStepgroupId() {
        return stepgroupId;
    }

    public void setStepgroupId(String stepgroupId) {
        this.stepgroupId = stepgroupId == null ? null : stepgroupId.trim();
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId == null ? null : stepId.trim();
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
        StepgroupRef other = (StepgroupRef) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStepgroupId() == null ? other.getStepgroupId() == null : this.getStepgroupId().equals(other.getStepgroupId()))
            && (this.getStepId() == null ? other.getStepId() == null : this.getStepId().equals(other.getStepId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStepgroupId() == null) ? 0 : getStepgroupId().hashCode());
        result = prime * result + ((getStepId() == null) ? 0 : getStepId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", stepgroupId=").append(stepgroupId);
        sb.append(", stepId=").append(stepId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}