package com.test;

import java.util.Date;

import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.XmlUtil;
import com.cloudwalk.ibis.model.RecogRequest;

/**
 * 特征注册测试
 * @author zhuyf
 *
 */
public class RegServiceTest {

	public static void main(String[] args) throws Exception {
		
		//1http 2socket
		int type = 1;
		//1json 2xml
		int datatype = 1;
				
		//设置请求报文
		RecogRequest request = new RecogRequest();
		request.setBuscode("reg");
		request.setChannel("0300");
		request.setCtfname("刘得花");
		request.setCtfno("5002211988812346");
		request.setCtftype("0200");
		request.setCustomerId("123456789");
		request.setEngineCode("cyface");
		request.setOrgCode("0000");
		request.setProperty("0400");
		request.setRegFilePath("D:/img/1.jpg");
		request.setVerCode("ver001");
		request.setTradingCode("0600");
		request.setTradingFlowNO(ObjectUtils.createUUID());
		String requestStr = "";
		
		if(datatype == 1) {
			requestStr = JsonUtil.toJSON(request);
		} else {
			//转出xml
			String xml_head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			requestStr = xml_head+XmlUtil.toXml(request);
		}
		
		String retData = "";
		long starttime = new Date().getTime();
		if(type == 1) {
			//通过http发送报文
			retData = sendHttp(requestStr);
		} else {
			//通过socket发送报文
			retData = sendSocket(requestStr);
		}
		long endtime = new Date().getTime();
		System.out.println("响应时间:"+(endtime-starttime));
		System.out.println(retData);
		
	}
	
	public static String sendHttp(String requestXml) {
		HttpConnectImpl httpConnectImpl = new HttpConnectImpl();
		String retxml = httpConnectImpl.sendPost("http://localhost:8080/ibis/recog/handle", "requestData="+requestXml);
		return retxml;
	}
	
	public static String sendSocket(String requestXml) {
		return "";
//		requestXml = ObjectUtils.getLength(requestXml.getBytes().length, "7")+requestXml;	
//		String retxml = SocketUtil.sendMsg("127.0.0.1", 9000, 75000, requestXml,7);
//		return retxml;
	}
}
