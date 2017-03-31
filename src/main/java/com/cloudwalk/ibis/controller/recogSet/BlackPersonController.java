package com.cloudwalk.ibis.controller.recogSet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonEntityResult;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.common.JsonResult;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.BlackPersonCheck;
import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonCheckQueryVo;
import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonVo;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.featurelib.PersonService;
import com.cloudwalk.ibis.service.recogSet.BlackPersonService;
import com.cloudwalk.ibis.service.system.DicService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
 * ClassName: BlackPersonController <br/>
 * Description: 黑名单控制类. <br/>
 * date: Sep 27, 2016 10:25:40 AM <br/>
 *
 * @author 杨维龙
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/blackPerson")
public class BlackPersonController extends BaseController {

	// 列表页面
	private final String LIST_INDEX = "platform/recogSet/blackPerson/blackPerson";
	// 编辑页面
	private final String LIST_EDIT = "platform/recogSet/blackPerson/blackPersonEdit";

	@Resource(name = "logOperService")
	private LogOperService logOperService;

	@Resource(name = "blackPersonService")
	private BlackPersonService blackPersonService;

	@Resource(name = "dicService")
	private DicService dicService;

	@Resource(name = "personService")
	private PersonService personService;

	/**
	 * index:(黑名单设置分页查询页面). <br/>
	 * Date: Sep 27, 2016 10:41:12 AM
	 * 
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
	 * insertSelective:(新增黑名单). <br/>
	 * TODO(这里描述这个方法适用条件 – AJAX请求).<br/>
	 *
	 * @author:杨维龙 Date: 2015年3月26日 上午11:12:53
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/create")
	public String insertSelective(HttpServletRequest request,
			HttpServletResponse response, Model model,
			BlackPersonVo blackPersonVo) throws IOException {
		model.addAttribute("action", "add");
		model.addAttribute("blackPerson", blackPersonVo);
		return LIST_EDIT;
	}

	/**
	 * edit:(跳转黑名单编辑页面). <br/>
	 *
	 * Date: Sep 30, 2016 11:40:13 AM
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param blackPersonVo
	 * @return
	 * @throws IOException
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response, Model model,
			BlackPersonVo blackPersonVo) throws IOException {
		model.addAttribute("action", "edit");
		if (!ObjectUtils.isEmpty(blackPersonVo.getId())) {
			BlackPersonVo blackPerson = blackPersonService
					.getBlackPersonCheckVo(blackPersonVo.getId());
			model.addAttribute("blackPerson", blackPerson);
		}
		return LIST_EDIT;
	}

	/**
	 * allBlackPersons:(分页查询黑名单数据). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * Date: Sep 27, 2016 4:37:49 PM
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/allBlackPersons")
	public void allBlackPersons(HttpServletRequest request,
			HttpServletResponse response, PageInfo pageInfo,
			BlackPersonCheck blackPersonCheck) {

		this.setPageInfo(request, pageInfo);
		JsonListResult<BlackPersonCheckQueryVo> result = new JsonListResult<BlackPersonCheckQueryVo>();
		String retJson = "";

		try {
			// 设置分页查询
			Map<String, Object> map = new HashMap<String, Object>();
			// 设置登陆用户信息
			User user = BaseUtil.getCurrentUser(request);
			blackPersonCheck.setLegalOrgCode(user.getLegalOrgCode());

			// 设置分页信息
			map.put("obj", blackPersonCheck);
			map.put("page", pageInfo);

			List<BlackPersonCheckQueryVo> faceList = blackPersonService
					.selectBlackPersonCheckByPage(map);
			// 设置返回结果
			result.setRows(faceList);
			result.setTotal(Long.valueOf(pageInfo.getTotalCount()));
			retJson = JsonUtil.toJSON(result);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}

		getPrintWriter(response, retJson);
	}

	/**
	 * saveBlackPerson:(保存黑名单记录). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * Date: Sep 28, 2016 9:49:29 AM
	 * 
	 * @param request
	 * @param response
	 * @param blackPersonVo
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveBlackPerson")
	public void saveBlackPerson(HttpServletRequest request,
			HttpServletResponse response, BlackPersonVo blackPersonVo) {
		JsonEntityResult<BlackPersonVo> resultReturn = new JsonEntityResult<BlackPersonVo>();
		String retJson = "";

		// 判断数据是否合法
		String validationMessage = validation(blackPersonVo);
		if (!ObjectUtils.isEmpty(validationMessage)) {
			retJson = ControllerUtil.getFailRetJson(validationMessage);
			getPrintWriter(response, retJson);
			return;
		}
		// 判断当前是更新，还是添加
		String action = request.getParameter("action");
		int count = 0;
		// 当前登陆用户
		User user = BaseUtil.getCurrentUser(request);
		blackPersonVo.setCreator(user.getUserId());
		blackPersonVo.setOrgCode(user.getOrgCodePath());
		blackPersonVo.setLegalOrgCode(user.getLegalOrgCode());

		try {

			if ("edit".equals(action)) {
				
				//获取审核的黑名单信息
				BlackPersonCheck bpc = blackPersonService.selectBpcByBlackId(blackPersonVo.getId());
				if(bpc == null) {
					throw new ServiceException("该客户黑名单不存在");
				}
				blackPersonVo.setId(bpc.getId());
				count = blackPersonService.editBlackPerson(blackPersonVo);				
			} else {
				
				// 新增
				BlackPersonCheck blackPersonCheck = new BlackPersonCheck();
				blackPersonCheck.setLegalOrgCode(blackPersonVo
						.getLegalOrgCode());
				blackPersonCheck.setCtftype(blackPersonVo.getCtftype());
				blackPersonCheck.setCtfno(blackPersonVo.getCtfno());
				blackPersonCheck.setCustomerId(blackPersonVo.getCustomerId());
				blackPersonCheck.setCtfname(blackPersonVo.getCtfname());
				blackPersonCheck = blackPersonService
						.getBlackPersonCheck(blackPersonCheck);
				if (blackPersonCheck != null) {
					resultReturn.setMessage("该客户黑名单已存在");
				} else {
					count = blackPersonService.createBlackPerson(blackPersonVo);
				}

			}			
			String remark = "create".equals(action) ? "新增" : "更新";
			
			if (count > 0) {
				resultReturn.setSuccess(true);
				// 添加操作日志
				remark = remark + "黑名单，详情：" + blackPersonVo.toString();
				logOperService.insertLogOper(request, 1, remark);
				getPrintWriter(response, JsonUtil.toJSON(resultReturn));
				return;
			} else {
				resultReturn.setSuccess(false);
				if (StringUtils.isBlank(resultReturn.getMessage())) {
					resultReturn.setMessage("黑名单数据插入数据库失败");
				}
				// 添加操作日志
				remark = remark + "黑名单，详情：" + blackPersonVo.toString();
				logOperService.insertLogOper(request, 1, remark);
				// 写入返回信息
				getPrintWriter(response, JsonUtil.toJSON(resultReturn));
				return;
			}
			
		} catch (Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);
			getPrintWriter(response, retJson);
		}

	}

	/**
	 * validation:(保存数据时，校验数据是否合法). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * Date: Sep 30, 2016 2:14:00 PM
	 * 
	 * @param blackPersonVo
	 * @return
	 * @since JDK 1.7
	 */
	private String validation(BlackPersonVo blackPersonVo) {
		String message = "";
		if (ObjectUtils.isEmpty(blackPersonVo)) {
			message = "保存对象不能为空，请检查";
		} else if (ObjectUtils.isEmpty(blackPersonVo.getBlackType())) {
			message = "黑名单类型不能为空，请检查";
		} else if (ObjectUtils.isEmpty(blackPersonVo.getCtftype())) {
			message = "证件类型不能为空，请检查";
		} else if (ObjectUtils.isEmpty(blackPersonVo.getCtfname())) {
			message = "姓名不能为空，请检查";
		} else if (ObjectUtils.isEmpty(blackPersonVo.getCtfno())) {
			message = "证件号不能为空，请检查";
		} else if (ObjectUtils.isEmpty(blackPersonVo.getChannels())) {
			message = "渠道类型不能为空，请检查";
		} else if (ObjectUtils.isEmpty(blackPersonVo.getTradingCodes())) {
			message = "交易类型不能为空，请检查";
		}
		return message;
	}

