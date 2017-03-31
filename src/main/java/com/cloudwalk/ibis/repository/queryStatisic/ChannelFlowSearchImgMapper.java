package com.cloudwalk.ibis.repository.queryStatisic;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowSearchImg;

public interface ChannelFlowSearchImgMapper {	
	
	/**
	 * 根据日期删除流水
	 * @param map
	 * @return
	 */
	public int deleterFlowsByDate(Map<String,Object> map);
	
	/**
	 * 新增按脸检索人信息
	 * @param channelFlowSearchImg
	 * @return
	 * @throws DataAccessException
	 */
	int insertSelective(ChannelFlowSearchImg channelFlowSearchImg) throws DataAccessException;

	ChannelFlowSearchImg selectChannelFlowSearchImgById(@Param("flowId")String flowId) throws DataAccessException;
	
}
