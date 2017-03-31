package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.cloudwalk.common.util.ObjectUtils;

public class WhitePersonCheck implements Serializable {
	public WhitePersonCheck() {
    	String uuid = ObjectUtils.createUUID();
    	this.id = uuid;
    } 
	/**
	 *  主键
	 */
    private String id;
    /**
	 * 机构代码（全路径）
	 */
    private String orgCode;
    
    /**
	 * 法人机构代码（全路径）
	 */
    private String legalOrgCode;
    
    /**
	 *   核心客户号
	 */
    private String customerId;
    /**
	 * 证件类型（对应字典）
	 */
    private String ctftype;
    /**
	 *  证件号码
	 */
    private String ctfno;
    /**
	 * 姓名 
	 */
    private String ctfname;
    /**
	 * 渠道（对应字典）
	 */
    private String channel;
    /**
	 * 交易代码（对应字典）
	 */
    private String tradingCode;
    /**
	 * 白名单类型（对应字典）
	 */
    private String whiteType;
    /**
	 * 状态，1:通过 0:不通过 2:待审核
	 */
    private Short status;
    /**
	 * 操作类型，1新增 2修改 3删除
	 */
    private Short operateStatus;
    /**
	 *  白名单信息ID
	 */
    private String whiteId;
    /**
	 * 备注
	 */
    private String remark;
    /**
	 * 创建人
	 */
    private String creator;
    /**
	 *  创建时间
	 */
    private Date createTime;
    /**
	 *  更新人
	 */
    private String updator;
    /**
	 * 更新时间
	 */
    private Date updateTime;
    /**
	 * 算法引擎代码
	 */
    private String engineCode;
    /**
   	 * 算法引擎版本
   	 */
    private String engineverCode;
    /**
   	 * 阈值0-1
   	 */
    private BigDecimal score;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public String getCtftype() {
        return ctftype;
    }

    public void setCtftype(String ctftype) {
        this.ctftype = ctftype == null ? null : ctftype.trim();
    }

    public String getCtfno() {
        return ctfno;
    }

    public void setCtfno(String ctfno) {
        this.ctfno = ctfno == null ? null : ctfno.trim();
    }

    public String getCtfname() {
        return ctfname;
    }

    public void setCtfname(String ctfname) {
        this.ctfname = ctfname == null ? null : ctfname.trim();
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode == null ? null : engineCode.trim();
    }

    public String getEngineverCode() {
        return engineverCode;
    }

    public void setEngineverCode(String engineverCode) {
        this.engineverCode = engineverCode == null ? null : engineverCode.trim();
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getWhiteType() {
        return whiteType;
    }

    public void setWhiteType(String whiteType) {
        this.whiteType = whiteType == null ? null : whiteType.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(Short operateStatus) {
        this.operateStatus = operateStatus;
    }

    public String getWhiteId() {
        return whiteId;
    }

    public void setWhiteId(String whiteId) {
        this.whiteId = whiteId == null ? null : whiteId.trim();
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

    public String getLegalOrgCode() {
		return legalOrgCode;
	}

	public void setLegalOrgCode(String legalOrgCode) {
		this.legalOrgCode = legalOrgCode;
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
        WhitePersonCheck other = (WhitePersonCheck) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrgCode() == null ? other.getOrgCode() == null : this.getOrgCode().equals(other.getOrgCode()))
            && (this.getCustomerId() == null ? other.getCustomerId() == null : this.getCustomerId().equals(other.getCustomerId()))
            && (this.getCtftype() == null ? other.getCtftype() == null : this.getCtftype().equals(other.getCtftype()))
            && (this.getCtfno() == null ? other.getCtfno() == null : this.getCtfno().equals(other.getCtfno()))
            && (this.getCtfname() == null ? other.getCtfname() == null : this.getCtfname().equals(other.getCtfname()))
            && (this.getEngineCode() == null ? other.getEngineCode() == null : this.getEngineCode().equals(other.getEngineCode()))
            && (this.getEngineverCode() == null ? other.getEngineverCode() == null : this.getEngineverCode().equals(other.getEngineverCode()))
            && (this.getScore() == null ? other.getScore() == null : this.getScore().equals(other.getScore()))
            && (this.getWhiteType() == null ? other.getWhiteType() == null : this.getWhiteType().equals(other.getWhiteType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getOperateStatus() == null ? other.getOperateStatus() == null : this.getOperateStatus().equals(other.getOperateStatus()))
            && (this.getWhiteId() == null ? other.getWhiteId() == null : this.getWhiteId().equals(other.getWhiteId()))
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
        result = prime * result + ((getOrgCode() == null) ? 0 : getOrgCode().hashCode());
        result = prime * result + ((getCustomerId() == null) ? 0 : getCustomerId().hashCode());
        result = prime * result + ((getCtftype() == null) ? 0 : getCtftype().hashCode());
        result = prime * result + ((getCtfno() == null) ? 0 : getCtfno().hashCode());
        result = prime * result + ((getCtfname() == null) ? 0 : getCtfname().hashCode());
        result = prime * result + ((getEngineCode() == null) ? 0 : getEngineCode().hashCode());
        result = prime * result + ((getEngineverCode() == null) ? 0 : getEngineverCode().hashCode());
        result = prime * result + ((getScore() == null) ? 0 : getScore().hashCode());
        result = prime * result + ((getWhiteType() == null) ? 0 : getWhiteType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getOperateStatus() == null) ? 0 : getOperateStatus().hashCode());
        result = prime * result + ((getWhiteId() == null) ? 0 : getWhiteId().hashCode());
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
        sb.append(", orgCode=").append(orgCode);
        sb.append(", customerId=").append(customerId);
        sb.append(", ctftype=").append(ctftype);
        sb.append(", ctfno=").append(ctfno);
        sb.append(", ctfname=").append(ctfname);
        sb.append(", engineCode=").append(engineCode);
        sb.append(", engineverCode=").append(engineverCode);
        sb.append(", score=").append(score);
        sb.append(", whiteType=").append(whiteType);
        sb.append(", status=").append(status);
        sb.append(", operateStatus=").append(operateStatus);
        sb.append(", whiteId=").append(whiteId);
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