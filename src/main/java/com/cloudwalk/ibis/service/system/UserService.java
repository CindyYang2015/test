package com.cloudwalk.ibis.service.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.Md5;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.system.Group;
import com.cloudwalk.ibis.model.system.Organization;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.model.system.UserGroupRefKey;
import com.cloudwalk.ibis.repository.system.GroupMapper;
import com.cloudwalk.ibis.repository.system.UserExtendMapper;
import com.cloudwalk.ibis.repository.system.UserGroupRefMapper;
import com.cloudwalk.ibis.repository.system.UserMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("userService")
public class UserService {
	@Resource(name = "userMapper")
	private UserMapper userMapper;

	@Resource(name = "userExtendMapper")
	private UserExtendMapper userExtendMapper;

	@Resource(name = "userGroupRefMapper")
	private UserGroupRefMapper userGroupRefMapper;

	@Resource(name = "groupMapper")
	private GroupMapper groupMapper;

	@Resource(name = "resourcesService")
	private ResourcesService resourcesService;
	
	@Resource(name = "departmentService")
	private DepartmentService departmentService;
	
	@Resource(name = "organizationService")
	private OrganizationService organizationService;
	
	@Resource(name = "logOperService")
	private LogOperService logOperService;
	

	// for activiti
	/*
	 * @Resource(name = "identityService") private IdentityService
	 * identityService;
	 */
	// @Resource(name = "organizationService")
	// private OrganizationService organizationService;

	/**
	 * 从ACTIVITI 删除用户
	 *
	 * @author: Jackson He
	 * @param userId
	 *            用户id
	 */
	/*
	 * private void deleteActivitiUser(String userId) { // 删除用户的membership
	 * List<org.activiti.engine.identity.Group> activitiGroups = identityService
	 * .createGroupQuery().groupMember(userId).list(); for
	 * (org.activiti.engine.identity.Group group : activitiGroups) { //
	 * logger.debug("delete group from activit: {}", //
	 * ToStringBuilder.reflectionToString(group));
	 * identityService.deleteMembership(userId, group.getId()); } // 删除用户
	 * identityService.deleteUser(userId); }
	 */

	/**
	 *
	 * deleteByPrimaryKey:(根据用户的ID删除用户(批量删除)). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月23日 上午9:41:31
	 * @param userId
	 * @return 受影响的记录
	 * @throws DataAccessException
	 * @since JDK 1.7
	 */
	public int deleteByPrimaryKey(String userId) throws DataAccessException {
		// 判断用户角色表中是否有记录
		UserGroupRefKey userGroupRefKey = new UserGroupRefKey();
		userGroupRefKey.setUserId(userId);
		List<UserGroupRefKey> userGroupRefKeyList=userGroupRefMapper.searchAll(userGroupRefKey);
		
		if (!ObjectUtils.isEmpty(userGroupRefKeyList)) {
			return Constants.DELETE_FAILURED_REF;
		}
		if (userMapper.deleteByPrimaryKey(userId) > 0) {
			return Constants.DELETE_SUCCESSED; // 删除成功
		} else {
			return Constants.DELETE_FAILURED;// 删除失败
		}
	}

	/**
	 *
	 * insertSelective:(添加用户). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月23日 上午9:43:32
	 * @param record
	 * @return 受影响的行数
	 * @throws DataAccessException
	 * @since JDK 1.7
	 */
	public int insertSelective(User record,User user)
			throws DataAccessException {
		if (record == null) {
			return Constants.PARAMETER_ERROR;
		}
		//层层查询员工的：机构级别(数据字典):员工的部门编号--》机构代码--》机构级别(对应字典)
		String departmentCode=record.getDeptCode();
		String orgCodePath=departmentService.selectByPrimaryKey(departmentCode).getOrgCode();
		String[] orgCodePaths=orgCodePath.split("@");
		Organization organization=new Organization();
		String legalOrgCode="";
		for(int i=orgCodePaths.length-1;i>-1;i--){
			organization=organizationService.selectByPrimaryKey(orgCodePaths[i]);
			int legalStatus=organization.getLegalStatus();
			if(legalStatus==0){
				continue;
			}else if(legalStatus==1){
				legalOrgCode=organization.getOrgCodePath();
				break;
			}
		}
		//List<String> orgCodePathsList=Arrays.asList(orgCodePaths);
	
		
		/*for(int i=orgCodePathsList.size();i>0;i--){
			String a=orgCodePathsList.get(i);
			organization=organizationService.selectByPrimaryKey(orgCodePathsList.get(i));
			int legalStatus=organization.getLegalStatus();
			if(legalStatus=='0'){
				continue;
			}else if(legalStatus=='1'){
				legalOrgCode=organization.getOrgCodePath();
			}
			
		}*/
		//String[] orgCodes=legalOrgCode.split("@");
		String orgCode=orgCodePaths[orgCodePaths.length-1];
		String orgLevel=organizationService.selectByPrimaryKey(orgCode).getOrgLevel();
		// 检查工号是否存在
		User tmpRecord = new User();
		tmpRecord.setWorkCode(record.getWorkCode());
		int retCount = userMapper.selectCount(tmpRecord);
		// TODO 用户的法人机构代码：查询其所属机构，如果机构是法人机构则将机构编码存入 如果不是则再查询其父机构
		//Department department=departmentService.selectByPrimaryKey(record.getDeptCode());
		
		//Organization organization=organizationService.selectByOrgCodePath(department.getOrgCode());
		//legalOrgCode=selectLegalOrgCode(organization);
		
		if (retCount == 0) {
			record.setCreateDate(new Date());
			record.setLegalOrgCode(legalOrgCode);
			record.setUserPwd(Md5.md5(Constants.USER_DEFAULT_PWD).toLowerCase());
			record.setUserStatus(3);//设置为3，表示，该用户首次登陆时需要强制修改密码
			record.setUserLoginName(record.getWorkCode());
			record.setOrgLevel(orgLevel);
			retCount = userMapper.insertSelective(record);
			if (retCount > 0) {
				return Constants.INSERT_SUCCESSED;
			} else {
				return Constants.INSERT_FAILURED;
			}

		} else {
			// 返回3表示工号已存在
			return Constants.INSERT_FAILURED_DATA_EXIST;
		}
	}
	
