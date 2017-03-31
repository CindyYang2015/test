package com.cloudwalk.ibis.service.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.spring.SpringBeanUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.system.Department;
import com.cloudwalk.ibis.model.system.Organization;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.repository.system.DepartmentMapper;
import com.cloudwalk.ibis.repository.system.OrganizationMapper;
import com.cloudwalk.ibis.repository.system.UserMapper;
import com.cloudwalk.ibis.service.base.CacheService;
import com.google.common.collect.Maps;

/**
 *
 * ClassName:OrganizationMapper <br/>
 * Description: TODO Description. <br/>
 * Date: 2015年3月26日 上午10:32:52 <br/>
 *
 * @author Jackson He
 * @version
 * @since JDK 1.7
 */
@Service("organizationService")
public class OrganizationService {

	@Resource(name = "organizationMapper")
	private OrganizationMapper organizationMapper;

	@Resource(name = "userMapper")
	private UserMapper userMapper;

	@Resource(name = "departmentMapper")
	private DepartmentMapper departmentMapper;
	
	/**
	 * 根据机构代码查询所有父级法人机构的代码
	 * @param orgCode 机构代码
	 * @return
	 */
	public List<String> selectParentCodes(String orgCode) {
		return this.organizationMapper.selectParentCodes(orgCode);
	}
	
	/**
	 *
	 * deleteOrganization:根据单位编码删除单位信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午9:05:27
	 * @param orgCode
	 * @return
	 * @since JDK 1.7
	 */
	public int deleteOrganization(String orgCode) {
		int ret = this.organizationMapper.deleteByPrimaryKey(orgCode);
		if(ret > 0) {
			CacheService cacheService = SpringBeanUtil.getBean("cacheService", CacheService.class);
			cacheService.cacheOrgCodes();
		}
		return ret;
	}

	/**
	 *
	 * getOrgAndDeptTree:传入父节点，递归获取下面所有(包括部门)的子集合. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午8:19:44
	 * @param fastList
	 * @param parent
	 * @param departmentList
	 * @return
	 * @since JDK 1.7
	 */
	public Organization getOrgAndDeptTree(List<Organization> fastList,
			Organization parent, List<Department> departmentList) {
		List<Organization> children = new ArrayList<Organization>();
		for (int i = 0; i < fastList.size(); i++) {
			if (parent.getOrgCode().equals(fastList.get(i).getParentCode())) {
				Organization org = getOrgAndDeptTree(fastList, fastList.get(i),
						departmentList);
				if (!ObjectUtils.isEmpty(org.getChildren())) {
					children.add(org);
				}

			}
		}

		for (Department department : departmentList) {
			String orgCodePath=department.getOrgCode();
			String[] orgCodes=orgCodePath.split("@");
			//根据取出的全路径，拆出直接所属机构
			String orgCode=orgCodes[orgCodes.length-1];
			if (parent.getOrgCode().equalsIgnoreCase(orgCode)) {
				Organization recordBank = new Organization();
				recordBank.setOrgCode(department.getDeptCode());
				recordBank.setOrgName(department.getDeptName());
				// 3:代表部门
				recordBank.setStatus(3);
				// 设置图片
				recordBank.setFax("icon-department");
				recordBank.setState("open");
				children.add(recordBank);
			}
		}

		if (children.size() > 0) {
			parent.setFax("icon-menu");
			parent.setChildren(children);
			if (ObjectUtils.isEmpty(parent.getParentCode())) {
				parent.setState("open");
			} else {
				parent.setState("closed");
			}
		}
		return parent;
	}

	/**
	 * insertSelective:(添加机构). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * @author:李戴月 Date: 2015年3月23日 上午9:43:32
	 * @param record
	 * @return 受影响的行数
	 * @throws DataAccessException
	 * @since JDK 1.7
	 */
	public int insertSelective(Organization record) {
		if (record == null) {
			return Constants.PARAMETER_ERROR;
		}
		if (organizationMapper.insertSelective(record) > 0) {
			CacheService cacheService = SpringBeanUtil.getBean("cacheService", CacheService.class);
			cacheService.cacheOrgCodes();
			return Constants.INSERT_SUCCESSED;
		} else {
			return Constants.INSERT_FAILURED;
		}
	}

	/**
	 *
	 * isExistsChildDept:判断该结构是否存在部门. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午8:54:57
	 * @param orgCode
	 * @return
	 * @since JDK 1.7
	 */
	public boolean isExistsChildDept(String orgCode) {
		return this.organizationMapper.isExistsChildDept(orgCode) > 0 ? true
				: false;
	}

