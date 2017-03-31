package com.cloudwalk.ibis.service.base.recog;

import com.cloudwalk.ibis.model.TransferObj;

/**
 * 异步保存识别认证流水信息
 * @author zhuyf
 *
 */
public class RecogFlowThread implements Runnable {
	
	/**
	 * 识别认证服务
	 */
	private RecogService recogService;
	
	/**
	 * 业务处理传输对象
	 */
	private TransferObj transferObj;
	
	public RecogFlowThread(){}
	
	public RecogFlowThread(RecogService recogService,TransferObj transferObj){
		this.recogService = recogService;
		this.transferObj = transferObj;
	}
	

	@Override
	public void run() {
		//保存流水
		if(this.recogService == null) return;
		this.recogService.saveRecogFlowInfo(transferObj);	
	}

}
