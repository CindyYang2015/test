package com.cloudwalk.ibis.service.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.system.Group;
import com.cloudwalk.ibis.model.system.GroupAuthRefKey;
import com.cloudwalk.ibis.model.system.GroupDto;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.model.system.UserGroupRefKey;
import com.cloudwalk.ibis.repository.system.GroupAuthRefMapper;
import com.cloudwalk.ibis.repository.system.GroupMapper;
import com.cloudwalk.ibis.repository.system.UserGroupRefMapper;
import com.google.common.collect.Lists;

/**
 *
 * ClassName: GroupService <br/>
 * Description: 角色service. <br/>
 * date: 2015年8月21日 下午2:23:48 <br/>
 *
 * @author Jackson He
 * @version
 * @since JDK 1.7
 */
@Service("groupService")
public class GroupService {

	@Resource(name = "groupMapper")
	private GroupMapper groupMapper;

	@Resource(name = "groupAuthRefMapper")
	private GroupAuthRefMapper groupAuthRefMapper;

	@Resource(name = "userGroupRefMapper")
	private UserGroupRefMapper userGroupRefMapper;
	
	

	/**
	 *
	 * deleteGroup:根据角色id删除单位信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:47:59
	 * @param groupId
	 * @return
	 * @since JDK 1.7
	 */
	public int deleteGroup(String groupId) {
		if (ObjectUtils.isEmpty(groupId)) {
			return Constants.PARAMETER_ERROR;
		}
		UserGroupRefKey userGroupRefKey = new UserGroupRefKey();
		userGroupRefKey.setGroupId(groupId);
		List<UserGroupRefKey> userGroupRefKeyList = this.userGroupRefMapper
				.selectAll(userGroupRefKey);
		if (!ObjectUtils.isEmpty(userGroupRefKeyList)) {
			return Constants.DELETE_FAILURED_REF;
		}
		//当前角色是否关联了权限，如果关联了，不能删除
		GroupAuthRefKey groupAuthRefKey = new GroupAuthRefKey();
		groupAuthRefKey.setGroupId(groupId);
		int counts = groupAuthRefMapper.selectCount(groupAuthRefKey);
		if(counts > 0) {
			return Constants.DELETE_FAILURED;
		}
		
		return this.groupMapper.deleteByPrimaryKey(groupId);
	}

	/**
	 *
	 * insertSelective:添加角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:47:32
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public int insertSelective(Group record) {
		if (record == null) {
			return Constants.PARAMETER_ERROR;
		}
		if (groupMapper.insertSelective(record) > 0) {
			return Constants.INSERT_SUCCESSED;
		} else {
			return Constants.INSERT_FAILURED;
		}
	}

	/**
	 *
	 * saveGroupAuthority:保存角色与权限的信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月24日 下午4:03:49
	 * @param groupId
	 * @param authorityIds
	 * @return
	 * @since JDK 1.7
	 */
	public int saveGroupAuthority(String groupId, String authorityIds) {
		// 删除角色与权限的信息
		this.groupAuthRefMapper.deleteByGroupId(groupId);
		// 添加角色与权限的配置
		if("".endsWith(authorityIds)||StringUtils.isEmpty(authorityIds)){
			return 0;
		}
		else{
		String[] authorityIdArray = authorityIds.split(",");
		List<GroupAuthRefKey> items = Lists.newArrayList();
		for (String authorityId : authorityIdArray) {
			GroupAuthRefKey key = new GroupAuthRefKey();
			key.setAuthorityId(authorityId);
			key.setGroupId(groupId);
			items.add(key);
		}
		return this.groupAuthRefMapper.insertSelectiveBatch(items);
		}
	}

	/**
	 *
	 * saveGroups:保存角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月24日 上午10:49:41
	 * @param records
	 * @return
	 * @since JDK 1.7
	 */
	public int saveGroups(GroupDto group,User user) {
		
		// 新增数据
		List<Group> addGroupList = Lists.newArrayList();
		String[] groupIds = group.getGroupId();
		Date curdate = new Date();
		for (int i = 0; i < groupIds.length; i++) {
			if ("0".equals(groupIds[i])) {
				if (group.getGroupCname()[i].equals("0")) {
					continue;
				}
				// 新增
				Group g1 = new Group();
				g1.setGroupCname(group.getGroupCname()[i]);
				String groupEname = group.getGroupEname()[i].equals("0") ? ""
						: group.getGroupEname()[i];
				g1.setGroupEname(groupEname);
				g1.setLegalOrgCode(user.getLegalOrgCode());
				g1.setCreateDate(curdate);
				addGroupList.add(g1);
			} else {
				Group g2 = new Group();
				g2.setGroupId(group.getGroupId()[i]);
				g2.setLegalOrgCode(user.getLegalOrgCode());
				g2.setGroupCname(group.getGroupCname()[i]);
				String groupEname = group.getGroupEname()[i].equals("0") ? ""
						: group.getGroupEname()[i];
				g2.setGroupEname(groupEname);
				g2.setCreateDate(curdate);
				this.groupMapper.updateByPrimaryKeySelective(g2);
			}
		}
		
		// 新增
		if (!ObjectUtils.isEmpty(addGroupList)) {
			this.groupMapper.insertSelectiveBatch(addGroupList);
		}
		
		return 1;
	}
	