	/**
	 * 
	 * selectLegalOrgCode:(查询法人机构代码).
	 * @author:胡钰鑫   Date: 2016年10月14日 下午4:55:50
	 * @param user
	 * @return
	 * @since JDK 1.7
	 */
	public String selectLegalOrgCode(Organization organization){
		String legalOrgCode=null;
		
		if(organization.getLegalStatus()==1){
			//如果用户所属机构为法人机构
			legalOrgCode=organization.getOrgCode();
		}else if(organization.getLegalStatus()==0){
			//如果用户所属机构不为法人机构，则递归
			organization=organizationService.selectByPrimaryKey(organization.getParentCode());
			legalOrgCode=selectLegalOrgCode(organization);
		}
		return legalOrgCode;
	}
	 
	/**
	 * 用户登录
	 * @param request
	 * @param user
	 * @throws ServiceException
	 */
	public void login(HttpServletRequest request,
			User user) throws ServiceException {
		
		//登录用户对象
		User record = new User();
		record.setWorkCode(user.getWorkCode());

		// 根据前台信息查询后台用户信息
		List<User> list = this.userMapper
				.selectAll(record);
		
		// 根据用户名和密码未查询到值
		if (null == list || list.size() == 0) {
			throw new ServiceException("登录名或密码错误");
		} else {
			// 根据用户名和密码查询到多个值
			if (list.size() > 1) {
				throw new ServiceException("用户："+record.getWorkCode()+"记录重复，请与管理员联系");
			} else {
				User currentUser = list.get(0);
				
				// 比较密码是否正确
				if (currentUser.getUserPwd() != null
						&& currentUser.getUserPwd().equals(user.getUserPwd())) {
					// 获取用户的部门信息
					User u = this.userExtendMapper
							.selectByPrimaryKey(currentUser.getUserId());
					if (u != null) {
						currentUser.setDeptCode(u.getDeptCode());
						currentUser.setDeptName(u.getDeptName());
						currentUser.setOrgCode(u.getOrgCode());
						currentUser.setOrgAName(u.getOrgAName());
						currentUser.setOrgName(u.getOrgName());
						currentUser.setLegalOrgCode(u.getLegalOrgCode());
						currentUser.setOrgCodePath(u.getOrgCodePath());
					}
					// 将用户放入session
					BaseUtil.setCurrentUser(request, currentUser);
					resourcesService.setSessionUserAuth(currentUser);	
					
					// 检查用户状态，首次登陆抛出异常，要求强制修改初始密码
					if(3 == currentUser.getUserStatus()){
						throw new ServiceException("用户首次登陆需要强制修改密码");
					}	
					
				} else {					
					throw new ServiceException("登录名或密码错误"); 
				}
			}
		}
	}

	/**
	 * 新增一个用户到Activiti {@link org.activiti.engine.identity.User}
	 *
	 * @author: Jackson He
	 * @param customUser
	 *            用户对象
	 */
	/*
	 * private void newActivitiUser(User
	 * customUser) { // 添加，保存 org.activiti.engine.identity.User activitiUser =
	 * identityService .newUser(customUser.getUserId());
	 * ActivitiUtils.cloneActivitiUser(customUser, activitiUser);
	 * identityService.saveUser(activitiUser); }
	 */

	/**
	 * 添加一个用户到Activiti {@link org.activiti.engine.identity.User}
	 *
	 * @author: Jackson He
	 * @param customUser
	 *            用户对象
	 */
	/*
	 * private void saveActivitiUser(User
	 * customUser) { UserQuery userQuery = identityService.createUserQuery();
	 * 
	 * List<User> activitiUsers = userQuery.userId(customUser.getUserId())
	 * .list();
	 * 
	 * if (activitiUsers.size() == 1) { updateActivitiUser(customUser,
	 * activitiUsers.get(0)); } else if (activitiUsers.size() > 1) { String
	 * errorMsg = "发现重复用户：id=" + customUser.getUserId(); //
	 * logger.error(errorMsg); throw new RuntimeException(errorMsg); } else {
	 * newActivitiUser(customUser); } }
	 */

