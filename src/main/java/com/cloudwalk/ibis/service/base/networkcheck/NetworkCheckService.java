package com.cloudwalk.ibis.service.base.networkcheck;

import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.ibis.model.TransferObj;

/**
 * 联网核查服务
 * @author zhuyf
 *
 */
public interface NetworkCheckService {
	
	/**
	 * 联网核查获取文件数据
	 * @param transferObj
	 * @return
	 */
	public void getFileData(TransferObj transferObj) throws ServiceException;

}
