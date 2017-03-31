package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;
import java.util.Date;

import com.cloudwalk.common.annotation.ExportField;
import com.cloudwalk.common.util.ObjectUtils;

public class ChannelFlow implements Serializable{
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	/**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = 4269416158052337256L;
    
    public ChannelFlow() {
    	String uuid = ObjectUtils.createUUID();
    	this.flowId = uuid;
    }
    
    /**
     * FLOW_ID
     * 流水id 主键
     */
    @ExportField(name = "流水号")
    private String flowId;
    /**
	 * LEGAL_ORG_CODE
	 * 机构代码全路径
	 */
    @ExportField(name = "机构代码全路径")
	private String legalOrgCode;
    /**
     * CHANNEL
     * 渠道code 对应字典表
     */
    @ExportField(name = "渠道类型")
	private String channel;
	/**
     * BUSCODE
     * 接口代码 对应接口版本管理
     */
    @ExportField(name = "接口类型")
	private String busCode;
    /**
     * INTERFACE_CODE
     * 接口名称
     */
    @ExportField(name = "接口名称")
	private String interfaceName;
    
	/**
     * INTERVER_CODE
     * 接口版本代码
     */
    @ExportField(name = "接口版本")
	private String interVerCode;
	/**
     * TRADING_CODE
     * 交易代码  对应字典表
     */
    @ExportField(name = "交易类型")
	private String tradingCode;
	/**
	 * ENGINE_TYPE
	 * 算法引擎类型
	 */
    @ExportField(name = "算法引擎类型")
	private String engineType;
	/**
	 * ENGINE_CODE
	 * 算法引擎代码
	 */
    @ExportField(name = "算法引擎名称")
	private String engineCode;
	/**
	 * ENGINEVER_ID
	 * 算法引擎版本id
	 */
    @ExportField(name = "算法引擎版本")
	private String engineVerCode;
	/**
     * RECOGSTEP_ID
     * 识别策略id
     */
    @ExportField(name = "识别策略")
	private String recogstepId;
	/**
     * REQUEST_MSG
     * 请求报文
     */
    @ExportField(name = "请求报文")
	private String requestMsg;
	/**
     * REPONSE_MSG
     * 返回报文
     */
    @ExportField(name = "接受报文")
	private String reponseMsg;
	/**
     * RESULT
     * 处理结果 1：成功 0：失败
     */
    @ExportField(name = "处理结果")
	private Integer result;
	/**
     * ERROR_MSG
     * 错误信息
     */
    @ExportField(name = "错误信息")
	private String errorMsg;
	/**
     * REMARK
     * 备注
     */
    @ExportField(name = "备注")
	private String remark;
	/**
     * CMPSCORE
     * 比对返回分值
     */
    @ExportField(name = "比对分数")
	private String cmpScore;
	/**
     * FPONE_CMPSCORE
     * 比对分数0-1,生物文件1与特征库比对
     */
	private String fpOneCmpScore;
	/**
     * FPTWO_CMPSCORE
     * 比对分数0-1,生物文件2与特征库比对
     */
	private String fpTwoCmpScore;
	
	/**
     * 生物文件注册路径
     */
	private String regFilePath;
	
	/**
     * FILEONE_PATH
     * 生物文件1路径
     */
	private String fileOnePath;
	/**
     * FILETWO_PATH
     * 生物文件2路径
     */
	private String fileTwoPath;
	/**
     * PERSON_ID
     * 客户ID
     */
	private String personId;
	/**
     * FEATURE_ID
     * 客户特征ID
     */
	private String featureId;
	/**
	 * CTFTYPE
	 * 证件类型
	 */
	@ExportField(name = "证件类型")
	private String ctftype;
	/**
	 * CTFNO
	 * 证件号码
	 */
	@ExportField(name = "证件号码")
	private String ctfno;
	/**
	 * CTFNAME
	 * 证件名称
	 */
	@ExportField(name = "证件名称")
	private String ctfname;
	/**
	 * CUSTOMER_ID
	 * 核心客户号
	 */
	@ExportField(name = "核心客户号")
	private String customerId;
	/**
     * TRADING_FLOW_NO
     * 业务交易流水号
     */
	@ExportField(name = "业务交易流水号")
	private String tradingFlowNo;
	/**
     * TRADING_DATE
     * 渠道交易日期
     */
	private String tradingDate;
	/**
     * TRADING_TIME
     * 渠道交易时间
     */
	private String tradingTime;
	/**
     * EQUIPMENT_NO
     * 设备号
     */
	private String equipmentNo;
	/**
     * ORGANIZATION_NO
     * 机构号
     */
	private String organizationNo;
	/**
     * TELLER_NO
     * 柜员号
     */
	private String tellerNo;
	/**
     * BANKCARD_NO
     * 银行卡号
     */
	private String bankcardNo;
	/**
     * CREATE_TIME
     * 创建日期
     */
	private Date createTime;

