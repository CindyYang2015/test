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
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.BlackPersonCheck;
import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonCheckQueryVo;
import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonVo;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.recogSet.BlackPersonCheckService;
import com.cloudwalk.ibis.service.recogSet.BlackPersonService;
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
@RequestMapping("/blackPersonCheck")
public class BlackPersonCheckController extends BaseController {

	// 列表页面
	private final String LIST_INDEX = "platform/recogSet/blackPersonCheck/blackPersonCheck";
	// 审核页面
	private final String LIST_CHECK = "platform/recogSet/blackPersonCheck/blackPersonCheckJuge";

	@Resource(name = "logOperService")
	private LogOperService logOperService;

	@Resource(name = "blackPersonCheckService")
	private BlackPersonCheckService blackPersonCheckService;

	@Resource(name = "blackPersonService")
	private BlackPersonService blackPersonService;

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
	 * allBlackPersonChecks:(黑名单审核页面分页列表). <br/>
	 * Date: Sep 30, 2016 11:27:59 AM
	 * 
	 * @param request
	 * @param response
	 * @param pageInfo
	 * @param blackPersonCheck
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/allBlackPersonChecks")
	public void allBlackPersonChecks(HttpServletRequest request,
			HttpServletResponse response, PageInfo pageInfo,
			BlackPersonCheck blackPersonCheck) {

		this.setPageInfo(request, pageInfo);
		JsonListResult<BlackPersonCheckQueryVo> result = new JsonListResult<BlackPersonCheckQueryVo>();
		String retJson = "";

		try {
			// 设置分页查询
			Map<String, Object> map = new HashMap<String, Object>();
			// 设置用户信息
			User user = BaseUtil.getCurrentUser(request);
			blackPersonCheck.setOrgCode(user.getOrgCodePath());

			// 设置默认条件:待审核
			/*if (blackPersonCheck.getStatus() == null) {
				blackPersonCheck.setStatus((short) 2);
			}*/

			// 设置分页信息
			map.put("obj", blackPersonCheck);
			map.put("page", pageInfo);

			List<BlackPersonCheckQueryVo> faceList = blackPersonCheckService
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
	 * edit:(跳转到审核页面). <br/>
	 *
	 * Date: Sep 30, 2016 11:32:11 AM
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param blackPersonCheck
	 * @return
	 * @throws IOException
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response, Model model,
			BlackPersonCheck blackPersonCheck) throws IOException {
		model.addAttribute("action", "edit");
		model.addAttribute("blackPersonCheck", blackPersonCheck);
		return LIST_CHECK;
	}

	/**
	 * checkBlackPerson:(黑名单审核). <br/>
	 *
	 * Date: Sep 30, 2016 11:32:47 AM
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/check")
	public void checkBlackPerson(HttpServletRequest request,
			HttpServletResponse response, String id) {
		JsonEntityResult<BlackPersonVo> resultReturn = new JsonEntityResult<BlackPersonVo>();
		String retJson = "";

		if (ObjectUtils.isEmpty(id)) {
			retJson = ControllerUtil.getFailRetJson("请选中审核记录");
			getPrintWriter(response, retJson);
			return;
		}
		// 审批结果：同意，不同意
		String result = request.getParameter("checkResult");
		// 备注
		String remark = request.getParameter("remark");
		// 获取审核对象
		try {
			BlackPersonCheck blackPersonCheck = blackPersonService
					.getBlackPersonCheck(id);
			if (ObjectUtils.isEmpty(blackPersonCheck)) {
				retJson = ControllerUtil.getFailRetJson("记录未找到，请刷新再试");
				getPrintWriter(response, retJson);
				return;
			}
			// 当前操作人
			User user = BaseUtil.getCurrentUser(request);
			blackPersonCheck.setRemark(remark);
			// 操作类型
			short operateStatus = blackPersonCheck.getOperateStatus();
			if (EnumClass.DicCheckTypeEnum.CHECK_STATUS_NO_PASS.getValue()
					.equals(result)) {
				// 不同意
				blackPersonCheck
						.setStatus(Short
								.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_NO_PASS
										.getValue()));
				blackPersonCheck.setUpdator(user.getUserId());
				blackPersonCheck.setUpdateTime(new Date());
				blackPersonService.updateBlackPersonCheck(blackPersonCheck);
			} else {
				// 同意
				if (EnumClass.DicOptTypeEnum.OPERATE_STATUS_NEW.getValue()
						.equals(operateStatus + "")) {
					// 新增
					blackPersonService
							.insertBlackPerson(blackPersonCheck, user);
				} else if (EnumClass.DicOptTypeEnum.OPERATE_STATUS_MODIFY
						.getValue().equals(operateStatus + "")) {
					// 修改
					blackPersonService
							.updateBlackPerson(blackPersonCheck, user);
				} else if (EnumClass.DicOptTypeEnum.OPERATE_STATUS_DELETE
						.getValue().equals(operateStatus + "")) {
					// 删除
					// 删除黑名单审核信息,渠道交易控制
					blackPersonService.deleteBlackPersonCheck(id);
					blackPersonService.deleteByCheckBlackPersonID(id);
					// 删除黑名单信息 ,渠道交易控制
					blackPersonService.deleteBlackPerson(blackPersonCheck
							.getBlackId());
					blackPersonService
							.deleteBlackPersonControl(blackPersonCheck
									.getBlackId());
				} else {
					// 未知操作类型
					retJson = ControllerUtil.getFailRetJson("未知操作类型，请刷新重试");
					getPrintWriter(response, retJson);
					return;
				}
			}
			// 添加操作日志
			remark = "审核成功" + "详情：" + blackPersonCheck.toString();
			logOperService.insertLogOper(request, 1, remark);
			resultReturn.setSuccess(true);
			getPrintWriter(response, JsonUtil.toJSON(resultReturn));

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
			getPrintWriter(response, retJson);
		}
	}
}
