package com.cloudwalk.ibis.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.cloudwalk.ibis.model.featurelib.Person;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.result.ver001.Response;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.service.base.engine.EngineService;

/**
 * 业务传输对象
 * @author zhuyf
 *
 */
public class TransferObj {
	
	/**
	 * 请求的原始消息
	 */
	private String requestString;
	
	/**
	 * 识别认证请求实体
	 */
	private RecogRequest recogRequest;
	
	/**
	 * 响应消息
	 */
	private String respnseString;
	
	/**
	 * 当前业务响应结果实体
	 */
	private Result result;
	
	/**
	 * 整个业务响应的结果
	 */
	private Response response;
	
	/**
	 * 当前客户信息
	 */
	private Person p;
	
	/**
	 * 生物特征ID
	 */
	private String featureId;
	
	/**
	 * 当前客户生物特征
	 */
	private String feature;
	
	/**
	 * 特征类型 默认为1高清 2水印
	 */
	private int featureType = 1;
	
	/**
	 * 当前客户生物文件路径
	 */
	private String filePath;
	
	/**
	 * 当前客户生物文件base64数据
	 */
	private String fileData;
	
	/**
	 * 当前识别分数1
	 */
	private double sim1Score;
	
	/**
	 * 当前识别分数2
	 */
	private double sim2Score;
	
	/**
	 * 是否查询白名单
	 */
	private int queryWhiteStatus;

	/**
	 * 引擎服务
	 */
	private EngineService engineService;
	
	/**
	 * 每次业务处理具体响应时间
	 */
	private String handleTimes;
	
	public EngineService getEngineService() {
		return engineService;
	}

	public void setEngineService(EngineService engineService) {
		this.engineService = engineService;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	/**
	 * 流水记录
	 */
	private ChannelFlow flow = new ChannelFlow();
	
	/**
	 * 当前引擎版本信息
	 */
	private EngineVer engineVer;	

	public String getRequestString() {
		return requestString;
	}

	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}

	public RecogRequest getRecogRequest() {
		return recogRequest;
	}

	public void setRecogRequest(RecogRequest recogRequest) {
		this.recogRequest = recogRequest;
	}

	public String getRespnseString() {
		return respnseString;
	}

	public void setRespnseString(String respnseString) {
		this.respnseString = respnseString;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Person getP() {
		return p;
	}

	public void setP(Person p) {
		this.p = p;
	}	

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFeatureType() {
		return featureType;
	}

	public void setFeatureType(int featureType) {
		this.featureType = featureType;
	}	

	public EngineVer getEngineVer() {
		return engineVer;
	}

	public void setEngineVer(EngineVer engineVer) {
		this.engineVer = engineVer;
	}

	public ChannelFlow getFlow() {
		return flow;
	}

	public void setFlow(ChannelFlow flow) {
		this.flow = flow;
	}	

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}	

	public double getSim1Score() {
		return sim1Score;
	}

	public void setSim1Score(double sim1Score) {
		this.sim1Score = sim1Score;
	}

	public double getSim2Score() {
		return sim2Score;
	}

	public void setSim2Score(double sim2Score) {
		this.sim2Score = sim2Score;
	}	

	public int getQueryWhiteStatus() {
		return queryWhiteStatus;
	}

	public void setQueryWhiteStatus(int queryWhiteStatus) {
		this.queryWhiteStatus = queryWhiteStatus;
	}	

	public String getFileData() {
		return fileData;
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}	

	public String getHandleTimes() {
		return handleTimes;
	}

	public void setHandleTimes(String handleTimes) {
		this.handleTimes = handleTimes;
	}
	
	/**
	 * 添加业务处理时间
	 * @param serviceHandleTimes
	 */
	public void addHandleTimes(String serviceHandleTimes) {		
		if(StringUtils.isBlank(this.handleTimes)) {
			this.handleTimes = serviceHandleTimes;
		} else {
			this.handleTimes += ";"+serviceHandleTimes;
		}
	}

	/**
	 * 初始化流水记录
	 */
	public void initFlowInfo() {
		if(this.recogRequest != null) {
			Date curDate = new Date();
			this.flow.setBankcardNo(recogRequest.getBankcardNo());
			this.flow.setBusCode(recogRequest.getBuscode());
			this.flow.setChannel(recogRequest.getChannel());
			this.flow.setCreateTime(curDate);
			this.flow.setCtfname(recogRequest.getCtfname());
			this.flow.setCtfno(recogRequest.getCtfno());
			this.flow.setCtftype(recogRequest.getCtftype());
			this.flow.setCustomerId(recogRequest.getCustomerId());
			this.flow.setEngineCode(recogRequest.getEngineCode());
			this.flow.setFlowNum(new Date().getTime()+"");
			if(this.engineVer != null) {
				this.flow.setEngineVerCode(this.engineVer.getVerCode());
			}
			this.flow.setEquipmentNo(recogRequest.getEquipmentNo());
			this.flow.setInterVerCode(recogRequest.getVerCode());
			this.flow.setLegalOrgCode(recogRequest.getOrgCodePath());
			this.flow.setOrganizationNo(recogRequest.getOrganizationNo());
			this.flow.setTellerNo(recogRequest.getTellerNo());
			this.flow.setTradingCode(recogRequest.getTradingCode());
			this.flow.setTradingDate(recogRequest.getTradingDate());
			this.flow.setTradingFlowNo(recogRequest.getTradingFlowNO());
			this.flow.setTradingTime(recogRequest.getTradingTime());
			if(this.engineVer != null) {
				this.flow.setRecogstepId(this.engineVer.getRecogstepId());
			}
			this.flow.setRequestMsg(this.requestString);
			this.flow.setReponseMsg(this.respnseString);
			if(this.getP() != null) {
				this.flow.setPersonId(this.getP().getPersonId());
				if(this.getP().getPf() != null) {
					this.flow.setFeatureId(this.getP().getPf().getFeatureId());
				}
			}
		}
	}	
	
}
