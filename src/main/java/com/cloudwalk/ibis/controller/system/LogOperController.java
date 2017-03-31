package com.cloudwalk.ibis.controller.system;

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
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.ibis.model.system.LogOper;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
 *
 * ClassName: LogOperController <br/>
 * Description: 系统操作日志controller. <br/>
 * date: 2015年8月31日 下午5:04:42 <br/>
 *
 * @author Jackson He
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/logOper")
public class LogOperController extends BaseController {

	// 列表页面
	private final String LIST_INDEX = "platform/system/logOper";

	@Resource(name = "logOperService")
	private LogOperService logOperService;

	/**
	 * index:跳转至日志页面方法 <br/>
	 * 适用条件：登录成功，跳转至日志页面<br/>
	 * 执行流程：AJAX调用<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return LIST_INDEX;
	}

	/**
	 *
	 * selectDepartmentByPage:分页查询日志信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午2:20:22
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectPage")
	public void selectPage(HttpServletRequest request,
			HttpServletResponse response, LogOper record, PageInfo pageInfo) {

		this.setPageInfo(request, pageInfo);
		JsonListResult<LogOper> result = new JsonListResult<LogOper>();
		//返回结果
		String retJson = "";
		try {
			//权限控制 获取user的全路径
			User user =BaseUtil.getCurrentUser(request);
			record.setOrgCode(user.getOrgCode());
			// 设置分页查询
			Map<String, Object> map = new HashMap<String, Object>();
			// 设置分页信息
			map.put("obj", record);
			map.put("page", pageInfo);
	
			List<LogOper> list = this.logOperService.selectLogOperByPage(map);
	
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


}
