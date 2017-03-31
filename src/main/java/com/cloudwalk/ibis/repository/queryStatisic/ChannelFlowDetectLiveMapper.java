package com.cloudwalk.ibis.repository.queryStatisic;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowDetectLive;


public interface ChannelFlowDetectLiveMapper {

	/**
	 * 活体检测流水插入
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
    int insertSelective(ChannelFlowDetectLive record);
	/**
	 * 根据flowID查询活体检测流水相关信息查询流水表基本信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
    ChannelFlowDetectLive selectChannelFlowDetectLiveById(@Param("flowId")String flowID);
    
	/**
	 * 根据活体检测流水相关信息查询流水表基本信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	List<ChannelFlow> searchChannelFlowDetectLiveAll(ChannelFlow record) throws DataAccessException;
}