	/**
	 *
	 * setUserRoles:设置用户角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月21日 下午3:29:59
	 * @param userId
	 * @param roleIds
	 * @return
	 * @since JDK 1.7
	 */
	public int saveUserRoles(String userId, String roleIds) {
		// 删除用户与角色的信息
		this.userGroupRefMapper.deleteByUserId(userId);
		// 添加用户与角色的配置
		if("".endsWith(roleIds)||StringUtils.isEmpty(roleIds)){
			return 0;
		}
		String[] roleIdArray = roleIds.split(",");
		List<UserGroupRefKey> items = Lists.newArrayList();
		for (String roleId : roleIdArray) {
			UserGroupRefKey userGroupRefKey = new UserGroupRefKey();
			userGroupRefKey.setUserId(userId);
			userGroupRefKey.setGroupId(roleId);
			items.add(userGroupRefKey);
		}
		return this.userGroupRefMapper.insertSelectiveBatch(items);
	}

	/**
	 *
	 * selectAll:(查询符合条件的的集合). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月23日 上午10:11:56
	 * @param record
	 * @return
	 * @throws DataAccessException
	 * @since JDK 1.7
	 */
	public List<User> selectAll(
			User record)
					throws DataAccessException {
		return userMapper.selectAll(record);
	}

	/**
	 *
	 * selectAllByPage:(分页查询用户信息). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月23日 上午10:11:56
	 * @param record
	 * @return
	 * @throws DataAccessException
	 * @since JDK 1.7
	 */
	public List<User> selectAllByPage(
			Map<String, Object> map) throws DataAccessException {
		return userMapper.selectAllByPage(map);
	}

	/**
	 *
	 * selectByPrimaryKey:(根据用户id(主键)查询指定用户). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月23日 上午10:13:06
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 * @since JDK 1.7
	 */
	public User selectByPrimaryKey(String userId)
			throws DataAccessException {
		return this.userExtendMapper.selectByPrimaryKey(userId);
	}

	/**
	 *
	 * selectUserByPage:分页查询用户信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午8:48:39
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<User> selectUserByPage(
			Map<String, Object> map,User user) {
		
		//如果点击的是机构节点
		String orgCode=user.getOrgCode();
		if(orgCode!=null&&!"".equals(orgCode)){
			//获取机构全路径
			String orgCodePath=organizationService.selectByPrimaryKey(orgCode).getOrgCodePath();
			user.setOrgCodePath(orgCodePath);
			user.setOrgCode("");
			map.put("obj", user);
		}
		// 获取用户信息
		List<User> users = this.userExtendMapper
				.selectAllByPage(map);
		if (ObjectUtils.isEmpty(users)) {
			return users;
		}
		// 设置用户角色信息
		List<String> userIdsList = Lists.newArrayList();
		Map<String, User> userMap = Maps
				.newHashMap();
		for (User u : users) {
			userIdsList.add(u.getUserId());
			userMap.put(u.getUserId(), u);
		}
		// 获取用户的角色信息
		List<Group> groups = this.groupMapper
				.selectUserGroupsByUserIds(userIdsList);
		if (!ObjectUtils.isEmpty(groups)) {
			for (Group g : groups) {
				if (userMap.containsKey(g.getUserId())) {
					User tempuUser = userMap.get(g
							.getUserId());
					StringBuffer roleName = new StringBuffer(
							tempuUser.getRoleName() == null ? ""
									: tempuUser.getRoleName());
					if (!ObjectUtils.isEmpty(roleName.toString())
							&& roleName.length() > 0) {
						roleName.append(",");
					}
					roleName.append(g.getGroupCname());
					tempuUser.setRoleName(roleName.toString());
				}
			}
		}
		return users;
	}

	/**
	 * 更新一个用户到Activiti {@link org.activiti.engine.identity.User}
	 *
	 * @author: Jackson He
	 * @param customUser
	 *            用户对象
	 * @param activitiUser
	 *            Activiti用户对象
	 */
	/*
	 * private void updateActivitiUser( User
	 * customUser, org.activiti.engine.identity.User activitiUser) { // 保存
	 * ActivitiUtils.cloneActivitiUser(customUser, activitiUser);
	 * identityService.saveUser(activitiUser); }
	 */

	/**
	 *
	 * updateByPrimaryKeySelective:(修改指定用户的信息). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月23日 上午10:14:06
	 * @param record
	 * @return
	 * @throws DataAccessException
	 * @since JDK 1.7
	 */
	public int updateByPrimaryKeySelective(
			User record)
					throws DataAccessException {
		//更新状态
		int ret = 0;
		if (record == null) return ret;
		record.setUpdateDate(new Date());
		ret = userMapper.updateByPrimaryKeySelective(record);
		return ret;
	}
}
