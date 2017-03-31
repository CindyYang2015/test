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
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.Recogstep;
import com.cloudwalk.ibis.model.recogSet.StepGroup;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.base.CacheService;
import com.cloudwalk.ibis.service.recogSet.RecogstepService;
import com.cloudwalk.ibis.service.recogSet.StepGroupService;
import com.cloudwalk.ibis.service.system.LogOperService;

@Controller
@RequestMapping("/recogStep")
public class RecogStepController extends BaseController {
	
	@Resource(name = "cacheService")
	private CacheService cacheService; 	
	@Resource(name = "stepGroupService")
	private StepGroupService stepGroupService;
	@Resource(name = "recogstepService")
	private RecogstepService recogstepService;
	@Resource(name = "logOperService")
	private LogOperService logOperService;	
	
//	@Resource(name = "recognizeruleService")
//	private RecognizeruleService recognizeruleService;	
//	
	/**
	 * 列表页面跳转
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return "platform/recogSet/recogStepList";
	}
	
	
	/**
	 * 添加页面跳转
	 */
	@RequestMapping("add")
	public String add(HttpServletRequest request,
			HttpServletResponse response, Model model,Recogstep record) throws IOException {
		//为了不 让html页面报错添加下面两行代码
		model.addAttribute("action", "create");
		model.addAttribute("vo", record);
		return "platform/recogSet/recogStepEdit";
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
			HttpServletResponse response, Model model,Recogstep record) throws IOException {
		model.addAttribute("action", "edit");
		if(null!=record && !record.getId().equals("")){
			Recogstep vo = recogstepService.selectRecogstepById(record.getId());
			model.addAttribute("vo", vo);
		}
		return "platform/recogSet/recogStepEdit";
	
	}
	
	/**
	 * 初始化字典项列表
	 */
	@RequestMapping("dicList")
	public void tradingCodeList(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		//返回结果
		String retJson = "";
		String type=  request.getParameter("type");
		List<DicValues> list = null;
		try {
			
			if(null!=type && "0".equals(type)){//交易代码
				list = cacheService.getDicValuesByType(EnumClass.DicTypeEnum.TRADING_CODE.getValue() );
			}else if(null!=type && "1".equals(type)){//渠道
				list = cacheService.getDicValuesByType(EnumClass.DicTypeEnum.CHANNEL.getValue() );
			}
			String jsonResult = null;
			if(null!=list){
				jsonResult = JSON.toJSONString(list);
			}
			if(list.size()>0){
				String nullStr = "";//"{\"dicCode\":\"\",\"dicType\":\"\",\"enabledFlag\":1,\"meaning\":\"--请选择--\"},";//选项前加入一个空行
				String subStr = jsonResult.substring(jsonResult.indexOf("[")+"[".length(), jsonResult.length());
				retJson = "["+nullStr+subStr;
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, retJson);

	}
	
	/**
	 * 策略组列表
	 */
	@RequestMapping("stepGroupList")
	public void stepGroupList(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		String jsonResult = null;
		try {
			User user = BaseUtil.getCurrentUser(request);
			List<StepGroup> list = stepGroupService.selectStepGroupList(user.getLegalOrgCode());
			if(null!=list){
				jsonResult = JsonUtil.toJSON(list);
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, jsonResult);
	}

	
	/**
	 * 策略组列表
	 */
	@RequestMapping(" list")
	public void list(HttpServletRequest request,
			HttpServletResponse response,Recogstep record,PageInfo pageInfo) throws IOException {
		this.setPageInfo(request, pageInfo);
		JsonListResult<Recogstep> result = new JsonListResult<Recogstep>();
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
			List<Recogstep> list = recogstepService.selectRecogstepPage(map);
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
	 * 保存识别策略
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request,
			HttpServletResponse response, Recogstep record)  {
		//返回结果
		String retJson = "";
		try {
			String action = request.getParameter("action");
			int i = 0;
			User user = BaseUtil.getCurrentUser(request);
			
			
			if ("create".equals(action)) {
				record.setId(ObjectUtils.createUUID());
				record.setLegalOrgCode(user.getLegalOrgCode());
				//判断是否有相同的识别策略:机构代码、交易渠道、交易代码
				if(recogstepService.selectRecogstepByRecogstep(record).size()>0){
					retJson = ControllerUtil.getFailRetJson("该识别策略已经存在!");
					getPrintWriter(response, retJson);
					return;
				}
				record.setStatus(Short.valueOf("1"));//默认有效
				record.setCreator(user.getUserId());
				record.setCreateTime(new Date());
				i = recogstepService.saveRecogstep(record);
			}else if("edit".equals(action)){
				//判断是否有相同的识别策略:机构代码、交易渠道、交易代码
				if(recogstepService.selectRecogstepByRecogstep(record).size()>1){
					retJson = ControllerUtil.getFailRetJson("该识别策略已经存在!");
					getPrintWriter(response, retJson);
					return;
				}
				record.setUpdator(user.getUserId());
				record.setUpdateTime(new Date());
				i = recogstepService.updateRecogstep(record);
			}
			
			if(i>0) {
				//添加操作日志
				String remark ="保存识别策略组成功，详情：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);
				retJson = ControllerUtil.getSuccessRetJson("保存成功");
			} else {
				retJson = ControllerUtil.getSuccessRetJson("保存失败");
			}
						
		}  catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);				
		}
		
		getPrintWriter(response, retJson);

	}	
	
	
	
