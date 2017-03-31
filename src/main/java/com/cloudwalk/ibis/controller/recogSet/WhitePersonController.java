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

import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.WhitePersonCheck;
import com.cloudwalk.ibis.model.recogSet.vo.WhitePersonCheckQueryVo;
import com.cloudwalk.ibis.model.recogSet.vo.WhitePersonCheckVo;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.featurelib.PersonService;
import com.cloudwalk.ibis.service.recogSet.WhitePersonService;
import com.cloudwalk.ibis.service.system.DicService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
 * ClassName: BlackPersonController <br/>
 * Description: 白名单控制类. <br/>
 * date: Sep 27, 2016 10:25:40 AM <br/>
 *
 * @author 杨维龙
 * @version 
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/whitePerson")
public class WhitePersonController extends BaseController {

	// 列表页面
	private final String LIST_INDEX = "platform/recogSet/whitePerson/whitePerson";
	// 编辑页面
	private final String LIST_EDIT = "platform/recogSet/whitePerson/whitePersonEdit";

	@Resource(name = "logOperService")
	private LogOperService logOperService;
	
	@Resource(name = "whitePersonService")
	private WhitePersonService whitePersonService;
	

	@Resource(name="dicService")
	private DicService dicService;
	
	@Resource(name = "personService")
	private PersonService personService;
	
