package com.cloudwalk.ibis.webservice;

import javax.jws.WebService;

@WebService
public interface RecogWebService {
	
	/**
	 * 处理业务
	 * @param requestData
	 */
	public String excute(String requestData);
}
