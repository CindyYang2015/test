package com.cloudwalk.ibis.repository.queryStatisic;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowCard;

public interface ChannelFlowCardMapper {	
	/**
	 * 新增身份证OCR信息
	 * @param channelFlowCard
	 * @return
	 * @throws DataAccessException
	 */
	int insertSelective(ChannelFlowCard channelFlowCard) throws DataAccessException;

	ChannelFlowCard selectChannelFlowCardById(@Param("flowId")String flowId) throws DataAccessException;
	
	/**
	 * 根据日期删除流水
	 * @param map
	 * @return
	 */
	public int deleterFlowsByDate(Map<String,Object> map);
}
