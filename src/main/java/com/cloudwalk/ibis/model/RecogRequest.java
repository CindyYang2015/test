package com.cloudwalk.ibis.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 生物识别服务请求对象
 * @author zhuyf
 *
 */
@XStreamAlias("request")
public class RecogRequest {

	/**
	 * 机构代码
	 */
	private String orgCode;
	
	/**
	 * 机构代码全路径
	 */
	private String orgCodePath;
	
	/**
	 * 活体数据
	 */
	private String livenessData;
		
	/**
	 * 机构名称
	 */
	private String orgName;
	
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
	 * 证件姓名
	 */
	private String ctfname;
	/**
	 * 客户属性
	 */
	private String property;	
	/**
	 * 渠道
	 */
	private String channel;
	/**
	 * 访问接口代码
	 */
	private String buscode;
	/**
	 * 交易代码
	 */
	private String tradingCode;
	/**
	 * 算法引擎代码
	 */
	private String engineCode;
	
	/**
	 * 接口版本代码
	 */
	private String verCode;
	/**
	 * 注册生物文件，文件相对路径
	 */
	private String regFilePath;
	/**
	 * 生物文件1，文件相对路径
	 */
	private String filePathone;
	/**
	 * 生物文件2，文件相对路径
	 */
	private String filePathtwo;
	/**
	 * 注册生物文件，base64数据
	 */
	private String regfileData;	
	/**
	 * 生物文件1，base64数据
	 */
	private String fileDataone;
	/**
	 * 生物文件2，base64数据
	 */
	private String fileDatatwo;
	/**
	 * 注册生物文件，特征数据
	 */
	private String regfeature;
	/**
	 * 生物文件1，特征数据
	 */
	private String featureone;
	/**
	 * 生物文件2，特征数据
	 */
	private String featuretwo;
	/**
	 * 业务交易流水号
	 */
	private String tradingFlowNO;
	/**
	 * 业务交易日期
	 */
	private String tradingDate;
	/**
	 * 业务交易时间
	 */
	private String tradingTime;
	/**
	 * 设备号
	 */
	private String equipmentNo;
	/**
	 * 机构号
	 */
	private String organizationNo;
	/**
	 * 柜员号	
	 */
	private String tellerNo;
	/**
	 * 银行卡号
	 */
	private String bankcardNo;
	/**
	 * 联网核查状态 1是 0否
	 */
	private Integer netCheckStatus;
	/**
	 * 联网核查照片，文件相对路径
	 */
	private String netCheckFilePath;
	/**
	 * 联网核查照片，base64数据
	 */
	private String netCheckFileData;
	/**
	 * 联网核查特征
	 */
	private String netCheckFeature;
	/**
	 * 联网核查照片类型，1高清 2水印
	 */
	private Integer netCheckImgType;
	/**
	 * 表示该请求报文是否加密 1 加密 0不加密
	 */
	private Integer isencrypt;	
	/**
	 * 显示数量，用于展示前端
	 */
	private Integer number;
	/**
	 * 通信协议，webservice：1，socket:2,Http:3
	 */
	private String requestWay;	
	
	/**
	 * 身份证正面照路径
	 */
	private String frontImgePath;
	
	/**
	 * 身份证正面照
	 */
	private String frontImge;
	/**
	 * 身份证反面照路径
	 */
	private String backImgePath;
	/**
	 * 身份证反面照
	 */
	private String backImge;
	
	/**
	 * 银行卡照片地址
	 */
	private String bankCardImgPath;
	
	/**
	 * 银行卡照片
	 */
	private String bankCardImg;
	
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getRequestWay() {
		return requestWay;
	}
	public void setRequestWay(String requestWay) {
		this.requestWay = requestWay;
	}
	
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
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
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getBuscode() {
		return buscode;
	}
	public void setBuscode(String buscode) {
		this.buscode = buscode;
	}
	public String getTradingCode() {
		return tradingCode;
	}
	public void setTradingCode(String tradingCode) {
		this.tradingCode = tradingCode;
	}
	public String getEngineCode() {
		return engineCode;
	}
	public void setEngineCode(String engineCode) {
		this.engineCode = engineCode;
	}
	
