/**
 * Project Name:ibisv2
 * File Name:BlackPersonService.java
 * Package Name:com.cloudwalk.ibis.service.recogSet
 * Date:Sep 27, 20162:25:16 PM
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 *
*/

package com.cloudwalk.ibis.service.recogSet;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonCheckQueryVo;
import com.cloudwalk.ibis.repository.recogSet.BlackPersonCheckControlMapper;
import com.cloudwalk.ibis.repository.recogSet.BlackPersonCheckMapper;
import com.cloudwalk.ibis.repository.recogSet.BlackPersonMapper;

/**
 * BlackPersonCheckService <br/>
 * Description: 黑名单审核服务类. <br/>
 * Date:     Sep 27, 2016 2:25:16 PM <br/>
 * @author   杨维龙
 * @version  1.0.0
 * @since    JDK 1.7	 
 */
@Service("blackPersonCheckService")
public class BlackPersonCheckService {
	
	@Resource(name = "blackPersonMapper")
	private BlackPersonMapper blackPersonMapper;

	@Resource(name = "blackPersonCheckMapper")
	private BlackPersonCheckMapper blackPersonCheckMapper;
	
	@Resource(name = "blackPersonCheckControlMapper")
	private BlackPersonCheckControlMapper blackPersonCheckControlMapper;
	/**
	 * selectBlackPersonCheckByPage:(查询所有审批数据). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 11:09:57 AM
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<BlackPersonCheckQueryVo> selectBlackPersonCheckByPage(Map<String, Object> map) {
		List<BlackPersonCheckQueryVo> list=this.blackPersonCheckMapper.searchBlackPersonCheckByPage(map);
		//查询渠道和交易类型
		for(BlackPersonCheckQueryVo blackPersonCheckQueryVo:list){
	     //渠道
		 List<String> listChannel=blackPersonCheckControlMapper.selectChannelByCheckBlackId(blackPersonCheckQueryVo.getId());
		 StringBuffer channelTemp=new StringBuffer("");
		 for(String channel:listChannel){
			 channelTemp.append(channel+" ");
		 }
		 blackPersonCheckQueryVo.setChannels(channelTemp.toString());
		 //交易类型
		 List<String> listTrading=blackPersonCheckControlMapper.selectTradingByCheckBlackId(blackPersonCheckQueryVo.getId());
		 StringBuffer tradingTemp=new StringBuffer("");
		 for(String trading:listTrading){
			 tradingTemp.append(trading+" ");
		 }
		 blackPersonCheckQueryVo.setTradingCodes(tradingTemp.toString());
		}
		
		return list;
	}
}

