package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;

import com.cloudwalk.common.util.ObjectUtils;

public class StepEngineRef implements Serializable {
    private String id;

    private String stepId;

    private String engineId;

    private String engineverId;

    private static final long serialVersionUID = 1L;

    
    public StepEngineRef(){
    	String uuid = ObjectUtils.createUUID();
		this.id = uuid;    	
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId == null ? null : stepId.trim();
    }

    public String getEngineId() {
        return engineId;
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId == null ? null : engineId.trim();
    }

    public String getEngineverId() {
        return engineverId;
    }

    public void setEngineverId(String engineverId) {
        this.engineverId = engineverId;
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
        StepEngineRef other = (StepEngineRef) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStepId() == null ? other.getStepId() == null : this.getStepId().equals(other.getStepId()))
            && (this.getEngineId() == null ? other.getEngineId() == null : this.getEngineId().equals(other.getEngineId()))
            && (this.getEngineverId() == null ? other.getEngineverId() == null : this.getEngineverId().equals(other.getEngineverId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStepId() == null) ? 0 : getStepId().hashCode());
        result = prime * result + ((getEngineId() == null) ? 0 : getEngineId().hashCode());
        result = prime * result + ((getEngineverId() == null) ? 0 : getEngineverId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", stepId=").append(stepId);
        sb.append(", engineId=").append(engineId);
        sb.append(", engineverId=").append(engineverId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}