	public String getVerCode() {
		return verCode;
	}
	public void setVerCode(String verCode) {
		this.verCode = verCode;
	}
	public String getTradingFlowNO() {
		return tradingFlowNO;
	}
	public void setTradingFlowNO(String tradingFlowNO) {
		this.tradingFlowNO = tradingFlowNO;
	}
	public String getTradingDate() {
		return tradingDate;
	}
	public void setTradingDate(String tradingDate) {
		this.tradingDate = tradingDate;
	}
	public String getTradingTime() {
		return tradingTime;
	}
	public void setTradingTime(String tradingTime) {
		this.tradingTime = tradingTime;
	}
	public String getEquipmentNo() {
		return equipmentNo;
	}
	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}
	public String getOrganizationNo() {
		return organizationNo;
	}
	public void setOrganizationNo(String organizationNo) {
		this.organizationNo = organizationNo;
	}
	public String getTellerNo() {
		return tellerNo;
	}
	public void setTellerNo(String tellerNo) {
		this.tellerNo = tellerNo;
	}
	public String getBankcardNo() {
		return bankcardNo;
	}
	public void setBankcardNo(String bankcardNo) {
		this.bankcardNo = bankcardNo;
	}
	public Integer getNetCheckStatus() {
		return netCheckStatus;
	}
	public void setNetCheckStatus(Integer netCheckStatus) {
		this.netCheckStatus = netCheckStatus;
	}
	
	public Integer getNetCheckImgType() {
		return netCheckImgType;
	}
	public void setNetCheckImgType(Integer netCheckImgType) {
		this.netCheckImgType = netCheckImgType;
	}
	public Integer getIsencrypt() {
		return isencrypt;
	}
	public void setIsencrypt(Integer isencrypt) {
		this.isencrypt = isencrypt;
	}
	
	public String getFilePathone() {
		return filePathone;
	}
	public void setFilePathone(String filePathone) {
		this.filePathone = filePathone;
	}
	public String getFilePathtwo() {
		return filePathtwo;
	}
	public void setFilePathtwo(String filePathtwo) {
		this.filePathtwo = filePathtwo;
	}
	
	public String getFileDataone() {
		return fileDataone;
	}
	public void setFileDataone(String fileDataone) {
		this.fileDataone = fileDataone;
	}
	public String getFileDatatwo() {
		return fileDatatwo;
	}
	public void setFileDatatwo(String fileDatatwo) {
		this.fileDatatwo = fileDatatwo;
	}
	public String getNetCheckFilePath() {
		return netCheckFilePath;
	}
	public void setNetCheckFilePath(String netCheckFilePath) {
		this.netCheckFilePath = netCheckFilePath;
	}
	public String getNetCheckFileData() {
		return netCheckFileData;
	}
	public void setNetCheckFileData(String netCheckFileData) {
		this.netCheckFileData = netCheckFileData;
	}
	public String getFeatureone() {
		return featureone;
	}
	public void setFeatureone(String featureone) {
		this.featureone = featureone;
	}
	public String getFeaturetwo() {
		return featuretwo;
	}
	public void setFeaturetwo(String featuretwo) {
		this.featuretwo = featuretwo;
	}
	public String getNetCheckFeature() {
		return netCheckFeature;
	}
	public void setNetCheckFeature(String netCheckFeature) {
		this.netCheckFeature = netCheckFeature;
	}
	
	public String getRegFilePath() {
		return regFilePath;
	}
	public void setRegFilePath(String regFilePath) {
		this.regFilePath = regFilePath;
	}
	public String getRegfileData() {
		return regfileData;
	}
	public void setRegfileData(String regfileData) {
		this.regfileData = regfileData;
	}
	public String getRegfeature() {
		return regfeature;
	}
	public void setRegfeature(String regfeature) {
		this.regfeature = regfeature;
	}
	
	public String getFrontImge() {
		return frontImge;
	}
	public void setFrontImge(String frontImge) {
		this.frontImge = frontImge;
	}
	public String getBackImge() {
		return backImge;
	}
	public void setBackImge(String backImge) {
		this.backImge = backImge;
	}
	public String getFrontImgePath() {
		return frontImgePath;
	}
	public void setFrontImgePath(String frontImgePath) {
		this.frontImgePath = frontImgePath;
	}
	public String getBackImgePath() {
		return backImgePath;
	}
	public void setBackImgePath(String backImgePath) {
		this.backImgePath = backImgePath;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}			
	
	public String getLivenessData() {
		return livenessData;
	}
	public void setLivenessData(String livenessData) {
		this.livenessData = livenessData;
	}
	public String getBankCardImg() {
		return bankCardImg;
	}
	public void setBankCardImg(String bankCardImg) {
		this.bankCardImg = bankCardImg;
	}
	public String getBankCardImgPath() {
		return bankCardImgPath;
	}
	public void setBankCardImgPath(String bankCardImgPath) {
		this.bankCardImgPath = bankCardImgPath;
	}
	public String getOrgCodePath() {
		return orgCodePath;
	}
	public void setOrgCodePath(String orgCodePath) {
		this.orgCodePath = orgCodePath;
	}		
	
	
	
}
