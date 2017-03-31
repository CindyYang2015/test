package com.cloudwalk.ibis.controller.featurelib;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.recogSet.Engine;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.service.featurelib.PersonFeatureService;
import com.cloudwalk.ibis.service.system.DicService;
import com.cloudwalk.ibis.service.system.LogOperService;
import com.google.common.collect.Maps;

/**
*
* ClassName: PersonFeatureController <br/>
* Description: 客户特征信息. <br/>
* date: 2016年9月27日 下午2:13:16 <br/>
*
* @author 方凯
* @version
* @since JDK 1.7
*/
@Controller
@RequestMapping("/personFeature")
public class PersonFeatureController extends BaseController{
	
	@Resource(name = "personFeatureService")
	private PersonFeatureService personFeatureService;
	
	@Resource(name="dicService")
	private DicService dicService;
	
	//日志操作
	@Resource(name = "logOperService")
	private LogOperService logOperService;
	/**
	 * 根据personId查询客户信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/selectFeatureByPrimaryKey")
	public void selectFeatureByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response,DicValues dicValues) {
		//返回结果
		JsonListResult<PersonFeature> resultReturn = new JsonListResult<PersonFeature>();
		String retJson = "";
		//参数判空
		String personId= request.getParameter("personId");
		if(personId == null || "".equals(personId)){
			retJson = ControllerUtil.getFailRetJson("客户ID不能为空");
			getPrintWriter(response, retJson);
			return;
		}
		
		//客户分区ID判断
		String partitionId = request.getParameter("partitionId");
		if(StringUtils.isBlank(partitionId)){
			retJson = ControllerUtil.getFailRetJson("客户分区ID不能为空");
			getPrintWriter(response, retJson);
			return;
		}
		Integer partitionIdInt = ObjectUtils.objToInt(partitionId, 0);
		if(partitionIdInt == 0) {
			partitionIdInt = null;
		}
		
		PersonFeature person = new PersonFeature();
		person.setPartitionId(partitionIdInt);
		person.setPersonId(personId);
		//重置uuid
		person.setFeatureId("");
		try{
			
			List<PersonFeature> list = this.personFeatureService.searchAllByPrimaryKey(person);
			if(list != null){
				resultReturn.setSuccess(true);	
				resultReturn.setRows(list);
				Map<Object,Object> data = Maps.newHashMap();
				//算法引擎列表
				data.put("engine", this.dicService.selectDicValuesByDicType(dicValues));
				resultReturn.setData(data);							
			}else{
				resultReturn.setSuccess(false);	
			}
			retJson = JsonUtil.toJSON(resultReturn);
		}catch(Exception e){
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, JsonUtil.toJSON(resultReturn));
	}
	
	/**
	 * 根据personId得到拥有的所有算法
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("validEngineAllList")
	public void selectEngineCode(HttpServletRequest request,
			HttpServletResponse response,PersonFeature feature) throws IOException {
		feature.setFeatureId("");
		String jsonResult = null;
		String inputId = request.getParameter("inputId");
		try {
			List<Engine> list = this.personFeatureService.selectValidEngineAllByFeature(feature.getPartitionId(),feature.getPersonId());
			for(int i=0;i<list.size();i++){
				list.get(i).setInputId(inputId);
			}
			if(null!=list){
				jsonResult = JSON.toJSONString(list);
			}
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		getPrintWriter(response, jsonResult);
	}
	
	/**
	 * 根据特征得到版本
	 * @param request
	 * @param response
	 * @param record
	 * @throws IOException
	 */
	@RequestMapping("selectEngineVer")
	public void selectEngineVer(HttpServletRequest request,
			HttpServletResponse response,PersonFeature feature) throws IOException {
		
		String jsonResult = null;
		String engineCode = request.getParameter("engineCode");//得到算法引擎代码
		feature.setEngineCode(engineCode);
		String inputId = request.getParameter("inputId");	
		try {
			List<EngineVer> list = this.personFeatureService.selectByCodeFromFeature(feature);
			for(int i=0;i<list.size();i++){
				list.get(i).setInputId(inputId);
			}
			if(null!=list){
				jsonResult = JSON.toJSONString(list);
			}
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, jsonResult);
		
	}
}
