package com.cloudwalk.ibis.service.base.recog.ver001;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowCard;
import com.cloudwalk.ibis.model.result.ver001.Data;
import com.cloudwalk.ibis.model.result.ver001.IDCardData;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowCardMapper;
import com.cloudwalk.ibis.service.base.recog.RecogServiceImpl;

/**
 * 身份证识别服务
 * @author zhuyf
 *
 */
public class RecogOCRService extends RecogServiceImpl {
	
	@Resource(name = "channelFlowCardMapper")
	private ChannelFlowCardMapper channelFlowCardMapper;
	
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
		//识别
		IDCardData idcardData = this.ocrIDCard(transferObj);
		//设置返回结果
		this.setRetResult(transferObj);	
		transferObj.getResult().setData(idcardData);
		this.setTradingResult(transferObj);

	}	
	
	@Override
	public boolean checkParam(RecogRequest recogRequest)
			throws ServiceException {
		if(super.checkParam(recogRequest)) {			
			//判断上传认证的身份证正面是否存在
			if(StringUtils.isBlank(recogRequest.getFrontImge())) {
				throw new ServiceException("上传认证的身份证正面不能为空");
			}
			//判断上传认证的身份证反面是否存在
			if(StringUtils.isBlank(recogRequest.getBackImge())) {
				throw new ServiceException("上传认证的身份证反面不能为空");
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
		String saveFilePath1 = this.saveFileData(request, EnumClass.LibTypeEnum.FLOW_TYPE.getValue(), request.getFrontImge());
		String saveFilePath2 = this.saveFileData(request, EnumClass.LibTypeEnum.FLOW_TYPE.getValue(), request.getBackImge());
		flow.setFileOnePath(saveFilePath1);
		flow.setFileTwoPath(saveFilePath2);
		
		//流水记录中清除文件信息
		if(!StringUtils.isBlank(request.getFrontImge())) {
			request.setFrontImge(null);
		}		
		if(!StringUtils.isBlank(request.getBackImge())) {
			request.setBackImge(null);
		}
				
		super.saveRecogFlowInfo(transferObj);
		
		this.saveChannelFlowCard(transferObj);
	}

	/**
	 * 保存身份证识别流水分表信息
	 * @param transferObj
	 */
	private void saveChannelFlowCard(TransferObj transferObj) {
		
		//组装身份证流水对象
		ChannelFlow flow=transferObj.getFlow();
		ChannelFlowCard channelFlowCard = new ChannelFlowCard();
		channelFlowCard.setFlowId(flow.getFlowId());
		channelFlowCard.setRemark(flow.getRemark());
		channelFlowCard.setBlackImgpath(flow.getFileTwoPath());
		channelFlowCard.setFrontImgpath(flow.getFileOnePath());
		
		//获取身份证识别结果对象
		Data data = transferObj.getResult().getData();
		if(data != null) {
			IDCardData idcardData = (IDCardData)data;
			channelFlowCard.setAddress(idcardData.getAddress());
			channelFlowCard.setBirthday(idcardData.getBirthday());
			channelFlowCard.setFolk(idcardData.getFolk());
			channelFlowCard.setIdcardNo(idcardData.getCtfno());
			channelFlowCard.setSex(idcardData.getSex());
			channelFlowCard.setIdcardName(idcardData.getCtfname());
			channelFlowCard.setAuthority(idcardData.getAuthority());
			//设置身份证有效期
			if(!StringUtils.isBlank(idcardData.getValiddate())) {
				String[] validdateArray = idcardData.getValiddate().split("_");
				if(validdateArray.length >= 1) {
					channelFlowCard.setValiddate1(validdateArray[0]);
				}
				if(validdateArray.length >= 2) {
					channelFlowCard.setValiddate2(validdateArray[1]);
				}
			}
		}		
		
		//保存身份证OCR信息
		this.channelFlowCardMapper.insertSelective(channelFlowCard);
	}

	@Override
	public void setRetResult(TransferObj transferObj) throws ServiceException {
		Result result = Result.initSuccessResult();		
		transferObj.setResult(result);
		super.setRetResult(transferObj);
	}
	
}
