package com.cloudwalk.ibis.controller;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.file.FileUtils;
import com.cloudwalk.ibis.service.base.recog.RecogManageService;

/**
 * 识别认证接口服务
 * @author zhuyf
 *
 */
@Controller
@RequestMapping("/recog")
public class RecogController extends BaseController{

	@Resource(name="recogManageService")
	private RecogManageService recogManageService;
	
	/**
	 * 人生物识别处理
	 * @param requestData 请求数据
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/handle")
	public void handle(String requestData, HttpServletRequest request,
			HttpServletResponse response) {
		getPrintWriter(response, this.recogManageService.handleRequest(requestData));
	}
	
	/**
	 * 根据文件相对路径获取文件信息
	 * @param requestData
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getFile")
	public void getFile(String fileUrl, HttpServletRequest request,
			HttpServletResponse response) {
		byte[] binaryData = null;
		//文件存储方式
		String fileStoreway = Constants.Config.FILE_STORAGE_WAY;
		try {
			if(EnumClass.FileAccessEnum.LOCAL.getValue().equals(fileStoreway)) {
				//本地获取时，路径为全路径
				binaryData = FileUtils.getFileDataByPath(Constants.Config.IBIS_FILE_PATH+fileUrl);
			} else if(EnumClass.FileAccessEnum.HTTP.getValue().equals(fileStoreway)) {
				binaryData = FileUtils.getFileDataByHttp(fileUrl);
			} else if(EnumClass.FileAccessEnum.HTTPS.getValue().equals(fileStoreway)) {
				binaryData = FileUtils.getFileDataByHttps(fileUrl);
			}
			//返回文件流
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(binaryData);
			outputStream.flush();
			
		} catch(Exception e) {
			log.info(e.getLocalizedMessage());
		}
		
	}
}
