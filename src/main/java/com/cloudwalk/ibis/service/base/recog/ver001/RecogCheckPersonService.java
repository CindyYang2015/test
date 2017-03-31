package com.cloudwalk.ibis.service.base.recog.ver001;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;

import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowCertificateFeature;
import com.cloudwalk.ibis.model.result.ver001.CmpData;
import com.cloudwalk.ibis.model.result.ver001.Data;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowCertificateFeatureMapper;
import com.cloudwalk.ibis.service.base.engine.EngineService;
import com.cloudwalk.ibis.service.base.recog.RecogServiceImpl;

/**
 * 人脸识别证脸比对
 * @author zhuyf
 *
 */
public class RecogCheckPersonService extends RecogServiceImpl {
	
	@Resource(name = "channelFlowCertificateFeatureMapper")
	private ChannelFlowCertificateFeatureMapper channelFlowCertificateFeatureMapper;

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
		//获取文件数据
		this.getFileBase64String(recogRequest);
		//识别分数
		double simScore = 0;
		if(transferObj.getP() == null) {
			//判断是否联网核查，如果否返回客户不存在
			Integer netCheckStatus = recogRequest.getNetCheckStatus();
			if(netCheckStatus != null && EnumClass.NetCheckStatusEnum.YES.getValue() == netCheckStatus) {
				//联网核查
				this.networkCheckService.getFileData(transferObj);				
			} else {
				throw new ServiceException("客户不存在");
			}
			//获取联网核查和上传文件特征信息
			this.getFeature(transferObj);
			//注册客户
			this.createPerson(transferObj, recogRequest.getNetCheckFeature(), recogRequest.getNetCheckFileData());
			
			//比对
			EngineService engineService = transferObj.getEngineService();
			simScore = engineService.compareFeatureFile(recogRequest.getNetCheckFeature(), recogRequest.getNetCheckImgType(), recogRequest.getFeatureone(), recogRequest.getFileDataone(), EnumClass.FileTypeEnum.HDTV.getValue());
		} else {
			//获取上传文件特征信息
			if(EnumClass.FileTypeEnum.HDTV.getValue() == transferObj.getP().getPf().getFeatureType()) {
				this.getFeature(transferObj);
			} else {
				String featureone =transferObj.getEngineService().getFeature(recogRequest.getFileDataone(), EnumClass.FileTypeEnum.WATERMARK.getValue());
				if(StringUtils.isBlank(featureone)) {
					throw new ServiceException("获取文件1特征失败");
				}
				recogRequest.setFeatureone(featureone);
			}
			
			//比对
			String feature = Base64.encodeBase64String(transferObj.getP().getPf().getFeature());
			simScore = transferObj.getEngineService().compareFeatureFile(feature, transferObj.getP().getPf().getFeatureType(), recogRequest.getFeatureone(), recogRequest.getFileDataone(), EnumClass.FileTypeEnum.HDTV.getValue());
		}		
		transferObj.setSim1Score(simScore);
		
		//设置返回结果
		this.setRetResult(transferObj);	
		this.setTradingResult(transferObj);
	}

	@Override
	public boolean checkParam(RecogRequest recogRequest)
			throws ServiceException {
		if(super.checkParam(recogRequest)) {
			if(StringUtils.isBlank(recogRequest.getCtfno())
					|| StringUtils.isBlank(recogRequest.getCtfname())
					|| StringUtils.isBlank(recogRequest.getCtftype())
					|| StringUtils.isBlank(recogRequest.getVerCode())
					) {
					throw new ServiceException("参数不完整");
				}
			//判断上传认证的文件是否存在
			if(StringUtils.isBlank(recogRequest.getFilePathone())
					&& StringUtils.isBlank(recogRequest.getFileDataone())
					&& StringUtils.isBlank(recogRequest.getFeatureone())) {
				throw new ServiceException("上传认证的文件不能为空");
			}
			
		}
		return true;
	}
	
	@Override
	public void saveRecogFlowInfo(TransferObj transferObj) {
		//请求报文
		RecogRequest request = transferObj.getRecogRequest();			
		//设置流水上传文件与特征库比对分数
		ChannelFlow flow = transferObj.getFlow();	
		
		//保存流水文件
		String saveFilePath = this.saveFileData(request, EnumClass.LibTypeEnum.FLOW_TYPE.getValue(), request.getFileDataone());
		flow.setFileOnePath(saveFilePath);
		
		//流水记录中清除文件和特征信息
		if(!StringUtils.isBlank(request.getFileDataone())) {
			request.setFileDataone(null);
		}
		if(!StringUtils.isBlank(request.getFeatureone())) {
			request.setFeatureone(null);
		}
		//保存流水信息到流水主表
		super.saveRecogFlowInfo(transferObj);
		
		//保存流水到证脸比对 
		saveCertificateFeatureFlow(transferObj);
	}
	
	/**
	 * 保存证脸比对流水分表信息
	 * @param transferObj
	 */
	public void saveCertificateFeatureFlow(TransferObj transferObj){
		
		//组装证脸流水信息		
		ChannelFlow flow=transferObj.getFlow();
		ChannelFlowCertificateFeature channelFlowCertificateFeature=new ChannelFlowCertificateFeature();
		channelFlowCertificateFeature.setFlowId(flow.getFlowId());
		//比对照片
		channelFlowCertificateFeature.setFilePath(flow.getFileOnePath());
		channelFlowCertificateFeature.setPersonId(flow.getPersonId());		
		channelFlowCertificateFeature.setFeatureId(flow.getFeatureId());
		//注册照片
		channelFlowCertificateFeature.setFeatureFilePath(transferObj.getFilePath());
		channelFlowCertificateFeature.setRemark(flow.getRemark());
		//获取证脸比对结果
		Data data = transferObj.getResult().getData();
		if(data != null) {
			//获取比对的分数
			CmpData cmpData = (CmpData)data;
			if(cmpData.getSimScore() != null) {
				channelFlowCertificateFeature.setCmpscore(BigDecimal.valueOf(cmpData.getSimScore()));
			}
		}
		//保存流水信息到证脸流水信息表
		channelFlowCertificateFeatureMapper.insertSelective(channelFlowCertificateFeature);
		
	}
	@Override
	public void setRetResult(TransferObj transferObj) throws ServiceException {
		Result result = null;		
		//设置需要查询白名单
		transferObj.setQueryWhiteStatus(EnumClass.StatusEnum.YES.getValue());
		//获取阈值
		double score = this.getThresholdScore(transferObj);
		//当前识别分数
		double simScore = transferObj.getSim1Score();
		if(simScore >= score) {
			result = Result.initSuccessResult();
		} else {
			result = Result.initRecogFailResult();
		}
		CmpData data = new CmpData(simScore);
		result.setData(data);
		transferObj.setResult(result);		
		super.setRetResult(transferObj);
	}	
	
}