	/**
	 * removeBlackPersonByPrimaryKey:(删除黑名单). <br/>
	 *
	 * Date: Sep 28, 2016 5:59:27 PM
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/removeBlackPersonByPrimaryKey")
	public void removeBlackPersonByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response, String id) {
		JsonResult result = new JsonResult();
		String retJson = "";

		if (ObjectUtils.isEmpty(id)) {
			retJson = ControllerUtil.getFailRetJson("请至少选中一行记录");
			getPrintWriter(response, retJson);
			return;
		}

		try {
			// 查询黑名单信息
			BlackPersonCheck blackPersonCheck = blackPersonService
					.getBlackPersonCheck(id);

			if (ObjectUtils.isEmpty(blackPersonCheck)) {
				retJson = ControllerUtil.getFailRetJson("获取信息失败");
				getPrintWriter(response, retJson);
				return;
			}
			// 当状态为待审核，且没有关联黑名单数据的时候直接删除，直接删除
			if (EnumClass.DicCheckTypeEnum.CHECK_STATUS_WAITING.getValue()
					.equals(blackPersonCheck.getStatus() + "")
					&& ObjectUtils.isEmpty(blackPersonCheck.getBlackId())) {
				blackPersonService.deleteBlackPersonCheck(id);
				blackPersonService.deleteByCheckBlackPersonID(id);
			} else {
				blackPersonCheck
						.setOperateStatus(Short
								.parseShort(EnumClass.DicOptTypeEnum.OPERATE_STATUS_DELETE
										.getValue()));
				blackPersonCheck
						.setStatus(Short
								.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_WAITING
										.getValue()));
				blackPersonService.updateBlackPersonCheck(blackPersonCheck);
			}
			// 如果用户设置了其他的相关资料不能删除

			// 添加操作日志
			String remark = "删除";
			remark = remark + "用户，详情为：" + blackPersonCheck.toString();
			logOperService.insertLogOper(request, 1, remark);
			retJson = JsonUtil.toJSON(result);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}

		getPrintWriter(response, retJson);
	}

	/**
	 * 根据类型代码，查询字典信息
	 * 
	 * @author:杨维龙
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "selectChannelDicValuesByDicType")
	public void selectChannelDicValuesByDicType(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "";
		// 调用service方法，得到要返回的数据，拼接成json字符串
		String id = request.getParameter("id");
		String dicType = request.getParameter("dicType");
		DicValues dicValues = new DicValues();
		dicValues.setDicType(dicType);
		// 渠道类型
		List<String> listCodes = null;
		try {
			if ("3".equals(dicType)) {
				listCodes = blackPersonService.getChannelCodes(id);
				// 交易类型
			} else {
				listCodes = blackPersonService.getTradingCodes(id);
			}
			str = blackPersonService.selectDicValuesByDicType(dicValues,
					listCodes);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}

		getPrintWriter(response, str);
	}
}
