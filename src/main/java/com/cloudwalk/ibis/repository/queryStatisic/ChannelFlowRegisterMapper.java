
package com.cloudwalk.ibis.repository.queryStatisic;
import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowRegister;

/**
 * 
 * Project Name: ibis-v1.2
 * File Name: ChannelFlowRegisterMapper.java
 * Package Name:com.cloudwalk.ibis.repository.queryStatisic
 * Description: 注册流水表mapper
 * @date : 2017年2月23日 下午3:23:39 
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
public interface ChannelFlowRegisterMapper {
	
	/**
	 * 根据日期删除流水
	 * @param map
	 * @return
	 */
	public int deleterFlowsByDate(Map<String,Object> map);
	
	/** 
	* @Title: selectRegisterFlowById 
	* @Description: 根据流水ID获取人脸注册流水记录
	* @param flowId
	* @return      
	* @author:huyuxin
	 */
	public ChannelFlowRegister selectRegisterFlowById(String flowId);
	/**
	* @Title: insertSelective 
	* @Description: 新增人脸注册流水记录
	* @param record
	* @return      
	* @author:huyuxin
	 */
	int insertSelective(ChannelFlowRegister record);
	/**
	 * 
	* @Title: searchRegisterFlowAll 
	* @Description: 联合查询流水主表和人脸注册流水表
	* @param record
	* @return      
	* @author:huyuxin
	 */
	List<ChannelFlow> searchRegisterFlowAll(ChannelFlow record);
}
