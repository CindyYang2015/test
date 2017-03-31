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
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.Engine;
import com.cloudwalk.ibis.model.recogSet.Recognizerule;
import com.cloudwalk.ibis.model.recogSet.Recogstep;
import com.cloudwalk.ibis.model.recogSet.vo.RecognizeruleVo;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.recogSet.RecognizeruleService;
import com.cloudwalk.ibis.service.system.LogOperService;
/**
 * 识别阈值控制器
 * @author 白乐
 *
 */
@Controller
@RequestMapping("/recognizerule")
public class RecognizeruleController extends BaseController {

	@Resource(name = "recognizeruleService")
	private RecognizeruleService recognizeruleService;	
	@Resource(name = "logOperService")
	private LogOperService logOperService;	
	/**
	 * 列表页面跳转
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return "platform/recogSet/recognizeruleList";
	}	
	
	/**
	 * 添加页面跳转
	 */
	@RequestMapping("add")
	public String add(HttpServletRequest request,
			HttpServletResponse response, Model model,RecognizeruleVo record) throws IOException {
		//为了不 让html页面报错添加下面两行代码
		model.addAttribute("action", "create");
		model.addAttribute("vo", record);
		return "platform/recogSet/recognizeruleEdit";
	}	
	
	
	/**
	 * 修改页面跳转页
	 */
	@RequestMapping("edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response, Model model,RecognizeruleVo record) throws IOException {
		model.addAttribute("action", "edit");
		if(null!=record && !record.getId().equals("")){
			Recognizerule vo = recognizeruleService.selectRecognizeruleById(record.getId());
			RecognizeruleVo viewvo = new RecognizeruleVo();
			viewvo.setId(vo.getId());
			viewvo.setInputEngineCode(vo.getEngineCode());
			viewvo.setInputRecogstepId(vo.getRecogstepId());
			viewvo.setInputRemark(vo.getRemark());
			viewvo.setInputScore(vo.getScore());
			viewvo.setInputStatus(vo.getStatus());
			model.addAttribute("vo", viewvo);
		}
		return "platform/recogSet/recognizeruleEdit";
	}	
	
	/**
	 * 初始化字典项列表
	 */
	@RequestMapping("recogStepList")
	public void tradingCodeList(HttpServletRequest request,
			HttpServletResponse response, Model model){
		//返回结果
		String jsonResult = null;
		try {
			User user = BaseUtil.getCurrentUser(request);
			List<Recogstep> list = recognizeruleService.selectRecogsetpList(user.getLegalOrgCode());
			if(null!=list){
				jsonResult = JsonUtil.toJSON(list);
			}
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, jsonResult);	
	}
	
	/**
	 * 根据识别策略得到相应的算法引擎
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping("engineList")
	public void engineList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		//返回结果
		String jsonResult = null;
		try {
			String recogStepId =  request.getParameter("recogStepId");
			List<Engine> list = recognizeruleService.selectEngineByRecogStepId(recogStepId);			
			if(null!=list){
				jsonResult = JsonUtil.toJSON(list);
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, jsonResult);	
	}
	
	/**
	 * 保存和更新
	 * @param request
	 * @param response
	 * @param record
	 * @throws Exception
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request,
			HttpServletResponse response, RecognizeruleVo vo) {
		//返回结果
		String retJson = "";
		try {
			String action = request.getParameter("action");
			int i = 0;
			Recognizerule record = new Recognizerule();
			User user = BaseUtil.getCurrentUser(request);
			if ("create".equals(action)) {
				record.setRecogstepId(vo.getInputRecogstepId());
				record.setEngineCode(vo.getInputEngineCode());
				record.setScore(vo.getInputScore());
				record.setStatus(vo.getInputStatus());
				record.setRemark(vo.getInputRemark());
				
				record.setCreator(user.getUserId());
				record.setCreateTime(new Date());
				record.setId(ObjectUtils.createUUID());
				record.setLegalOrgCode(user.getLegalOrgCode());
				List<Recognizerule> reList=recognizeruleService.selectRecogsetpRuleList(record);
				if(reList.size()<1){
					i = recognizeruleService.saveRecognizerule(record);
				}else{
					retJson = ControllerUtil.getFailRetJson("渠道识别已经存在,请重新输入");
					getPrintWriter(response, retJson);
					return;		
				}
				
				
			}else if("edit".equals(action)){
				record.setId(vo.getId());
				record.setRecogstepId(vo.getInputRecogstepId());
				record.setEngineCode(vo.getInputEngineCode());
				record.setScore(vo.getInputScore());
				record.setRemark(vo.getInputRemark());
				record.setStatus(vo.getInputStatus());
				record.setUpdator(user.getUserId());
				record.setUpdateTime(new Date());
				List<Recognizerule> reList=recognizeruleService.selectRecogsetpRuleList(record);
				if(reList.size()<=1){
					i = recognizeruleService.updateRecognizerule(record);
				}else{
					retJson = ControllerUtil.getFailRetJson("渠道识别已经存在");
					getPrintWriter(response, retJson);
					return;			
				}
				
			}
			if(i>0) {
				//添加操作日志
				String remark ="保存渠道识别规则成功，详情：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);
				retJson = ControllerUtil.getSuccessRetJson("保存成功");					
			} else {
				retJson = ControllerUtil.getSuccessRetJson("保存失败");	
			}
						
		}  catch (Exception e) {
			//添加操作日志
			String remark ="保存渠道识别规则失败，详情：" + e.getMessage();
			logOperService.insertLogOper(request, 0, remark);				
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, retJson);
	}
	
	
	/**
	 * 渠道识别规则列表
	 */
	@RequestMapping("list")
	public void list(HttpServletRequest request,
			HttpServletResponse response,Recognizerule record,PageInfo pageInfo) {		
		this.setPageInfo(request, pageInfo);
		JsonListResult<Recognizerule> result = new JsonListResult<Recognizerule>();
		//返回结果
		String retJson = "";
		User user = BaseUtil.getCurrentUser(request);
		record.setLegalOrgCode(user.getLegalOrgCode());
		// 设置分页查询
		Map<String, Object> map = new HashMap<String, Object>();
		// 设置分页信息
		map.put("obj", record);
		map.put("page", pageInfo);
		try {
			List<Recognizerule> list = recognizeruleService.selectRecognizerulePage(map);
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
	 * 删除识别规则
	 * @param request
	 * @param response
	 * @param record
	 */
	@RequestMapping("delete")
	public void delete(HttpServletRequest request,
			HttpServletResponse response, Recognizerule record)  {
		//返回结果
		String retJson = "";
		try {
			recognizeruleService.deleteRecognizerule(record.getId());
			//添加操作日志
			String remark ="删除渠道识别规则成功，详情：" + record.toString();
			logOperService.insertLogOper(request, 1, remark);		
			retJson = ControllerUtil.getSuccessRetJson("删除成功");
			
		} catch (Exception e) {
			//添加操作日志
			String remark ="删除渠道识别规则失败，详情：" + e.getMessage();
			logOperService.insertLogOper(request, 0, remark);	
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		}			
		
		getPrintWriter(response, retJson);

	}
	
	
	
}
