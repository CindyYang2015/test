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
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.WhitePersonCheck;
import com.cloudwalk.ibis.model.recogSet.vo.WhitePersonCheckQueryVo;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.recogSet.WhitePersonCheckService;
import com.cloudwalk.ibis.service.recogSet.WhitePersonService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
 * ClassName: WhitePersonController <br/>
 * Description: 白名单控制类. <br/>
 * date: Sep 27, 2016 10:25:40 AM <br/>
 *
 * @author 杨维龙
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/whitePersonCheck")
public class WhitePersonCheckController extends BaseController {


	// 列表页面
	private final String LIST_INDEX = "platform/recogSet/whitePersonCheck/whitePersonCheck";
    //审核页面
	private final String LIST_CHECK = "platform/recogSet/whitePersonCheck/whitePersonCheckJuge";

	
	@Resource(name = "logOperService")
	private LogOperService logOperService;


	@Resource(name = "whitePersonService")
	private WhitePersonService whitePersonService;
	
	@Resource(name = "whitePersonCheckService")
	private WhitePersonCheckService whitePersonCheckService;

	/**
	 * index:(白名单设置分页查询页面). <br/>
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
	 * allWhitePersonChecks:(白名单审核页面分页列表). <br/>
	 *  Date: Sep 30, 2016 11:27:59 AM
	 * @param request
	 * @param response
	 * @param pageInfo
	 * @param whitePersonCheck
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/allWhitePersonChecks")
	public void allWhitePersonChecks(HttpServletRequest request,
			HttpServletResponse response, PageInfo pageInfo,
			WhitePersonCheck whitePersonCheck) {

		this.setPageInfo(request, pageInfo);
		JsonListResult<WhitePersonCheckQueryVo> result = new JsonListResult<WhitePersonCheckQueryVo>();
		//返回结果
		String retJson = "";
		try {
			// 设置分页查询
			Map<String, Object> map = new HashMap<String, Object>();
			//设置用户信息
			User user = BaseUtil.getCurrentUser(request);
			whitePersonCheck.setOrgCode(user.getOrgCodePath());
					
			//设置默认查询条件:待审核
			/*if(whitePersonCheck.getStatus() == null) {
				whitePersonCheck.setStatus((short)2);
			}*/
			
			// 设置分页信息
			map.put("obj", whitePersonCheck);
			map.put("page", pageInfo);
	
			List<WhitePersonCheckQueryVo> personlist = this.whitePersonCheckService.selectWhitePersonCheckByPage(map);
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
     * edit:(跳转到审核页面). <br/>
     *
     *  Date: Sep 30, 2016 11:32:11 AM
     * @param request
     * @param response
     * @param model
     * @param whitePersonCheck
     * @return
     * @throws IOException
     * @since JDK 1.7
     */
	@RequestMapping(value = "/check")
	public String check(HttpServletRequest request,
			HttpServletResponse response, Model model,
			WhitePersonCheck whitePersonCheck) throws IOException {
		model.addAttribute("action", "edit");		
		model.addAttribute("whitePersonCheck", whitePersonCheck);
		return LIST_CHECK;
	}
	
	/**
	 * checkWhitePerson:(白名单审核). <br/>
	 *
	 *  Date: Sep 30, 2016 11:32:47 AM
	 * @param request
	 * @param response
	 * @param id
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/checkSave")
	public void checkWhitePerson(HttpServletRequest request,
			HttpServletResponse response, String id) {
		//返回结果
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
		try {
			// 获取审核对象
			WhitePersonCheck whitePersonCheck = whitePersonService.getWhitePersonCheckById(id);
			
			if (ObjectUtils.isEmpty(whitePersonCheck)) {
				retJson = ControllerUtil.getFailRetJson("记录未找到，请刷新再试");
				getPrintWriter(response, retJson);
				return;
			}
			
			//当前操作人
			User user=BaseUtil.getCurrentUser(request);
			whitePersonCheck.setRemark(remark);
			
			//操作类型
			short operateStatus = whitePersonCheck.getOperateStatus();
			
			if (EnumClass.DicCheckTypeEnum.CHECK_STATUS_NO_PASS.getValue().equals(result)) {
				// 不同意
				whitePersonCheck.setStatus(Short.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_NO_PASS.getValue()));
				whitePersonCheck.setUpdator(user.getUserId());
				whitePersonCheck.setUpdateTime(new Date());
				whitePersonService.updateWhitePersonCheck(whitePersonCheck);
			} else {
				// 同意
				if (EnumClass.DicOptTypeEnum.OPERATE_STATUS_NEW.getValue().equals(operateStatus + "")) {
					// 新增
					whitePersonCheckService.insertWhitePerson(whitePersonCheck,user);
				} else if (EnumClass.DicOptTypeEnum.OPERATE_STATUS_MODIFY.getValue().equals(operateStatus + "")) {
					// 修改
					whitePersonCheckService.updateWhitePerson(whitePersonCheck,user);
				} else if (EnumClass.DicOptTypeEnum.OPERATE_STATUS_DELETE.getValue().equals(operateStatus + "")) {
					// 删除
					//删除白名单审核信息,渠道交易控制
					whitePersonService.deleteWhitePersonCheck(id);
					whitePersonService.deleteWhitePersonCheckControlByWhiteID(id);
					//删除白名单信息 ,渠道交易控制
					whitePersonCheckService.deleteWhitePersonByID(whitePersonCheck.getWhiteId());
					whitePersonCheckService.deleteWhitePersonControlByWhiteId(whitePersonCheck.getWhiteId());
				} else {
					// 未知操作类型				
					retJson = ControllerUtil.getFailRetJson("未知操作类型，请刷新重试");
					getPrintWriter(response, retJson);
					return;
				}
			}
			
			 //添加操作日志
			remark = "审核成功" + "详情：" + whitePersonCheck.toString();
			logOperService.insertLogOper(request, 1, remark);
			retJson = ControllerUtil.getSuccessRetJson("审核成功");
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
		
	}

}
