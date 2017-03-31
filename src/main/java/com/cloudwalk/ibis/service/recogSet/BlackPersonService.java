/**
 * Project Name:ibisv2
 * File Name:BlackPersonService.java
 * Package Name:com.cloudwalk.ibis.service.recogSet
 * Date:Sep 27, 20162:25:16 PM
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 *
*/

package com.cloudwalk.ibis.service.recogSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.BlackPerson;
import com.cloudwalk.ibis.model.recogSet.BlackPersonCheck;
import com.cloudwalk.ibis.model.recogSet.BlackPersonCheckControl;
import com.cloudwalk.ibis.model.recogSet.BlackPersonControl;
import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonCheckQueryVo;
import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonVo;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.repository.recogSet.BlackPersonCheckControlMapper;
import com.cloudwalk.ibis.repository.recogSet.BlackPersonCheckMapper;
import com.cloudwalk.ibis.repository.recogSet.BlackPersonControlMapper;
import com.cloudwalk.ibis.repository.recogSet.BlackPersonMapper;
import com.cloudwalk.ibis.repository.system.DicMapper;
import com.google.common.collect.Maps;

/**
 * ClassName:BlackPersonService <br/>
 * Description: 黑名单服务类. <br/>
 * Date:     Sep 27, 2016 2:25:16 PM <br/>
 * @author   杨维龙
 * @version  1.0.0
 * @since    JDK 1.7	 
 */
@Service("blackPersonService")
public class BlackPersonService {
	@Resource(name = "blackPersonMapper")
	private BlackPersonMapper blackPersonMapper;

	@Resource(name = "blackPersonCheckMapper")
	private BlackPersonCheckMapper blackPersonCheckMapper;
	
	@Resource(name = "blackPersonCheckControlMapper")
	private BlackPersonCheckControlMapper blackPersonCheckControlMapper;
	
