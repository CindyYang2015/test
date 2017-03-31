package com.cloudwalk.ibis.service.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.StringUtil;
import com.cloudwalk.ibis.model.system.AuthResRefKey;
import com.cloudwalk.ibis.model.system.Authorities;
import com.cloudwalk.ibis.model.system.AuthoritiesDto;
import com.cloudwalk.ibis.model.system.GroupAuthRefKey;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.repository.system.AuthResRefMapper;
import com.cloudwalk.ibis.repository.system.AuthoritiesMapper;
import com.cloudwalk.ibis.repository.system.GroupAuthRefMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * ClassName: AuthoritiesService <br/>
 * Description: 权限service. <br/>
 * date: 2015年8月21日 下午2:23:48 <br/>
 *
 * @author Jackson He
 * @version
 * @since JDK 1.7
 */
@Service("authorityService")
public class AuthoritiesService {

	@Resource(name = "authoritiesMapper")
	private AuthoritiesMapper authorityMapper;

	@Resource(name = "authResRefMapper")
	private AuthResRefMapper authResRefMapper;
	
	@Resource(name = "groupAuthRefMapper")
	private GroupAuthRefMapper groupAuthRefMapper;
	

	/**
	 *
	 * deleteAuthorities:根据权限id删除权限信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:47:59
	 * @param authoritiesId
	 * @return
	 * @since JDK 1.7
	 */
	public int deleteAuthorities(String authorityId) {
		if (ObjectUtils.isEmpty(authorityId)) {
			return Constants.PARAMETER_ERROR;
		}
		AuthResRefKey authResRefKey = new AuthResRefKey();
		authResRefKey.setAuthorityId(authorityId);
		int counts = this.authResRefMapper.selectCount(authResRefKey);
		GroupAuthRefKey groupAuthRef=new GroupAuthRefKey();
		groupAuthRef.setAuthorityId(authorityId);
		int result=groupAuthRefMapper.selectCount(groupAuthRef);
		if (counts > 0||result>0) {
			return Constants.DELETE_FAILURED_REF;
		}
		
		return this.authorityMapper.deleteByPrimaryKey(authorityId);
	}

	/**
	 *
	 * insertSelective:添加权限信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:47:32
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public int insertSelective(Authorities record) {
		if (record == null) {
			return Constants.PARAMETER_ERROR;
		}
		if (authorityMapper.insertSelective(record) > 0) {
			return Constants.INSERT_SUCCESSED;
		} else {
			return Constants.INSERT_FAILURED;
		}
	}

	/**
	 *
	 * saveAuthoritiess:保存权限信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月24日 上午10:49:41
	 * @param records
	 * @return
	 * @since JDK 1.7
	 */
	public int saveAuthoritiess(AuthoritiesDto authority,User user) {

		// 新增数据
		List<Authorities> addAuthoritiesList = Lists.newArrayList();
		// 修改数据
		String[] authorityIds = authority.getAuthorityId();
		Date curdate = new Date();		
		for (int i = 0; i < authorityIds.length; i++) {
			if ("0".equals(authorityIds[i])) {
				// 新增
				if("0".equals(authority.getAuthorityName()[i])) {
					continue;
				}
				Authorities g1 = new Authorities();
				g1.setLegalOrgCode(user.getLegalOrgCode());
				g1.setAuthorityName(authority.getAuthorityName()[i]);
				g1.setCreateDate(curdate);
				addAuthoritiesList.add(g1);
			} else {
				Authorities g2 = new Authorities();
				g2.setLegalOrgCode(user.getLegalOrgCode());
				g2.setAuthorityId(authority.getAuthorityId()[i]);
				g2.setAuthorityName(authority.getAuthorityName()[i]);
				g2.setCreateDate(curdate);
				this.authorityMapper.updateByPrimaryKeySelective(g2);
			}
		}
		
		// 新增
		if (!ObjectUtils.isEmpty(addAuthoritiesList)) {
			this.authorityMapper.insertSelectiveBatch(addAuthoritiesList);
		}
		
		return 1;
	}

	/**
	 *
	 * saveAuthRes:保存权限与资源的关系. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月25日 下午8:00:04
	 * @param groupId
	 * @param authorityIds
	 * @return
	 * @since JDK 1.7
	 */
	public int saveAuthRes(String authorityId, String resIds) {		
		// 删除权限与资源的信息		
		this.authResRefMapper.deleteByAuthorityId(authorityId);		
		
		// 如果有资源，添加权限与资源的配置
		String[] resIdArray = resIds.split(",");
		if(!StringUtil.isBlank(resIds)){
			List<AuthResRefKey> items = Lists.newArrayList();
			for (String resId : resIdArray) {
				AuthResRefKey key = new AuthResRefKey();
				key.setAuthorityId(authorityId);
				key.setResourceId(resId);
				items.add(key);
			}
			return this.authResRefMapper.insertSelectiveBatch(items);
		}
		return 1;	
	}

