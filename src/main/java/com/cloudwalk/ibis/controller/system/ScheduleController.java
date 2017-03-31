package com.cloudwalk.ibis.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.schedule.ScheduleEntity;
import com.cloudwalk.ibis.model.schedule.ScheduleRecordEntity;
import com.cloudwalk.ibis.service.schedule.ScheduleService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
 * 定时任务
 * @author zhuyf
 *
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleController extends BaseController {
	
	@Resource(name = "scheduleService")
	private ScheduleService scheduleService;	
	
	//日志操作
	@Resource(name = "logOperService")
	private LogOperService logOperService;

	/**
	 * 跳转到定时任务列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String scheduleList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return  "platform/system/schedule";
	}
	
	/**
	 * 查询定时任务信息
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 */
	@RequestMapping(value = "selectscheduleByPage")
	public void selectscheduleByPage(HttpServletRequest request,
			HttpServletResponse response, ScheduleEntity record, PageInfo pageInfo) {
		this.setPageInfo(request, pageInfo);
		JsonListResult<ScheduleEntity> result = new JsonListResult<ScheduleEntity>();
		String retJson = "";
		// 封装分页查询所需要的信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("obj", record);
		map.put("page", pageInfo);
		try {
			
			List<ScheduleEntity> list = this.scheduleService.selectScheduleList(map);	
			// 设置返回结果
			result.setRows(list);
			result.setTotal(Long.valueOf(pageInfo.getTotalCount()));
			retJson = JSONArray.toJSONString(result);
			
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		getPrintWriter(response, retJson);
	}	
	
	/**
	 * 跳转到定时任务记录列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/recordList")
	public String recordList(HttpServletRequest request,
			HttpServletResponse response, Model model,String taskCode) {
		model.addAttribute("taskCode", taskCode);
		return  "platform/system/scheduleRecord";
	}
	
	/**
	 * 查询定时任务信息
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 */
	@RequestMapping(value = "selectscheduleRecordByPage")
	public void selectscheduleRecordByPage(HttpServletRequest request,
			HttpServletResponse response, ScheduleRecordEntity record, PageInfo pageInfo) {
		this.setPageInfo(request, pageInfo);
		JsonListResult<ScheduleRecordEntity> result = new JsonListResult<ScheduleRecordEntity>();
		String retJson = "";
		// 封装分页查询所需要的信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("obj", record);
		map.put("page", pageInfo);
		try {
			
			List<ScheduleRecordEntity> list = this.scheduleService.selectScheduleRecordList(map);
			// 设置返回结果
			result.setRows(list);
			result.setTotal(Long.valueOf(pageInfo.getTotalCount()));
			retJson = JSONArray.toJSONString(result);
			
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		getPrintWriter(response, retJson);
	}	
	
	
	/**
	 * 开启任务
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/openTask")
	public void openTask(HttpServletRequest request,
			HttpServletResponse response, String id){
		//返回结果
		String retJson = "";
		if (ObjectUtils.isEmpty(id)) {		
			retJson = ControllerUtil.getFailRetJson("请选中一行记录");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			
			//更新任务状态为开启			
			ScheduleEntity se = this.scheduleService.getScheduleInfo(id);
			if(se == null) {
				throw new ServiceException("该任务不存在");
			}
			
			se.setTaskStatus(ScheduleEntity.TASK_STATUS_1);
			this.scheduleService.updateScheduleStatus(se);
			
			//添加操作日志
			String remark = "任务<<"+se.getTaskName()+">>被开启";
			logOperService.insertLogOper(request, 1, remark);
			retJson = ControllerUtil.getSuccessRetJson("开启任务成功");
			
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, retJson);
	}
	
	/**
	 * 关闭任务
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/closeTask")
	public void closeTask(HttpServletRequest request,
			HttpServletResponse response, String id){
		//返回结果
		String retJson = "";
		if (ObjectUtils.isEmpty(id)) {		
			retJson = ControllerUtil.getFailRetJson("请选中一行记录");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			
			//更新任务状态为关闭			
			ScheduleEntity se = this.scheduleService.getScheduleInfo(id);
			if(se == null) {
				throw new ServiceException("该任务不存在");
			}
			
			se.setTaskStatus(ScheduleEntity.TASK_STATUS_0);
			this.scheduleService.updateScheduleStatus(se);
			
			//添加操作日志
			String remark = "任务<<"+se.getTaskName()+">>被关闭";
			logOperService.insertLogOper(request, 1, remark);
			retJson = ControllerUtil.getSuccessRetJson("关闭任务成功");
			
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, retJson);
	}
}
