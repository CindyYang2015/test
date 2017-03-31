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

import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonEntityResult;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.ibis.model.recogSet.Engine;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.recogSet.Step;
import com.cloudwalk.ibis.model.recogSet.vo.StepVo;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.recogSet.EngineVerService;
import com.cloudwalk.ibis.service.recogSet.StepEngineRefService;
import com.cloudwalk.ibis.service.recogSet.StepService;
import com.cloudwalk.ibis.service.system.LogOperService;
/**
 * 策略控制器
 * @author 白乐
 *
 */
@Controller
@RequestMapping("/step")
public class StepController extends BaseController {
	
	@Resource(name = "stepService")
	private StepService stepService;
	@Resource(name = "engineVerService")
	private EngineVerService engineVerService;
	@Resource(name = "stepEngineRefService")
	private StepEngineRefService stepEngineRefService;
	@Resource(name = "logOperService")
	private LogOperService logOperService;	
	
	/**
	 * 列表页面跳转
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return "platform/recogSet/stepList";
	}
	
	
	/**
	 * 添加页面跳转
	 */
	@RequestMapping("add")
	public String add(HttpServletRequest request,
			HttpServletResponse response, Model model,StepVo record) throws IOException {
		return "platform/recogSet/stepEdit";
	}	
	
	/**
	 * 修改页面跳转页
	 * @param request
	 * @param response
	 * @param model
	 * @param record
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response, Model model,StepVo record) throws IOException {
		
		if(null!=record && !record.getId().equals("")){
			StepVo vo = stepService.selectUpdateStep(record.getId());
			model.addAttribute("vo", vo);
		}
		return "platform/recogSet/stepUpdate";
	
	}

		
	
	
	
	/**
	 * 策略列表
	 */
	@RequestMapping("list")
	public void list(HttpServletRequest request,
			HttpServletResponse response, StepVo record,PageInfo pageInfo){
		this.setPageInfo(request, pageInfo);
		//返回结果
		String retJson = "";
		try {
			User user = BaseUtil.getCurrentUser(request);
			JsonListResult<StepVo> result = new JsonListResult<StepVo>();
			record.setLegalOrgCode(user.getLegalOrgCode());
			// 设置分页查询
			Map<String, Object> map = new HashMap<String, Object>();
			// 设置分页信息
			map.put("obj", record);
			map.put("page", pageInfo);
			List<StepVo> list = stepService.selectStepPage(map);
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
	 * 获得所有的算法引擎
	 * @param request
	 * @param response
	 * @param record
	 * @throws IOException
	 */
	@RequestMapping("validEngineAllList")
	public void selectEngineCode(HttpServletRequest request,
			HttpServletResponse response) {
		//返回结果
		String jsonResult = null;
		try {
	 		String inputId = request.getParameter("inputId");
			List<Engine> list = engineVerService.selectValidEngineAll();
			for(int i=0;i<list.size();i++){
				list.get(i).setInputId(inputId);
			}
			if(null!=list){
				jsonResult = JsonUtil.toJSON(list);
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, jsonResult);
		
	}
	
	/**
	 * 根据算法引擎得到版本
	 * @param request
	 * @param response
	 * @param record
	 * @throws IOException
	 */
	@RequestMapping("selectEngineVer")
	public void selectEngineVer(HttpServletRequest request,
			HttpServletResponse response){
		//返回结果
		String jsonResult = null;
		String engineCode = request.getParameter("engineCode");//得到算法引擎代码
		String inputId = request.getParameter("inputId");
		try {
			List<EngineVer> list = engineVerService.selectByCode(engineCode);
			for(int i=0;i<list.size();i++){
				list.get(i).setInputId(inputId);
			}
			if(null!=list){
				jsonResult = JsonUtil.toJSON(list);
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, jsonResult);
		
	}
	
	
	
	/**
	 * 保存策略
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request,
			HttpServletResponse response, StepVo record)  {
		String action = request.getParameter("action");
		JsonEntityResult<StepVo> result = new JsonEntityResult<StepVo>();
		//返回结果
		String retJson = "";
	 	try {
	 		String ids = record.getEngineIds();
			String[] idarr = ids.split("\\|");
			User user = BaseUtil.getCurrentUser(request);
			
	 		if(null!=action && action.equals("update")){//修改操作
	 			//判断是否已经存在相同的策略
				if(stepService.selectStepByName(record.getStepName()).size()>1){
					retJson = ControllerUtil.getFailRetJson("策略名已经存在!");
					getPrintWriter(response, retJson);
					return;
				}
	 			Step step = new Step();
	 			step.setId(record.getId());
	 			step.setStepName(record.getStepName());
	 			step.setRemark(record.getRemark());
	 			step.setUpdateTime(new Date());
	 			step.setUpdator(user.getUserId());
	 			stepService.saveStep(step,idarr,1);
	 		}else if(null!=action && action.equals("create") ){//新增操作
	 			if(stepService.selectStepByName(record.getStepName()).size()>0){
					retJson = ControllerUtil.getFailRetJson("策略名已经存在!");
					getPrintWriter(response, retJson);
					return;
				}
				Step step = new Step();
				step.setLegalOrgCode(user.getLegalOrgCode());
				step.setStepName(record.getStepName());
				step.setStatus(Short.valueOf("1"));
				step.setCreator(user.getUserId());
				step.setCreateTime(new Date());
				step.setRemark(record.getRemark());
				stepService.saveStep(step,idarr,0);
	 		}
			//添加操作日志
			String remark ="保存策略成功，详情：" + record.toString();
			logOperService.insertLogOper(request, 1, remark);		
			retJson = JsonUtil.toJSON(result);
			
		} catch (Exception e) {
			//添加操作日志
			String remark ="保存策略失败，详情：" + e.getMessage();
			logOperService.insertLogOper(request, 0, remark);				
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		} 
	 	
		getPrintWriter(response, retJson);

	}
	
	
	
	@RequestMapping("delete")
	public void delete(HttpServletRequest request,
			HttpServletResponse response, StepVo record)  {
		
		//返回结果
		String retJson = "";
	 	try {
	 		int count = 0;
	 		if(null!=record && null!=record.getId()){
	 			int qint = stepService.selectStepHaveGroup(record.getId());
				if(qint>0){				
					retJson = ControllerUtil.getFailRetJson("此策略与策略组有关联，不能够删除!");
					getPrintWriter(response, retJson);
					return;
				}			 			
				count = stepService.deleteStep(record.getId());
	 		}	 		
			if(count > 0) {
				retJson = ControllerUtil.getSuccessRetJson("删除成功");
				//添加操作日志
				String remark ="删除策略成功，详情：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);
			} else {
				retJson = ControllerUtil.getSuccessRetJson("删除失败");	
			}
			
		} catch (Exception e) {			
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
	 	
		getPrintWriter(response, retJson);	
		
	}
	
}
