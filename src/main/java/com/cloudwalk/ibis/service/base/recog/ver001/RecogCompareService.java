package com.cloudwalk.ibis.service.base.recog.ver001;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowFaceToFace;
import com.cloudwalk.ibis.model.result.ver001.CmpData;
import com.cloudwalk.ibis.model.result.ver001.Data;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowFaceCompareMapper;
import com.cloudwalk.ibis.service.base.recog.RecogServiceImpl;

/**
 * 人脸识别脸脸比对
 * @author zhuyf
 *
 */
public class RecogCompareService extends RecogServiceImpl {

	@Resource(name = "channelFlowFaceCompareMapper")
	private ChannelFlowFaceCompareMapper channelFlowFaceCompareMapper;

	@Override
	public void execute(TransferObj transferObj) throws ServiceException {
		//请求对象
		RecogRequest recogRequest = transferObj.getRecogRequest();
		//参数验证
		this.checkParam(recogRequest);
		//初始化算法引擎服务
		this.initEngineService(transferObj);		
		//获取文件数据
		this.getFileBase64String(recogRequest);		
		//获取上传文件特征信息
		this.getFeature(transferObj);
		//比对分数
		double compareScore = 0;
		//文件1与文件2比对
		compareScore = transferObj.getEngineService().compareFeature(recogRequest.getFeatureone(),EnumClass.FileTypeEnum.HDTV.getValue(),recogRequest.getFeaturetwo(),EnumClass.FileTypeEnum.HDTV.getValue());
		transferObj.setSim1Score(compareScore);
		
		//设置返回结果
		this.setRetResult(transferObj);	
		this.setTradingResult(transferObj);

	}
	
	@Override
	public boolean checkParam(RecogRequest recogRequest)
			throws ServiceException {
		if(super.checkParam(recogRequest)) {			
			//判断上传认证的文件1是否存在
			if(StringUtils.isBlank(recogRequest.getFilePathone())
					&& StringUtils.isBlank(recogRequest.getFileDataone())
					&& StringUtils.isBlank(recogRequest.getFeatureone())) {
				throw new ServiceException("上传认证的文件1不能为空");
			}
			//判断上传认证的文件2是否存在
			if(StringUtils.isBlank(recogRequest.getFilePathtwo())
					&& StringUtils.isBlank(recogRequest.getFileDatatwo())
					&& StringUtils.isBlank(recogRequest.getFeaturetwo())) {
				throw new ServiceException("上传认证的文件2不能为空");
			}			
		}
		return true;
	}

	@Override
	public void saveRecogFlowInfo(TransferObj transferObj) {
		//请求报文
		RecogRequest request = transferObj.getRecogRequest();
		//主表流水
		ChannelFlow flow = transferObj.getFlow();			
		
		//保存流水文件
		String saveFilePath1 = this.saveFileData(request, EnumClass.LibTypeEnum.FLOW_TYPE.getValue(), request.getFileDataone());
		String saveFilePath2 = this.saveFileData(request, EnumClass.LibTypeEnum.FLOW_TYPE.getValue(), request.getFileDatatwo());
		flow.setFileOnePath(saveFilePath1);
		flow.setFileTwoPath(saveFilePath2);
		
		//流水记录中清除文件和特征信息
		if(!StringUtils.isBlank(request.getFileDataone())) {
			request.setFileDataone(null);
		}
		if(!StringUtils.isBlank(request.getFeatureone())) {
			request.setFeatureone(null);
		}
		if(!StringUtils.isBlank(request.getFileDatatwo())) {
			request.setFileDatatwo(null);
		}
		if(!StringUtils.isBlank(request.getFeaturetwo())) {
			request.setFeaturetwo(null);
		}
		//保存流水到主流水表
		super.saveRecogFlowInfo(transferObj);
		
		//保存脸脸对比流水到脸脸对比流水表 
		saveFaceCompareFlow(transferObj);
	}
	
	public void saveFaceCompareFlow(TransferObj transferObj){
		
		//组装脸脸流水表信息
		ChannelFlow flow=transferObj.getFlow();
		ChannelFlowFaceToFace channelFlowFaceToFace=new ChannelFlowFaceToFace();
		channelFlowFaceToFace.setFlowId(flow.getFlowId());
		channelFlowFaceToFace.setFileonePath(flow.getFileOnePath());
		channelFlowFaceToFace.setFiletwoPath(flow.getFileTwoPath());
		channelFlowFaceToFace.setRemark(flow.getRemark());	
		
		///设置流水上传文件与特征库比对分数
		Data data = transferObj.getResult().getData();
		if(data != null) {
			//获取比对的分数
			CmpData cmpData = (CmpData)data;
			if(cmpData.getSimScore() != null) {
				channelFlowFaceToFace.setCmpscore(BigDecimal.valueOf(cmpData.getSimScore()));
			}
		}
		
		//插入脸脸对比流水表
		channelFlowFaceCompareMapper.insertSelective(channelFlowFaceToFace);
		
		
	}

	@Override
	public void setRetResult(TransferObj transferObj) throws ServiceException {
		Result result = null;
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
