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
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.ibis.model.recogSet.Step;
import com.cloudwalk.ibis.model.recogSet.StepGroup;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.recogSet.StepGroupService;
import com.cloudwalk.ibis.service.system.LogOperService;
/**
 * 策略组控制器
 * @author 白乐
 *
 */
@Controller
@RequestMapping("/stepGroup")
public class StepGroupController extends BaseController {

	@Resource(name = "stepGroupService")
	private StepGroupService stepGroupService;
	@Resource(name = "logOperService")
	private LogOperService logOperService;
	/**
	 * 列表页面跳转
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return "platform/recogSet/stepGroupList";
	}
	
	
	/**
	 * 添加页面跳转
	 */
	@RequestMapping("add")
	public String add(HttpServletRequest request,
			HttpServletResponse response, Model model,StepGroup record) throws IOException {
		//为了不 让html页面报错添加下面两行代码
		model.addAttribute("action", "create");
		model.addAttribute("sg", record);
		return "platform/recogSet/stepGroupEdit";
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
			HttpServletResponse response, Model model,StepGroup record) throws IOException {
		
		if(null!=record && !record.getId().equals("")){
			StepGroup sg = stepGroupService.selectStepGroupById(record.getId());
			model.addAttribute("sg", sg);
		}
		model.addAttribute("action", "edit");
		return "platform/recogSet/stepGroupEdit";
	
	}
	
	
	
	
	
	/**
	 * 显示策略组列表
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 * @throws IOException
	 */
	@RequestMapping("list")
	public void list(HttpServletRequest request,
			HttpServletResponse response, StepGroup record,PageInfo pageInfo) throws IOException {
		this.setPageInfo(request, pageInfo);
		//返回结果
		String retJson = "";
		User user = BaseUtil.getCurrentUser(request);
		JsonListResult<StepGroup> result = new JsonListResult<StepGroup>();
		record.setLegalOrgCode(user.getLegalOrgCode());
		// 设置分页查询
		Map<String, Object> map = new HashMap<String, Object>();
		// 设置分页信息
		map.put("obj", record);
		map.put("page", pageInfo);
		try {
			List<StepGroup> list = stepGroupService.selectStepGroupPage(map);
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
	 * 显示策略列表
	 */
	@RequestMapping("stepList")
	public void stepList(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		String jsonResult = null;
		try {
			User user = BaseUtil.getCurrentUser(request);
			List<Step> list = stepGroupService.selectStepList(user.getLegalOrgCode());
			if(null!=list){
				jsonResult = JsonUtil.toJSON(list);
			}
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		getPrintWriter(response, jsonResult);		
	}
	
	/**
	 * 保存策略组
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request,
			HttpServletResponse response, StepGroup record)  {
		//返回结果
		String retJson = "";
		try {
			String action = request.getParameter("action");			
			String repeatStr = stepGroupService.selectEngineRepeat(record.getSteps());
			if(null!=repeatStr && !"".equals(repeatStr)){			
				retJson = ControllerUtil.getFailRetJson("提交失败!选择的策略中包含了同种算法引擎不同的版本:<br>"+repeatStr);
				getPrintWriter(response, retJson);
				return;
			}		
			
			User user = BaseUtil.getCurrentUser(request);
			if("edit".equals(action)){
				record.setUpdator(user.getUserId());
				record.setUpdateTime(new Date());
				stepGroupService.saveStepGroup(record,1);
			}else if("create".equals(action)){
				record.setId(new StepGroup().getId());
				record.setLegalOrgCode(user.getLegalOrgCode());
				record.setStatus(Short.valueOf("1"));//默认为有效
				record.setCreateTime(new Date());
				record.setCreator(user.getUserId());
				stepGroupService.saveStepGroup(record,0);
			}

			//添加操作日志
			String remark ="保存策略组成功，详情：" + record.toString();
			logOperService.insertLogOper(request, 1, remark);		
			retJson = ControllerUtil.getSuccessRetJson("保存成功");
			
		}  catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);			

	}	
	
	/**
	 * 删除策略组
	 */
	@RequestMapping("delete")
	public void delete(HttpServletRequest request,
			HttpServletResponse response, StepGroup record)  {
		//返回结果
		String retJson = "";
		try {
			
			if(!stepGroupService.selectIsDelete(record.getId())){//查询是否可以删除本策略组，如果被识别策略引用则不能删除
				retJson = ControllerUtil.getFailRetJson("此策略组与识别策略有关联，不能够删除!");
				getPrintWriter(response, retJson);
				return;
			}
			
			int deleteCount = stepGroupService.deleteStepGroup(record.getId());
			
			if(deleteCount > 0) {
				retJson = ControllerUtil.getSuccessRetJson("删除成功");
				//添加操作日志
				String remark ="删除策略组成功，详情：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);	
			} else {
				retJson = ControllerUtil.getFailRetJson("删除失败");
			}			
			
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);			
		}			
		
		getPrintWriter(response, retJson);
	}
	
	
	
}
