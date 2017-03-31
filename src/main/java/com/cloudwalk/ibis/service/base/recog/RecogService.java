package com.cloudwalk.ibis.service.base.recog;

import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.ibis.model.TransferObj;

/**
 * 生物识别认证服务类
 * @author zhuyf
 *
 */
public interface RecogService {	
	
	/**
	 * 识别认证业务处理方法
	 * @param transferObj
	 * @return
	 */
	public void execute(TransferObj transferObj) throws ServiceException;
	
	/**
	 * 保存识别流水
	 * @param transferObj
	 */
	public void saveRecogFlowInfo(TransferObj transferObj);

}
