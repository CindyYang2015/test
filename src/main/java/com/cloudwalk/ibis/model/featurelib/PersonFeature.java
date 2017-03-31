package com.cloudwalk.ibis.model.featurelib;

import java.io.Serializable;
import java.util.Date;

import com.cloudwalk.common.util.ObjectUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("person")
public class PersonFeature implements Serializable{
	/**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = 4269416158052337256L;
	
	public PersonFeature() {
    	String uuid = ObjectUtils.createUUID();
    	this.featureId = uuid;
    }
	
	/**
	 * 分区ID
	 */
	private Integer partitionId;
	
	/**
	 * FEATURE_ID
	 * 特征id   主键
	 */
	private String featureId;
	/**
	 * PERSON_ID
	 * 客户id
	 */
	private String personId;
	/**
	 * ENGINE_TYPE
	 * 算法引擎类型
	 */
	private String engineType;
	/**
	 * ENGINE_CODE
	 * 算法引擎代码
	 */
	private String engineCode;
	/**
	 * ENGINEVER_CODE
	 * 算法引擎版本代码
	 */
	private String engineVerCode;
	/**
	 * FEATURE
	 * 特征值
	 */
	private byte[] feature;
	
	/**
	 * 特征类型 1高清 2水印
	 */
	private Integer featureType;
	/**
	 * WEIGHT
	 * 质量
	 */
	private String weight;
	/**
	 * FILEPATH
	 * 生物文件路径
	 */
	private String filePath;
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
	
	/**************冗余客户主要信息***************/
	/**
	 * 机构代码全路径
	 */
	private String legalOrgCode;
	/**
	 * 核心客户号
	 */
	private String customerId;
	/**
	 * 证件类型
	 */
	private String ctftype;
	/**
	 * 证件号码
	 */
	private String ctfno;
	/**
	 * 证件名称
	 */
	private String ctfname;
	
	/**
	 * 检索特征比对的分数
	 */
	private float score;
	
	/**************冗余客户主要信息***************/	

	public String getFeatureId() {
		return featureId;
	}
	public Integer getPartitionId() {
		return partitionId;
	}
	public void setPartitionId(Integer partitionId) {
		this.partitionId = partitionId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
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
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
	public Integer getFeatureType() {
		return featureType;
	}
	public void setFeatureType(Integer featureType) {
		this.featureType = featureType;
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
	}
	public String getCtfname() {
		return ctfname;
	}
	public void setCtfname(String ctfname) {
		this.ctfname = ctfname;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public byte[] getFeature() {
		return feature;
	}
	public void setFeature(byte[] feature) {
		this.feature = feature;
	}
		
	
}