	/**
	 * index:(白名单设置分页查询页面). <br/>
	 *  Date: Sep 27, 2016 10:41:12 AM
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return LIST_INDEX;
	}

	/**
	 * insertSelective:(新增白名单). <br/>
	 * TODO(这里描述这个方法适用条件 – AJAX请求).<br/>
	 *
	 * @author:杨维龙 Date: 2015年3月26日 上午11:12:53
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/create")
	public String insertSelective(HttpServletRequest request,
			HttpServletResponse response, Model model,WhitePersonCheck whitePersonCheck) throws IOException {
		model.addAttribute("action", "create");
		model.addAttribute("whitePerson", whitePersonCheck);
		return LIST_EDIT;
	}
	
	/**
	 * edit:(跳转白名单编辑页面). <br/>
	 *
	 *  Date: Sep 30, 2016 11:40:13 AM
	 * @param request
	 * @param response
	 * @param model
	 * @param whitePersonVo
	 * @return
	 * @throws IOException
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response, Model model, WhitePersonCheck whitePersonCheck) {
		
		model.addAttribute("action", "edit");
		if (!ObjectUtils.isEmpty(whitePersonCheck.getId())) {
			WhitePersonCheck whitePerson=whitePersonService.getWhitePersonkById(whitePersonCheck.getId());
			model.addAttribute("whitePerson", whitePerson);
		}		
		return LIST_EDIT;
		
	}
   
	/**
	 * saveBlackPerson:(保存白名单记录). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 28, 2016 9:49:29 AM
	 * @param request
	 * @param response
	 * @param whitePersonVo
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveWhitePerson")
	public void saveWhitePerson(HttpServletRequest request,
			HttpServletResponse response, WhitePersonCheckVo whitePersonVo) {
		//返回结果
		String retJson = "";
		whitePersonVo.setEngineCode(request.getParameter("engineId_whitePerson_add"));
		whitePersonVo.setEngineverCode(request.getParameter("engineverId_whitePerson_add"));
		//判断数据是否合法
		String validationMessage=validation(whitePersonVo);
		if (!ObjectUtils.isEmpty(validationMessage)) {			
			retJson = ControllerUtil.getFailRetJson(validationMessage);
			getPrintWriter(response, retJson);
			return;
		}
		//当前登陆用户
		User user=BaseUtil.getCurrentUser(request);					
		// 判断当前是更新，还是添加
		String action = request.getParameter("action");
		int count=0;
		//当前登陆用户
		whitePersonVo.setCreator(user.getUserId());
		whitePersonVo.setOrgCode(user.getOrgCodePath());
		whitePersonVo.setLegalOrgCode(user.getLegalOrgCode());
		try {
			if ("edit".equals(action)) {				
				//获取审核的白名单信息
				WhitePersonCheck wpc = whitePersonService.selectWpcByWhileId(whitePersonVo.getId());
				if(wpc == null) {
					throw new ServiceException("该客户白名单不存在");
				}
				whitePersonVo.setId(wpc.getId());
				//编辑
				count=whitePersonService.editWhitePerson(whitePersonVo);
			} else{
				//新增
				WhitePersonCheck  whitePersonCheck = new WhitePersonCheck();
				whitePersonCheck.setCtftype(whitePersonVo.getCtftype());
				whitePersonCheck.setCustomerId(whitePersonVo.getCustomerId());
				whitePersonCheck.setCtfno(whitePersonVo.getCtfno());
				whitePersonCheck.setLegalOrgCode(whitePersonVo.getLegalOrgCode());
				whitePersonCheck.setCtfname(whitePersonVo.getCtfname());
				whitePersonCheck.setWhiteType(whitePersonVo.getWhiteType());
				whitePersonCheck = whitePersonService.getWhitePersonCheck(whitePersonCheck);
				if(whitePersonCheck != null) {
					//该客户已添加到白名单
					retJson = ControllerUtil.getFailRetJson("该客户已添加到白名单");
					getPrintWriter(response, retJson);
					return;
				} 
				//创建黑名单
				count=whitePersonService.createWhitePersonCheck(whitePersonVo);						
			}
			
			String remark = "create".equals(action) ? "新增" : "更新";
			if(count > 0) {
				//添加操作日志
				remark = remark + "白名单，详情：" + whitePersonVo.toString();
				logOperService.insertLogOper(request, 1, remark);
				retJson = ControllerUtil.getSuccessRetJson("保存白名单成功！");
				
			} else {
				retJson = ControllerUtil.getFailRetJson("保存白名单失败！");
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		
		getPrintWriter(response, retJson);
		
	}
	/**
	 * validation:(保存数据时，校验数据是否合法). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 30, 2016 2:14:00 PM
	 * @param whitePersonVo
	 * @return
	 * @since JDK 1.7
	 */
	private String validation(WhitePersonCheckVo whitePersonVo) {
		String message = "";
		if (ObjectUtils.isEmpty(whitePersonVo)) {
			message = "保存对象不能为空，请检查";
		} else if (ObjectUtils.isEmpty(whitePersonVo.getWhiteType())) {
			message = "白名单类型不能为空，请检查";
		} else if (ObjectUtils.isEmpty(whitePersonVo.getCtftype())) {
			message = "证件类型不能为空，请检查";
		} else if (ObjectUtils.isEmpty(whitePersonVo.getCtfname())) {
			message = "姓名不能为空，请检查";
		} else if (ObjectUtils.isEmpty(whitePersonVo.getCtfno())) {
			message = "证件号不能为空，请检查";
		} else if (ObjectUtils.isEmpty(whitePersonVo.getEngineCode())) {
			message = "算法引擎不能为空，请检查";
		}else if (ObjectUtils.isEmpty(whitePersonVo.getEngineverCode())) {
			message = "算法引擎版本不能为空，请检查";
		}else if (ObjectUtils.isEmpty(whitePersonVo.getScore())) {
			message = "阈值不能为空，请检查";
		}else if(ObjectUtils.isEmpty(whitePersonVo.getChannels())){
			message = "渠道类型不能为空，请检查";
		} else if(ObjectUtils.isEmpty(whitePersonVo.getTradingCodes())){
			message = "交易类型不能为空，请检查";
		}
		
		double score=0;
		try {
			score=Double.parseDouble(whitePersonVo.getScore()+"");
			
		} catch (Exception e) {
			message = "阈值不符合规范，取值范围为0-1，请检查";
		}
		if(score>1 || score<0){
			message = "阈值不符合规范，取值范围为0-1，请检查";
		}
		return message;
	}
	