	@Resource(name = "blackPersonControlMapper")
	private BlackPersonControlMapper blackPersonControlMapper;
	
	
	@Resource(name = "dicMapper")
	private DicMapper dicMapper;	
	
	
	/**
	 * 根据已审核的黑名单ID获取审核的黑名单信息
	 * @param id 已审核的黑名单ID
	 * @return
	 */
	public BlackPersonCheck selectBpcByBlackId(String id) {
		return this.blackPersonCheckMapper.selectByBlackId(id);
	}
	
	
	/**
	 *
	 * selectFaceByPage:分页查询黑名单信息. <br/>
	 *
	 * @author:杨维龙 Date: 2016年9月27日 下午8:04:29
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<BlackPersonCheckQueryVo> selectBlackPersonCheckByPage(Map<String, Object> map) {
		List<BlackPersonCheckQueryVo> list=this.blackPersonCheckMapper.searchAllByPage(map);
		//查询渠道和交易类型
		for(BlackPersonCheckQueryVo blackPersonCheckQueryVo:list){
	     //渠道
		 List<String> listChannel=blackPersonCheckControlMapper.selectChannelByBlackId(blackPersonCheckQueryVo.getId());
		 StringBuffer channelTemp=new StringBuffer("");
		 for(String channel:listChannel){
			 channelTemp.append(channel+" ");
		 }
		 blackPersonCheckQueryVo.setChannels(channelTemp.toString());
		 //交易类型
		 List<String> listTrading=blackPersonCheckControlMapper.selectTradingByBlackId(blackPersonCheckQueryVo.getId());
		 StringBuffer tradingTemp=new StringBuffer("");
		 for(String trading:listTrading){
			 tradingTemp.append(trading+" ");
		 }
		 blackPersonCheckQueryVo.setTradingCodes(tradingTemp.toString());
		}
		
		return list;
	}
	/**
	 * createBlackPerson:(创建黑名单). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 28, 2016 1:15:40 PM
	 * @param blackPersonVo
	 * @return
	 * @since JDK 1.7
	 */
	public int createBlackPerson(BlackPersonVo blackPersonVo){
		//影响条数初始化
		int result=0;
		if(blackPersonVo !=null){			
			BlackPersonCheck blackPersonCheck=new BlackPersonCheck();
			blackPersonCheck.setBlackType(blackPersonVo.getBlackType());
			blackPersonCheck.setCtfname(blackPersonVo.getCtfname());
			blackPersonCheck.setCtfno(blackPersonVo.getCtfno());
			blackPersonCheck.setCustomerId(blackPersonVo.getCustomerId());
			blackPersonCheck.setCreateTime(new Date());			
			blackPersonCheck.setCreator(blackPersonVo.getCreator());
			blackPersonCheck.setCtftype(blackPersonVo.getCtftype());
			blackPersonCheck.setOrgCode(blackPersonVo.getOrgCode());
			blackPersonCheck.setLegalOrgCode(blackPersonVo.getLegalOrgCode());
			blackPersonCheck.setStatus(Short.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_WAITING.getValue()));
			blackPersonCheck.setOperateStatus(Short.parseShort(EnumClass.DicOptTypeEnum.OPERATE_STATUS_NEW.getValue()));
			//插入新的黑名单数据，
		     result=blackPersonCheckMapper.insertSelective(blackPersonCheck);
			//插入黑名单渠道交易控制表
			if(result>0){
				//渠道
				List<String> channels=blackPersonVo.getChannels();
				//交易类型
				List<String> tradingCodes=blackPersonVo.getTradingCodes();
				//保存数据
				insertBlackPersonCheckControl(channels,tradingCodes,blackPersonCheck);	
			}
		
		}
            return result;		
	}
	/**
	 * insertBlackPersonCheckControl:(插入黑名单审核渠道交易信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 30, 2016 2:34:06 PM
	 * @since JDK 1.7
	 */
	private void insertBlackPersonCheckControl(List<String> channels, List<String> tradingCodes,BlackPersonCheck blackPersonCheck ){
		
		BlackPersonCheckControl blackPersonCheckControl=null;
		
		if(!ObjectUtils.isEmpty(channels) && !ObjectUtils.isEmpty(tradingCodes)){
			for(String channel:channels){
				for(String tradingCode:tradingCodes){
					blackPersonCheckControl=new BlackPersonCheckControl();
					blackPersonCheckControl.setBlackId(blackPersonCheck.getId());
					blackPersonCheckControl.setChannel(channel);
					blackPersonCheckControl.setTradingCode(tradingCode);
					blackPersonCheckControl.setLegalOrgCode(blackPersonCheck.getLegalOrgCode());
					//插入数据库
					blackPersonCheckControlMapper.insert(blackPersonCheckControl);
				}
			
			}
		}else if(!ObjectUtils.isEmpty(channels) && ObjectUtils.isEmpty(tradingCodes)){
				
			for(String channel:channels){
					blackPersonCheckControl=new BlackPersonCheckControl();
					blackPersonCheckControl.setBlackId(blackPersonCheck.getId());
					blackPersonCheckControl.setChannel(channel);
					blackPersonCheckControl.setLegalOrgCode(blackPersonCheck.getLegalOrgCode());
					//插入数据库
					blackPersonCheckControlMapper.insert(blackPersonCheckControl);
			}
				
		}else if(ObjectUtils.isEmpty(channels) && !ObjectUtils.isEmpty(tradingCodes)){
				for(String tradingCode:tradingCodes){
					blackPersonCheckControl=new BlackPersonCheckControl();
					blackPersonCheckControl.setBlackId(blackPersonCheck.getId());
					blackPersonCheckControl.setTradingCode(tradingCode);
					blackPersonCheckControl.setLegalOrgCode(blackPersonCheck.getLegalOrgCode());
					//插入数据库
					blackPersonCheckControlMapper.insert(blackPersonCheckControl);
				}
	  }
		
	}
	/**
	 * editBlackPerson:(黑名单编辑). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 10:39:43 AM
	 * @param blackPersonVo
	 * @return
	 * @since JDK 1.7
	 */
	public int editBlackPerson(BlackPersonVo blackPersonVo){
		//影响条数初始化
		int result=0;
		if(blackPersonVo !=null){
			BlackPersonCheck blackPersonCheck=new BlackPersonCheck();
			blackPersonCheck.setId(blackPersonVo.getId());
			blackPersonCheck.setBlackType(blackPersonVo.getBlackType());
			blackPersonCheck.setCtfname(blackPersonVo.getCtfname());
			blackPersonCheck.setCtfno(blackPersonVo.getCtfno());
			blackPersonCheck.setCustomerId(blackPersonVo.getCustomerId());
			blackPersonCheck.setUpdator(blackPersonVo.getCreator());
			blackPersonCheck.setCtftype(blackPersonVo.getCtftype());
			blackPersonCheck.setOrgCode(blackPersonVo.getOrgCode());
			blackPersonCheck.setLegalOrgCode(blackPersonVo.getLegalOrgCode());
			blackPersonCheck.setUpdateTime(new Date());
			blackPersonCheck.setStatus(Short.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_WAITING.getValue()));
			blackPersonCheck.setOperateStatus(Short.parseShort(EnumClass.DicOptTypeEnum.OPERATE_STATUS_MODIFY.getValue()));
			//更新审核表的黑名单数据，
		     result=blackPersonCheckMapper.updateByPrimaryKeySelective(blackPersonCheck);
		    //删除审核表的黑名单渠道交易控制表记录
		     blackPersonCheckControlMapper.deleteByCheckBlackPersonID(blackPersonVo.getId());
			//插入审核表的黑名单渠道交易控制表
			if(result>0){
				//渠道
				List<String> channels=blackPersonVo.getChannels();
				//交易类型
				List<String> tradingCodes=blackPersonVo.getTradingCodes();
			    //保存数据
				insertBlackPersonCheckControl(channels,tradingCodes,blackPersonCheck);
			}
		
		}
            return result;		
	}
	/**
	 * getBlackPersonCheck:(根据主键查询黑名单信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 28, 2016 6:05:07 PM
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	public BlackPersonCheck getBlackPersonCheck(String id){
		return blackPersonCheckMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 获取黑名单信息
	 * @param record
	 * @return
	 */
	public BlackPersonCheck getBlackPersonCheck(BlackPersonCheck record){
		List<BlackPersonCheck> records = blackPersonCheckMapper.selectBlackPersonChecks(record);
		if(!ObjectUtils.isEmpty(records)) {
			return records.get(0);
		}
		return null;
	}
	
	/**
	 * getBlackPersonCheckVo:(根据主键查询黑名单信息). <br/>
	 *
	 *  Date: Sep 30, 2016 1:30:48 PM
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	public BlackPersonVo getBlackPersonCheckVo(String id){
		return blackPersonCheckMapper.selectBlackPersonVoByPrimaryKey(id);
	}
	
	
	/**
	 * deleteBlackPersonCheck:(删除黑名单审核信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 28, 2016 6:10:26 PM
	 * @param id
	 * @since JDK 1.7
	 */
	public void deleteBlackPersonCheck(String id){
		 blackPersonCheckMapper.deleteByPrimaryKey(id);
	}
	/**
	 * deleteByBlackPersonID:(删除黑名单审核渠道交易控制信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 30, 2016 1:31:35 PM
	 * @param id
	 * @since JDK 1.7
	 */
	public void deleteByCheckBlackPersonID(String id){
		blackPersonCheckControlMapper.deleteByCheckBlackPersonID(id);
	}
	/**
	 * deleteBlackPerson:(删除黑名单信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 30, 2016 1:32:34 PM
	 * @param id
	 * @since JDK 1.7
	 */
	public void deleteBlackPerson(String id){
		 blackPersonMapper.deleteByPrimaryKey(id);
	}
	/**
	 * deleteBlackPersonControl:(删除黑名单渠道交易控制信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 30, 2016 1:33:16 PM
	 * @param id
	 * @since JDK 1.7
	 */
	public void deleteBlackPersonControl(String id){
		blackPersonControlMapper.deleteByBlackId(id);
	}
	/**
	 * updateBlackPersonCheck:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 28, 2016 6:12:26 PM
	 * @param blackPersonCheck
	 * @since JDK 1.7
	 */
	public void updateBlackPersonCheck(BlackPersonCheck blackPersonCheck){
		 blackPersonCheckMapper.updateByPrimaryKeySelective(blackPersonCheck);
	}
	
	/**
	 * 根据机构，渠道，交易代码，证件类型，证件号码，核心客户号获取黑名单信息
	 * @param bpc
	 * @return
	 */
	public List<BlackPersonControl> selectBlackPersonBykey(BlackPersonControl bpc) {
		return this.blackPersonMapper.selectBlackPersonBykey(bpc);
	}
	/**
	 * insertBlackPerson:(插入黑名单). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 2:02:06 PM
	 * @param blackPerson
	 * @param user
	 * @return
	 * @since JDK 1.7
	 */
	public void insertBlackPerson(BlackPersonCheck blackPersonCheck,User user) {
		if(blackPersonCheck !=null){
				BlackPerson blackPerson=new BlackPerson();
				blackPerson.setBlackType(blackPersonCheck.getBlackType());
				blackPerson.setCtfname(blackPersonCheck.getCtfname());
				blackPerson.setCtfno(blackPersonCheck.getCtfno());
				blackPerson.setCustomerId(blackPersonCheck.getCustomerId());
				blackPerson.setCreateTime(new Date());
				blackPerson.setCreator(user.getUserId());
				blackPerson.setCtftype(blackPersonCheck.getCtftype());
				blackPerson.setOrgCode(user.getOrgCodePath());
				blackPerson.setLegalOrgCode(blackPersonCheck.getLegalOrgCode());
				//插入黑名单信息表
			    int ret = this.blackPersonMapper.insertSelective(blackPerson);
			    if(ret < 1) return;
			    
			    List<String> channels=blackPersonCheckControlMapper.selectChannelCodeByCheckBlackId(blackPersonCheck.getId());
			    List<String> tradings=blackPersonCheckControlMapper.selectTradingCodeByCheckBlackId(blackPersonCheck.getId());
			    //插入黑名单渠道交易控制表
			    insertBlackPersonControl(channels,tradings,blackPerson);
			    //更新黑名单审核信息表
			    blackPersonCheck.setBlackId(blackPerson.getId());
			    blackPersonCheck.setStatus(Short.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_PASS.getValue()));
			    blackPersonCheckMapper.updateByPrimaryKeySelective(blackPersonCheck);
		}
	}
	
	
	/**
	 * insertBlackPersonControl:(插入黑名单渠道交易信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 30, 2016 2:34:06 PM
	 * @since JDK 1.7
	 */
	private void insertBlackPersonControl(List<String> channels,
			List<String> tradingCodes, BlackPerson blackPerson) {
		BlackPersonControl blackPersonControl = null;

		if (!ObjectUtils.isEmpty(channels) && !ObjectUtils.isEmpty(tradingCodes)) {
			for (String channel : channels) {
				for (String tradingCode : tradingCodes) {
					blackPersonControl = new BlackPersonControl();
					blackPersonControl.setBlackId(blackPerson.getId());
					blackPersonControl.setChannel(channel);
					blackPersonControl.setTradingCode(tradingCode);
					blackPersonControl.setLegalOrgCode(blackPerson
							.getLegalOrgCode());
					// 插入数据库
					blackPersonControlMapper.insert(blackPersonControl);
				}

			}
		} else if (!ObjectUtils.isEmpty(channels) && ObjectUtils.isEmpty(tradingCodes)) {

			for (String channel : channels) {
				blackPersonControl = new BlackPersonControl();
				blackPersonControl.setBlackId(blackPerson.getId());
				blackPersonControl.setChannel(channel);
				blackPersonControl.setLegalOrgCode(blackPerson
						.getLegalOrgCode());
				// 插入数据库
				blackPersonControlMapper.insert(blackPersonControl);
			}
		} else if (ObjectUtils.isEmpty(channels) && !ObjectUtils.isEmpty(tradingCodes)) {
			for (String tradingCode : tradingCodes) {
				blackPersonControl = new BlackPersonControl();
				blackPersonControl.setBlackId(blackPerson.getId());
				blackPersonControl.setTradingCode(tradingCode);
				blackPersonControl.setLegalOrgCode(blackPerson
						.getLegalOrgCode());
				// 插入数据库
				blackPersonControlMapper.insert(blackPersonControl);
			}
		}

	}
	/**
	 * insertBlackPerson:(更新黑名单). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 2:02:06 PM
	 * @param blackPerson
	 * @return
	 * @since JDK 1.7
	 */
	public void updateBlackPerson(BlackPersonCheck blackPersonCheck,User user){
		if(blackPersonCheck !=null){
				BlackPerson blackPerson=new BlackPerson();
				blackPerson.setId(blackPersonCheck.getBlackId());
				blackPerson.setBlackType(blackPersonCheck.getBlackType());
				blackPerson.setCtfname(blackPersonCheck.getCtfname());
				blackPerson.setCtfno(blackPersonCheck.getCtfno());
				blackPerson.setCustomerId(blackPersonCheck.getCustomerId());
				blackPerson.setCtftype(blackPersonCheck.getCtftype());
				blackPerson.setOrgCode(user.getOrgCodePath());
				blackPerson.setUpdateTime(new Date());
				blackPerson.setUpdator(user.getUserId());
				blackPerson.setLegalOrgCode(blackPersonCheck.getLegalOrgCode());
				//插入黑名单信息表
			    this.blackPersonMapper.updateByPrimaryKeySelective(blackPerson);
			    
			    //删除之前的黑名单渠道交易控制表记录
			    blackPersonControlMapper.deleteByBlackId(blackPerson.getId());
			    
			    List<String> channels=blackPersonCheckControlMapper.selectChannelCodeByCheckBlackId(blackPersonCheck.getId());
			    List<String> tradings=blackPersonCheckControlMapper.selectTradingCodeByCheckBlackId(blackPersonCheck.getId());
			    
			    
			    //插入黑名单渠道交易控制表
			    //插入黑名单渠道交易控制表
			    insertBlackPersonControl(channels,tradings,blackPerson);

			    //更新黑名单审核信息表
			    blackPersonCheck.setBlackId(blackPerson.getId());
			    blackPersonCheck.setStatus(Short.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_PASS.getValue()));
			    blackPersonCheckMapper.updateByPrimaryKeySelective(blackPersonCheck);
		}
	}
	
	/**
	 * getChannels:(获取黑名单审批表中，关联的渠道). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 4:16:48 PM
	 * @param blackPersonCheckId
	 * @return
	 * @since JDK 1.7
	 */
	public String getChannels(String blackPersonCheckId){
		List<String> listChannel=blackPersonCheckControlMapper.selectChannelByBlackId(blackPersonCheckId);
		 StringBuffer channelTemp=new StringBuffer("");
		 for(String channel:listChannel){
			 channelTemp.append(channel+",");
		 }
		return channelTemp.toString();
	}
	
	/**
	 * getTradings:(获取黑名单审批表中，关联的交易代码). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 4:15:55 PM
	 * @param blackPersonCheckId
	 * @return
	 * @since JDK 1.7
	 */
	public String getTradings(String blackPersonCheckId){
		 //交易类型
		 List<String> listTrading=blackPersonCheckControlMapper.selectTradingByBlackId(blackPersonCheckId);
		 StringBuffer tradingTemp=new StringBuffer("");
		 for(String trading:listTrading){
			 tradingTemp.append(trading+",");
		 }
		return tradingTemp.toString();
	}
	
	/**
	 * getChannels:(获取黑名单审批表中，关联的渠道). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 4:16:48 PM
	 * @param blackPersonCheckId
	 * @return
	 * @since JDK 1.7
	 */
	public List<String> getChannelCodes(String blackPersonCheckId){
		return blackPersonCheckControlMapper.selectChannelCodeByBlackId(blackPersonCheckId);
	}
	
	/**
	 * getTradings:(获取黑名单审批表中，关联的交易代码). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 4:15:55 PM
	 * @param blackPersonCheckId
	 * @return
	 * @since JDK 1.7
	 */
	public List<String> getTradingCodes(String blackPersonCheckId){
		 //交易类型
		return blackPersonCheckControlMapper.selectTradingCodeByBlackId(blackPersonCheckId);
	}
	/**
	 * selectDicValuesByDicType:(获取数据字典数据，并封装数据). <br/>
	 *
	 *  Date: Sep 30, 2016 1:34:28 PM
	 * @param dicValues
	 * @param codes
	 * @return
	 * @since JDK 1.7
	 */
	public String selectDicValuesByDicType(DicValues dicValues,List<String> codes) {
		List<DicValues> list = dicMapper.selectAll(dicValues);
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = Maps.newHashMap();
		l.add(map);
		if (list != null && list.size() > 0) {			
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, Object>();
				DicValues d = list.get(i);
				map.put("id", d.getDicCode());
				map.put("text", d.getMeaning());
				map.put("checkbox", false);
				for(String code:codes){
					if(d.getDicCode().equals(code)){
						map.put("checkbox", true);
					}
				}
				l.add(map);
			}
		}
		String str = JSONArray.toJSONString(l);
		return str;
	}
}