	/**
	 *
	 * searchAllGroup:搜索角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:47:16
	 * @param org
	 * @return
	 * @since JDK 1.7
	 */
	public List<Group> searchAllGroup(Group group) {
		return this.groupMapper.searchAll(group);
	}

	/**
	 *
	 * searchAllGroupByUserId:查询所有的角色并且给关联的用户做上标志. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月21日 下午5:32:47
	 * @param group
	 * @return
	 * @since JDK 1.7
	 */
	public List<Group> searchAllGroupByUserId(Group group) {
		List<Group> groupList = this.groupMapper.searchAll(group);
		if (ObjectUtils.isEmpty(groupList)) {
			return groupList;
		}
		List<String> userIdList = Lists.newArrayList();
		userIdList.add(group.getUserId());
		List<Group> groupUList = this.groupMapper
				.selectUserGroupsByUserIds(userIdList);
		if (ObjectUtils.isEmpty(groupUList)) {
			return groupList;
		}
		for (Group g1 : groupUList) {
			for (Group g2 : groupList) {
				if (g1.getGroupId().equals(g2.getGroupId())) {
					// 99表示被选中
					g2.setGroupLevel(99);
					break;
				}
			}
		}
		return groupList;
	}

	/**
	 *
	 * selectAllGroup:查询指定的角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:46:13
	 * @param org
	 * @return
	 * @since JDK 1.7
	 */
	public List<Group> selectAllGroup(Group group) {
		return this.groupMapper.selectAll(group);
	}

	/**
	 *
	 * selectByPrimaryKey:(根据机构id(主键)查询指定角色). <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:45:38
	 * @param groupId
	 * @return
	 * @since JDK 1.7
	 */
	public Group selectByPrimaryKey(String groupId) {
		return groupMapper.selectByPrimaryKey(groupId);
	}

	/**
	 *
	 * selectCount:查询所有的记录. <br/>
	 *
	 * @author:朱云飞Date: 2015年8月19日 下午1:43:19
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public int selectCount(Group record) {
		return groupMapper.selectCount(record);
	}

	/**
	 *
	 * selectGroupByPage:分页查询角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午2:17:10
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<Group> selectGroupByPage(Map<String, Object> map) {
		return this.groupMapper.searchAllByPage(map);
	}

	/**
	 *
	 * selectGroupsByGroupNames:根据角色名称获取角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月24日 下午5:37:27
	 * @param group
	 * @return
	 * @since JDK 1.7
	 */
	public List<Group> selectGroupsByGroupNames(GroupDto group ) {	   
		
		//判断角色组信息的名字是否重复
		String groupCname = "";
		String groupEname = "";
        for (int i = 0; i < group.getGroupId().length - 1; i++)
        {
    	   groupCname = group.getGroupCname()[i];
    	   groupEname = group.getGroupEname()[i];
            for (int j = i + 1; j < group.getGroupId().length; j++)
            {
                if ((groupCname.equals(group.getGroupCname()[j]) && !groupCname.equals("0")) 
                		|| (groupEname.equals(group.getGroupEname()[j]) && !groupEname.equals("0")))
                {
                	List<Group> groupList = Lists.newArrayList();
                	Group g = new Group();
                	g.setGroupCname(groupCname);
                	groupList.add(g);
                	return groupList;
                }
            }
         }
        
        //判断数据库中角色信息是否重复
		for (int i = 0; i < group.getGroupId().length; i++) {
			Group g = new Group();
			g.setGroupCname(group.getGroupCname()[i]);
			g.setGroupEname(group.getGroupEname()[i]);
			g.setGroupId(group.getGroupId()[i]);
			g.setLegalOrgCode(group.getLegalOrgCode());
			
			List<Group> groupList = groupMapper.selectGroupsByNames(g);
			if(!ObjectUtils.isEmpty(groupList)){
				return groupList;
			}
		}
		
		return null;
	}

	/**
	 *
	 * updateByPrimaryKeySelective:修改指定的角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:45:03
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public int updateByPrimaryKeySelective(Group record) {
		if (record == null) {
			return Constants.PARAMETER_ERROR; // 参数错误
		}
		if (groupMapper.updateByPrimaryKeySelective(record) > 0) {
			return Constants.UPDATE_SUCCESSED; // 修改成功
		} else {
			return Constants.UPDATE_FAILURED; // 修改失败
		}
	}

}