	/**
     * allBlackPersons:(分页查询白名单数据). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     *
     *  Date: Sep 27, 2016 4:37:49 PM
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     * @since JDK 1.7
     */
	@RequestMapping(value = "/allWhitePersons")
	public void allWhitePersons(HttpServletRequest request,
			HttpServletResponse response, PageInfo pageInfo, WhitePersonCheck whitePersonCheck) {
		
		this.setPageInfo(request, pageInfo);
		JsonListResult<WhitePersonCheckQueryVo> result = new JsonListResult<WhitePersonCheckQueryVo>();
		//返回结果
		String retJson = "";
		// 设置分页查询
		Map<String, Object> map = new HashMap<String, Object>();
		//设置登陆用户信息
		User user = BaseUtil.getCurrentUser(request);
		whitePersonCheck.setLegalOrgCode(user.getLegalOrgCode());
				
		// 设置分页信息
		//whitePersonCheck.setOperateStatus();
		map.put("obj", whitePersonCheck);
		map.put("page", pageInfo);

		try {
			List<WhitePersonCheckQueryVo> personlist = whitePersonService.selectWhitePersonByPage(map);
			// 设置返回结果
			result.setRows(personlist);
			result.setTotal(Long.valueOf(pageInfo.getTotalCount()));
			retJson = JsonUtil.toJSON(result);
		
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
	}	
	
	/**
	 * 根据类型代码，查询字典信息
	 * @author:杨维龙 
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "selectChannelDicValuesByDicType")
	public void selectChannelDicValuesByDicType(HttpServletRequest request,
			HttpServletResponse response) {
		// 返回结果
		String retJson = "";
		// 调用service方法，得到要返回的数据，拼接成json字符串
		String id = request.getParameter("id");
		String dicType = request.getParameter("dicType");
		DicValues dicValues = new DicValues();
		dicValues.setDicType(dicType);
		try {
			// 渠道类型
			List<String> listCodes = null;
			if ("3".equals(dicType)) {
				listCodes = whitePersonService.getChannelCodes(id);
				// 交易类型
			} else {
				listCodes = whitePersonService.getTradingCodes(id);
			}
			retJson = whitePersonService.selectDicValuesByDicType(dicValues,
					listCodes);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}

		getPrintWriter(response, retJson);

	}
	/**
	 * removeBlackPersonByPrimaryKey:(删除白名单). <br/>
	 *
	 *  Date: Sep 28, 2016 5:59:27 PM
	 * @param request
	 * @param response
	 * @param id
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/removeWhitePersonByPrimaryKey")
	public void removeBlackPersonByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response, String id) {
		//返回结果
		String retJson = "";
		if (ObjectUtils.isEmpty(id)) {			
			retJson = ControllerUtil.getFailRetJson("请至少选中一行记录");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
	        //查询白名单信息
			WhitePersonCheck whitePersonCheck=whitePersonService.getWhitePersonCheckById(id);
	
			if (ObjectUtils.isEmpty(whitePersonCheck)) {			
				retJson = ControllerUtil.getFailRetJson("该白名单不存在");
				getPrintWriter(response, retJson);
				return;
			}
			
			//当状态为待审核，且没有关联黑名单数据的时候直接删除，直接删除
			if( EnumClass.DicCheckTypeEnum.CHECK_STATUS_WAITING.getValue().equals(whitePersonCheck.getStatus()+"")
					&& ObjectUtils.isEmpty(whitePersonCheck.getWhiteId())){
				whitePersonService.deleteWhitePersonCheck(id);
				whitePersonService.deleteWhitePersonCheckControlByWhiteID(id);
			}else{
				whitePersonCheck.setOperateStatus(Short.parseShort(EnumClass.DicOptTypeEnum.OPERATE_STATUS_DELETE.getValue()));
				whitePersonCheck.setStatus(Short.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_WAITING.getValue()));
				whitePersonService.updateWhitePersonCheck(whitePersonCheck);
			}
			// 如果用户设置了其他的相关资料不能删除	
			// 添加操作日志
			String remark = "删除";
			remark = remark + "用户，详情为：" +whitePersonCheck.toString();
			logOperService.insertLogOper(request, 1, remark);
			retJson = ControllerUtil.getSuccessRetJson("删除成功");
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
	}
}