	/**
	 *
	 * isExistsChildOrg:判断该单位是否有下属机构. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午8:53:01
	 * @param orgCode
	 * @return
	 * @since JDK 1.7
	 */
	public boolean isExistsChildOrg(String orgCode) {
		return this.organizationMapper.isExistChildOrg(orgCode) > 0 ? true
				: false;
	}

	/**
	 * OrganizationTree:(传入父节点，递归获取下面所有的子集合). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * @author:李戴月 Date: 2015年4月30日 下午3:40:12
	 * @param fastList
	 * @param parentId
	 * @return
	 * @since JDK 1.7
	 */
	public Organization OrganizationTree(List<Organization> fastList,
			Organization parent) {
		List<Organization> children = new ArrayList<Organization>();
		for (int i = 0; i < fastList.size(); i++) {
			if (parent.getOrgCode().equals(fastList.get(i).getParentCode())) {
				children.add(OrganizationTree(fastList, fastList.get(i)));
			}
		}
		if (children.size() > 0) {
			parent.setFax("icon-menu");
			parent.setChildren(children);
			if (ObjectUtils.isEmpty(parent.getParentCode())) {
				parent.setState("open");
			} else {
				parent.setState("closed");
			}

		}
		return parent;
	}

	/**
	 *
	 * searchAllOrganization:搜索单位信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 上午11:17:59
	 * @param org
	 * @return
	 * @since JDK 1.7
	 */
	public List<Organization> searchAllOrganization(Organization org) {
		return this.organizationMapper.searchAll(org);
	}
	
	/**
	 * 查询机构及父机构数据
	 * @param org
	 * @return
	 */
	public List<Organization> selectOrgsByEx(Organization org) {
		return this.organizationMapper.selectOrgsByEx(org);
	}

	/**
	 *
	 * searchAllOrganization:查询单位信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 上午11:17:59
	 * @param org
	 * @return
	 * @since JDK 1.7
	 */
	public List<Organization> selectAllOrganization(Organization org) {
		return this.organizationMapper.selectAll(org);
	}

	/**
	 * selectAllOrganizationInfo:(查询所有机构). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月26日 上午10:34:54
	 * @return
	 * @since JDK 1.7
	 */
	public List<Organization> selectAllOrganizationInfo() {
		List<Organization> newList = new ArrayList<Organization>();
		List<Organization> list = organizationMapper
				.searchAll(new Organization());
		List<Organization> parentList = list;
		for (Organization parentOrganization : parentList) {
			boolean flag = false;
			for (Organization Organization : list) {
				if (Organization.getParentCode() == null) {
					continue;
				}
				if (parentOrganization.getOrgCode().equals(
						Organization.getParentCode())) {
					flag = true;
					break;
				}
			}
			if (flag) {
				parentOrganization.setState("closed");
			}
			newList.add(parentOrganization);
		}
		return newList;
	}

	/**
	 * selectByPrimaryKey:(根据机构id(主键)查询指定机构). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * @author:李戴月 Date: 2015年3月23日 上午10:13:06
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 * @since JDK 1.7
	 */
	public Organization selectByPrimaryKey(String OrganizationId) {
		return organizationMapper.selectByPrimaryKey(OrganizationId);
	}
	
	/**
	 * 根据机构代码全路径获取机构信息
	 * @param orgCodePath 机构代码全路径
	 * @return
	 */
	public Organization selectByOrgCodePath(String orgCodePath) {
		return organizationMapper.selectByOrgCodePath(orgCodePath);
	}

	public List<Organization> selectChildOrganizationOrgByPage(
			Map<String, Object> map) {
		return this.organizationMapper.searchAllByPage(map);
	}

	/**
	 * selectCount:(查询所有记录数). <br/>
	 *
	 * @author:lidaiyue Date: 2015年4月8日 下午6:44:55
	 * @since JDK 1.7
	 */
	public int selectCount(Organization record) {
		return organizationMapper.selectCount(record);
	}

	/**
	 * selectBlankOrg:(查询用户机构下属列支机构). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * @author:孟杰 Date: 2015年7月2日 上午17:41:06
	 * @param record
	 * @return
	 * @throws DataAccessException
	 * @since JDK 1.7
	 */
	public List<Organization> selectOrganizationOrg(String OrganizationId) {
		return organizationMapper.selectOrganizationOrg(OrganizationId);
	}

