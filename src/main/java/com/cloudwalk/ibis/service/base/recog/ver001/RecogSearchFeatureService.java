package com.cloudwalk.ibis.service.base.recog.ver001;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowSearchImg;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.model.result.ver001.SearchData;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowSearchImgMapper;
import com.cloudwalk.ibis.service.base.engine.SearchFeature;
import com.cloudwalk.ibis.service.base.recog.RecogServiceImpl;
import com.google.common.collect.Lists;

/**
 * 人脸识别按照人脸特征检索相似客户
 * @author zhuyf
 *
 */
public class RecogSearchFeatureService extends RecogServiceImpl {
	
	@Resource(name = "channelFlowSearchImgMapper")
	private ChannelFlowSearchImgMapper channelFlowSearchImgMapper;
	
	@Override
	public void execute(TransferObj transferObj) throws ServiceException {
		//请求对象
		RecogRequest recogRequest = transferObj.getRecogRequest();
		//参数验证
		this.checkParam(recogRequest);
		//初始化算法引擎服务
		this.initEngineService(transferObj);	
		
		//获取检索文件特征
		this.getFileBase64String(recogRequest);
		this.getFeature(transferObj);		
		List<SearchFeature> sfList = transferObj.getEngineService().searchPersonByFeature(recogRequest.getFeatureone(), EnumClass.FileTypeEnum.HDTV.getValue(), Constants.Config.FACE_GROUP_ID, recogRequest.getNumber());
		if(ObjectUtils.isEmpty(sfList)) {
			throw new ServiceException("检索结果为空");
		}
		//获取客户信息
		List<PersonFeature> pfList = Lists.newArrayList();
		for(SearchFeature sf:sfList) {			
			PersonFeature temppf = this.personService.selectPersonByFeatureId(sf.getFeatureId());
			if(temppf == null) continue;
			temppf.setScore(sf.getScore());
			pfList.add(temppf);
		}
		if(pfList.isEmpty()) {
			throw new ServiceException("未找到匹配的客户信息");
		}
		SearchData data = new SearchData(pfList);
		
		//设置返回结果
		this.setRetResult(transferObj);		
		transferObj.getResult().setData(data);
		this.setTradingResult(transferObj);
		
	}

	@Override
	public boolean checkParam(RecogRequest recogRequest)
			throws ServiceException {
		if(super.checkParam(recogRequest)) {			
			//判断上传认证的文件是否存在
			if(StringUtils.isBlank(recogRequest.getFilePathone())
					&& StringUtils.isBlank(recogRequest.getFileDataone())
					&& StringUtils.isBlank(recogRequest.getFeatureone())) {
				throw new ServiceException("上传检索的文件不能为空");
			}			
		}
		return true;
	}
	
	@Override
	public void saveRecogFlowInfo(TransferObj transferObj) {
		//请求报文
		RecogRequest request = transferObj.getRecogRequest();
		//保存流水文件
		ChannelFlow flow = transferObj.getFlow();	
		String saveFilePath = this.saveFileData(request, EnumClass.LibTypeEnum.FLOW_TYPE.getValue(), request.getFileDataone());
		flow.setFileOnePath(saveFilePath);
		
		//流水记录中清除文件和特征信息
		if(!StringUtils.isBlank(request.getFileDataone())) {
			request.setFileDataone(null);
		}
		if(!StringUtils.isBlank(request.getFeatureone())) {
			request.setFeatureone(null);
		}	
		
		super.saveRecogFlowInfo(transferObj);
		
		this.saveChannelFlowSearchImg(transferObj);
	}

	/**
	 * 保存按脸检索流水分表信息
	 * @param flow
	 */
	private void saveChannelFlowSearchImg(TransferObj transferObj) {
		
		//组装按脸检索流水对象
		ChannelFlow flow=transferObj.getFlow();
		ChannelFlowSearchImg channelFlowSearchImg = new ChannelFlowSearchImg();
		channelFlowSearchImg.setFlowId(flow.getFlowId());
		channelFlowSearchImg.setFilePath(flow.getFileOnePath());
		channelFlowSearchImg.setRemark(flow.getRemark());
		
		this.channelFlowSearchImgMapper.insertSelective(channelFlowSearchImg);
	}

	@Override
	public void setRetResult(TransferObj transferObj) throws ServiceException {
		Result result = Result.initSuccessResult();		
		transferObj.setResult(result);
		super.setRetResult(transferObj);
	}
	
}
