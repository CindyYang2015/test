package com.cloudwalk.ibis.repository.queryStatisic;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowSearchPerson;

public interface ChannelFlowSearchPersonMapper {	
	
	/**
	 * 根据日期删除流水
	 * @param map
	 * @return
	 */
	public int deleterFlowsByDate(Map<String,Object> map);
	
	/**
	 * 新增按证检索人信息
	 * @param channelFlowCard
	 * @return
	 * @throws DataAccessException
	 */
	int insertSelective(ChannelFlowSearchPerson channelFlowSearchPerson) throws DataAccessException;

	ChannelFlowSearchPerson selectChannelFlowSearchPersonById(@Param("flowId")String flowId) throws DataAccessException;
	
}
