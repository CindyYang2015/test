package com.cloudwalk.ibis.service.base.networkcheck;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;

/**
 * 联网核查服务实现类
 * @author zhuyf
 *
 */
@Service("networkCheckService")
public class NetworkCheckServiceImpl implements NetworkCheckService {

	@Override
	public void getFileData(TransferObj transferObj) throws ServiceException{
		//请求对象
		RecogRequest recogRequest = transferObj.getRecogRequest();		
		//判断联网核查文件是否已经上传
		if(StringUtils.isBlank(recogRequest.getNetCheckFileData())) {			
			//通过网络联网核查
			this.networkCheck(transferObj);
		} else {
			//设置当前联网核查文件的类型
			transferObj.setFeatureType(recogRequest.getNetCheckImgType());
		}
	}
	
	/**
	 * 通过第三方获取客户生物文件信息
	 * @param transferObj
	 */
	public void networkCheck(TransferObj transferObj) throws ServiceException{
		//如果核查失败，直接跑出ServiceException异常，如果成功，将文件数据和类型设置到RecogRequest相应的联网核查字段里面
//		transferObj.getRecogRequest().setNetCheckImgType(EnumClass.FileTypeEnum.HDTV.getValue());
//		transferObj.setFeatureType(EnumClass.FileTypeEnum.HDTV.getValue());
//		transferObj.getRecogRequest().setNetCheckFileData(netCheckFileData);
	}
	
}
