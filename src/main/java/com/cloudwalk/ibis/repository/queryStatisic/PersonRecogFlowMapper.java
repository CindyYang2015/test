package com.cloudwalk.ibis.repository.queryStatisic;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.cloudwalk.ibis.model.queryStatisic.PersonRecogFlow;
import com.google.code.ssm.api.ReadThroughMultiCache;

public interface PersonRecogFlowMapper {
	/**
	 * 分页查询流水统计表基本信息
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	@ReadThroughMultiCache(namespace = "PersonRecogFlow", expiration = 30)
	List<PersonRecogFlow> selectAllByPage(Map<String, Object> map)
			throws DataAccessException;
	/**
	 * 根据流水相关信息查询流水统计表基本信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	@ReadThroughMultiCache(namespace = "PersonRecogFlow", expiration = 30)
	List<PersonRecogFlow> searchAll(PersonRecogFlow record) throws DataAccessException;
	
	/**
	 * 根据流水相关信息查询流水统计表基本信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	@ReadThroughMultiCache(namespace = "PersonRecogFlow", expiration = 30)
	List<PersonRecogFlow> searchAllByPrimaryKey(PersonRecogFlow record) throws DataAccessException;
	
	/**
	 * 新增流水信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	int insertSelective(PersonRecogFlow record) throws DataAccessException;
	
	/**
	 * 查询客户识别流水统计信息
	 * @param record
	 * @return
	 */
	public List<PersonRecogFlow> selectPersonRecogFlows(PersonRecogFlow record) throws DataAccessException;
	
	/**
	 * 根据ID更新成功数或者失败数
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePersonRecogFlowBykey(PersonRecogFlow record) throws DataAccessException;
}
