package com.cloudwalk.ibis.repository.queryStatisic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowCompareStrengthen;
/**
 * ClassName: ChannelFlowCompareStrengthenMapper <br/>
 * Description: TODO Description. <br/>
 * date: Feb 24, 2017 10:54:06 AM <br/>
 *
 * @author 杨维龙
 * @version 
 * @since JDK 1.7
 */
public interface ChannelFlowCompareStrengthenMapper {
	
	/**
	 * 根据日期删除流水
	 * @param map
	 * @return
	 */
	public int deleterFlowsByDate(Map<String,Object> map);
	
	/**
	 * 两证一脸接口流水插入
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
    int insertSelective(ChannelFlowCompareStrengthen record);
	/**
	 * 根据flowID查询两证一脸流水相关信息查询流水表基本信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
    ChannelFlowCompareStrengthen selectChannelFlowCompareStrengthenById(@Param("flowId")String flowID);
    
	/**
	 * 根据两证一脸流水相关信息查询流水表基本信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	List<ChannelFlow> searchCheckPersonExAll(ChannelFlow record) throws DataAccessException;

}