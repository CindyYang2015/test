/**
 * Project Name:fssc
 * File Name:ResourcesController.java
 * Package Name:com.linc.fssc.controller.identity
 * Date:2015年3月26日 上午11:10:24
 * Copyright @ 2015 上海企垠信息科技有限公司  All Rights Reserved.
 *
 */
package com.cloudwalk.ibis.controller.system;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.druid.util.StringUtils;
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.common.BaseConstants;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.system.AuthResRefKey;
import com.cloudwalk.ibis.model.system.GroupAuthRefKey;
import com.cloudwalk.ibis.model.system.Resources;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.system.AuthoritiesService;
import com.cloudwalk.ibis.service.system.LogOperService;
import com.cloudwalk.ibis.service.system.ResourcesService;
import com.google.common.collect.Lists;

/**
 *
 * ClassName: ResourcesController <br/>
 * Description: TODO Description. <br/>
 * date: 2015年8月17日 下午12:37:54 <br/>
 *
 * @author 朱云飞
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/resources")
public class ResourcesController extends BaseController {
	
	// 列表页面
	private final String LIST_INDEX = "platform/system/resource";
	// 编辑页面
	private final String LIST_EDIT = "platform/system/resourceEdit";
	// 资源图标页面
	private final String ICON_LIST_EDIT = "platform/system/resicon_list";
	// 权限资源列表页面
	private final String AUTH_RES_LIST_INDEX = "platform/system/setAuthResource";

	@Resource(name = "resourcesService")
	private ResourcesService resourcesService;
	
	@Resource(name = "logOperService")
	private LogOperService logOperService;
	
	@Resource(name = "authorityService")
	private AuthoritiesService authorityService;

	/**
	 * index:跳转至资源新增页面方法 <br/>
	 * 适用条件：登录成功，跳转至资源新增页面<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/create")
	public String create(HttpServletRequest request,
			HttpServletResponse response, Model model, Resources resources){
		String resourceId = resources.getResourceId();
		if (!ObjectUtils.isEmpty(resourceId)) {
			//先查询资源ID是否存在，不存在则需要把resourceParentId设置成null
			Resources r = this.resourcesService.selectByPrimaryKey(resourceId);
			if(r == null){
				resources.setResourceParentId(null);
				resources.setResourceType("1");
			}else{
				resources.setResourceParentId(resourceId);
			}
			resources.setResourceId(null);
			// 获取资源父节点
			Resources resources1 = this.resourcesService
					.selectByPrimaryKey(resourceId);
			if (resources1 != null) {
				resources.setResourceParentName(resources1.getResourceName());
			}
		}

		model.addAttribute("action", "create");
		model.addAttribute("resources", resources);
		return LIST_EDIT;
	}

	/**
	 *
	 * createResources:新增资源信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午1:41:54
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	private void createResources(HttpServletRequest request,
			HttpServletResponse response, Resources record) {
		
		if(record != null && ObjectUtils.isEmpty(record.getResourceParentId())) {
			record.setResourceParentId(null);
		}
		
		//返回结果
		String retJson = "";
		
		try {
			//判断是不是超级管理员，如果不是超级管理员，不允许添加资源
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(BaseConstants.USER);
			List<GroupAuthRefKey> list = this.resourcesService.selectByUserId(user.getUserId());
			boolean b = this.resourcesService.isSuperAuthority(list);
			if(b == false){
				retJson = ControllerUtil.getFailRetJson("用户权限不足");
				getPrintWriter(response, retJson);
				return;				
			}
			//判断是不是顶级资源，顶级资源不能够添加资源链接
			if(StringUtils.isEmpty(record.getResourceParentId())){
				record.setResourceUrl(null);
			}
			//添加资源
			int num = this.resourcesService.insertSelective(record);
			
			if (num < 1) {			
				retJson = ControllerUtil.getFailRetJson("新增资源信息失败");
				getPrintWriter(response, retJson);
				return;
			}else{
				//资源添加成功，设置资源的权限为超级管理员
				AuthResRefKey authResRefKey = new AuthResRefKey();
				authResRefKey.setAuthorityId("100");
				authResRefKey.setResourceId(record.getResourceId());
				int n = this.resourcesService.insertAuthResRefKey(authResRefKey);
				if(n < 1){
					retJson = ControllerUtil.getFailRetJson("新增资源加入超级管理权限失败");
					getPrintWriter(response, retJson);
					return;					
				}
				//code == 1表示树形列表新增数据，主要用于前端树形列表数据控制
				retJson = ControllerUtil.getSuccessRetJson(record,"1","新增资源信息成功");
				//添加操作日志
				String remark ="新增资源信息，详情：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response,retJson);

	}

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
		// 判断当前节点是否为根节点，及子节点
		Resources resources = this.resourcesService.selectByPrimaryKey(id);

		if (resources == null) {			
			retJson = ControllerUtil.getFailRetJson("该资源不存在，请重新选择");
			getPrintWriter(response, retJson);
			return;
		}

		// 判断该节点是否有子节点
		if (this.resourcesService.isExistsChildRes(id)) {			
			retJson = ControllerUtil.getFailRetJson("该资源下面有子资源，不能删除");
			getPrintWriter(response, retJson);
			return;
		}

		// 判断该资源是否关联了权限
		if (this.resourcesService.isExistsAuthAndRes(id)) {		
			retJson = ControllerUtil.getFailRetJson("该资源下已设定了权限，不能删除");
			getPrintWriter(response, retJson);
			return;
		}

		int deleteCount = this.resourcesService.deleteResource(id);
		if(deleteCount > 0) {
			retJson = ControllerUtil.getSuccessRetJson("删除成功");
			//添加操作日志
			String remark = "删除";
			remark = remark + "资源，详情为：" + resources.toString();
			logOperService.insertLogOper(request, 1, remark);
		} else {
			retJson = ControllerUtil.getFailRetJson("删除失败");
		}	
		
		getPrintWriter(response, retJson);
	}

	/**
	 * index:跳转至资源编辑页面方法 <br/>
	 * 适用条件：登录成功，跳转至资源编辑页面<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response, Model model, Resources resources)
					throws IOException {
		model.addAttribute("action", "edit");
		if (resources == null || resources.getResourceId() == null
				|| resources.getResourceId().equals("")) {
			return LIST_EDIT;
		} else {
			String resourceParentName = "";
			resources = resourcesService.selectByPrimaryKey(resources
					.getResourceId());
			if (!ObjectUtils.isEmpty(resources.getResourceParentId())) {
				Resources presources = resourcesService
						.selectByPrimaryKey(resources.getResourceParentId());
				if (!ObjectUtils.isEmpty(presources)) {
					resourceParentName = presources.getResourceName();
				}
			}
			resources.setResourceParentName(resourceParentName);
		}
		model.addAttribute("resources", resources);
		return LIST_EDIT;
	}

	/**
	 *
	 * getResourceIcon:获取服务器资源图片信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午4:22:38
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/resourceIconList")
	public void getResourceIcon(HttpServletRequest request,
			HttpServletResponse response) {

		JsonListResult<String> result = new JsonListResult<String>();
		//返回结果
		String retJson = "";
		try {
			URL iconFilePath = this.getClass().getResource(
					"/images/platform/icons/business");
			File file = new File(iconFilePath.getFile());
			String[] tempList = file.list();
			List<String> iconList = Lists.newArrayList();
			if (!ObjectUtils.isEmpty(tempList)) {
				for (String iconStr : tempList) {
					iconList.add("/images/icons/business/" + iconStr);
				}
			}	
			result.setRows(iconList);
			retJson = JsonUtil.toJSON(result);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, retJson);
	}

	/**
	 *
	 * icon:资源图标列表. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午6:24:11
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/icon")
	public String icon(HttpServletRequest request,
			HttpServletResponse response, String dialogId, Model model)
			throws IOException {
		model.addAttribute("dialogId", dialogId);
		return ICON_LIST_EDIT;
	}

	/**
	 *
	 * index:跳转到资源列表页面. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 上午10:32:09
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
	 *
	 * saveResources:保存资源信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午1:40:45
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveResources")
	public void saveResources(HttpServletRequest request,
			HttpServletResponse response, Resources record) {
		// 判断当前是更新，还是添加
		String action = request.getParameter("action");
		if ("create".equals(action)) {
			this.createResources(request, response, record);
		} else if ("edit".equals(action)) {
			this.updateResources(request, response, record);
		}
	}

	/**
	 *
	 * selectAllResourcesInfo:查询所有的资源信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 上午10:22:19
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectAllResourcesInfo")
	public void selectAllResourcesInfo(HttpServletRequest request,
			HttpServletResponse response, Resources record) {

		JsonListResult<Resources> result = new JsonListResult<Resources>();
		//返回结果
		String retJson = "";
		try {
			//获取当前用户的资源列表
			User user=BaseUtil.getCurrentUser(request);
			
			List<Resources> list = resourcesService.selectAllResInfo(record,user);
			if (!ObjectUtils.isEmpty(list)) {
				result.setRows(list);
				result.setTotal(Long.valueOf(list.size()));
			}
	
			retJson = JsonUtil.toJSON(result).replaceAll(
					"resourceParentId", "_parentId");
			System.out.println(retJson);
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
	}

	/**
	 *
	 * selectAllResourcesForIndex:查询主页面资源数据 <br/>
	 * 适用条件：成功登录系统调用该方法，初始化主页面菜单<br/>
	 * 执行流程：前台AJAX调用<br/>
	 *
	 * @author:朱云飞 Date: 2015年8月17日 下午12:38:33
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectAllResourcesMapForIndex")
	public void selectAllResourcesMapForIndex(HttpServletRequest request,
			HttpServletResponse response) {
		//返回结果
		String retJson = "";
		try {
			User user = BaseUtil.getCurrentUser(request);
			List<Resources> list = resourcesService
					.selectAllResourcesForIndex(user);
			retJson = JsonUtil.toJSON(list);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
	}

	/**
	 *
	 * selectBtnListByResId:根据菜单id获取该菜单的按钮集合. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月28日 下午7:16:22
	 * @param request
	 * @param response
	 * @param resId
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectBtnListByResId")
	public void selectBtnListByResId(HttpServletRequest request,
			HttpServletResponse response, String resId) {

		JsonListResult<Resources> result = new JsonListResult<Resources>();
		//返回结果
		String retJson = "";
		try {
			User user = BaseUtil.getCurrentUser(request);	
			List<Resources> list = user.getBtnMapList().get(resId);
			if (!ObjectUtils.isEmpty(list)) {
				result.setRows(list);
				result.setTotal(Long.valueOf(list.size()));
			}	
			retJson = JsonUtil.toJSON(result);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);
	}

	@RequestMapping(value = "/setAuthResource")
	public String setAuthResource(HttpServletRequest request,
			HttpServletResponse response, String authorityId, Model model)
			throws IOException {
		model.addAttribute("authorityId", authorityId);
		return AUTH_RES_LIST_INDEX;
	}

	/**
	 *
	 * updateResources:更新资源信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午1:48:54
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	private void updateResources(HttpServletRequest request,
			HttpServletResponse response, Resources record) {
		//返回结果
		String retJson = "";
		try {
			record.setResourceParentId(null);
			int status = this.resourcesService.updateByPrimaryKeySelective(record);
			if (status < 1) {				
				retJson = ControllerUtil.getFailRetJson("更新失败");
				getPrintWriter(response, retJson);
				return;
			}else{
				retJson = ControllerUtil.getSuccessRetJson(record,"更新成功");
				//添加操作日志
				String remark ="更新资源信息，详情：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);
			}			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response,retJson);

	}

}
