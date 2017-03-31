package com.cloudwalk.ibis.service.base.recog.ver001;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowBank;
import com.cloudwalk.ibis.model.result.ver001.BankCardData;
import com.cloudwalk.ibis.model.result.ver001.Data;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowBankMapper;
import com.cloudwalk.ibis.service.base.recog.RecogServiceImpl;

/**
 * 银行卡识别服务
 * @author zhuyf
 *
 */
public class RecogBankCardOCRService extends RecogServiceImpl {
	
	@Resource(name = "channelFlowBankMapper")
	private ChannelFlowBankMapper channelFlowBankMapper;
	
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
		BankCardData bankCardData = this.ocrBankCard(transferObj);
		//设置返回结果
		this.setRetResult(transferObj);	
		transferObj.getResult().setData(bankCardData);
		this.setTradingResult(transferObj);

	}	
	
	@Override
	public boolean checkParam(RecogRequest recogRequest)
			throws ServiceException {
		if(super.checkParam(recogRequest)) {			
			//判断上传的银行卡是否存在
			if(StringUtils.isBlank(recogRequest.getBankCardImg())) {
				throw new ServiceException("上传的银行卡不能为空");
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
		String saveFilePath1 = this.saveFileData(request, EnumClass.LibTypeEnum.FLOW_TYPE.getValue(), request.getBankCardImg());
		flow.setFileOnePath(saveFilePath1);
		
		//流水记录中清除文件信息
		if(!StringUtils.isBlank(request.getBankCardImg())) {
			request.setFrontImge(null);
		}			
				
		super.saveRecogFlowInfo(transferObj);
		
		this.saveChannelFlowBank(transferObj);
	}

	/**
	 * 保存银行卡流水分表信息
	 * @param transferObj
	 */
	private void saveChannelFlowBank(TransferObj transferObj) {
		
		ChannelFlow flow=transferObj.getFlow();
		ChannelFlowBank channelFlowBank = new ChannelFlowBank();
		channelFlowBank.setFlowId(transferObj.getFlow().getFlowId());		
		channelFlowBank.setImgpath(flow.getFileOnePath());
		channelFlowBank.setRemark(transferObj.getFlow().getRemark());
		
		Data data = transferObj.getResult().getData();
		if(data != null) {
			BankCardData bankCardData = (BankCardData) transferObj.getResult().getData();
			channelFlowBank.setBankCardType(bankCardData.getCardType());
			channelFlowBank.setBankName(bankCardData.getBankName());
			channelFlowBank.setCardName(bankCardData.getCardName());
			channelFlowBank.setBankCardNo(bankCardData.getCardNum());
		}		
		
		this.channelFlowBankMapper.insertSelective(channelFlowBank);
	}

	@Override
	public void setRetResult(TransferObj transferObj) throws ServiceException {
		Result result = Result.initSuccessResult();		
		transferObj.setResult(result);
		super.setRetResult(transferObj);
	}
	
}
