package com.cloudwalk.ibis.service.base.recog;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.common.InterfaceConst;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.DesUtil;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;
import com.cloudwalk.ibis.model.result.ver001.Response;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.service.base.CacheService;
import com.cloudwalk.ibis.service.base.ThreadPoolService;

/**
 * 识别认证总控制服务管理
 * 1.请求字符串解密
 * 2.根据数据格式转换成请求实体类
 * 3.根据接口代码和版本号调用对应的服务
 * 4.响应客户端
 * 5.将识别认证结果转成对应格式的字符串返回
 * @author zhuyf
 *
 */
@Service("recogManageService")
public class RecogManageService {
	
	protected static Logger logger = Logger.getLogger(RecogManageService.class);
	
	/**
	 * 数据解析对象
	 */
	private DataParse dataParse;
	
	/**
	 * 接口版本管理
	 */
	@Resource(name="recogVerManage")
	private RecogVerManageService recogVerManage;
	
	/**
	 * 线程池服务
	 */
	@Resource(name="threadPoolService")
	private ThreadPoolService threadPoolService;
	
	/**
	 * 缓存服务
	 */
	@Resource(name="cacheService")
	private CacheService cacheService;
	
	@PostConstruct
	public void init() {
		dataParse = new DataParse();
	}

	/**
	 * 识别认证处理
	 * @param requestData 请求数据
	 * @return
	 */
	public String handleRequest(String requestData) {
		
		long startTime = new Date().getTime();
		
		//响应信息
		String retMsg = "";
		//业务传输对象
		TransferObj transferObj = new TransferObj();
		//识别认证服务
		RecogService recogService = null;
		try{
			//1.请求数据判空
			if(StringUtils.isBlank(requestData)) {
				throw new ServiceException("请求数据不能为空");
			}			
			
			//2.请求数据解密
			long startTime1 = new Date().getTime();
			String decryptData = this.decrypt(requestData);		
			long endTime1 = new Date().getTime();
			transferObj.addHandleTimes("数据解密耗时："+(endTime1-startTime1)+"毫秒");
			
			//3.请求数据解析成java对象
			long startTime2 = new Date().getTime();
			RecogRequest recogRequest = this.dataParse.parseDataBean(decryptData);			
			//获取机构代码全路径
			String orgCodePath = this.cacheService.getLegalOrgCodePath(recogRequest.getOrgCode());
			if(StringUtils.isBlank(orgCodePath)) {
				throw new ServiceException("机构"+recogRequest.getOrgCode()+"不存在");
			}
			recogRequest.setOrgCodePath(orgCodePath);			
			//设置请求报文
			transferObj.setRecogRequest(recogRequest);
			transferObj.setRequestString(requestData);
			long endTime2 = new Date().getTime();
			transferObj.addHandleTimes("数据解析耗时："+(endTime2-startTime2)+"毫秒");
						
			//4.识别认证处理			
			//判断该接口对应的版本是否开启 ocrBankCard ver001
			long startTime3 = new Date().getTime();
			if(this.cacheService.getInterfaceVer(recogRequest.getBuscode(), recogRequest.getVerCode()) == null) {
				throw new ServiceException("该接口服务不存在或者未被开启");
			}
			long endTime3 = new Date().getTime();
			transferObj.addHandleTimes("判断接口服务是否开启耗时："+(endTime3-startTime3)+"毫秒");
			//获取接口对象
			recogService = this.recogVerManage.getRecogService(recogRequest.getBuscode(), recogRequest.getVerCode());
			if(recogService == null) {
				throw new ServiceException("该接口服务不存在");
			}			
			
			//5.执行业务处理
			long startTime5 = new Date().getTime();
			recogService.execute(transferObj);
			long endTime5 = new Date().getTime();
			transferObj.addHandleTimes("业务处理耗时："+(endTime5-startTime5)+"毫秒");
			
			//6.将识别认证结果转成对应格式的字符串返回
			long startTime4 = new Date().getTime();
			retMsg = this.dataParse.parseDataStr(transferObj.getResponse());
			long endTime4 = new Date().getTime();
			transferObj.addHandleTimes("识别认证结果解析耗时："+(endTime4-startTime4)+"毫秒");
			
		} catch(Exception e) {
			logger.error("接口处理发生异常",e);
			Result result = new Result();
			if(e instanceof ServiceException) {
				ServiceException se = (ServiceException)e;
				result.setCode(InterfaceConst.SERVICE_EXCEPTIONE);
				result.setMessage(se.getMessage());
			} else {
				result.setCode(InterfaceConst.EXCEPTIONE);
				result.setMessage("系统异常，请联系管理员");
			}
			transferObj.setResult(result);
			transferObj.setResponse(Response.getFailResponse(result));
			retMsg = this.dataParse.parseDataStr(transferObj.getResponse());			
		}	
		
		//7.设置响应消息
		transferObj.setRespnseString(retMsg);		
				
		//8.异步保存流水信息
		long startTime6 = new Date().getTime();
		this.saveRecogFlowInfo(transferObj, recogService);
		long endTime6 = new Date().getTime();
		transferObj.addHandleTimes("启动保存流水耗时："+(endTime6-startTime6)+"毫秒");
		
		long endTime = new Date().getTime();
		transferObj.addHandleTimes("总耗时："+(endTime-startTime)+"毫秒");		
		//输出业务处理响应时间
		logger.info(transferObj.getHandleTimes());
		
		return retMsg;
	}
	
	/**
	 * 请求报文解密
	 * @param requestData
	 * @return
	 * @throws ServiceException
	 */
	private String decrypt(String requestData) throws ServiceException {
		//解析后的数据
		String decryptData = requestData;
		//是否需要解密
		if (Constants.PARAM_ISENCRYPT.equals(Constants.Config.IBIS_REQUEST_ISENCRYPT)) {
			try {
				requestData = DesUtil.decrypt(requestData, Constants.Config.IBIS_REQUEST_KEY);
			} catch (Exception e) {
				logger.error("报文解密发生异常，原因：" + e.getMessage(), e);
				throw new ServiceException("生物识别认证请求数据解密异常");
			}
		}
		
		return decryptData;
	}
	
	/**
	 * 保存识别认证流水信息
	 * @param transferObj
	 * @param recogService
	 */
	private void saveRecogFlowInfo(TransferObj transferObj,RecogService recogService) {		
		RecogFlowThread RecogFlowThread = new RecogFlowThread(recogService,transferObj);
		this.threadPoolService.addTask(RecogFlowThread);		
	}
	
}
