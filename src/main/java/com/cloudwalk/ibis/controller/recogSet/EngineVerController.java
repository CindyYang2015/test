package com.cloudwalk.ibis.controller.recogSet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonEntityResult;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.common.JsonResult;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.Engine;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.service.recogSet.EngineVerService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
 * 算法引擎版本
 * @author 白乐
 *
 */
@Controller
@RequestMapping("/engineVer")
public class EngineVerController extends BaseController {
	
	@Resource(name = "engineVerService")
	private EngineVerService engineVerService;
		
	@Resource(name = "logOperService")
	private LogOperService logOperService;	
	
	
	
	
	/**
	 * 列表页面跳转
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return "platform/recogSet/engineVerList";
	}
	
	/**
	 * 添加页面跳转
	 */
	@RequestMapping("add")
	public String add(HttpServletRequest request,
			HttpServletResponse response, Model model,EngineVer record) throws IOException {
		//为了不 让html页面报错添加下面两行代码
		model.addAttribute("action", "create");
		model.addAttribute("engine", record);
		return "platform/recogSet/engineVerEdit";
	}	
	
	/**
	 * 获得算法引擎
	 * @param request
	 * @param response
	 * @param record
	 * @throws IOException
	 */
	@RequestMapping("selectEngineCode")
	public void selectEngineCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String jsonResult = null;
		try {
			List<Engine> list = engineVerService.selectEngineAll();
			
			if(null!=list){
				jsonResult = JSON.toJSONString(list);
			}
			
		    if(list.size()>0){
		      String subStr = jsonResult.substring(jsonResult.indexOf("[")+"[".length(), jsonResult.length());
		      jsonResult = "["+subStr;		      
		    }
		    
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, jsonResult);
	}
	
	
	/**
	 * 算法引擎版本修改跳转页
	 */
	@RequestMapping("edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response,Model model, EngineVer record) throws IOException {
		model.addAttribute("action", "edit");
		if(null!=record && !record.getId().equals("")){
			EngineVer result = engineVerService.selectEngineVerById(record.getId());
			model.addAttribute("engineVer", result);
		}
		return "platform/recogSet/engineVerUpdate";
		
	}		
	
	
	/**
	 * 保存算法引擎
	 */
	@RequestMapping("delete")
	public void delete(HttpServletRequest request,
			HttpServletResponse response, String id)  {
		JsonResult result = new JsonResult();
		String retJson = "";
		
		try {
			if (ObjectUtils.isEmpty(id)) {		
				retJson = ControllerUtil.getFailRetJson("请至少选中一行记录");
				getPrintWriter(response, retJson);
				return;
			}
			
			int qint = engineVerService.selectEngineVerHaveStep(id);
			if(qint>0){			
				retJson = ControllerUtil.getFailRetJson("此引擎版本与策略有关联，不能够删除!");
				getPrintWriter(response, retJson);
				return;
			}			
			
			engineVerService.deleteEngineVer(id);
			//添加操作日志
			String remark ="删除算法引擎版本成功，详情：" + id;
			logOperService.insertLogOper(request, 1, remark);
			
			retJson = JSONArray.toJSONString(result);
			
		} catch (Exception e) {
			//添加操作日志
			String remark = "删除";
			remark = remark + "算法引擎版本失败，详情为：" + e.toString();
			logOperService.insertLogOper(request, 0, remark);
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);

	}	
	
	
	/**
	 * 保存算法引擎版本
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request,
			HttpServletResponse response, EngineVer record)  {
		
		String retJson = "";
		JsonEntityResult<Engine> result = new JsonEntityResult<Engine>();
		
		String action = request.getParameter("action");
		//判断是否已存在：算法引擎+版本号
		Map<String,String> map=new HashMap<String,String>();
		map.put("engineCode", record.getEngineCode());
		map.put("verCode", record.getVerCode());
		map.put("verNo", record.getVerNo());
		try {
			List<EngineVer> list=engineVerService.selectByEngineVer(map);
			if(list.size()>0){
				retJson = ControllerUtil.getFailRetJson("引擎版本已经存在，请重新输入");
				getPrintWriter(response, retJson);
				return;
			} 
			//必须选择算法引擎
			if(""==record.getEngineCode()||"".equals(record.getEngineCode())){
				retJson = ControllerUtil.getFailRetJson("请选择算法引擎");
				getPrintWriter(response, retJson);
				return;
			}
			if ("create".equals(action)) {
				engineVerService.saveEngineVer(record);
			}else if("edit".equals(action)){
				engineVerService.updateEngineVer(record);
			}
			
			//添加操作日志
			String remark ="保存算法引擎版本成功，详情：" + record.toString();
			logOperService.insertLogOper(request, 1, remark);
			
			retJson = JsonUtil.toJSON(result);
			
		} catch (Exception e) {
			//添加操作日志
			String remark ="保存算法引擎版本失败，详情：" + e.getMessage();
			logOperService.insertLogOper(request, 0, remark);
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);

	}	
	
	/**
	 * 算法引擎列表
	 */
	@RequestMapping("list")
	public void list(HttpServletRequest request,
			HttpServletResponse response, EngineVer record,PageInfo pageInfo) throws IOException {
		this.setPageInfo(request, pageInfo);
		
		JsonListResult<EngineVer> result = new JsonListResult<EngineVer>();
		String retJson = "";
		// 设置分页查询
		Map<String, Object> map = new HashMap<String, Object>();
		// 设置分页信息
		map.put("obj", record);
		map.put("page", pageInfo);
		try {
			List<EngineVer> list = engineVerService.selectEngineVerPage(map);
			// 设置返回结果
			result.setRows(list);
			result.setTotal(Long.valueOf(pageInfo.getTotalCount()));
			retJson = JsonUtil.toJSON(result);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);

	}		
	
	
	
}