	/**
	 *
	 * searchAllAuthorities:搜索权限信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:47:16
	 * @param org
	 * @return
	 * @since JDK 1.7
	 */
	public List<Authorities> searchAllAuthorities(Authorities authority) {
		return this.authorityMapper.searchAll(authority);
	}

	/**
	 *
	 * searchAllAuthorityByGroupId:查询所有的权限并且给已经勾选的权限做上标志. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月21日 下午5:32:47
	 * @param group
	 * @return
	 * @since JDK 1.7
	 */
	public List<Authorities> searchAllAuthorityByGroupId(
			Authorities authorities) {
		//查询出所有的权限
		List<Authorities> authoritiesList = this.authorityMapper
				.selectAll(authorities);
		if (ObjectUtils.isEmpty(authoritiesList)) {
			return authoritiesList;
		}
		List<String> groupIdList = Lists.newArrayList();
		groupIdList.add(authorities.getGroupId());
		//根据角色id,查询权限。（角色：权限=1：N）。查询需要勾选的权限
		List<Authorities> authoritiesUList = this.authorityMapper
				.selectGroupAuthsByGroupIds(groupIdList);
		if (ObjectUtils.isEmpty(authoritiesUList)) {
			return authoritiesList;
		}
		//如果查询出的所有权限与该角色的权限相同，则默认勾选改权限
		for (Authorities a1 : authoritiesUList) {
			for (Authorities a2 : authoritiesList) {
				if (a1.getAuthorityId().equals(a2.getAuthorityId())) {
					// 99表示被选中
					a2.setIssys(99);
					break;
				}
			}
		}
		return authoritiesList;
	}

	/**
	 *
	 * selectAllAuthorities:查询指定的权限信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:46:13
	 * @param org
	 * @return
	 * @since JDK 1.7
	 */
	public List<Authorities> selectAllAuthorities(Authorities authority) {
		return this.authorityMapper.selectAll(authority);
	}

	/**
	 *
	 * selectAuthoritiesByPage:分页查询权限信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午2:17:10
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<Authorities> selectAuthoritiesByPage(Map<String, Object> map) {
		return this.authorityMapper.searchAllByPage(map);
	}

	/**
	 *
	 * selectGroupsByGroupNames:根据角色名称获取角色信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月24日 下午5:37:27
	 * @param group
	 * @return
	 * @throws ServiceException 
	 * @since JDK 1.7
	 */
	public List<Authorities> selectAuthsByAuthNames(AuthoritiesDto authority) throws ServiceException {
		
		//权限名称集合
		Set<String> authNames = new HashSet<String>();
		
		//判断权限名称是否为空，是否重复
		for (int i=0;i<authority.getAuthorityId().length;i++) {	
			
			String authorityName = authority.getAuthorityName()[i];
			if(StringUtils.isBlank(authorityName)) {
				throw new ServiceException("权限名称不能为空");
			}
			
			if(authNames.contains(authorityName)) {
				throw new ServiceException("权限名称"+authorityName+"不能重复");
			}
			
			authNames.add(authorityName);
		}		
		
		//判断数据库中权限名称是否重复
		List<String> authNameList = new ArrayList<String>(authNames);
		Map<String,Object> paramMap = Maps.newHashMap();
		paramMap.put("list", authNameList);
		paramMap.put("legalOrgCode", authority.getLegalOrgCode());
		return this.authorityMapper.selectAuthsByAuthNames(paramMap);
	}

	/**
	 *
	 * selectByPrimaryKey:(根据机构id(主键)查询指定权限). <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:45:38
	 * @param authorityId
	 * @return
	 * @since JDK 1.7
	 */
	public Authorities selectByPrimaryKey(String authorityId) {
		return this.authorityMapper.selectByPrimaryKey(authorityId);
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
	public int selectCount(Authorities record) {
		return authorityMapper.selectCount(record);
	}

	/**
	 *
	 * updateByPrimaryKeySelective:修改指定的权限信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:45:03
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public int updateByPrimaryKeySelective(Authorities record) {
		if (record == null) {
			return Constants.PARAMETER_ERROR; // 参数错误
		}
		if (authorityMapper.updateByPrimaryKeySelective(record) > 0) {
			return Constants.UPDATE_SUCCESSED; // 修改成功
		} else {
			return Constants.UPDATE_FAILURED; // 修改失败
		}
	}
}
