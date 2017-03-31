package com.cloudwalk.ibis.model.featurelib;

import java.io.Serializable;
import java.util.Date;

import com.cloudwalk.common.util.ObjectUtils;
/***
 * 批量导入表
 * @author 方凯
 * 2016年9月28日16:28:10
 */
public class PersonImportFlow implements Serializable{
	/**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = 4269416158052337256L;
    
    public PersonImportFlow() {
    	String uuid = ObjectUtils.createUUID();
    	this.flowId = uuid;
    }
    
    /**
     * FLOW_ID
     * 导入流水id 主键
     */
    private String flowId;
    /**
     * 	FEATURE_ID
     *  特征id
     */
    private String featureId;
    /**
     * ORG_CODE
     * 机构代码全路径
     */
    private String orgCode;
    /**
     * DEPT_CODE
     * 部门节点编码
     */
    private String deptCode;
    /**
     * CTF_TYPE
     * 证件类型
     */
    private String ctftype;
    /**
     * CTF_NO
     * 证件号码
     */
    private String ctfno;
    /**
     * CTF_NAME
     * 证件名称
     */
    private String ctfname;
    /**
     * FILE_NAME
     * 文件名称
     */
    private String fileName;
    /**
     * FILE_PATH
     * 文件全路径
     */
    private String filePath;
    /**
     * STATUS
     * 状态  0成功 1失败
     */
    private String status;
    /**
     * REASON
     * 失败原因
     */
    private String reason;
    /**
     * MASK
     * 备注
     */
    private String mask;
    /**
     * CREATOR
     * 创建人导入人
     */
    private String creator;
    /**
     * CUSTOMERID
     * 核心客户号
     */
    private String customerId;
    /**
     * ENGINECODE
     * 算法引擎
     */
    private String engineCode;
    /**
     * ENGINEVERCODE
     * 算法引擎版本
     */
    private String engineverCode;
    /**
     * ENGINETYPE
     * 算法引擎类型
     */
    private String engineType;
    /**
     * CREATE_TIME
     * 导入时间
     */
    private Date createTime;
    
    private Date beginTime;
    
    private Date endTime;
    
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	public String getCtfname() {
		return ctfname;
	}
	public void setCtfname(String ctfname) {
		this.ctfname = ctfname;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMask() {
		return mask;
	}
	public void setMask(String mask) {
		this.mask = mask;
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
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	public String getEngineType() {
		return engineType;
	}
	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}
	
}
