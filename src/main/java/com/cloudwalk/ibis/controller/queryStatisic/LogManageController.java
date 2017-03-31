package com.cloudwalk.ibis.controller.queryStatisic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.common.InterfaceConst;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.engine.face.recg.RecogniseUtils;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.HttpRequestProxy;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.ibis.model.queryStatisic.LogFile;
import com.google.common.collect.Maps;
/**
 * @author weifeng
 *
 */
@Controller
@RequestMapping("/logManage")
public class LogManageController extends BaseController{
	
	/**
	 * 拼接URL的路径
	 */
	private static final String PATH = "/ibis/logManage/";
	private static final String REXP_IP = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
	private static final String REXP_SORT = "\\d{0,5}$";


	/** 
	 * @Title: selectChannelFlowByPage 
	 * @Description: TODO
	 * @param @param request
	 * @param @param response
	 * @param @param channelFlow
	 * @param @param pageInfo   
	 * @return void  
	 * @throws 
	 * @author 梁玮峰
	 * 配置文件格式：ibis.log.list = 192.168.10.237:8080+/opt/applog/ibis;192.168.10.237:8087+/opt/applog/ibis;
	 */
	@RequestMapping(value = "/search")
	public void searchLog(HttpServletRequest request,
			HttpServletResponse response) {
		//初始化最终返回的对象
		JsonListResult<LogFile> result = new JsonListResult<LogFile>();		
		//初始化最终需要查询的ip+端口 
		String ipAndSort = new String();
		String retJson = new String();
		//输入参数为空时，默认全部查询，初始化ip+sort数组
		List<String> ipAndSorts = new ArrayList<String>() ;
		//获取用户输入的IP和端口
		String fileIP = request.getParameter("ip");
		String fileSort = request.getParameter("sort");		
		//获取配置文件中的服务器地址
		String baseUrl = Constants.Config.IBIS_LOG_LIST;
		try {
			//拼接IP+端口
			if (StringUtils.hasText(fileIP)&&StringUtils.hasText(fileSort)){
				//正则匹配IP和端口的格式
				if(fileIP.matches(REXP_IP)&&fileSort.matches(REXP_SORT)){
	
					ipAndSort = fileIP + ":" +fileSort;
	
					String[] urlList = baseUrl.split(";");
					//校验url是否在配置文件中存在
					boolean isUrlExist = false;
					if(urlList.length>0){
						for(String tempUrl : urlList){
							if(StringUtils.hasText(tempUrl)&&tempUrl.contains(ipAndSort)){
								//用户输入正确的ip+sort则将该记录存入 之前初始化的ip+sort数组
								ipAndSorts.add(tempUrl);
								isUrlExist = true;
								//找到了用户输入的ip+sort就终止循环
								break;
							};
						}
					}
					if(!isUrlExist){
					  result.setSuccess(false);
						result.setCode("5002");
						result.setMessage("IP或端口未配置，请核对后重新输入");
						retJson = JSONArray.toJSONString(result);
						getPrintWriter(response, retJson);
					}
				}else{
				  result.setSuccess(false);
					result.setCode("5002");
					result.setMessage("IP或端口输入有误，请核对后重新输入");
					retJson = JSONArray.toJSONString(result);
					getPrintWriter(response, retJson);
				}	
			}
			//如果用户输入为空，则默认全部查询
			else if(!StringUtils.hasText(fileIP) && !StringUtils.hasText(fileSort)){
				String[] urlList = baseUrl.split(";");
				if(urlList.length>0){
					for(String tempUrl : urlList){
						if(StringUtils.hasText(tempUrl)){
							ipAndSorts.add(tempUrl);
						};
					}
				}
	
			}
			//用户必须同时输入ip+sort 不能只输一个
			else{
			  result.setSuccess(false);
				result.setCode("5002");
				result.setMessage("IP或端口为空，请核对后重新输入");
				retJson = JSONArray.toJSONString(result);
				getPrintWriter(response, retJson);
			}
			List<LogFile> fileList = new ArrayList<LogFile>();
			for(String tempReq:ipAndSorts){
				//拼接http请求
				String reqURL = "http://"+tempReq.split(",")[0] + PATH
						+ "getFileList";
				Map<String, String> paramMaps = Maps.newHashMap();
				//将当前ip+sort+path封装 
				paramMaps.put("requestData", tempReq);
				//通过http请求获取服务器的文件名列表
				String ret = HttpRequestProxy.post(reqURL, paramMaps, InterfaceConst.ENCODING);
				@SuppressWarnings("unchecked")
				List<LogFile> logFiles =(List<LogFile>) JSONArray.parse(ret);
				if(null != logFiles){
					fileList.addAll(logFiles);
				}
			}
			// 设置返回结果
			result.setRows(fileList);
			result.setTotal(Long.valueOf(fileList.size()));
			retJson = JSONArray.toJSONString(result);

		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		
		getPrintWriter(response, retJson);
	}
	/** 
	 * 文件下载功能
	 * @Title: downloadFile 
	 * @Description: TODO
	 * @param @param request
	 * @param @param response   
	 * @return void  
	 * @throws 
	 * @author 梁玮峰
	 */
	@RequestMapping(value = "/download")
	public void downloadFile(HttpServletRequest request,
			HttpServletResponse response) {
		//获取前端参数
		String ip = request.getParameter("ip");
		String sort = request.getParameter("sort");
		//拼接http请求
		String reqURL = "http://" + ip + ":" + sort + PATH + "getLog";
		Map<String, String> paramMaps = Maps.newHashMap();
		try {
			//将path+文件名拼接，并封装成输入参数
			//解决日志文件名出现中文乱码
			String logName=URLDecoder.decode(request.getParameter("logName"),InterfaceConst.ENCODING);
			//通过http获取日志列表数据
			paramMaps.put("requestData",  request.getParameter("path")+"/"+logName);
			String ret = HttpRequestProxy.post(reqURL, paramMaps, InterfaceConst.ENCODING);
			@SuppressWarnings("unchecked")
			Map<String,String> logFileMap =(Map<String,String>) JSONArray.parse(ret);
			if(null != logFileMap){
				String fileString = logFileMap.get("fileStream");
				byte[] fileStream = RecogniseUtils.decodeBase64(fileString);
				if(null != fileStream){
					ServletOutputStream outputStream;				
			        //文件流输出，提供下载功能
			        response.setContentType("txt/log/out");
			        String fileName = new String( logName.getBytes(InterfaceConst.ENCODING), "ISO8859_1");
			        response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
					  
					outputStream = response.getOutputStream();
					outputStream.write(fileStream);
					outputStream.flush();
					outputStream.close();
				}
			}
		
		} catch (Exception e) {		
			log.error(e.getLocalizedMessage());
			JsonListResult<LogFile> result = new JsonListResult<LogFile>();
			result.setSuccess(false);
			result.setCode("5002");
			result.setMessage("文件获取异常");
			String retJson = JSONArray.toJSONString(result);
			getPrintWriter(response, retJson);
		}
	}

	/** 
	 * 获取当前ip下的文件目录
	 * @Title: getFileList 
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return String  
	 * @throws 
	 * @author 梁玮峰
	 */
	@RequestMapping(value = "/getFileList")
	public void getFileList(HttpServletRequest request,
			HttpServletResponse response){
		
		String retJson = "";
		try {
			String tempPath = request.getParameter("requestData");
			List<LogFile> fileList = handleRet(tempPath);
			retJson = JsonUtil.toJSON(fileList);			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		getPrintWriter(response, retJson);
	}


	/** 获取文件流，用于下载文件
	 * @Title: getLog 
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return String  
	 * @throws 
	 * @author 梁玮峰
	 */
	@RequestMapping(value = "/getLog")
	public void getLog(HttpServletRequest request,
			HttpServletResponse response){
		String filePath = request.getParameter("requestData");
		
		try {
			//读取文件
			File logFile = new File(filePath);
			long len = logFile.length();
			byte[] bytes = new byte[(int)len];
			
			BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(logFile));
			int r = bufferedInputStream.read( bytes );
			if (r != len){
				JsonListResult<LogFile> result = new JsonListResult<LogFile>();
				result.setCode("5002");
				result.setMessage("文件获取异常");
				String retJson = JSONArray.toJSONString(result);
				getPrintWriter(response, retJson);
			}

			bufferedInputStream.close();
			
			Map<String,String> resp = Maps.newHashMap();
			//字节流编码成base64 用于消息传递
			resp.put("fileStream",RecogniseUtils.encodeBase64String(bytes) );
			getPrintWriter(response, JSONArray.toJSONString(resp));			
			
		} catch (Exception e) {
			JsonListResult<LogFile> result = new JsonListResult<LogFile>();
			result.setSuccess(false);
			result.setCode("5002");
			result.setMessage("文件获取异常");
			String retJson = JSONArray.toJSONString(result);
			getPrintWriter(response, retJson);
		}

	}
	/** 
	 * 获取当前路径下的文件列表
	 * @Title: getFileName 
	 * @Description: TODO
	 * @param @param path
	 * @param @return   
	 * @return String[]  
	 * @throws 
	 * @author 梁玮峰
	 */
	private  String [] getFileName(String path)
	{
		File file = new File(path);
		String [] fileName = file.list();
		return fileName;
	}

	/** 
	 * 封装文件列表到logFile对象中
	 * 
	 * @Title: handleRet 
	 * @Description: TODO
	 * @param @param ret
	 * @param @param tempReq
	 * @param @return   
	 * @return List<LogFile>  
	 * @throws 
	 * @author 梁玮峰
	 */
	private List<LogFile> handleRet(String tempReq) {
		List<LogFile> logFiles = new ArrayList<LogFile>();
		String [] files = getFileName(tempReq.split(",")[1]);
		if(files != null && files.length>0){
			for(String fileName:files){
				LogFile logFile = new LogFile();
				logFile.setFileName(fileName);
				logFile.setIpAndSort(tempReq.split(",")[0]);
				//拆分IP+sort 用于前端展示
				logFile.setIp(logFile.getIpAndSort().split(":")[0]);
				logFile.setSort(logFile.getIpAndSort().split(":")[1]);
				logFile.setPath(tempReq.split(",")[1]);
				logFiles.add(logFile);
			}
		}

		return logFiles;
	}
}
