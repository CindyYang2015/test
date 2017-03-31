
package com.cloudwalk.ibis.repository.queryStatisic;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowFaceToFace;
/**
 * 
 * Project Name: ibis-v1.2
 * File Name: ChannelFlowFaceCompareMapper.java
 * Package Name:com.cloudwalk.ibis.repository.queryStatisic
 * Description: 脸脸对比mapper
 * @date : 2017年2月24日 上午9:43:19 
 * @author: Hu Yuxin
 * @version: V1.0 
 * @since: 1.6
 * *******************************************************************************
 * 序号  修改时间  修改人  修改内容
 *  1
 *  2
 * *******************************************************************************
 * @Copyright: @ 2010-2016 重庆中科云丛科技有限公司  All Rights Reserved.
 */
public interface ChannelFlowFaceCompareMapper {
	
	/**
	 * 根据日期删除流水
	 * @param map
	 * @return
	 */
	public int deleterFlowsByDate(Map<String,Object> map);
	
	/**
	 * 
	* @Title: selectFaceCompareFlowById 
	* @Description: 根据流水id查询脸脸对比流水信息
	* @param flowId
	* @return      
	* @author:huyuxin
	 */
	public ChannelFlowFaceToFace selectFaceCompareFlowById(String flowId);
	/**
	 * 
	* @Title: insertSelective 
	* @Description: 向脸脸比对流水信息表中插入数据
	* @param record
	* @return      
	* @author:huyuxin
	 */
	int insertSelective(ChannelFlowFaceToFace record);
	/**
	 * 
	* @Title: searchFaceCompareFlowAll 
	* @Description: 联合查询主表和脸脸比对流水表 
	* @param record
	* @return      
	* @author:huyuxin
	 */
	List<ChannelFlow> searchFaceCompareFlowAll(ChannelFlow record);
}
