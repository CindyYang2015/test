package com.cloudwalk.ibis.service.base.recog;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.common.InterfaceConst;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.XmlUtil;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.result.ver001.Response;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.model.result.ver001.SearchData;

/**
 * 数据解析服务
 * @author zhuyf
 *
 */
public class DataParse {
	
	protected static Logger logger = Logger.getLogger(DataParse.class);

	/**
	 * 解析数据成识别认证请求对象
	 * @return
	 * @throws ServiceException 
	 */
	public RecogRequest parseDataBean(String requestData) throws ServiceException {
		RecogRequest recogRequest = null;
		if(StringUtils.isBlank(requestData)) return recogRequest;
		//解析格式
		String dataType = Constants.Config.DATA_TYPE;
		if(EnumClass.DataTypeEnum.XML.getValue().equals(dataType)) {
			recogRequest = (RecogRequest)XmlUtil.toBean(requestData, RecogRequest.class);
		} else if(EnumClass.DataTypeEnum.JSON.getValue().equals(dataType)){
			recogRequest = JsonUtil.parse(requestData, RecogRequest.class);
		} else if(EnumClass.DataTypeEnum.OTHER.getValue().equals(dataType)){
			//当本系统报文格式完全不符合行方要求时，需要重新实现数据的解析
			recogRequest = null;
		} else {
			throw new ServiceException(dataType+"解析格式不存在");
		}
		return recogRequest;
	}
	
	/**
	 * 将识别认证结果解析成字符串
	 * @param result
	 * @return
	 * @throws ServiceException 
	 */
	public String parseDataStr(Response result) {
		String retStr = "";
		//解析格式
		String dataType = Constants.Config.DATA_TYPE;
		try{
			if(result == null) {
				result = Response.getFailResponse(new Result(InterfaceConst.NULL_EXCE,"返回结果为NULL"));
			}			
			if(EnumClass.DataTypeEnum.XML.getValue().equals(dataType)) {
				retStr = XmlUtil.toXml(result);
			} else if(EnumClass.DataTypeEnum.JSON.getValue().equals(dataType)){
				retStr = JsonUtil.toJSON(result);
			} else if(EnumClass.DataTypeEnum.OTHER.getValue().equals(dataType)){
				//当本系统报文格式完全不符合行方要求时，需要重新实现数据的解析
				retStr = null;
			} 
		} catch(Exception e) {
			logger.info("识别认证结果解析出错："+e.getLocalizedMessage());
			if(EnumClass.DataTypeEnum.XML.getValue().equals(dataType)) {
				return InterfaceConst.PARSE_XML_EXCEPTION;
			} else if(EnumClass.DataTypeEnum.JSON.getValue().equals(dataType)){
				return InterfaceConst.PARSE_JSON_EXCEPTION;
			}			
		}
		return retStr;
	}
	
	/**
	 * 将请求报文解析成字符串
	 * @param request
	 * @return
	 */
	public String parseDataStr(RecogRequest request) {
		String retStr = "";
		//解析格式
		String dataType = Constants.Config.DATA_TYPE;
		try{
			if(request == null) {
				return retStr;
			}			
			if(EnumClass.DataTypeEnum.XML.getValue().equals(dataType)) {
				retStr = XmlUtil.toXml(request);
			} else if(EnumClass.DataTypeEnum.JSON.getValue().equals(dataType)){
				retStr = JsonUtil.toJSON(request);
			} else if(EnumClass.DataTypeEnum.OTHER.getValue().equals(dataType)){
				//当本系统报文格式完全不符合行方要求时，需要重新实现数据的解析
				retStr = null;
			} 
		} catch(Exception e) {
			logger.info("识别认证请求解析出错："+e.getLocalizedMessage());					
		}
		return retStr;
	}
	
	/**
	 * 将当前系统的数据格式的字符串转成json格式
	 * @param parseStr 被解析的字符串
	 * @param classz 中间转换的java对象
	 * @return
	 * @throws ServiceException 
	 */
	public String toJson(String parseStr,Class<?> classz) throws ServiceException {
		String result = "";
		Object obj = null;
		if(StringUtils.isBlank(parseStr) || classz == null) return result;
		//当前系统的解析格式
		String dataType = Constants.Config.DATA_TYPE;
		if(EnumClass.DataTypeEnum.XML.getValue().equals(dataType)) {
			parseStr = parseStr.replace("<data>", "<data class=\""+SearchData.class.getCanonicalName()+"\">");			
			obj = XmlUtil.toBean(parseStr, classz);
			result = JsonUtil.toJSON(obj);
		} else if(EnumClass.DataTypeEnum.JSON.getValue().equals(dataType)){
			result = parseStr;
		} else if(EnumClass.DataTypeEnum.OTHER.getValue().equals(dataType)){
			//当本系统报文格式完全不符合行方要求时，需要重新实现数据的解析			
		}
		return result;
	}
	

	
	
	public static void main(String[] args) {
		System.out.println(SearchData.class.getCanonicalName());
	}
		
	
	
}
