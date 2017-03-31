package com.cloudwalk.ibis.service.system;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.ibis.model.system.Department;
import com.cloudwalk.ibis.repository.system.DepartmentMapper;

/**
 *
 * ClassName: DepartmentService <br/>
 * Description: 部门管理. <br/>
 * date: 2015年8月19日 下午1:35:12 <br/>
 *
 * @author Jackson He
 * @version
 * @since JDK 1.7
 */
@Service("departmentService")
public class DepartmentService {

	@Resource(name = "departmentMapper")
	private DepartmentMapper departmentMapper;

	/**
	 *
	 * deleteDepartment:根据部门编码删除单位信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:47:59
	 * @param deptCode
	 * @return
	 * @since JDK 1.7
	 */
	public int deleteDepartment(String deptCode) {
		return this.departmentMapper.deleteByPrimaryKey(deptCode);
	}

	/**
	 *
	 * insertSelective:添加部门信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:47:32
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public int insertSelective(Department record) {
		if (record == null) {
			return Constants.PARAMETER_ERROR;
		}
		if (departmentMapper.insertSelective(record) > 0) {
			return Constants.INSERT_SUCCESSED;
		} else {
			return Constants.INSERT_FAILURED;
		}
	}

	/**
	 *
	 * isExistsChildUser:该部门是否存在人员. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:59:29
	 * @param deptCode
	 * @return
	 * @since JDK 1.7
	 */
	public boolean isExistsChildUser(String deptCode) {
		return this.departmentMapper.isExistsChildUser(deptCode) > 0 ? true
				: false;
	}

	/**
	 *
	 * searchAllDepartment:搜索部门信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:47:16
	 * @param org
	 * @return
	 * @since JDK 1.7
	 */
	public List<Department> searchAllDepartment(Department org) {
		return this.departmentMapper.searchAll(org);
	}

	/**
	 *
	 * selectAllDepartment:查询指定的部门信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:46:13
	 * @param org
	 * @return
	 * @since JDK 1.7
	 */
	public List<Department> selectAllDepartment(Department org) {
		return this.departmentMapper.selectAll(org);
	}

	/**
	 *
	 * selectByPrimaryKey:(根据机构id(主键)查询指定部门). <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:45:38
	 * @param departmentId
	 * @return
	 * @since JDK 1.7
	 */
	public Department selectByPrimaryKey(String departmentId) {
		return departmentMapper.selectByPrimaryKey(departmentId);
	}

	/**
	 *
	 * selectCount:查询所有的记录信息. <br/>
	 *
	 * @author:朱云飞Date: 2015年8月19日 下午1:43:19
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public int selectCount(Department record) {
		return departmentMapper.selectCount(record);
	}

	/**
	 *
	 * selectDepartmentByPage:分页查询部门信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午2:17:10
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<Department> selectDepartmentByPage(Map<String, Object> map) {
		return this.departmentMapper.searchAllByPage(map);
	}

	/**
	 *
	 * updateByPrimaryKeySelective:修改指定的部门信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午1:45:03
	 * @param record
	 * @return
	 * @since JDK 1.7
	 */
	public int updateByPrimaryKeySelective(Department record) {
		if (record == null) {
			return Constants.PARAMETER_ERROR; // 参数错误
		}
		if (departmentMapper.updateByPrimaryKeySelective(record) > 0) {
			return Constants.UPDATE_SUCCESSED; // 修改成功
		} else {
			return Constants.UPDATE_FAILURED; // 修改失败
		}
	}

}
