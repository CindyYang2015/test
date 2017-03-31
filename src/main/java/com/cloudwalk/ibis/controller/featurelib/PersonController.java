package com.cloudwalk.ibis.controller.featurelib;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonEntityResult;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.common.JsonResult;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.featurelib.Person;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.featurelib.PersonService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
*
* ClassName: PersonController <br/>
* Description: 客户信息. <br/>
* date: 2016年9月27日 下午2:13:16 <br/>
*
* @author 方凯
* @version
* @since JDK 1.7
*/
@Controller
@RequestMapping("/person")
public class PersonController extends BaseController{
	
	// 编辑页面
	private final String LIST_EDIT = "platform/featurelib/person_detail_edit";

	@Resource(name = "personService")
	private PersonService personService;
	
	//日志操作
	@Resource(name = "logOperService")
	private LogOperService logOperService;
	
	/**
	 *
	 * selectPersonFeatureByPage:查询生物特征库维护. <br/>
	 *
	 * @author:方凯 Date: 2015年8月21日 下午2:37:20
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectPersonFeatureByPage")
	public void selectPersonFeatureByPage(HttpServletRequest request,
			HttpServletResponse response, Person person, PageInfo pageInfo) {
		this.setPageInfo(request, pageInfo);
		JsonListResult<Person> result = new JsonListResult<Person>();
		String retJson = "";
		try {
			//证件号码必须输入
			if(StringUtils.isBlank(person.getCtfno())) {
				getPrintWriter(response, JsonUtil.toJSON(result));
				return;
			}
			person.initPartition();
			person.setLegalOrgCode(BaseUtil.getCurrentUser(request).getLegalOrgCode());
			// 设置分页查询
			JSONObject map = new JSONObject();
			// 设置分页信息
			map.put("obj", person);
			map.put("page", pageInfo);
			List<Person> list = this.personService.selectAllByPage(map);
	
			// 设置返回结果
			result.setRows(list);
			result.setTotal(Long.valueOf(Long.valueOf(pageInfo.getTotalCount())));
			retJson = JSONArray.toJSONString(result);
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);
		}
		getPrintWriter(response, retJson);

	}
	
	/**
	 * 逻辑删除客户信息 返回0表示失败返回1表示成功
	 * @param request
	 * @param response
	 */
	@RequestMapping("/removePersonByPrimaryKey")
	public void removePersonByPrimaryKey(HttpServletRequest request,Person person,
			HttpServletResponse response) {
		//返回json消息
		String retJson = "";
		
		String personId= person.getPersonId();
		if(personId == null || "".equals(personId)){
			retJson = ControllerUtil.getFailRetJson("客户ID不能为空");
			getPrintWriter(response, retJson);
			return;
		}
		
		//分区ID不能为空
		Integer partitionId = person.getPartitionId();
		if(partitionId == null){
			retJson = ControllerUtil.getFailRetJson("分区ID不能为空");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			int record = this.personService.removePersonByPrimaryKey(person);
			if(record > 0){
				retJson = ControllerUtil.getSuccessRetJson("删除成功");			
			} else {
				retJson = ControllerUtil.getFailRetJson("删除失败");
			}
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);			
		}
		
