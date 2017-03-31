package com.cloudwalk.ibis.controller.system;

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
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.system.Authorities;
import com.cloudwalk.ibis.model.system.AuthoritiesDto;
import com.cloudwalk.ibis.model.system.Resources;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.system.AuthoritiesService;
import com.cloudwalk.ibis.service.system.LogOperService;
import com.cloudwalk.ibis.service.system.ResourcesService;

/**
 *
 * ClassName: AuthoritiesController <br/>
 * Description: 权限. <br/>
 * date: 2015年8月21日 下午2:13:16 <br/>
 *
 * @author 朱云飞
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/authority")
public class AuthoritiesController extends BaseController {

	// 列表页面
	private final String LIST_INDEX = "platform/system/authority";

	// 角色权限列表页面
	private final String GROUP_AUTH_LIST_INDEX = "platform/system/setGroupAuthority";

	@Resource(name = "authorityService")
	private AuthoritiesService authorityService;

	@Resource(name = "logOperService")
	private LogOperService logOperService;
	
	@Resource(name = "resourcesService")
	private ResourcesService resourcesService;
	
	@RequestMapping(value = "/deleteJson")
	public void deleteJson(HttpServletRequest request,
			HttpServletResponse response, String id) {
		
		//返回结果
		String retJson = "";
		if (ObjectUtils.isEmpty(id)) {			
			retJson = ControllerUtil.getFailRetJson("请至少选中一行记录");
			getPrintWriter(response, retJson);
			return;
		}
		
		//超级权限不允许删除
		if ("100".equals(id)) {
			retJson = ControllerUtil.getFailRetJson("超级权限不能删除");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			//删除权限信息
			Authorities authorities = this.authorityService.selectByPrimaryKey(id);
			int status = this.authorityService.deleteAuthorities(id);
	
			if (status == Constants.DELETE_FAILURED_REF) {
				retJson = ControllerUtil.getFailRetJson("请移出该权限关联的角色和资源！");
			} else {
				retJson = ControllerUtil.getSuccessRetJson("删除成功");
				//添加操作日志
				String remark = "删除";
				remark = remark + "权限，详情为：" + authorities.toString();
				logOperService.insertLogOper(request, 1, remark);				
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
	}

	/**
	 * index:跳转至权限页面方法 <br/>
	 * 适用条件：登录成功，跳转至权限页面<br/>
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
		User user=BaseUtil.getCurrentUser(request);
		model.addAttribute("legalOrgCode", user.getLegalOrgCode());
		return LIST_INDEX;
	}

	/**
	 *
	 * saveAuthorities:保存权限信息（新增，更新）. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:22:37
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveAuthorities")
	public void saveAuthorities(HttpServletRequest request,
			HttpServletResponse response, AuthoritiesDto authority) {

		//返回结果
		String retJson = "";
		if (ObjectUtils.isEmpty(authority.getAuthorityId())) {
			retJson = ControllerUtil.getFailRetJson("权限信息不能为空");
			getPrintWriter(response, retJson);
			return;
		}
		
		//权限名称不能重复
		if (ObjectUtils.isEmpty(authority.getAuthorityName())) {
			retJson = ControllerUtil.getFailRetJson("权限名字不能为空");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			User user=BaseUtil.getCurrentUser(request);		
			if(StringUtils.isBlank(authority.getLegalOrgCode())) {
				//设置法人机构代码
				authority.setLegalOrgCode(user.getLegalCode());
			}			
			
			// 判断权限名称是否重复
			List<Authorities> authoritiesList = this.authorityService
					.selectAuthsByAuthNames(authority);
			if (!ObjectUtils.isEmpty(authoritiesList)) {					
				retJson = ControllerUtil.getFailRetJson("权限名称不能重名");
				getPrintWriter(response, retJson);
				return;
			} 
			
			//保存权限信息	
			int saveCount = this.authorityService.saveAuthoritiess(authority,user);
			if(saveCount > 0) {
				retJson = ControllerUtil.getSuccessRetJson("保存权限成功");
				//添加操作日志
				String remark ="保存权限信息，详情：" + authority.toString();
				logOperService.insertLogOper(request, 1, remark);
			} else {
				retJson = ControllerUtil.getFailRetJson("保存权限失败");
			}
			
		} catch(Exception e) {
			log.error("保存权限信息发生异常",e);
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);

	}

	/**
	 *
	 * saveGroupAuthority:保存权限与资源的关系. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午7:57:55
	 * @param request
	 * @param response
	 * @param id
	 * @param contentId
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveAuthRes")
	public void saveAuthRes(HttpServletRequest request,
			HttpServletResponse response, String id, String contentId) {

		//返回结果
		String retJson = "";
		//没有资源的时候   && !ObjectUtils.isEmpty(contentId)
		/*if (ObjectUtils.isEmpty(id) || ObjectUtils.isEmpty(contentId)) {
			retJson = ControllerUtil.getFailRetJson("权限ID，资源ID不能为空！");
			getPrintWriter(response, retJson);
			return;
			
		}*/
		
		try {
			// 设置角色与权限的关系
			int saveCount = this.authorityService.saveAuthRes(id, contentId);
			if(saveCount > 0) {
				 retJson = ControllerUtil.getSuccessRetJson("保存成功");
				 //添加操作日志
				 Authorities authorities = this.authorityService.selectByPrimaryKey(id);
				 Resources resource = null; 
				 String remark = "为权限：" + authorities.getAuthorityName() +",赋予资源：";
				 //获取资源信息
				 String[] resourceArray = contentId.split(",");
				 for (String resourceId : resourceArray) {
					resource = this.resourcesService.selectByPrimaryKey(resourceId);
					if(resource != null) {
						remark = remark + resource.getResourceName() + " ";
					}
				 }	
				 logOperService.insertLogOper(request, 1, remark);
				 
			} else {
				retJson = ControllerUtil.getSuccessRetJson("保存失败");
			}	
			
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);
		}

		getPrintWriter(response, retJson);
	}

	/**
	 *
	 * selectGroupByPage:分页查询权限信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午2:20:22
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectAuthoritiesByPage")
	public void selectAuthoritiesByPage(HttpServletRequest request,
			HttpServletResponse response, Authorities record, PageInfo pageInfo) {

		this.setPageInfo(request, pageInfo);
		JsonListResult<Authorities> result = new JsonListResult<Authorities>();
		//返回结果
		String retJson = "";
		try {
			// 设置分页查询
			Map<String, Object> map = new HashMap<String, Object>();
			// 设置分页信息
			map.put("obj", record);
			map.put("page", pageInfo);
	
			List<Authorities> list = this.authorityService
					.selectAuthoritiesByPage(map);
	
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
	 *
	 * selectGroupByPage:查询所有的权限信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月21日 下午2:37:20
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectAllAuthorities")
	public void selectGroupByPage(HttpServletRequest request,
			HttpServletResponse response, Authorities authority) {
		JsonListResult<Authorities> result = new JsonListResult<Authorities>();
		//返回结果
		String retJson = "";
		try {
			//获取当前登录用户的信息
			User user=BaseUtil.getCurrentUser(request);
			authority.setLegalOrgCode(user.getLegalOrgCode());
			
			List<Authorities> list = this.authorityService
					.searchAllAuthorityByGroupId(authority);
	
			// 设置返回结果
			result.setRows(list);
			result.setTotal(Long.valueOf(list.size()));
			retJson = JsonUtil.toJSON(result);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		
		getPrintWriter(response, retJson);

	}

	/**
	 * index:跳转至角色权限页面方法 <br/>
	 * 适用条件：登录成功，跳转至角色权限页面<br/>
	 * 执行流程：AJAX调用<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/setGroupAuthority")
	public String setUserRole(HttpServletRequest request,
			HttpServletResponse response, String groupId, Model model)
			throws IOException {
		model.addAttribute("groupId", groupId);
		return GROUP_AUTH_LIST_INDEX;
	}

}
