/**
 * Copyright @ 2015-2018 重庆中科云丛科技有限公司  All Rights Reserved.
 *
 * This file is automatically generated by MyBatis Generator, do not modify.
 * MyBatis Generator was modified by FUNDSTAR team, if you have any questions please contact with author.
 * Project Name: FundStar
 * File Name: PersonMapper.java
 * Creat Date: 2015-08-15 11:30:16
 */
package com.cloudwalk.ibis.repository.featurelib;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.cloudwalk.ibis.model.featurelib.Person;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.google.code.ssm.api.ReadThroughMultiCache;

public interface PersonMapper {

	/**
	 * 分页查询客户信息
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	@ReadThroughMultiCache(namespace = "Person", expiration = 30)
	List<Person> selectAllByPage(Map<String, Object> map)
			throws DataAccessException;

	/**
	 * 根据客户信息查询满足条件的客户
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	@ReadThroughMultiCache(namespace = "Person", expiration = 30)
	List<Person> searchAll(Person record) throws DataAccessException;

	/**
	 * 逻辑删除客户信息
	 * @param record
	 * @return
	 */
	int removePersonByPrimaryKey(Person record);
	
	/**
	 * 新增客户信息
	 * @param record
	 * @return
	 */
	int insertSelective(Person record);
	
	/**
	 * 修改客户信息
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(Person record);
	
	/**
	 * 根据特征ID获取客户信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	@ReadThroughMultiCache(namespace = "Person", expiration = 30)
	List<PersonFeature> selectPersonByFeatureId(PersonFeature record) throws DataAccessException;
	
}