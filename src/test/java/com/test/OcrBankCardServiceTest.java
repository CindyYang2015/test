package com.test;

import java.util.Date;
import java.util.Map;

import com.cloudwalk.common.util.ImgUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.XmlUtil;
import com.cloudwalk.ibis.model.RecogRequest;
import com.google.common.collect.Maps;

/**
 * 银行卡识别测试
 * @author zhuyf
 *
 */
public class OcrBankCardServiceTest {

	public static void main(String[] args) throws Exception {
		
		//1http 2socket
		int type = 1;
		//1json 2xml
		int datatype = 1;
				
		//设置请求报文
		RecogRequest request = new RecogRequest();
		request.setBuscode("ocrBankCard");
		request.setChannel("0300");
		request.setEngineCode("cyface");
		request.setOrgCode("0000");
		request.setBankCardImg(ImgUtil.getBase64Img("C:/Users/zhuyf/Pictures/img01/500115197605017877_刘得花_1001.jpg"));
		request.setVerCode("ver001");
		request.setTradingCode("0600");
		request.setTradingFlowNO("1000002111122222");
		String requestString = "";
		
		if(datatype == 1) {
			requestString = JsonUtil.toJSON(request);
		} else if(datatype == 2) {
			//转出xml
			String xml_head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			requestString = xml_head+XmlUtil.toXml(request);
		}
		
		String retData = "";
		long starttime = new Date().getTime();
		if(type == 1) {
			//通过http发送报文
			retData = sendHttp(requestString);
		} else {
			//通过socket发送报文
			retData = sendSocket(requestString);
		}
		long endtime = new Date().getTime();
		System.out.println("响应时间:"+(endtime-starttime));
		System.out.println(retData);
		
	}
	
	public static String sendHttp(String requestString) {
		HttpConnectImpl httpConnectImpl = new HttpConnectImpl();
		Map<String,String> param = Maps.newHashMap();
		param.put("requestData", requestString);
		String retxml = httpConnectImpl.sendPost("http://localhost:8080/ibis/recog/handle",param);
		return retxml;
	}
	
	public static String sendSocket(String requestXml) {
		return "";
//		requestXml = ObjectUtils.getLength(requestXml.getBytes().length, "7")+requestXml;	
//		String retxml = SocketUtil.sendMsg("127.0.0.1", 9000, 75000, requestXml,7);
//		return retxml;
	}
}
