package com.cloudwalk.ibis.webservice;

import javax.annotation.Resource;
import javax.jws.WebService;

import com.cloudwalk.ibis.service.base.recog.RecogManageService;

@WebService(endpointInterface = "com.cloudwalk.ibis.webservice.RecogWebService")
public class RecogWebServiceImpl implements RecogWebService {

	/**
	 * 业务处理接口
	 */
	@Resource(name="recogManageService")
	private RecogManageService recogManageService;

	@Override
	public String excute(String requestData) {
		return this.recogManageService.handleRequest(requestData);
	}
	
	
	
	
	
}