	/**
	 * 查询单位与该单位的部门信息
	 */
	public List<Organization> selectTreeOrgAndDept(User user) {
		//获取当前登录用户的：机构编码全路径
		String orgCodePath=user.getOrgCodePath();
		Organization organization1=new Organization();
		organization1.setOrgCodePath(orgCodePath);
		// 得到单位信息
		List<Organization> fastList = this.organizationMapper
				.selectAll(organization1);
		//查出当前用户具体属于哪个单位
		organization1=organizationMapper.selectByPrimaryKey(user.getOrgCode());
		// 得到部门信息
		List<Department> departmentList = departmentMapper.selectAll(null);
		// 存储树形区域格式的集合
		List<Organization> list = new ArrayList<Organization>();
		Organization  organization = null;
		for (int i = 0; i < fastList.size(); i++) {
			if(fastList.get(i).getOrgCodePath().equals(organization1.getOrgCodePath())){
				organization = fastList.get(i);
				fastList.remove(i);
				break;
			}
			//如果获取的父节点与当前用户的的父节点相同，则使父节点为空
			/*String P1=organization.getParentCode();
			String P2=organization1.getParentCode();
			if(P1 != null && P1.equals(P2)){
				organization.setParentCode("");
			}
			if (ObjectUtils.isEmpty(organization.getParentCode())) {
				organization = this.getOrgAndDeptTree(fastList, organization,
						departmentList);
				list.add(organization);
			}*/
		}
		organization = this.getOrgAndDeptTree(fastList, organization,
				departmentList);
		list.add(organization);
		
		return list;
	}

	/**
	 *
	 * selectTreeOrganizationInfo:查询所有机构并且组装成下拉树. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午4:20:23
	 * @return
	 * @since JDK 1.7
	 */
	public List<Organization> selectTreeOrganizationInfo(Organization organization) {
		List<Organization> fastList = organizationMapper
				.searchAll(organization);
		// 存储树形区域格式的集合
		for(int i = 0; i < fastList.size(); i++){
			if(organization.getOrgCodePath().equals(fastList.get(i).getOrgCodePath())){
				organization=fastList.get(i);
				fastList.remove(i);
				break;
			}	
		}
		organization = OrganizationTree(fastList, organization);
		fastList.clear();
		fastList.add(organization);
		return fastList;
	}

	/**
	 * updateByPrimaryKeySelective:(修改指定机构的信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * @author:李戴月 Date: 2015年3月23日 上午10:14:06
	 * @param record
	 * @return
	 * @throws DataAccessException
	 * @since JDK 1.7
	 */
	public int updateByPrimaryKeySelective(Organization record) {
		if (record == null) {
			return Constants.PARAMETER_ERROR; // 参数错误
		}
		if (organizationMapper.updateByPrimaryKeySelective(record) > 0) {
			return Constants.UPDATE_SUCCESSED; // 修改成功
		} else {
			return Constants.UPDATE_FAILURED; // 修改失败
		}
	}
	/**
	 * 
	 * selectAllParentNode:查询所有父节点（一层一层）.
	 * @author:胡钰鑫   Date: 2016年10月8日 下午3:30:40
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public List selectAllParentNode(Organization record,List<String> list){
		//得到上级父节点编码
		String pCode=record.getParentCode();
		//如果当前节点的父节点不为空
		if(pCode!=null&&""!=pCode){
			//将父节点编码存入list
			list.add(pCode);
			//递归
			record=selectByPrimaryKey(pCode);
			selectAllParentNode(record,list);
		}
		return list;
	}
	
	/**
	 * 获取所有法人机构的数据，key:当前机构节点代码 value:机构代码全路径
	 * @return
	 */
	public Map<String,String> getLegalOrgMap() {
		Map<String,String> orgMap = Maps.newHashMap();
		Organization org = new Organization();
		org.setLegalStatus(EnumClass.StatusEnum.YES.getValue());
		List<Organization> orgList = this.organizationMapper.selectAll(org);
		if(ObjectUtils.isEmpty(orgList)) {
			return orgMap;
		}
		for(Organization temp:orgList) {
			orgMap.put(temp.getOrgCode(), temp.getOrgCodePath());
		}
		return orgMap;
	}
	
	/**
	 * 根据机构代码获取机构信息
	 * @param orgCode 机构代码
	 * @return
	 */
	public Organization getOrgByOrgCode(String orgCode) {
		
		if(StringUtils.isBlank(orgCode)) return null;
		
		//根据机构代码获取法人机构信息
		Organization org = new Organization();
		org.setOrgCode(orgCode);
		org.setLegalStatus(EnumClass.StatusEnum.YES.getValue());
		List<Organization> orgList = this.organizationMapper.selectAll(org);
		if(ObjectUtils.isEmpty(orgList)) {
			return null;
		}
		
		return orgList.get(0);
	}
}
