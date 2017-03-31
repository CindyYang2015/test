package com.cloudwalk.ibis.repository.system;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.cloudwalk.ibis.model.system.User;

/**
 * Class Name: UserMapper <br>
 * Description: This interface corresponds to the table - fssc_id_user <br>
 * Create Date: 2015-03-30 00:40:55 <br>
 */
public interface UserExtendMapper {

	int deleteByPrimaryKeyBatch(String[] asList); // 批量删除用户信息功能

	List<User> selectAll(User record) throws DataAccessException; // 查询所有用户功能

	List<User> selectAllByPage(Map<String, Object> map) throws DataAccessException; // 分页查询用户信息功能

	List<User> selectByPage(Map<String, Object> map) throws DataAccessException; // 查询除当前登录用户的所有用户信息

	User selectByPrimaryKey(String userId) throws DataAccessException; // 查询指定用户信息功能

	int selectCount(User record) throws DataAccessException; // 查询用户记录数功能

	int selectCountOfDepartment(String[] key); // 查询用户记录中是否有指定部门信息记录功能

}