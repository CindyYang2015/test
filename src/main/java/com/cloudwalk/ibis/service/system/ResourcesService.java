/**
 * Project Name:fssc
 * File Name:ResourcesService.java
 * Package Name:com.linc.fssc.service.identity
 * Date:2015年3月26日 上午11:10:24
 * Copyright @ 2015 上海企垠信息科技有限公司  All Rights Reserved.
 *
 */

package com.cloudwalk.ibis.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.system.AuthResRefKey;
import com.cloudwalk.ibis.model.system.GroupAuthRefKey;
import com.cloudwalk.ibis.model.system.Resources;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.repository.system.AuthResRefMapper;
import com.cloudwalk.ibis.repository.system.GroupAuthRefMapper;
import com.cloudwalk.ibis.repository.system.ResourcesMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * ResourcesService <br/>
 * Description: 资源表业务层. <br/>
 * date: 2015年8月17日 下午12:47:02 <br/>
 *
 * @author zhuyf
 * @version
 * @since JDK 1.7
 */
@Service("resourcesService")
public class ResourcesService {
	@Resource(name = "resourcesMapper")
	private ResourcesMapper resourcesMapper;

	@Resource(name = "authResRefMapper")
	private AuthResRefMapper authResRefMapper;
	
	@Resource(name = "groupAuthRefMapper")
	private GroupAuthRefMapper groupAuthRefMapper;

		
	/**
	 *
	 * deleteResource:根据资源id删除资源. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午3:28:51
	 * @param resId
	 * @return
	 * @since JDK 1.7
	 */
	public int deleteResource(String resId) {
		return this.resourcesMapper.deleteByPrimaryKey(resId);
	}

	/**
	 *
	 * findChildRen:在集合里面查找节点的子节点 <br/>
	 * 适用条件：查找元素子节点<br/>
	 * 执行流程：内部调用<br/>
	 *
	 * @author:朱云飞 Date: 2015年8月17日 下午12:57:36
	 * @param navChildrenList
	 * @param res
	 * @return
	 * @since JDK 1.7
	 */
	private Map<String, List<Resources>> findChildRen(
			List<Resources> navChildrenList, Resources res) {
		Map<String, List<Resources>> map = new HashMap<String, List<Resources>>();
		// 传入节点的子节点集合
		List<Resources> resChildList = new ArrayList<Resources>();
		// 剩下的子节点集合
		List<Resources> remainChildList = new ArrayList<Resources>();
		// 传入的节点集合
		for (Resources resource : navChildrenList) {
			// 找到子节点
			if (res.getResourceId().equals(resource.getResourceParentId())) {
				resChildList.add(resource);
			} else {
				remainChildList.add(resource);
			}
		}
		// 传入节点的子节点集合
		map.put("resChildList", resChildList);
		// 剩下的子节点集合
		map.put("remainChildList", remainChildList);
		return map;
	}

