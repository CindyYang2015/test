package com.cloudwalk.ibis.controller.recogSet;

import java.io.IOException;
import java.util.Date;
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
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonEntityResult;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.common.JsonResult;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.Engine;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.service.base.CacheService;
import com.cloudwalk.ibis.service.recogSet.EngineService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
 * 算法引擎控制器
 * @author 白乐
 *
 */
@Controller
@RequestMapping("/engine")
public class EngineController extends BaseController {
	
	@Resource(name = "engineService")
	private EngineService engineService;
	
	@Resource(name = "logOperService")
	private LogOperService logOperService;	
	
	@Resource(name = "cacheService")
	private CacheService cacheService; 
	/**
	 * 列表页面跳转
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return "platform/recogSet/engineList";
	}
	
	
	/**
	 * 添加页面跳转
	 */
	@RequestMapping("add")
	public String add(HttpServletRequest request,
			HttpServletResponse response, Model model,Engine record) throws IOException {
		//为了不 让html页面报错添加下面两行代码
		model.addAttribute("action", "create");
		model.addAttribute("engine", record);
		return "platform/recogSet/engineEdit";
	}	

	/**
	 * 保存算法引擎
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request,
			HttpServletResponse response, Engine record)  {
		
		String retJson = "";
		try {		
			JsonEntityResult<Engine> result = new JsonEntityResult<Engine>();
			int num = engineService.checkEngineCode(record.getEngineCode());
			String action = request.getParameter("action");
			if(""==record.getEngineType()||"".equals(record.getEngineType())){
				retJson = ControllerUtil.getFailRetJson("请选择引擎类型");
				getPrintWriter(response, retJson);
				return;
			}
			if ("create".equals(action)) {
				if (num >= 1) {				
					retJson = ControllerUtil.getFailRetJson("算法引擎代码重复!");
					getPrintWriter(response, retJson);
					return;
				}
				record.setCreator(BaseUtil.getCurrentUser(request).getUserId());// 创建用户
				record.setCreateTime(new Date());// 创建时间
				engineService.saveEngine(record);
			} else if ("edit".equals(action)) {
				record.setUpdator(BaseUtil.getCurrentUser(request).getUserId());// 更新用户
				record.setUpdateTime(new Date());// 更新时间
				engineService.updateEngine(record);
			}

			// 添加操作日志
			String remark = "保存算法引擎成功，详情：" + record.toString();
			logOperService.insertLogOper(request, 1, remark);
			
			retJson = JsonUtil.toJSON(result);
			
		} catch (Exception e) {
			//添加操作日志
			String remark ="保存算法引擎失败，详情：" + e.getMessage();
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
			HttpServletResponse response, Engine record,PageInfo pageInfo) throws IOException {
		this.setPageInfo(request, pageInfo);
		
		JsonListResult<Engine> result = new JsonListResult<Engine>();
		String retJson = "";
		
		try {
			// 设置分页查询
			Map<String, Object> map = new HashMap<String, Object>();
			// 设置分页信息
			map.put("obj", record);
			map.put("page", pageInfo);
			List<Engine> list = engineService.selectEnginePage(map);
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
	
	
	/**
	 * 算法引擎修改跳转页
	 */
	@RequestMapping("edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response,Model model, Engine record) throws IOException {
		model.addAttribute("action", "edit");
		if(null!=record && !record.getId().equals("")){
			Engine result = engineService.selectEngineById(record.getId());
			model.addAttribute("engine", result);
		}
		return "platform/recogSet/engineUpdate";
		
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
			int qint = engineService.selectEngineHaveStep(id);
			if(qint>0){			
				retJson = ControllerUtil.getFailRetJson("此引擎与策略有关联，不能够删除!");
				getPrintWriter(response, retJson);
				return;
			}
			
			engineService.deleteEngine(id);
			//添加操作日志
			String remark ="删除算法引擎成功，详情：" + id;
			logOperService.insertLogOper(request, 1, remark);
			
			retJson = JsonUtil.toJSON(result);
			
		} catch (Exception e) {
			//添加操作日志
			String remark = "删除";
			remark = remark + "算法引擎失败，详情为：" + e.toString();
			logOperService.insertLogOper(request, 0, remark);
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);

	}
	
	/**
	 * 算法引擎类型字典
	 * @param request
	 * @param response
	 * @param record
	 * @throws IOException
	 */
	@RequestMapping("selectEngineType")
	public void selectEngineType(HttpServletRequest request,
			HttpServletResponse response, Engine record) throws IOException {
		String jsonResult = null;
		try {
			List<DicValues> list = cacheService.getDicValuesByType(EnumClass.DicTypeEnum.ENGINE_TYPE.getValue() );
			if(null!=list){
				jsonResult = JSON.toJSONString(list);
			}
		    if(list.size()>0){
//		      String nullStr = "{\"dicCode\":\"\",\"dicType\":\"\",\"enabledFlag\":1,\"meaning\":\"--请选择--\"},";//选项前加入一个空行
		      String subStr = jsonResult.substring(jsonResult.indexOf("[")+"[".length(), jsonResult.length());
		      jsonResult = "["+subStr;
		    }
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
	    getPrintWriter(response, jsonResult);

	}
	
	
	
	
}