	private Date beginTime;
	
	private Date endTime;
	
	/**
     * FLOW_NUM
     * 时间戳
     */
	private String flowNum;
	
	
	public String getFlowNum() {
		return flowNum;
	}
	public void setFlowNum(String flowNum) {
		this.flowNum = flowNum;
	}
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
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	
	public String getInterVerCode() {
		return interVerCode;
	}
	public void setInterVerCode(String interVerCode) {
		this.interVerCode = interVerCode;
	}
	public String getTradingCode() {
		return tradingCode;
	}
	public void setTradingCode(String tradingCode) {
		this.tradingCode = tradingCode;
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
	public String getRecogstepId() {
		return recogstepId;
	}
	public void setRecogstepId(String recogstepId) {
		this.recogstepId = recogstepId;
	}
	public String getRequestMsg() {
		return requestMsg;
	}
	public void setRequestMsg(String requestMsg) {
		this.requestMsg = requestMsg;
	}
	public String getReponseMsg() {
		return reponseMsg;
	}
	public void setReponseMsg(String reponseMsg) {
		this.reponseMsg = reponseMsg;
	}	
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCmpScore() {
		return cmpScore;
	}
	public void setCmpScore(String cmpScore) {
		this.cmpScore = cmpScore;
	}
	public String getFpOneCmpScore() {
		return fpOneCmpScore;
	}
	public void setFpOneCmpScore(String fpOneCmpScore) {
		this.fpOneCmpScore = fpOneCmpScore;
	}
	public String getFpTwoCmpScore() {
		return fpTwoCmpScore;
	}
	public void setFpTwoCmpScore(String fpTwoCmpScore) {
		this.fpTwoCmpScore = fpTwoCmpScore;
	}
	public String getFileOnePath() {
		return fileOnePath;
	}
	public void setFileOnePath(String fileOnePath) {
		this.fileOnePath = fileOnePath;
	}
	public String getFileTwoPath() {
		return fileTwoPath;
	}
	public void setFileTwoPath(String fileTwoPath) {
		this.fileTwoPath = fileTwoPath;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
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
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getTradingFlowNo() {
		return tradingFlowNo;
	}
	public void setTradingFlowNo(String tradingFlowNo) {
		this.tradingFlowNo = tradingFlowNo;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getRegFilePath() {
		return regFilePath;
	}
	public void setRegFilePath(String regFilePath) {
		this.regFilePath = regFilePath;
	}
	@Override
	public String toString() {
		return "ChannelFlow [flowId=" + flowId + ", legalOrgCode="
				+ legalOrgCode + ", channel=" + channel + ", busCode="
				+ busCode + ", interfaceName=" + interfaceName
				+ ", interVerCode=" + interVerCode + ", tradingCode="
				+ tradingCode + ", engineType=" + engineType + ", engineCode="
				+ engineCode + ", engineVerCode=" + engineVerCode
				+ ", recogstepId=" + recogstepId + ", requestMsg=" + requestMsg
				+ ", reponseMsg=" + reponseMsg + ", result=" + result
				+ ", errorMsg=" + errorMsg + ", remark=" + remark
				+ ", cmpScore=" + cmpScore + ", fpOneCmpScore=" + fpOneCmpScore
				+ ", fpTwoCmpScore=" + fpTwoCmpScore + ", regFilePath="
				+ regFilePath + ", fileOnePath=" + fileOnePath
				+ ", fileTwoPath=" + fileTwoPath + ", personId=" + personId
				+ ", featureId=" + featureId + ", ctftype=" + ctftype
				+ ", ctfno=" + ctfno + ", ctfname=" + ctfname + ", customerId="
				+ customerId + ", tradingFlowNo=" + tradingFlowNo
				+ ", tradingDate=" + tradingDate + ", tradingTime="
				+ tradingTime + ", equipmentNo=" + equipmentNo
				+ ", organizationNo=" + organizationNo + ", tellerNo="
				+ tellerNo + ", bankcardNo=" + bankcardNo + ", createTime="
				+ createTime + ", beginTime=" + beginTime + ", endTime="
				+ endTime + "]";
	}
	
}
