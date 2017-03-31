package com.cloudwalk.ibis.controller.system;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import com.cloudwalk.ibis.model.system.Group;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.system.GroupService;
import com.cloudwalk.ibis.service.system.LogOperService;
import com.cloudwalk.ibis.service.system.UserService;

/**
 *
 * ClassName: UserController <br/>
 * Description: 用户. <br/>
 * date: 2015年8月19日 下午8:33:53 <br/>
 *
 * @author Jackson He
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	// 列表页面
	private final String LIST_INDEX = "platform/system/user";
	// 编辑页面
	private final String LIST_EDIT = "platform/system/userEdit";

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "groupService")
	private GroupService groupService;

	@Resource(name = "logOperService")
	private LogOperService logOperService;

	/**
	 * index:跳转至用户新增页面方法 <br/>
	 * 适用条件：登录成功，跳转至用户新增页面<br/>
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
			HttpServletResponse response, Model model, User user)
					throws IOException {
		String orgName = request.getParameter("orgName");
		String deptName = request.getParameter("deptName");
		if (!ObjectUtils.isEmpty(orgName)) {
			orgName = URLDecoder.decode(orgName, "UTF-8");
		}
		if (!ObjectUtils.isEmpty(deptName)) {
			deptName = URLDecoder.decode(deptName, "UTF-8");
		}
		user.setOrgName(orgName);
		user.setDeptName(deptName);
		model.addAttribute("user", user);
		model.addAttribute("action", "create");
		return LIST_EDIT;
	}

	/**
	 *
	 * createUser:新增一个用户. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:02:28
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	private void createUser(HttpServletRequest request,
			HttpServletResponse response, User record) {

		//返回结果
		String retJson = "";
		try {
			//获取当前登录用户的信息
			User user=BaseUtil.getCurrentUser(request);
			int retStatus = this.userService.insertSelective(record,user);
			if (retStatus < 1) {			
				retJson = ControllerUtil.getFailRetJson("新增用户信息失败");
				getPrintWriter(response, retJson);
				return;
			} else if (retStatus == Constants.INSERT_FAILURED_DATA_EXIST) {			
				retJson = ControllerUtil.getFailRetJson("工号已存在，请重新输入");
				getPrintWriter(response, retJson);
				return;
			}
			
			retJson = ControllerUtil.getSuccessRetJson(record, "新增用户成功");
			// 添加操作日志
			String remark = "新增";
			remark = remark + "用户信息，详情：" + record.toString();
			logOperService.insertLogOper(request, 1, remark);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, retJson);

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
		
		//超级用户不能删除
		if ("1".equals(id)) {			
			retJson = ControllerUtil.getFailRetJson("超级用户不能删除");
			getPrintWriter(response, retJson);
			return;
		}
		
		//获取当前登录用户的信息
		User loginUser=BaseUtil.getCurrentUser(request);
		if(loginUser.getUserId().equals(id)) {
			retJson = ControllerUtil.getFailRetJson("不能对自己进行删除");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			User user = this.userService.selectByPrimaryKey(id);
	
			if (user == null) {			
				retJson = ControllerUtil.getFailRetJson("该用户不存在");
				getPrintWriter(response, retJson);
				return;
			}
	
			// 如果用户设置了其他的相关资料不能删除
			int status=this.userService.deleteByPrimaryKey(id);
	
			// 添加操作日志
			if (status == Constants.DELETE_FAILURED_REF) {			
				retJson = ControllerUtil.getFailRetJson("该人员关联了角色，不可删除");
			} else if (status == Constants.DELETE_FAILURED) {
				retJson = ControllerUtil.getFailRetJson("删除失败");			
			} else {
				retJson = ControllerUtil.getSuccessRetJson("删除成功");		
				// 添加操作日志
				String remark = "删除";
				remark = remark + "用户，详情为：" + user.toString();
				logOperService.insertLogOper(request, 1, remark);
			}		
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		}

		getPrintWriter(response, retJson);
	}

	/**
	 * index:跳转至用户编辑页面方法 <br/>
	 * 适用条件：登录成功，跳转至用户编辑页面<br/>
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
			HttpServletResponse response, Model model, User user)
					throws IOException {

		model.addAttribute("action", "edit");

		if (user == null || user.getUserId() == null
				|| user.getUserId().equals("")) {
			return LIST_EDIT;
		} else {
			user = userService.selectByPrimaryKey(user.getUserId());
		}

		model.addAttribute("user", user);
		return LIST_EDIT;
	}

	/**
	 * index:跳转至用户页面方法 <br/>
	 * 适用条件：登录成功，跳转至用户页面<br/>
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
		
		model.addAttribute("deptCode", user.getDeptCode());
		return LIST_INDEX;
	}

	/**
	 *
	 * resetUserPwd:重置用户密码. <br/>
	 *
	 * @author:朱云飞 Date: 2015年9月1日 下午4:13:07
	 * @param request
	 * @param response
	 * @param id
	 * @param contentId
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/resetPwd")
	public void resetUserPwd(HttpServletRequest request,
			HttpServletResponse response, User user) {

		//返回结果
		String retJson = "";
		try {		
			
			int ret = this.userService.updateByPrimaryKeySelective(user);			
			if(ret > 0) {
				retJson = ControllerUtil.getSuccessRetJson("重置密码成功");	
				// 添加操作日志
				String remark = "重置密码，";
				remark = remark + "用户信息，详情：" + user.toString();
				logOperService.insertLogOper(request, 1, remark);
			} else {
				retJson = ControllerUtil.getSuccessRetJson("重置密码失败");	
			}	
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		
		getPrintWriter(response, retJson);
	}

	/**
	 *
	 * saveuser:保存用户信息（新增，更新）. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:22:37
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveUser")
	public void saveUser(HttpServletRequest request,
			HttpServletResponse response, User record) {
		// 判断当前是更新，还是添加
		String action = request.getParameter("action");
		if ("create".equals(action)) {
			this.createUser(request, response, record);
		} else if ("edit".equals(action)) {
			this.updateUser(request, response, record);
		}		
	}

	/**
	 *
	 * selectUserByPage:分页查询用户信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午2:20:22
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectUserByPage")
	public void selectUserByPage(HttpServletRequest request,
			HttpServletResponse response, User record, PageInfo pageInfo) {

		this.setPageInfo(request, pageInfo);
		JsonListResult<User> result = new JsonListResult<User>();
		//返回结果
		String retJson = "";
		try {
			// 设置分页查询
			Map<String, Object> map = new HashMap<String, Object>();
			// 设置分页信息
			map.put("obj", record);
			map.put("page", pageInfo);
	
			List<User> list = this.userService.selectUserByPage(map ,record);
	
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
	 * 设置用户与角色的关系
	 * @param request
	 * @param response
	 * @param id
	 * @param contentId
	 */
	@RequestMapping(value = "/setUserRole")
	public void setUserRole(HttpServletRequest request,
			HttpServletResponse response, String id, String contentId) {

		//返回结果
		String retJson = "";
		try {

			if (ObjectUtils.isEmpty(id) ) {
				retJson = ControllerUtil.getFailRetJson("用户ID不能为空");
				getPrintWriter(response, retJson);
				return;
			}
			
			//获取当前登录用户的信息
			User loginUser=BaseUtil.getCurrentUser(request);
			if(loginUser.getUserId().equals(id)) {
				retJson = ControllerUtil.getFailRetJson("不能对自己进行设定角色");
				getPrintWriter(response, retJson);
				return;
			}
	
			// 设置用户与角色的关系
			int saveCount = this.userService.saveUserRoles(id, contentId);
			
			if(saveCount > 0) {				
				retJson = ControllerUtil.getSuccessRetJson("设置用户与角色的关系成功");
				
				// 添加操作日志
				User user = this.userService.selectByPrimaryKey(id);
				Group group = null;
				String remark = "为用户：" + user.getUserName() + "[" + user.getWorkCode()
						+ "]" + ",赋予角色：";
				if(!"".endsWith(contentId)&&!StringUtils.isEmpty(contentId)){
					String[] roleIdArray = contentId.split(",");
					for (String roleId : roleIdArray) {
						group = this.groupService.selectByPrimaryKey(roleId);
						remark = remark + group.getGroupCname() + " ";
					}
				}				
				logOperService.insertLogOper(request, 1, remark);
				
			} else {
				retJson = ControllerUtil.getFailRetJson("设置用户与角色的关系失败");
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		getPrintWriter(response, retJson);
	}

	/**
	 *
	 * updateUser:更新用户信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:05:38
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	private void updateUser(HttpServletRequest request,
			HttpServletResponse response, User record) {
		//返回结果
		String retJson = "";
		try {
			int status = this.userService.updateByPrimaryKeySelective(record);
			if (status < 1) {				
				retJson = ControllerUtil.getFailRetJson("更新失败");
			} else {
				retJson = ControllerUtil.getSuccessRetJson("更新成功");
				// 添加操作日志
				String remark = "更新";
				remark = remark + "用户信息，详情：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);
			}
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		
		getPrintWriter(response, retJson);

	}

	/**
	 * 用户首次登录时，强制修改密码。
	 * @param request
	 * @param response
	 * @param user
	 */
	@RequestMapping(value = "/initPwd")
	public void initPwd(HttpServletRequest request,
			HttpServletResponse response, String workCode, String userPwd) {

		//返回结果
		String retJson = "";
		try {
			User user = new User();
			user.setWorkCode(workCode);
			List<User> listUser = this.userService.selectAll(user);
			if(CollectionUtils.isEmpty(listUser)){
				retJson = ControllerUtil.getSuccessRetJson("该用户工号不存在！");
			}else if(listUser.size()>1){
				retJson = ControllerUtil.getSuccessRetJson("该用户工号存在多个！");
			}else{
				User u = listUser.get(0);
				u.setUserPwd(userPwd);
				u.setUserStatus(1);//修改密码后设置为有效		
				int ret = this.userService.updateByPrimaryKeySelective(u);			
				if(ret > 0) {
					retJson = ControllerUtil.getSuccessRetJson("密码修改成功");	
					// 添加操作日志
					String remark = "用户首次登录强制修改密码，";
					remark = remark + "用户信息，详情：" + user.toString();
					logOperService.insertLogOper(request, 1, remark);
				} else {
					retJson = ControllerUtil.getSuccessRetJson("修改密码失败");	
				}	
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		
		getPrintWriter(response, retJson);
	}	
	
}
