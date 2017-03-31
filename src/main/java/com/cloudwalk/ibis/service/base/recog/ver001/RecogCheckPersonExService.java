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
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowCompareStrengthen;
import com.cloudwalk.ibis.model.result.ver001.CmpExData;
import com.cloudwalk.ibis.model.result.ver001.Data;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowCompareStrengthenMapper;
import com.cloudwalk.ibis.service.base.recog.RecogServiceImpl;

/**
 * 人脸识别两证一脸比对
 * @author zhuyf
 *
 */
public class RecogCheckPersonExService extends RecogServiceImpl {

	@Resource(name = "channelFlowCompareStrengthenMapper")
	private ChannelFlowCompareStrengthenMapper channelFlowCompareStrengthenMapper;
	
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
		//识别分数1
		double sim1Score = 0;
		//识别分数2
		double sim2Score = 0;
		if(transferObj.getP() == null) {
			//判断是否联网核查，如果否返回客户不存在
			Integer netCheckStatus = recogRequest.getNetCheckStatus();
			if(netCheckStatus != null && EnumClass.NetCheckStatusEnum.YES.getValue() == netCheckStatus) {
				//联网核查
				this.networkCheckService.getFileData(transferObj);	
				//判断联网核查照片是否存在
				if(StringUtils.isBlank(recogRequest.getNetCheckFileData())){
					throw new ServiceException("客户不存在，且联网核查失败");
				}
			} else {
				throw new ServiceException("客户不存在");
			}
			//获取联网核查和上传文件特征信息
			this.getFeature(transferObj);			
			//注册客户
			this.createPerson(transferObj, recogRequest.getNetCheckFeature(), recogRequest.getNetCheckFileData());
			//联网核查和上传文件1比对
			sim1Score = transferObj.getEngineService().compareFeatureFile(recogRequest.getFeatureone(),EnumClass.FileTypeEnum.HDTV.getValue(),recogRequest.getNetCheckFeature(),recogRequest.getNetCheckFileData(),recogRequest.getNetCheckImgType());
			//联网核查和上传文件2比对
			sim2Score = transferObj.getEngineService().compareFeatureFile(recogRequest.getFeaturetwo(),EnumClass.FileTypeEnum.HDTV.getValue(),recogRequest.getNetCheckFeature(),recogRequest.getNetCheckFileData(),recogRequest.getNetCheckImgType());
		
		} else {
			//获取上传文件特征信息
			this.getFeature(transferObj);
			//比对
			String feature = Base64.encodeBase64String(transferObj.getP().getPf().getFeature());
			int featureType = transferObj.getP().getPf().getFeatureType();
			sim1Score = transferObj.getEngineService().compareFeatureFile(feature,featureType,recogRequest.getFeatureone(),recogRequest.getFileDataone(),EnumClass.FileTypeEnum.HDTV.getValue());
			sim2Score =  transferObj.getEngineService().compareFeatureFile(feature,featureType,recogRequest.getFeaturetwo(),recogRequest.getFileDatatwo(),EnumClass.FileTypeEnum.HDTV.getValue());
		}		
		transferObj.setSim1Score(sim1Score);
		transferObj.setSim2Score(sim2Score);
		
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
					) {
					throw new ServiceException("参数不完整");
				}
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
		//返回结果		
		ChannelFlow flow = transferObj.getFlow();	
		//设置流水上传文件与特征库比对分数
			
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
		
		//保存主表信息
		super.saveRecogFlowInfo(transferObj);
		
		//保存分表信息
		saveChannelFlowCpe(transferObj);

	}
	
	
	/**
	 * 保存两证一脸比对流水分表信息
	 * @param transferObj
	 * @return
	 */
	public int saveChannelFlowCpe(TransferObj transferObj){
		
		//保存分表信息
		ChannelFlow flow=transferObj.getFlow();
		ChannelFlowCompareStrengthen channelFlowCompareStrengthen=new ChannelFlowCompareStrengthen();
		channelFlowCompareStrengthen.setFeatureFilePath(transferObj.getFilePath());
		channelFlowCompareStrengthen.setFeatureId(flow.getFeatureId());
		channelFlowCompareStrengthen.setFileOnePath(flow.getFileOnePath());
		channelFlowCompareStrengthen.setFileTwoPath(flow.getFileTwoPath());
		channelFlowCompareStrengthen.setFlowId(flow.getFlowId());
		//两证一脸识别结果信息
		Data data = transferObj.getResult().getData();
		if(data != null) {
			CmpExData cmpExData = (CmpExData)data;
			//获取识别分数1		
			if(cmpExData.getSim1Score() != null) {
				channelFlowCompareStrengthen.setFpOneCmpscore(BigDecimal.valueOf(cmpExData.getSim1Score()));
			}
			//识别分数2
			if(cmpExData.getSim2Score() != null) {
				channelFlowCompareStrengthen.setFpTwoCmpscore(BigDecimal.valueOf(cmpExData.getSim2Score()));
			}
		}
		channelFlowCompareStrengthen.setPersonId(flow.getPersonId());
		channelFlowCompareStrengthen.setRemark(flow.getRemark());
				
		int ret = channelFlowCompareStrengthenMapper.insertSelective(channelFlowCompareStrengthen);
		return ret;
	}

	@Override
	public void setRetResult(TransferObj transferObj) throws ServiceException {
		Result result = null;
		transferObj.setQueryWhiteStatus(EnumClass.StatusEnum.YES.getValue());
		//获取阈值
		double score = this.getThresholdScore(transferObj);
		//当前识别分数1
		double sim1Score = transferObj.getSim1Score();
		//当前识别分数2
		double sim2Score = transferObj.getSim2Score();
		if(sim1Score >= score && sim2Score >= score) {
			result = Result.initSuccessResult();
		} else {
			result = Result.initRecogFailResult();
		}
		//设置识别分数
		CmpExData data = new CmpExData(sim1Score,sim2Score);
		result.setData(data);
		transferObj.setResult(result);
		super.setRetResult(transferObj);	
	}
	
}
