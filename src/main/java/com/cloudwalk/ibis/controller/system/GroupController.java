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
import com.cloudwalk.ibis.model.system.Group;
import com.cloudwalk.ibis.model.system.GroupDto;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.system.AuthoritiesService;
import com.cloudwalk.ibis.service.system.GroupService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
 *
 * ClassName: GroupController <br/>
 * Description: 角色. <br/>
 * date: 2015年8月21日 下午2:13:16 <br/>
 *
 * @author 朱云飞
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/group")
public class GroupController extends BaseController {

	// 列表页面
	private final String LIST_INDEX = "platform/system/group";

	// 用户角色列表页面
	private final String USER_ROLE_LIST_INDEX = "platform/system/setUserRole";

	// 编辑页面
	private final String LIST_EDIT = "platform/perm/groupEdit";

	@Resource(name = "groupService")
	private GroupService groupService;

	@Resource(name = "logOperService")
	private LogOperService logOperService;

	@Resource(name = "authorityService")
	private AuthoritiesService authoritiesService;

	/**
	 * index:跳转至角色新增页面方法 <br/>
	 * 适用条件：登录成功，跳转至角色新增页面<br/>
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
			HttpServletResponse response, Model model, Group group)
					throws IOException {
		model.addAttribute("action", "create");
		return LIST_EDIT;
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
		
		//超级角色不能删除
		if ("1".equals(id)) {			
			retJson = ControllerUtil.getFailRetJson("超级角色不能删除");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			Group group = this.groupService.selectByPrimaryKey(id);
			int status = this.groupService.deleteGroup(id);	
			if (status == Constants.DELETE_FAILURED_REF) {				
				retJson = ControllerUtil.getFailRetJson("该角色下已经设定了相关人员，不可删除");
			} else if (status == Constants.DELETE_FAILURED) {				
				retJson = ControllerUtil.getFailRetJson("该角色下已经设定了权限，不可删除");
			} else {
				retJson = ControllerUtil.getSuccessRetJson("删除成功");
				// 添加操作日志
				String remark = "删除";
				remark = remark + "角色，详情为：" + group.toString();
				logOperService.insertLogOper(request, 1, remark);
			}		
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}

		getPrintWriter(response, retJson);
	}

	/**
	 * index:跳转至角色编辑页面方法 <br/>
	 * 适用条件：登录成功，跳转至角色编辑页面<br/>
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
			HttpServletResponse response, Model model, Group group)
					throws IOException {

		model.addAttribute("action", "edit");

		if (group == null || group.getGroupId() == null
				|| group.getGroupId().equals("")) {
			return LIST_EDIT;
		} else {
			group = groupService.selectByPrimaryKey(group.getGroupId());
		}

		model.addAttribute("group", group);
		return LIST_EDIT;
	}

	/**
	 * index:跳转至角色页面方法 <br/>
	 * 适用条件：登录成功，跳转至角色页面<br/>
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
	 * insertSelective:(新增机构信息). <br/>
	 * TODO(这里描述这个方法适用条件 – AJAX请求).<br/>
	 *
	 * @author:李戴月 Date: 2015年3月26日 上午11:12:53
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/insertSelective")
	public void insertSelective(HttpServletRequest request,
			HttpServletResponse response, Group group) {
		getPrintWriter(response, groupService.insertSelective(group));

	}

	/**
	 *
	 * saveGroup:保存角色信息（新增，更新）. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:22:37
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveGroup")
	public void saveGroup(HttpServletRequest request,
			HttpServletResponse response, GroupDto group) {
		
		//返回结果
		String retJson = "";
		try {
			//获取当前登录用户的信息
			User user=BaseUtil.getCurrentUser(request);
			if (ObjectUtils.isEmpty(group.getGroupId())){
				retJson = ControllerUtil.getFailRetJson("角色信息不能为空");
				getPrintWriter(response, retJson);
				return;
			}			
			
		   //角色中文名判空
		   if (group == null || ObjectUtils.isEmpty(group.getGroupCname())) {
			    retJson = ControllerUtil.getFailRetJson("角色中文名不能为空");
				getPrintWriter(response, retJson);
				return;
		   }
		   
		   //角色英文名判空
		   if (group == null || ObjectUtils.isEmpty(group.getGroupEname())) {
			    retJson = ControllerUtil.getFailRetJson("角色英文名不能为空");
				getPrintWriter(response, retJson);
				return;
		   }
		   
		   if(StringUtils.isBlank(group.getLegalOrgCode())) {
			 //设置当前法人机构
			   group.setLegalOrgCode(user.getLegalCode());
		   }		   
					
			// 验证角色名称是否为空，是否重复
			List<Group> groupList = this.groupService
					.selectGroupsByGroupNames(group);
			if (!ObjectUtils.isEmpty(groupList)) {					
				retJson = ControllerUtil.getFailRetJson("角色名称不能重复");
				getPrintWriter(response, retJson);
				return;
			} 
			
			//更新角色
			int updateCount = this.groupService.saveGroups(group,user);
			if(updateCount > 0) {
				retJson = ControllerUtil.getSuccessRetJson("保存角色成功");
				// 添加操作日志
				String remark = "保存角色信息，详情：" + group.toString();
				logOperService.insertLogOper(request, 1, remark);
			} else {
				retJson = ControllerUtil.getFailRetJson("保存角色失败");
			}	
			
		} catch(Exception e) {
			log.error("保存角色信息发生异常",e);
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, retJson);

	}

	/**
	 *
	 * saveGroupAuthority:设置角色与权限的关系. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月24日 下午3:45:57
	 * @param request
	 * @param response
	 * @param id
	 * @param contentId
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveGroupAuthority")
	public void saveGroupAuthority(HttpServletRequest request,
			HttpServletResponse response, String id, String contentId) {

		//返回结果
		String retJson = "";
		if (ObjectUtils.isEmpty(id) ) {
			retJson = ControllerUtil.getFailRetJson("角色ID不能为空");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			// 设置角色与权限的关系
			int saveCount = this.groupService.saveGroupAuthority(id, contentId);
			if(saveCount > 0) {
				retJson = ControllerUtil.getSuccessRetJson("保存成功");		
				// 添加操作日志
				Group group = this.groupService.selectByPrimaryKey(id);
				Authorities authorities = null;
				String remark = "为角色：" + group.getGroupCname() + ",赋予权限：";
				if(!"".contains(contentId)&&!StringUtils.isEmpty(contentId)){
					String[] authorArray = contentId.split(",");
					for (String authorId : authorArray) {
						authorities = this.authoritiesService
								.selectByPrimaryKey(authorId);
						remark = remark + authorities.getAuthorityName() + " ";
					}
				}
				logOperService.insertLogOper(request, 1, remark);
			} else {
				retJson = ControllerUtil.getFailRetJson("保存失败");
			}		
			
		} catch(Exception e) {
			log.error(e);
			retJson = ControllerUtil.getExceRetJson(e);	
		}

		getPrintWriter(response, retJson);
	}

	/**
	 *
	 * selectAllGroup:查询所有的角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月21日 下午2:37:20
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectAllGroup")
	public void selectGroupByPage(HttpServletRequest request,
			HttpServletResponse response, Group record) {

		JsonListResult<Group> result = new JsonListResult<Group>();
		//返回结果
		String retJson = "";
		try {
			//获取当前登录用户信息
			User user=BaseUtil.getCurrentUser(request);
			record.setLegalOrgCode(user.getLegalOrgCode());
			
			List<Group> list = this.groupService.searchAllGroupByUserId(record);	
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
	 *
	 * selectGroupByPage:分页查询角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午2:20:22
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectGroupByPage")
	public void selectGroupByPage(HttpServletRequest request,
			HttpServletResponse response, Group record, PageInfo pageInfo) {

		this.setPageInfo(request, pageInfo);
		JsonListResult<Group> result = new JsonListResult<Group>();
		//返回结果
		String retJson = "";
		try {
			// 设置分页查询
			Map<String, Object> map = new HashMap<String, Object>();
			// 设置分页信息
			map.put("obj", record);
			map.put("page", pageInfo);
	
			List<Group> list = this.groupService.selectGroupByPage(map);
	
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
	 * index:跳转至用户角色页面方法 <br/>
	 * 适用条件：登录成功，跳转至角色页面<br/>
	 * 执行流程：AJAX调用<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/setUserRole")
	public String setUserRole(HttpServletRequest request,
			HttpServletResponse response, String userId, Model model)
			throws IOException {
		model.addAttribute("userId", userId);
		return USER_ROLE_LIST_INDEX;
	}

}
