package com.cloudwalk.ibis.model.featurelib;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.util.ObjectUtils;

public class Person implements Serializable{
	
	/**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = 4269416158052337256L;
	
	public Person() {
    	String uuid = ObjectUtils.createUUID();
    	this.personId = uuid;
    }
	/**
	 * PERSON_ID
	 * 客户id   主键
	 */
	private String personId;
	/**
	 * LEGAL_ORG_CODE
	 * 机构代码全路径
	 */
	private String legalOrgCode;
	/**
	 * CUSTOMER_ID
	 * 核心客户号
	 */
	private String customerId;
	/**
	 * CTFTYPE
	 * 证件类型
	 */
	private String ctftype;
	/**
	 * CTFNO
	 * 证件号码
	 */
	private String ctfno;
	
	/**
	 * 分区ID
	 */
	private Integer partitionId;
	
	/**
	 * CTFNAME
	 * 证件名称
	 */
	private String ctfname;
	/**
	 * ORG_NAME
	 * 机构名称
	 */
	private String orgName;
	/**
	 * PROPERTY
	 * 客户属性
	 */
	private String property;
	/**
	 * STATUS
	 * 客户状态 1正常0失效
	 */
	private Integer status;
	/**
	 * REMARK
	 * 备注
	 */
	private String remark;
	/**
	 * CREATOR
	 * 创建人
	 */
	private String creator;
	/**
	 * CREATE_TIME
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * UPDATOR
	 * 创建人
	 */
	private String updator;
	/**
	 * UPDATE_TIME
	 * 创建时间
	 */
	private Date updateTime;
	
	/*************冗余特征信息****************/
	
	/**
	 * 当前客户生物特征信息
	 */
	private PersonFeature pf;	
	
	/*
	 * 引擎类型
	 */
	private String engineType;
	
	/**
	 * 引擎代码
	 */
	private String engineCode;
	
	/**
	 * 引擎版本代码
	 */
	private String engineVerCode;
	
	/**
	 * 当前生物文件数据
	 */
	private byte[] fileData;
	
	/*************冗余特征信息****************/

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getLegalOrgCode() {
		return legalOrgCode;
	}

	public void setLegalOrgCode(String legalOrgCode) {
		this.legalOrgCode = legalOrgCode;
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
		this.initPartition();
	}

	public Integer getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(Integer partitionId) {
		this.partitionId = partitionId;
	}

	public String getCtfname() {
		return ctfname;
	}

	public void setCtfname(String ctfname) {
		this.ctfname = ctfname;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgNmae) {
		this.orgName = orgNmae;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
		this.updator = updator;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public PersonFeature getPf() {
		return pf;
	}

	public void setPf(PersonFeature pf) {
		this.pf = pf;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public String getEngineCode() {
		return engineCode;
	}

	public void setEngineCode(String engineCode) {
		this.engineCode = engineCode;
	}

	public String getEngineVerCode() {
		return engineVerCode;
	}

	public void setEngineVerCode(String engineVerCode) {
		this.engineVerCode = engineVerCode;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	
	/**
	 * 初始化分区
	 */
	public void initPartition() {
		//如果证件类型为身份证，根据身份证号码前两位设置分区
		if(this.partitionId == null && !StringUtils.isBlank(ctfno) 
				&& ctfno.length() >= 2) {
			String partitionStr = ctfno.substring(0,2);
			if(Constants.IDCARD_NUM_SET.contains(partitionStr)) {
				this.partitionId = ObjectUtils.objToInt(partitionStr,Constants.NOT_IDCARD_NUM);
			} else {
				this.partitionId = Constants.NOT_IDCARD_NUM;
			}			
		}
	}
	
		
}
