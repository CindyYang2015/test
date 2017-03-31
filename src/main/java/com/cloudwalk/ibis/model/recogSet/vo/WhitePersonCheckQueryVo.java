/**
 * Project Name:ibisv2
 * File Name:BlackPersonVo.java
 * Package Name:com.cloudwalk.ibis.model.recogSet
 * Date:Sep 28, 201610:32:56 AM
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 *
*/

package com.cloudwalk.ibis.model.recogSet.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName:WhitePersonCheckQueryVo <br/>
 * Description: TODO Description. <br/>
 * Date:     Sep 28, 2016 10:32:56 AM <br/>
 * @author   杨维龙
 * @version  1.0.0
 * @since    JDK 1.7	 
 */
public class WhitePersonCheckQueryVo  implements Serializable {
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
    /**
	 * 渠道类型
	 */
    private String channels;
    /**
	 * 交易类型
	 */
    private String tradingCodes;
    
    private static final long serialVersionUID = 1L;

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
	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getTradingCodes() {
		return tradingCodes;
	}

	public void setTradingCodes(String tradingCodes) {
		this.tradingCodes = tradingCodes;
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

 

    public String getWhiteType() {
		return whiteType;
	}

	public void setWhiteType(String whiteType) {
		this.whiteType = whiteType;
	}

	public String getWhiteId() {
		return whiteId;
	}

	public void setWhiteId(String whiteId) {
		this.whiteId = whiteId;
	}

	public String getEngineCode() {
		return engineCode;
	}

	public void setEngineCode(String engineCode) {
		this.engineCode = engineCode;
	}

	public String getEngineverCode() {
		return engineverCode;
	}

	public void setEngineverCode(String engineverCode) {
		this.engineverCode = engineverCode;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
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
    
    

}