	@RequestMapping("delete")
	public void delete(HttpServletRequest request,
			HttpServletResponse response, Recogstep record)  {
		
		//返回结果
		String retJson = "";
	 	try {
	 		if(null!=record && null!=record.getId()){
	 			int qint = recogstepService.selectRecogStepHaveRule(record.getId());
				if(qint>0){					
					retJson = ControllerUtil.getFailRetJson("此识别策略与渠道识别规则设置有关联，不能够删除!");
					getPrintWriter(response, retJson);
					return;
				}			
	 			recogstepService.deleteRecogstepById(record.getId());
	 		}
	 		//添加操作日志
			String remark ="删除识别策略成功，详情：" + record.toString();
			logOperService.insertLogOper(request, 1, remark);
			retJson = ControllerUtil.getSuccessRetJson("删除成功");	
			
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		}
	 	
	 	getPrintWriter(response, retJson);		
		
	}	
	
	/*@RequestMapping("batchAddStep")
	public void batchAddStep(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		try {
		List<DicValues> dicVals1 = this.cacheService.getDicValuesByType("3");
		List<DicValues> dicVals2 = this.cacheService.getDicValuesByType("6");
		for(DicValues dicv1:dicVals1) {
			for(DicValues dicv2:dicVals2){
				Recogstep recogstep = new Recogstep();
				recogstep.setId(ObjectUtils.createUUID());
				recogstep.setChannel(dicv1.getDicCode());
				recogstep.setTradingCode(dicv2.getDicCode());
				recogstep.setLegalOrgCode("0000");
				recogstep.setBankOrgName("云丛银行总行");
				recogstep.setCreator("1");
				recogstep.setRecogstepName(dicv1.getMeaning()+"+"+dicv2.getMeaning());
				recogstep.setRemark(dicv1.getMeaning()+"+"+dicv2.getMeaning());
				recogstep.setStatus((short)1);
				recogstep.setStepgroupId("b3e45f586990456c9ad97da642473654");
				recogstep.setUpdator("1");
				this.recogstepService.saveRecogstep(recogstep);
				//初始化识别策略阈值
				Recognizerule rule = new Recognizerule();
				rule.setId(ObjectUtils.createUUID());
				rule.setCreator("1");
				rule.setEngineCode("cyface");
				rule.setLegalOrgCode("0000");
				rule.setRecogstepId(recogstep.getId());
				rule.setScore(new BigDecimal(0.7));
				rule.setStatus((short)1);
				rule.setUpdator("1");
				this.recognizeruleService.saveRecognizerule(rule);
			}
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
		getPrintWriter(response, "");
	}*/
	
}
