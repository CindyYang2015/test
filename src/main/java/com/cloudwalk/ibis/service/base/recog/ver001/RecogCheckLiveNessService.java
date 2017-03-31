package com.cloudwalk.ibis.service.base.recog.ver001;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.cloudwalk.common.engine.face.eoc.Liveness;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowDetectLive;
import com.cloudwalk.ibis.model.result.ver001.LivenessData;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowDetectLiveMapper;
import com.cloudwalk.ibis.service.base.recog.RecogServiceImpl;

/**
 * 活体检测
 * @author 罗磊
 *
 */
public class RecogCheckLiveNessService extends RecogServiceImpl {
	
	@Resource(name = "channelFlowDetectLiveMapper")
	private ChannelFlowDetectLiveMapper channelFlowDetectLiveMapper;
	@Override
	public void execute(TransferObj transferObj) throws ServiceException {
		
		//请求对象
		RecogRequest recogRequest = transferObj.getRecogRequest();
		//参数验证
		this.checkParam(recogRequest);
		//初始化算法引擎服务
		this.initEngineService(transferObj);
		//处理
		Liveness liveness = this.judgeLiveness(transferObj);	
		//返回最佳人脸数据
		LivenessData livenessData = new LivenessData();
		livenessData.setImg(liveness.getImg());
		//设置返回结果
		this.setRetResult(transferObj);
		transferObj.getResult().setData(livenessData);
		
		this.setTradingResult(transferObj);
	}

	@Override
	public boolean checkParam(RecogRequest recogRequest)
			throws ServiceException {
		if(super.checkParam(recogRequest)) {
			if(StringUtils.isBlank(recogRequest.getLivenessData())) {
					throw new ServiceException("活体数据为空");
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
		//保存返回结果的最佳人脸
		Result result = transferObj.getResult();
		if(result.getData() != null) {
			LivenessData data = (LivenessData)result.getData();
			String saveFilePath1 = this.saveFileData(request, EnumClass.LibTypeEnum.FLOW_TYPE.getValue(),data.getImg());
			flow.setFileOnePath(saveFilePath1);
		}
		
		//流水记录中清除文件信息
		if(!StringUtils.isBlank(request.getLivenessData())) {
			request.setLivenessData(null);
		}				
		
		super.saveRecogFlowInfo(transferObj);
		
		this.saveChannelFlowLiveness(transferObj);
	}
	
	/**
	 * 保存活体检测数据
	 * @param transferObj
	 */
	private void saveChannelFlowLiveness(TransferObj transferObj) {
		
		//保存分表流水信息
		ChannelFlow flow = transferObj.getFlow();		
		//保存分表记录
		ChannelFlowDetectLive channelFlowDetectLive=new ChannelFlowDetectLive();
		channelFlowDetectLive.setFlowId(flow.getFlowId());
		channelFlowDetectLive.setImgpath(flow.getFileOnePath());
		this.channelFlowDetectLiveMapper.insertSelective(channelFlowDetectLive);
	}	

	@Override
	public void setRetResult(TransferObj transferObj) throws ServiceException {
		Result result = Result.initSuccessResult();
		transferObj.setResult(result);
		super.setRetResult(transferObj);
	}
	
}
