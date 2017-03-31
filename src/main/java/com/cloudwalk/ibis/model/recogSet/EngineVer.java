package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;

import com.cloudwalk.common.util.ObjectUtils;

public class EngineVer implements Serializable {
	
    private static final long serialVersionUID = 1L;
	
    private String id;
    
    /**
     * 引擎ID
     */
    private String engineId;
    
    /**
     * 识别策略ID
     */
    private String recogstepId;
    
    /**
     * 策略ID
     */
    private String stepId;

    private String engineCode;

    private String verCode;

    private String verNo;

    private Short status;

    private String remark;
    
    private String engineName;//算法引擎名称 

    private String companyName;//算法引擎厂家名称
    
    private String orgCode;
    
    private String channel;
    
    private String tradingCode;
    
    private String inputId;
    
    //引擎类型
    private String engineType;
    
	public EngineVer() {
		String uuid = ObjectUtils.createUUID();
		this.id = uuid;
	} 
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode == null ? null : engineCode.trim();
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode == null ? null : verCode.trim();
    }

    public String getVerNo() {
        return verNo;
    }

    public void setVerNo(String verNo) {
        this.verNo = verNo == null ? null : verNo.trim();
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
    
    public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTradingCode() {
		return tradingCode;
	}

	public void setTradingCode(String tradingCode) {
		this.tradingCode = tradingCode;
	}
	
	
	public String getEngineName() {
		return engineName;
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getInputId() {
		return inputId;
	}
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}	
	
	public String getRecogstepId() {
		return recogstepId;
	}
	public void setRecogstepId(String recogstepId) {
		this.recogstepId = recogstepId;
	}
	
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}	
	
	public String getEngineId() {
		return engineId;
	}
	public void setEngineId(String engineId) {
		this.engineId = engineId;
	}	
	
	public String getEngineType() {
		return engineType;
	}
	public void setEngineType(String engineType) {
		this.engineType = engineType;
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
        EngineVer other = (EngineVer) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getEngineCode() == null ? other.getEngineCode() == null : this.getEngineCode().equals(other.getEngineCode()))
            && (this.getVerCode() == null ? other.getVerCode() == null : this.getVerCode().equals(other.getVerCode()))
            && (this.getVerNo() == null ? other.getVerNo() == null : this.getVerNo().equals(other.getVerNo()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getEngineCode() == null) ? 0 : getEngineCode().hashCode());
        result = prime * result + ((getVerCode() == null) ? 0 : getVerCode().hashCode());
        result = prime * result + ((getVerNo() == null) ? 0 : getVerNo().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", engineCode=").append(engineCode);
        sb.append(", verCode=").append(verCode);
        sb.append(", verNo=").append(verNo);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}