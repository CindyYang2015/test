package com.cloudwalk.ibis.repository.queryStatisic;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.FlowStatisic;
import com.google.code.ssm.api.ReadThroughMultiCache;

public interface ChannelFlowMapper {
		
	/**
	 * 分页查询流水表基本信息(ORACLE)
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	List<ChannelFlow> selectAllByPage(Map<String, Object> map)
			throws DataAccessException;
	/**
	 * 根据流水相关信息查询流水表基本信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	List<ChannelFlow> searchAll(ChannelFlow record) throws DataAccessException;
	/**
	 * 根据流水相关信息查询请求报文以及返回报文
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	@ReadThroughMultiCache(namespace = "ChannelFlow", expiration = 30)
	List<ChannelFlow> searchAllBLOB(ChannelFlow record) throws DataAccessException;
	
	/**
	 * 新增流水信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	int insertSelective(ChannelFlow record) throws DataAccessException;
	
	@ReadThroughMultiCache(namespace = "ChannelFlow", expiration = 30)
	public List<ChannelFlow> selectFlows(ChannelFlow record);
	
	/**
	 * 根据业务交易流水号查询已成功的交易流水信息
	 * @param record
	 * @return
	 */
	public List<ChannelFlow> selectTradingFlows(ChannelFlow record);	
	
	/**
	 * 插入已认证成功的交易信息
	 * @param record
	 * @return
	 */
	public int insertTradingFlow(ChannelFlow record);
	
	/**
	 * 根据流水ID获取流水记录
	 * @param flowId 流水ID
	 * @return
	 */
	public List<ChannelFlow> selectPersonFlowById(String flowId);
	
	/**
	 * 
	 * resultCount:(统计result结果). <br/>
	 */
	public List<FlowStatisic> resultCount(Map<String, Object> map);
	
	List<ChannelFlow> selectAllFlows(ChannelFlow record);
	
	/**
	 * 根据日期删除流水
	 * @param map
	 * @return
	 */
	public int deleterFlowsByDate(Map<String,Object> map);
	
}
