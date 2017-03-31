package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;
import java.util.Date;
import com.cloudwalk.common.annotation.ExportField;

/**
 * 识别认证统计表
 * @author fangkai
 *
 */
public class PersonRecogFlow implements Serializable{
	/**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = 4269416158052337256L;
    
    public PersonRecogFlow() {  
    }
    /**
     * ID
     * 主键
     */
    @ExportField(name = "主键")
    private String id;
    /**
     * PERSON_ID
     * 客户ID
     */
    @ExportField(name = "客户ID")
    private String personId;
    /**
	 * LEGAL_ORG_CODE
	 * 机构代码全路径
	 */
    @ExportField(name = "机构代码全路径")
	  private String legalOrgCode;
	/**
	 * BUSCODE
	 * 接口代码
	 */
    @ExportField(name = "接口代码")
	  private String busCode;
	/**
	 * INTERVER_ID
	 * 接口版本ID
	 */
	 @ExportField(name = "接口版本ID")
	 private String interVerCode;
	/**
	 * CTFTYPE
	 * 证件类型
	 */
	@ExportField(name = "证件类型")
	private String ctftype;
	
	/**
	 * 证件类型名称
	 */
	private String ctftypeName;
	
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
	@ExportField(name = "姓名")
	private String ctfname;
	/**
	 * CUSTOMER_ID
	 * 核心客户号
	 */
	@ExportField(name = "核心客户号")
	private String customerId;
	/**
	 * ENGINE_TYPE
	 * 算法引擎类型
	 */
	@ExportField(name = "算法引擎类型")
	private String engineType;
	
	/**
	 * 引擎类型名称
	 */
	private String engineTypeName;
	
	/**
	 * ENGINE_CODE
	 * 算法引擎代码
	 */
	@ExportField(name = "算法引擎代码")
	private String engineCode;
	/**
	 * ENGINEVER_ID
	 * 算法引擎版本id
	 */
	@ExportField(name = "算法引擎版本id")
	private String engineVerCode;
	/**
     * TRADING_CODE
     * 交易代码  对应字典表
     */
	@ExportField(name = "交易类型")
	private String tradingCode;
	
	/**
	 * 交易代码名称
	 */
	private String tradingCodeName;
	
	/**
	 * SUCCESS_COUNT
	 * 成功次数
	 */
	@ExportField(name = "通过数量")
	private Integer sucessCount;
	/**
	 * FAIL_COUNT
	 * 失败次数
	 */
	@ExportField(name = "失败数量")
	private Integer failCount;
	/**
	 * CREATE_TIME
	 * 创建时间
	 */
	@ExportField(name = "创建时间")
	private Date createTime;
	
	@ExportField(name = "开始时间")
	private Date beginTime;
	
	@ExportField(name = "结束时间")
	private Date endTime;
	/**
	 * 渠道类型
	 */
	@ExportField(name = "渠道类型")
	private String channel;
	
	/**
	 * 渠道名称
	 */
	private String channelName;
	
	/**
	 * isFail为空按照失败条数降序排序，若不为空按照成功条数降序排序
	 */
	private String isFail = "1";
	
	public String getInterVerCode() {
		return interVerCode;
	}

	public void setInterVerCode(String interVerCode) {
		this.interVerCode = interVerCode;
	}

	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getIsFail() {
		return isFail;
	}
	
	public void setIsFail(String isFail) {
		this.isFail = isFail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getTradingCode() {
		return tradingCode;
	}

	public void setTradingCode(String tradingCode) {
		this.tradingCode = tradingCode;
	}

	public Integer getSucessCount() {
		return sucessCount;
	}

	public void setSucessCount(Integer sucessCount) {
		this.sucessCount = sucessCount;
	}

	public Integer getFailCount() {
		return failCount;
	}

	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getTradingCodeName() {
		return tradingCodeName;
	}

	public void setTradingCodeName(String tradingCodeName) {
		this.tradingCodeName = tradingCodeName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCtftypeName() {
		return ctftypeName;
	}

	public void setCtftypeName(String ctftypeName) {
		this.ctftypeName = ctftypeName;
	}

	public String getEngineTypeName() {
		return engineTypeName;
	}

	public void setEngineTypeName(String engineTypeName) {
		this.engineTypeName = engineTypeName;
	}
	
	
}