	/**
	 *
	 * insertSelective:新增资源. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午1:44:56
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public int insertSelective(Resources record) {
		if (record == null) {
			return Constants.PARAMETER_ERROR;
		}
		record.setResourceId(ObjectUtils.createUUID());
		if (this.resourcesMapper.insertSelective(record) > 0) {
			return Constants.INSERT_SUCCESSED;
		} else {
			return Constants.INSERT_FAILURED;
		}
	}

	/**
	 *
	 * isExistsAuthAndRes:判断该资源是否关联了权限. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午3:26:09
	 * @param resId
	 * @return
	 * @since JDK 1.7
	 */
	public boolean isExistsAuthAndRes(String resId) {
		AuthResRefKey authResRefKey = new AuthResRefKey();
		authResRefKey.setResourceId(resId);
		int counts = authResRefMapper.selectCount(authResRefKey);
		if (counts > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 * isExistsChildRes:查询该资源下是否存在子资源. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午3:20:48
	 * @param resId
	 * @return
	 * @since JDK 1.7
	 */
	public boolean isExistsChildRes(String resId) {
		return this.resourcesMapper.isExistChildRes(resId) > 0 ? true : false;
	}

	/**
	 *
	 * recursionNavResList:递归查找资源子节点 <br/>
	 * 适用条件：查询树形数据<br/>
	 * 执行流程：内部调用<br/>
	 *
	 * @author:朱云飞 Date: 2015年8月17日 下午12:56:36
	 * @param navChildrenList
	 * @param res
	 * @param btnMapListMap
	 *            按钮集合
	 * @since JDK 1.7
	 */
	private void recursionNavResList(List<Resources> navChildrenList,
			Resources res, Map<String, List<Resources>> btnMapListMap) {
		// 查找子元素
		Map<String, List<Resources>> map = findChildRen(navChildrenList,
				res);
		List<Resources> resChildList = map.get("resChildList");
		List<Resources> remainChildList = map.get("remainChildList");

		// 设置菜单的按钮集合
		if ("1".equals(res.getResourceType())
				&& !ObjectUtils.isEmpty(res.getResourceUrl())
				&& !ObjectUtils.isEmpty(resChildList)) {
			// 为当前资源菜单设置按钮集合
			btnMapListMap.put(res.getResourceId(), resChildList);
		}

		if (resChildList.size() > 0) {
			// 给元素添加子节点集合
			res.setChildren(resChildList);
			// 遍历子节点集合
			for (int i = 0; i < resChildList.size(); i++) {
				// 递归调用
				recursionNavResList(remainChildList, resChildList.get(i),
						btnMapListMap);
			}
		}
	}

	/**
	 *
	 * selectAllByAuth:根据权限查询资源 <br/>
	 * 适用条件：根据权限查询资源<br/>
	 * 执行流程：controller层调用<br/>
	 *
	 * @author:朱云飞 Date: 2015年8月17日 下午12:53:00
	 * @param user
	 * @return
	 * @since JDK 1.7
	 */
	public List<Resources> selectAllByAuth(User user) {
		return resourcesMapper.selectAllByAuth(user);
	}

	/**
	 *
	 * selectAllResInfo:查询所有资源信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 上午10:15:23
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public List<Resources> selectAllResInfo(Resources record,User user) {
		List<AuthResRefKey> authResRefKeyList = Lists.newArrayList();
		if (record != null && !ObjectUtils.isEmpty(record.getAuthorityId())) {
			AuthResRefKey authResRefKey = new AuthResRefKey();
			//权限与资源关系表：authorityId
			authResRefKey.setAuthorityId(record.getAuthorityId());
			authResRefKeyList = this.authResRefMapper.selectAll(authResRefKey);
		}
		List<Resources> newList = new ArrayList<Resources>();
		//当前登录用户拥有的所有资源，也就是可分配资源
	
		//List<Resources> list =resourcesList; //resourcesMapper.selectAll(record);
		//获取当前登陆用户的资源信息		
		List<Resources> parentList = this.resourcesMapper.selectAllByAuth(user);		
		
		for (Resources parentResources : parentList) {
			//如果查询出来的所有资源与改权限的资源相同的，则默认勾选
			for (AuthResRefKey key : authResRefKeyList) {
				if (key.getResourceId().equals(parentResources.getResourceId())) {
					// 已勾选
					parentResources.setIssys(99);
					
				}
			}
			newList.add(parentResources);
		}
		return newList;
	}	
	

	/**
	 *
	 * selectAllResourcesForIndex:查找主页面的菜单资源 <br/>
	 * 适用条件：查询主页面的树形资源<br/>
	 * 执行流程：CONTROLL层调用<br/>
	 *
	 * @author:朱云飞 Date: 2015年8月17日 下午12:54:56
	 * @param user
	 * @return
	 * @since JDK 1.7
	 */
	public List<Resources> selectAllResourcesForIndex(User user) {

		if (user == null) {
			return null;
		}

		// 判断当前session是否存在该资源列表
		List<Resources> sessionResList = user.getResourcesList();
		if (!ObjectUtils.isEmpty(sessionResList)) {
			return sessionResList;
		} else {
			return null;
		}

	}

	public Resources selectByPrimaryKey(String resourceId) {
		return this.resourcesMapper.selectByPrimaryKey(resourceId);
	}

	/**
	 * setChildren:添加子节点 <br/>
	 * 适用条件：给导航栏目录添加子节点<br/>
	 * 执行流程：内部调用<br/>
	 *
	 * @author:焦少平 Date: 2015年4月24日 下午7:26:02
	 * @param navigateList
	 *            导航菜单数据
	 * @param navChildrenList
	 *            子节点集合
	 * @param btnMapListMap
	 *            功能菜单的按钮集合
	 */
	private void setChildren(List<Resources> navigateList,
			List<Resources> navChildrenList,
			Map<String, List<Resources>> btnMapListMap) {
		for (Resources resource : navigateList) {
			// 递归查找子节点
			recursionNavResList(navChildrenList, resource, btnMapListMap);
		}
	}

	/**
	 *
	 * setSessionUserAuth:根据当前用户获取权限列表，设置到当前session中. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月28日 下午4:51:10
	 * @param u
	 * @since JDK 1.7
	 */
	public void setSessionUserAuth(User user) {
		// 资源url map
		Map<String, String> authUrlMap = Maps.newHashMap();
		// 权限列表
		List<String> authUrlList = Lists.newArrayList();
		// 按钮列表
		Map<String, List<Resources>> btnMapListMap = Maps.newHashMap();
		// 导航栏菜单
		List<Resources> navigateList = new ArrayList<Resources>();
		// 导航栏子节点菜单
		List<Resources> navChildrenList = new ArrayList<Resources>();
		// 所有资源菜单
		List<Resources> allReslist = resourcesMapper.selectAllByAuth(user);
		// 循环遍历资源菜单
		for (Resources res : allReslist) {
			if (null == res.getResourceParentId()) {
				// 添加导航栏数据
				navigateList.add(res);
			} else {
				// 导航栏子节点数据
				navChildrenList.add(res);
			}
			if (!ObjectUtils.isEmpty(res.getResourceUrl())) {
				authUrlList.add(res.getResourceUrl());
				authUrlMap.put(res.getResourceUrl(),
						res.getResourceName());
			}

		}
		// 给导航栏菜单赋值
		setChildren(navigateList, navChildrenList, btnMapListMap);

		// 设置当前用户的资源的session
		user.setAuthUrllist(authUrlList);
		user.setBtnMapList(btnMapListMap);
		user.setResourcesList(navigateList);
		user.setAuthUrlMap(authUrlMap);
	}

	/**
	 *
	 * updateByPrimaryKeySelective:更新资源信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午1:48:13
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public int updateByPrimaryKeySelective(Resources record) {
		if (record == null) {
			return Constants.PARAMETER_ERROR; // 参数错误
		}
		if (this.resourcesMapper.updateByPrimaryKeySelective(record) > 0) {
			return Constants.UPDATE_SUCCESSED; // 修改成功
		} else {
			return Constants.UPDATE_FAILURED; // 修改失败
		}
	}
	
	/**
	 * 根据userId获取角色和权限的关系
	 * @param userId
	 * @return
	 */
	public List<GroupAuthRefKey> selectByUserId(String userId) {
		return this.groupAuthRefMapper.selectByUserId(userId);
	}

	public boolean isSuperAuthority(List<GroupAuthRefKey> list) {
		for(GroupAuthRefKey g : list){
			if("100".equals(g.getAuthorityId())){
				return true;
			}
		}
		return false;
	}

	public int insertAuthResRefKey(AuthResRefKey authResRefKey) {
		return this.authResRefMapper.insertSelective(authResRefKey);
	}
}
