package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;
import java.util.Date;

import com.cloudwalk.common.util.ObjectUtils;
/**
 * ClassName: BlackPersonCheck <br/>
 * Description:黑名单审核信息. <br/>
 * date: Sep 27, 2016 2:46:27 PM <br/>
 *
 * @author 杨维龙
 * @version 
 * @since JDK 1.7
 */
public class BlackPersonCheck implements Serializable {
	public BlackPersonCheck() {
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
	 * 黑名单类型（对应字典）
	 */
    private String blackType;
    /**
	 * 状态，1:通过 0:不通过 2:待审核
	 */
    private Short status;
    /**
	 * 操作类型，1新增 2修改 3删除
	 */
    private Short operateStatus;
    /**
	 *  黑名单信息ID
	 */
    private String blackId;
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

    public String getBlackType() {
        return blackType;
    }

    public void setBlackType(String blackType) {
        this.blackType = blackType == null ? null : blackType.trim();
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

    public String getBlackId() {
        return blackId;
    }

    public void setBlackId(String blackId) {
        this.blackId = blackId == null ? null : blackId.trim();
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
        BlackPersonCheck other = (BlackPersonCheck) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrgCode() == null ? other.getOrgCode() == null : this.getOrgCode().equals(other.getOrgCode()))
            && (this.getCustomerId() == null ? other.getCustomerId() == null : this.getCustomerId().equals(other.getCustomerId()))
            && (this.getCtftype() == null ? other.getCtftype() == null : this.getCtftype().equals(other.getCtftype()))
            && (this.getCtfno() == null ? other.getCtfno() == null : this.getCtfno().equals(other.getCtfno()))
            && (this.getCtfname() == null ? other.getCtfname() == null : this.getCtfname().equals(other.getCtfname()))
            && (this.getBlackType() == null ? other.getBlackType() == null : this.getBlackType().equals(other.getBlackType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getOperateStatus() == null ? other.getOperateStatus() == null : this.getOperateStatus().equals(other.getOperateStatus()))
            && (this.getBlackId() == null ? other.getBlackId() == null : this.getBlackId().equals(other.getBlackId()))
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
        result = prime * result + ((getBlackType() == null) ? 0 : getBlackType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getOperateStatus() == null) ? 0 : getOperateStatus().hashCode());
        result = prime * result + ((getBlackId() == null) ? 0 : getBlackId().hashCode());
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
        sb.append(", channel=").append(channel);
        sb.append(", tradingCode=").append(tradingCode);
        sb.append(", blackType=").append(blackType);
        sb.append(", status=").append(status);
        sb.append(", operateStatus=").append(operateStatus);
        sb.append(", blackId=").append(blackId);
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