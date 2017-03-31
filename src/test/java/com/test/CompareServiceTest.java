package com.test;

import java.util.Date;
import java.util.Map;

import com.cloudwalk.common.util.ImgUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.SocketUtil;
import com.cloudwalk.common.util.XmlUtil;
import com.cloudwalk.ibis.model.RecogRequest;
import com.google.common.collect.Maps;

/**
 * 比对测试
 * @author zhuyf
 *
 */
public class CompareServiceTest {

	public static void main(String[] args) throws Exception {
		
		//1http 2socket
		int type = 2;
		//1json 2xml
		int datatype = 1;
				
		//设置请求报文
		RecogRequest request = new RecogRequest();
		request.setBuscode("compare");
		request.setChannel("0300");
		request.setCtfname("刘得花");
		request.setCtfno("5002211988812346");
		request.setCtftype("0200");
		request.setCustomerId("123456789");
		request.setEngineCode("cyface");
		request.setOrgCode("0000");
		request.setProperty("0400");
//		request.setRegFilePath("D:/img/1.jpg");
		request.setFileDataone(ImgUtil.getBase64Img("D:/img/1.jpg"));
		request.setFileDatatwo(ImgUtil.getBase64Img("D:/img/1.jpg"));
		request.setVerCode("ver001");
		request.setTradingCode("0600");
		request.setTradingFlowNO("1000002");
		String requestString = "";
		
		if(datatype == 1) {
			requestString = JsonUtil.toJSON(request);
		} else if(datatype == 2) {
			//转出xml
			String xml_head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			requestString = xml_head+XmlUtil.toXml(request);
		}
		
		for(int i=0;i<100;i++) {
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
		
	}
	
	public static String sendHttp(String requestString) {
		HttpConnectImpl httpConnectImpl = new HttpConnectImpl();
		Map<String,String> param = Maps.newHashMap();
		param.put("requestData", requestString);
		String retxml = httpConnectImpl.sendPost("http://localhost:8080/ibis/recog/handle",param);
		return retxml;
	}
	
	public static String sendSocket(String requestData) {
//		return "";
		int length = requestData.getBytes().length; // 报文长度
		String reportHead =  String.format("%08d", length);	
		return SocketUtil.sendMsg("192.168.10.56", 8082, 0, reportHead+requestData, 8);
	}
}
