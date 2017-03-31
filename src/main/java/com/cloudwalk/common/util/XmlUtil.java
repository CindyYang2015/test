package com.cloudwalk.common.util;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.cloudwalk.common.exception.ServiceException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * xml解析工具类
 * @author zhuyf
 *
 */
public class XmlUtil {

	protected static Logger logger = Logger.getLogger(XmlUtil.class);
	
	/**
	 * 将对象转成xml
	 * @param obj
	 * @return
	 * @throws ServiceException 
	 */
	public static String toXml(Object obj) throws ServiceException {
		String retXml = null;
		if(obj == null) return retXml;
		try{
			XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder(
					"_-", "_")));  
	        xStream.autodetectAnnotations(true); 	
	        xStream.aliasSystemAttribute(null, "class");
	        retXml = xStream.toXML(obj);  
	        
		} catch(Exception e) {
			logger.info(e.getLocalizedMessage());
			throw new ServiceException("java对象转xml异常");
		}
        return retXml;
	}
	
	/**
	 * 将xml转成对应的java对象
	 * @param xml
	 * @return
	 * @throws ServiceException 
	 */
	public static Object toBean(String xml,Class<?> classz) throws ServiceException {
		try{
		XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder(
				"_-", "_")));  
        xStream.autodetectAnnotations(true); 
        //检查对象别名
        XStreamAlias className = classz.getAnnotation(XStreamAlias.class);
		if(className != null) {
			xStream.alias(className.value(), classz);
		}       
        return xStream.fromXML(xml);  
        
		} catch(Exception e) {
			logger.info(e.getLocalizedMessage());
			throw new ServiceException("xml转java对象异常");
		}
	}
	
	/**
	 * 将xml流转成对应的java对象
	 * @param xml
	 * @return
	 */
	public static Object toBean(InputStream input) {
		XStream xStream = new XStream();  
        xStream.autodetectAnnotations(true); 
        return xStream.fromXML(input);
	}	
	
	public static void main(String[] args) {
		/*RecogRequest recogRequest = new RecogRequest();
		recogRequest.setBankcardNo("5002224546467");
		recogRequest.setOrgCode("10320350353");
		recogRequest.setCustomerId("sssss");
		String xmlStr = "";
		try {
			xmlStr = toXml(recogRequest);
			System.out.println(xmlStr);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		String xml = "<request><orgCode>10320350353</orgCode><cusID>sssss</cusID><bankcardNo>5002224546467</bankcardNo><netCheckStatus>0</netCheckStatus><netCheckImgType>0</netCheckImgType><isencrypt>0</isencrypt></request>";
		try {
			RecogRequest recogRequest1 = (RecogRequest) toBean(xml,RecogRequest.class);
			System.out.println(recogRequest1);
		} catch (ServiceException e) {
			e.printStackTrace();
		}*/

		
	}
	
}