		getPrintWriter(response, retJson);		
	}
	
	/**
	 * 物理删除客户特征信息 返回0表示失败返回1表示成功
	 * @param request
	 * @param response
	 */
	@RequestMapping("/deletePersonFeature")
	public void deletePersonFeature(HttpServletRequest request,
			HttpServletResponse response,PersonFeature personFeature) {
		//返回json消息
		String retJson = "";
		//参数非空判断
		String personId= personFeature.getPersonId();
		String engineCode= personFeature.getEngineCode();
		String engineVerId= personFeature.getEngineVerCode();
		if(StringUtils.isBlank(personId) || StringUtils.isBlank(engineCode) || StringUtils.isBlank(engineVerId)){
			retJson = ControllerUtil.getFailRetJson("客户ID,引擎代码，引擎版本ID不能为空");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			personFeature.setFeatureId(null);
			List<PersonFeature> personFeatures = this.personService.searchAll(personFeature);
			if(personFeatures != null && personFeatures.size()>0){					
				int record = this.personService.deleteByPersonIdAndEngine(personFeature);
				if(record > 0){
					retJson = ControllerUtil.getSuccessRetJson("删除成功");	
					logOperService.insertLogOper(request, 1, "物理删除客户特征信息,详情:"+personFeature.toString());
				} else {
					retJson = ControllerUtil.getFailRetJson("删除失败");	
				}
			}else{
				retJson = ControllerUtil.getFailRetJson("删除失败,当前算法引擎下无数据可以删除！");	
			}
			
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, retJson);
	}
	
	/**
	 * 根据personId查询客户信息 
	 * ajax请求 json串code返回0表示查询成功，code返回1表示查询失败
	 * json 串data表示person类对象
	 * @param request
	 * @param response
	 */
	@RequestMapping("/selectPersonByPrimaryKey")
	public void selectPersonByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		JsonEntityResult<Person> resultReturn = new JsonEntityResult<Person>();	
		String retJson = "";
		String personId= request.getParameter("personId");
		if(personId == null || "".equals(personId)){		
			retJson = ControllerUtil.getFailRetJson("请选择客户");
			getPrintWriter(response, retJson);
			return;
		}
		
		//分区ID
		String partitionId = request.getParameter("partitionId");
		if(StringUtils.isBlank(partitionId)){		
			retJson = ControllerUtil.getFailRetJson("分区ID不能为空");
			getPrintWriter(response, retJson);
			return;
		}
		Integer partitionIdInt = ObjectUtils.objToInt(partitionId, 0);
		if(partitionIdInt == 0) partitionIdInt = null;
		
		Person person = new Person();
		//将uuid覆盖
		person.setPersonId(personId);
		person.setPartitionId(partitionIdInt);
		
		try{
			List<Person> list = this.personService.searchAll(person);
			if(list != null){					
				resultReturn.setSuccess(true);
				resultReturn.setEntity(list.get(0));
			}else{
				resultReturn.setSuccess(false);
			}
			retJson = JsonUtil.toJSON(resultReturn);
			
		}catch(Exception e){
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
	}
	
	/**
	 * 前端获取person信息，算法信息以及生物文件
	 * 提取生物文件特征保存客户特征信息以及保存或者新增person信息
	 * @param request
	 * @param response
	 */
	public void addOrSavePersonFeature(HttpServletRequest request,
			HttpServletResponse response,Person person) {
		//返回结果
		JsonEntityResult<Person> resultReturn = new JsonEntityResult<Person>();	
		String retJson = "";
		//设置登录用户信息
		User user = BaseUtil.getCurrentUser(request);
		person.setCreateTime(new Date());
		person.setCreator(user.getUserId());
		person.setLegalOrgCode(user.getLegalOrgCode());
		person.setOrgName(user.getOrgName());
		String personId = person.getPersonId();
		person.setPersonId(null);//置空
		try {
			//判断客户是否存在			
			List<Person> list = this.personService.selectPersonBykey(person);
			if(list != null && list.size() >0){
				resultReturn.setSuccess(false);
				resultReturn.setMessage(person.getCtfname()+"已存在");				
			}else{
				person.setPersonId(personId);
				int recorod = this.personService.insertSelective(person);
				if(recorod != 0){
					logOperService.insertLogOper(request, 1, "新增客户基本信息,详情:"+person.toString());
					PersonFeature feature = new PersonFeature();
					feature.setPartitionId(person.getPartitionId());
					feature.setEngineType(person.getEngineType());
					feature.setEngineCode(person.getEngineCode());
					feature.setEngineVerCode(person.getEngineVerCode());
					feature.setPersonId(person.getPersonId());
					feature.setCreateTime(new Date());
					feature.setCreator(user.getUserId());
					int fea = this.personService.insertSelectiveByFile(feature,person.getFileData(),user.getOrgCode());
					if(fea != 0){
						logOperService.insertLogOper(request, 1, "新增客户特征信息,详情:"+feature.toString());					
						resultReturn.setSuccess(true);
						resultReturn.setMessage("操作成功");	
					}else{					
						resultReturn.setSuccess(true);
						resultReturn.setMessage("特征保存失败");	
					}
				}else{					
					resultReturn.setSuccess(true);
					resultReturn.setMessage("操作失败");	
				}
			}
			retJson = JsonUtil.toJSON(resultReturn);
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
	}
	
	/**
	 * 跳转新增页面
	 * @param request
	 * @param response
	 * @param model
	 * @param person
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/create")
	public String create(HttpServletRequest request,
			HttpServletResponse response, Model model, Person person)
					throws IOException {
		model.addAttribute("action", "create");		
		return LIST_EDIT;		
	}
	
	/**
	 * edit:(跳转客户信息编辑页面). <br/>
	 *
	 *  Date: Sep 30, 2016 11:40:13 AM
	 * @param request
	 * @param response
	 * @param model
	 * @param person
	 * @return
	 * @throws IOException
	 * @since JDK 1.7
	 */
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response, Model model, Person person)
					throws IOException {
		JsonResult result = new JsonResult();
		model.addAttribute("action", "edit");
		if (ObjectUtils.isEmpty(person.getPersonId())) {
			result.setSuccess(false);
			result.setMessage("请至少选中一行记录");
			getPrintWriter(response, JSONArray.toJSONString(result));
			return "";
		}
		
		List<Person> list=this.personService.searchAllByPrimaryKey(person);
		if(list != null && list.size() >0){			
			model.addAttribute("person", list.get(0));
			return LIST_EDIT;
		}else{
			model.addAttribute("person", null);
			return LIST_EDIT;
		}
	}
	
	@RequestMapping("/savePersonFeature")
	public void savePersonFeature(HttpServletRequest request,
			HttpServletResponse response,Person person) {
		
		JsonEntityResult<Person> resultReturn = new JsonEntityResult<Person>();		
		/** 判断参数 **/
		if(StringUtils.isBlank(person.getEngineCode()) || 
				StringUtils.isBlank(person.getEngineVerCode()) || 
				StringUtils.isBlank(person.getCtftype()) || 
				StringUtils.isBlank(person.getCtfno()) || 
				StringUtils.isBlank(person.getCtfname())
				){
			resultReturn.setSuccess(false);
			resultReturn.setMessage("客户信息不完整");
			getPrintWriter(response, JsonUtil.toJSON(resultReturn));
			return;
		}		
		person.initPartition();
		
		/** 判断是否有文件上传 **/
		Map<String,byte[]> fileMap = ControllerUtil.getRequestByteData(request, new String[]{"person_feature_file"});
		if(!fileMap.isEmpty()) {
			byte[] outBuf = fileMap.get("person_feature_file");
			person.setFileData(outBuf);
		}		
			
		// 判断当前是更新，还是添加
		String action = request.getParameter("action");
		if ("create".equals(action)) {
			this.addOrSavePersonFeature(request, response, person);
		} else if ("edit".equals(action)) {
			this.updatePersonFeature(request, response, person);
		}
		
	}
	
	/**
	 * 前端获取person信息，算法信息以及生物文件
	 * 提取生物文件特征更新客户特征信息以及保存或者新增person信息
	 * @param request
	 * @param response
	 */
	public void updatePersonFeature(HttpServletRequest request,
			HttpServletResponse response,Person person) {
		//返回结果
		JsonEntityResult<Person> resultReturn = new JsonEntityResult<Person>();
		String retJson = "";
		//设置更新用户信息
		User user = BaseUtil.getCurrentUser(request);
		person.setUpdateTime(new Date());
		person.setUpdator(user.getUserId());	
		
		try {
			
			int i = this.personService.updateByPrimaryKeySelective(person);
			if(i>0){
				logOperService.insertLogOper(request, 1, "更新客户基本信息,详情:"+person.toString());
				PersonFeature feature = new PersonFeature();
				feature.setPartitionId(person.getPartitionId());
				feature.setEngineType(person.getEngineType());
				feature.setEngineCode(person.getEngineCode());
				feature.setEngineVerCode(person.getEngineVerCode());
				feature.setPersonId(person.getPersonId());
				feature.setCreateTime(new Date());
				feature.setCreator(user.getUserId());
				int fea = this.personService.updateSelectiveByFile(feature,person.getFileData(),user.getOrgCode());
				if(fea != 0){
					logOperService.insertLogOper(request, 1, "更新或者新增客户特征信息,详情:"+feature.toString());
					resultReturn.setSuccess(true);
					resultReturn.setMessage("操作成功");					
				}else{
					resultReturn.setSuccess(false);
					resultReturn.setMessage("特征保存失败");					
				}
			}else{
				resultReturn.setSuccess(false);
				resultReturn.setMessage("操作失败");
			}
			retJson = JsonUtil.toJSON(resultReturn);
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
		return;
		
	}
	
	/**
	 * g根据engineType、engineCode、engineVerCode、personId得到相应的特征信息（理论最多只有一条数据）
	 * @param request
	 * @param response
	 * @param feature
	 */
	@RequestMapping("/selectFeatureByEngine")
	public void selectFeatureByEngine(HttpServletRequest request,
			HttpServletResponse response,PersonFeature feature) {
		//返回结果
		JsonEntityResult<PersonFeature> resultReturn = new JsonEntityResult<PersonFeature>();
		String retJson = "";
		//参数判空
		String engineType = feature.getEngineType();
		String engineCode = feature.getEngineCode();
		String engineVerCode = feature.getEngineVerCode();
		String personId = feature.getPersonId();
		if(StringUtils.isBlank(engineType) ||
				StringUtils.isBlank(engineCode) ||
				StringUtils.isBlank(engineVerCode) ||
				StringUtils.isBlank(personId)){
			retJson = ControllerUtil.getFailRetJson("参数不完整");
			getPrintWriter(response, retJson);
			return;
		}	
		
		try {
			feature.setFeatureId("");
			List<PersonFeature> list = this.personService.searchAll(feature);
			if(!ObjectUtils.isEmpty(list)){
				resultReturn.setSuccess(true);
				resultReturn.setEntity(list.get(0));					
			}else{
				resultReturn.setSuccess(false);
			}	
			retJson = JsonUtil.toJSON(resultReturn);
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
	}
	
}
