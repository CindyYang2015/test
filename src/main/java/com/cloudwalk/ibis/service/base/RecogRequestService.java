/**
 * Project Name:IBIS-platform
 * File Name:FaceRequestService.java
 * Package Name:com.cloudwalk.ibis.service.face
 * Date:2015年9月6日下午8:01:45
 * Copyright @ 2015-2015 重庆中科云丛科技有限公司  All Rights Reserved.
 *
 */

package com.cloudwalk.ibis.service.base;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.common.InterfaceConst;
import com.cloudwalk.common.util.HttpRequestProxy;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.SocketUtil;
import com.cloudwalk.ibis.webservice.RecogWebService;
import com.google.common.collect.Maps;

/**
 * ClassName:RecogRequestService <br/>
 * Description: 提供三种访问方式：webservice,socket,http. <br/>
 * Date: 2015年9月6日 下午8:01:45 <br/>
 *
 * @author zhuyf
 * @version
 * @since JDK 1.7
 * @see
 */
@Service("recogRequestService")
public class RecogRequestService {

	protected final Logger log = Logger.getLogger(this.getClass());
	@Resource(name = "recogWebServiceClient")
	RecogWebService recogWebService;
	

	/**
	 *
	 * invoke:通过三种方式调用人脸识别接口. <br/>
	 *
	 * @author:朱云飞 Date: 2015年9月6日 下午8:09:50
	 * @param requestWay
	 * @param requeString
	 * @return
	 * @since JDK 1.7
	 */
	public String invoke(int requestWay, String requeString) {
		String retString = null;
		if (requestWay == Constants.WEBSERVICE) {
			retString = this.sendRequestByWebsevice(requeString);
		} else if (requestWay == Constants.HTTP) {
			retString = this.sendRequestByHttp(requeString);
		} else if (requestWay == Constants.SOCKET) {
			retString = this.sendRequestBySocket(requeString);
		}	
		return retString;
	}
	
	/**
	 * 通过http方式访问数据
	 * @param requestData 数据
	 * @return
	 */
	public String sendRequestByHttp(String requestData){
		String reqURL = Constants.Config.IBIS_HTTP_REQURL;
		Map<String, String> paramMaps = Maps.newHashMap();
		paramMaps.put("requestData", requestData);
		return HttpRequestProxy.post(reqURL, paramMaps, InterfaceConst.ENCODING);
	}
	
	/**
	 * 通过socket发送报文请求
	 * @param requestData 请求数据
	 * @return
	 */
	public String sendRequestBySocket(String requestData){
		String retMsg = "";
		int serverPort = ObjectUtils.objToInt(Constants.Config.IBIS_SOCKET_PORT, -1);
		if(serverPort == -1) {
			log.error("端口没有配置");
			return retMsg;
		}
		int length = requestData.getBytes().length; // 报文长度
		String reportHead =  String.format("%08d", length);	
		return SocketUtil.sendMsg("", serverPort, 0, reportHead+requestData, 8);
	}
	
	/**
	 * 通过websevice处理消息
	 * @param requestData
	 * @return
	 */
	public String sendRequestByWebsevice(String requestData){
		return this.recogWebService.excute(requestData);
	}
}
