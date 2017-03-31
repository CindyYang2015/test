package com.cloudwalk.ibis.controller.featurelib;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.DateUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.file.FileType;
import com.cloudwalk.common.util.file.FileTypeJudge;
import com.cloudwalk.ibis.model.featurelib.Person;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.featurelib.PersonImportFlow;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.featurelib.PersonImportFlowService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
*
* ClassName: PersonImportFlowController <br/>
* Description: 客户信息. <br/>
* date: 2016年9月27日 下午2:13:16 <br/>
*
* @author 方凯
* @version
* @since JDK 1.7
*/
@Controller
@RequestMapping("/batchImport")
public class PersonImportFlowController extends BaseController {
	@Resource(name = "personImportFlowService")
	private PersonImportFlowService personImportFlowService;
	
	//日志操作
	@Resource(name = "logOperService")
	private LogOperService logOperService;
	
	/**
	 *
	 * selectImportFlowByPage:查询导入记录流水. <br/>
	 *
	 * @author:方凯 Date: 2015年8月21日 下午2:37:20
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectImportFlowByPage")
	public void selectImportFlowByPage(HttpServletRequest request,
			HttpServletResponse response, PersonImportFlow personImportFlow, PageInfo pageInfo, String beginDateStr,
			String endDateStr) {
		this.setPageInfo(request, pageInfo);
		JsonListResult<PersonImportFlow> result = new JsonListResult<PersonImportFlow>();
		String retJson = "";
		// 设置分页查询
		JSONObject map = new JSONObject();
		
		long oneDay = 1 * 24 * 60 * 60 * 1000;
		if (StringUtils.hasText(beginDateStr)) {
			personImportFlow.setBeginTime(DateUtil.valueOf(beginDateStr));
		}
		
		if (StringUtils.hasText(endDateStr)) {
			Timestamp b = DateUtil.valueOf(endDateStr);
			b = new Timestamp(b.getTime() + oneDay);
			personImportFlow.setEndTime(b);
		}
		
		try {
			User user=BaseUtil.getCurrentUser(request);
			personImportFlow.setCreator(user.getUserId());
			// 设置分页信息
			map.put("obj", personImportFlow);
			map.put("page", pageInfo);
			List<PersonImportFlow> list = this.personImportFlowService.selectAllByPage(map);
	
			// 设置返回结果
			result.setRows(list);
			result.setTotal(Long.valueOf(Long.valueOf(pageInfo.getTotalCount())));
			retJson = JsonUtil.toJSON(result);

		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		
		getPrintWriter(response, retJson);

	}
	
	/**
	 * 批量导入
	 * @param request
	 * @param response
	 * @param person
	 * @param personFeature
	 * @return
	 */
	@RequestMapping("/saveBatImport")
	public void saveBatImport(HttpServletRequest request,
			HttpServletResponse response, Person person, PersonFeature personFeature) {
		
		JSONObject result = new JSONObject();
		personFeature.setEngineCode(request.getParameter("engineId_batchImport"));
		personFeature.setEngineVerCode(request.getParameter("engineverId_batchImport"));
		personFeature.setEngineType(request.getParameter("engineType_batchImport"));
		try {			
			//判断 request 是否有文件上传,即多部分请求
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					//得到压缩包的名字					 
					if (file != null) {
						FileType type=FileTypeJudge.getType(file.getBytes());
						if(FileType.ZIP==type){
							// 处理上传文件
							JSONObject json = this.personImportFlowService.batchImport(file.getInputStream(),request, person,personFeature,type);
							if(json.containsKey("msg")){
								result.put("code", "0");
								result.put("message", json.getString("msg"));
							}
						}else{
							result.put("code", "1");
							result.put("message", "导入失败，只能上传zip格式的压缩文件!");
							logOperService.insertLogOper(request, 1, "批量导入失败,上传文件格式错误!");
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("批量导入异常："+e.getLocalizedMessage());
			result.put("code", "1");
			result.put("message", e.getLocalizedMessage());
			logOperService.insertLogOper(request, 1, "批量导入异常:"+e.getLocalizedMessage());
		}
		
		getPrintWriter(response, result.toJSONString());
		 
	}
}
