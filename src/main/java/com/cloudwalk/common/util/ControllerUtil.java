package com.cloudwalk.common.util;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cloudwalk.common.common.BaseConstants;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.common.JsonEntityResult;
import com.cloudwalk.common.common.JsonResult;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.file.FileType;
import com.cloudwalk.common.util.file.FileTypeJudge;
import com.google.common.collect.Maps;

/**
 * controller层工具类
 * @author zhuyf
 *
 */
public class ControllerUtil {	
	
	protected final static Logger log = Logger.getLogger(ControllerUtil.class);
	
	/**
	 * 上传图片返回图片的base64字符串
	 * @param request
	 * @param response
	 * @return
	 */
	public static JsonResult uploadImage(HttpServletRequest request, HttpServletResponse response)  {
		JsonResult resultEntity = new JsonResult();
		try {
			// 判断 request 是否有文件上传,即多部分请求
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						//判断文件大小
						if(file.getSize() > (Constants.IMAGE_SIZE*1024)) {
							resultEntity.setSuccess(false);
				        	resultEntity.setMessage("图片大小不能超过"+Constants.IMAGE_SIZE+"KB");
						} else {
							//判断图片类型
							FileType fileType=FileTypeJudge.getType(file.getBytes());
					        if(FileType.PNG!=fileType&&FileType.JPG!=fileType){
					        	resultEntity.setSuccess(false);
					        	resultEntity.setMessage("图片类型必须是.png,.jpeg,.jpg中的一种！");
					        } else {
					        	resultEntity.setSuccess(true);
					        	Map<Object,Object> imageMap = Maps.newHashMap();
					        	imageMap.put("image", Base64Utils.encodeToString(file.getBytes()));
					        	resultEntity.setData(imageMap);
					        }
						}												
					}
				}
			}
			 
		}catch (Exception e) {			 
			log.error(e);
			resultEntity.setSuccess(false);
        	resultEntity.setMessage("图片类型必须是.png,.jpeg,.jpg中的一种！");
			 
		} 		
		return resultEntity;
	}

	/**
	 * 根据请求文件名从流中获取文件信息
	 * @param fileNames 文件名列表
	 * @return
	 */
	public static Map<String,byte[]> getRequestFileData(HttpServletRequest request,String[] fileNames) {
		//文件map
		Map<String,byte[]> fileMap = Maps.newHashMap();
		if(fileNames == null || fileNames.length < 1) return fileMap;
		//从流中获取文件信息
		try {			
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				for(String fileName:fileNames) {
					MultipartFile file = multiRequest.getFile(fileName);
					fileMap.put(fileName, file.getBytes());
				}			
			}
		} catch (Exception e) {
			e.fillInStackTrace();
		} 
		return fileMap;
	}
	
	/**
	 * 根据请求文件名从流中获取base64文件信息
	 * @param fileNames 文件名列表
	 * @return
	 */
	public static Map<String,String> getRequestBase64FileData(HttpServletRequest request,String[] fileNames) {
		//文件map
		Map<String,String> fileMap = Maps.newHashMap();
		if(fileNames == null || fileNames.length < 1) return fileMap;
		//从流中获取文件信息
		try {			
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				for(String fileName:fileNames) {
					MultipartFile file = multiRequest.getFile(fileName);
					byte[] fileData = file.getBytes();
					if(fileData != null&&fileData.length>0) {
						fileMap.put(fileName, Base64.encodeBase64String(file.getBytes()));
					}					
				}			
			}
		} catch (Exception e) {
			e.fillInStackTrace();
		} 
		return fileMap;
	}
	
	/**
	 * 从request中获取对应的图片的二进制数据
	 * @param fileNames 文件名列表
	 * @return
	 */
	public static Map<String,byte[]> getRequestByteData(HttpServletRequest request,String[] fileNames) {
		//文件map
		Map<String,byte[]> fileMap = Maps.newHashMap();
		if(fileNames == null || fileNames.length < 1) return fileMap;
		//从流中获取图片base64信息，并转成字节数据
		for(String fileName:fileNames) {
			String base64FileData = request.getParameter(fileName);			
			if(!StringUtils.isBlank(base64FileData)) {
				fileMap.put(fileName, Base64.decodeBase64(base64FileData));
			}			
		}
		return fileMap;
	}
	
	/**
	 * 从request中获取对应的图片的base64数据
	 * @param fileNames 文件名列表
	 * @return
	 */
	public static Map<String,String> getRequestBase64Data(HttpServletRequest request,String[] fileNames) {
		//文件map
		Map<String,String> fileMap = Maps.newHashMap();
		if(fileNames == null || fileNames.length < 1) return fileMap;
		//从流中获取图片base64信息
		for(String fileName:fileNames) {
			String base64FileData = request.getParameter(fileName);
			if(!StringUtils.isBlank(base64FileData)) {
				fileMap.put(fileName, base64FileData);
			}			
		}
		return fileMap;
	}
	
	/**
	 * 获取异常的返回json消息
	 * @param e
	 * @return
	 */
	public static String getExceRetJson(Exception e) {
		String retJson = BaseConstants.SYSTEM_EXCEPTION;
		if(e == null) return retJson;		
		if(e instanceof ServiceException) {
			ServiceException se = (ServiceException)e;
			JsonResult jsonResult = new JsonResult();
			jsonResult.setSuccess(false);
			jsonResult.setMessage(se.getMessage());
			retJson = JsonUtil.toJSON(jsonResult);
		} 
		return retJson;
	}
	
	/**
	 * 获取失败的返回json消息
	 * @param e
	 * @return
	 */
	public static String getFailRetJson(String msg) {		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(false);
		jsonResult.setMessage(msg);
		String retJson = JsonUtil.toJSON(jsonResult);
		return retJson;
	}
	
	/**
	 * 获取失败的返回json消息
	 * @param code 响应编码
	 * @param msg 响应消息
	 * @return
	 */
	public static String getFailRetJson(String code,String msg) {		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(false);
		jsonResult.setMessage(msg);
		String retJson = JsonUtil.toJSON(jsonResult);
		return retJson;
	}
	
	/**
	 * 获取成功的返回json消息
	 * @param e
	 * @return
	 */
	public static String getSuccessRetJson(String msg) {		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		jsonResult.setMessage(msg);
		String retJson = JsonUtil.toJSON(jsonResult);
		return retJson;
	}
	
	/**
	 * 获取成功的返回json消息
	 * @param entity 实体对象
	 * @param msg 响应消息
	 * @return
	 */
	public static <T> String getSuccessRetJson(T entity,String msg) {		
		JsonEntityResult<T> jsonResult = new JsonEntityResult<T>();
		jsonResult.setSuccess(true);
		jsonResult.setMessage(msg);
		jsonResult.setEntity(entity);
		String retJson = JsonUtil.toJSON(jsonResult);
		return retJson;
	}
	
	/**
	 * 获取成功的返回json消息
	 * @param entity 实体对象
	 * @param code 成功码或者其他业务编码
	 * @param msg 响应消息
	 * @return
	 */
	public static <T> String getSuccessRetJson(T entity,String code,String msg) {		
		JsonEntityResult<T> jsonResult = new JsonEntityResult<T>();
		jsonResult.setSuccess(true);
		jsonResult.setCode(code);
		jsonResult.setMessage(msg);
		jsonResult.setEntity(entity);
		String retJson = JsonUtil.toJSON(jsonResult);
		return retJson;
	}
}
