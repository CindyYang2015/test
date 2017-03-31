package com.cloudwalk.ibis.service.base.recog.ver001;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowRegister;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowRegisterMapper;
import com.cloudwalk.ibis.service.base.recog.RecogServiceImpl;

/**
 * 人脸识别注册
 * @author zhuyf
 *
 */
public class RecogAddService extends RecogServiceImpl {
	
	@Resource(name = "channelFlowRegisterMapper")
	private ChannelFlowRegisterMapper channelFlowRegisterMapper;
	
	protected final Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public void execute(TransferObj transferObj) throws ServiceException {
		//请求对象
		RecogRequest recogRequest = transferObj.getRecogRequest();
		//参数验证
		this.checkParam(recogRequest);		
		//初始化算法引擎服务
		this.initEngineService(transferObj);		
		//验证黑名单信息
		this.checkBlackPerson(transferObj);
		//获取客户信息
		this.getPerson(transferObj);
		
		//判断客户或者生物特征是否存在
		if(transferObj.getP() == null || transferObj.getP().getPf() == null) {			
			
			//获取上传文件base64信息
			this.getFileBase64String(recogRequest);
			//判断是否联网核查获取客户文件信息
			Integer netCheckStatus = recogRequest.getNetCheckStatus();
			
			if(netCheckStatus != null && EnumClass.NetCheckStatusEnum.YES.getValue() == netCheckStatus) {
				
				if(StringUtils.isBlank(recogRequest.getNetCheckFeature())) {					
					//联网核查
					this.networkCheckService.getFileData(transferObj);
					//获取联网核查文件特征信息
					this.getFeature(transferObj);
					//注册客户
					this.createPerson(transferObj, recogRequest.getNetCheckFeature(), recogRequest.getNetCheckFileData());
				}
				
			} else {		
				//默认为高清
				transferObj.setFeatureType(EnumClass.FileTypeEnum.HDTV.getValue());
				//获取上传文件特征信息
				this.getFeature(transferObj);
				//注册客户
				this.createPerson(transferObj, recogRequest.getRegfeature(), recogRequest.getRegfileData());
					
			}				
			
			//设置返回结果
			this.setRetResult(transferObj);
			this.setTradingResult(transferObj);
			
		} else {			
			throw new ServiceException("客户已存在");
		}
	}

	@Override
	public boolean checkParam(RecogRequest recogRequest)
			throws ServiceException {
		if(super.checkParam(recogRequest)) {
			if(StringUtils.isBlank(recogRequest.getCtfno())
					|| StringUtils.isBlank(recogRequest.getCtfname())
					|| StringUtils.isBlank(recogRequest.getCtftype())
					) {
					throw new ServiceException("参数不完整");
				}
			if(recogRequest.getNetCheckStatus() == null || EnumClass.NetCheckStatusEnum.YES.getValue() != recogRequest.getNetCheckStatus()) {
				if(StringUtils.isBlank(recogRequest.getRegFilePath())
						&& StringUtils.isBlank(recogRequest.getRegfileData())
						&& StringUtils.isBlank(recogRequest.getRegfeature())) {
					throw new ServiceException("注册文件数据不能为空");
				}
			}
		}
		return true;
	}

	@Override
	public void saveRecogFlowInfo(TransferObj transferObj) {		
		//请求报文对象
		RecogRequest recogRequest = transferObj.getRecogRequest();
		
		if(!StringUtils.isBlank(recogRequest.getRegfileData())) {
			recogRequest.setRegfileData(null);
		}
		if(!StringUtils.isBlank(recogRequest.getRegfeature())) {
			recogRequest.setRegfeature(null);
		}
		//保存流水信息到流水主表
		super.saveRecogFlowInfo(transferObj);
		
		//保存流水信息到注册人脸流水表
		saveRegisterFlow(transferObj);
	}
	
	/**
	 * 保存注册流水分表信息
	 * @param transferObj
	 */
	public void saveRegisterFlow(TransferObj transferObj){
		
		//组装人脸注册流水信息
		ChannelFlowRegister channelFlowRegister=new ChannelFlowRegister();
		ChannelFlow flow=transferObj.getFlow();
		channelFlowRegister.setPersonId(flow.getPersonId());		
		channelFlowRegister.setFeatureId(flow.getFeatureId());
		channelFlowRegister.setFlowId(flow.getFlowId());
		channelFlowRegister.setFilePath(transferObj.getFilePath());
		channelFlowRegister.setRemark(flow.getRemark());
		
		//保存流水信息到人脸注册流水信息表
		channelFlowRegisterMapper.insertSelective(channelFlowRegister);		
	}

	@Override
	public void setRetResult(TransferObj transferObj) throws ServiceException {
		Result result = Result.initSuccessResult();
		transferObj.setResult(result);
		super.setRetResult(transferObj);
	}
	
	
}
