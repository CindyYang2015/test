
package com.cloudwalk.ibis.repository.queryStatisic;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowCertificateFeature;

/**   
 * Project Name: ibis-v1.2
 * File Name: ChannelFlowCardFaceMapper.java
 * Package Name:com.cloudwalk.ibis.repository.queryStatisic
 * Description: 证脸对比mapper
 * @date : 2017年2月23日 下午5:42:30 
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
public interface ChannelFlowCertificateFeatureMapper {
	
	/**
	 * 根据日期删除流水
	 * @param map
	 * @return
	 */
	public int deleterFlowsByDate(Map<String,Object> map);
	
	/**
	 * 
	* @Title: selectCertificateFeatureFlowById 
	* @Description: 获取证脸对比流水表信息 
	* @param flowId
	* @return      
	* @author:huyuxin
	 */
	public ChannelFlowCertificateFeature selectCertificateFeatureFlowById(String flowId);
	/**
	 * 
	* @Title: insertSelective 
	* @Description: 向证脸对比流水表中插入信息
	* @param record
	* @return      
	* @author:huyuxin
	 */
	int insertSelective(ChannelFlowCertificateFeature record);
	/**
	 * 
	* @Title: searchCertificateFeatureFlowAll 
	* @Description: 联合查询流水主表和证脸比对流水表
	* @param record
	* @return      
	* @author:huyuxin
	 */
	List<ChannelFlow> searchCertificateFeatureFlowAll(ChannelFlow record);
}
