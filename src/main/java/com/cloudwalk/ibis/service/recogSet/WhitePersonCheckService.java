/**
 * Project Name:ibisv2
 * File Name:WhitePersonService.java
 * Package Name:com.cloudwalk.ibis.service.recogSet
 * Date:Sep 27, 20162:25:16 PM
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 *
*/

package com.cloudwalk.ibis.service.recogSet;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.WhitePerson;
import com.cloudwalk.ibis.model.recogSet.WhitePersonCheck;
import com.cloudwalk.ibis.model.recogSet.WhitePersonControl;
import com.cloudwalk.ibis.model.recogSet.vo.WhitePersonCheckQueryVo;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.repository.recogSet.WhitePersonCheckControlMapper;
import com.cloudwalk.ibis.repository.recogSet.WhitePersonCheckMapper;
import com.cloudwalk.ibis.repository.recogSet.WhitePersonControlMapper;
import com.cloudwalk.ibis.repository.recogSet.WhitePersonMapper;

/**
 * WhitePersonCheckService <br/>
 * Description: 白名单审核服务类. <br/>
 * Date:     Sep 27, 2016 2:25:16 PM <br/>
 * @author   杨维龙
 * @version  1.0.0
 * @since    JDK 1.7	 
 */
@Service("whitePersonCheckService")
public class WhitePersonCheckService {
	
	@Resource(name = "whitePersonMapper")
	private WhitePersonMapper whitePersonMapper;
	
	@Resource(name = "whitePersonCheckControlMapper")
	private WhitePersonCheckControlMapper whitePersonCheckControlMapper;
	
	@Resource(name = "whitePersonCheckMapper")
	private WhitePersonCheckMapper whitePersonCheckMapper;
	
	@Resource(name = "whitePersonControlMapper")
	private WhitePersonControlMapper whitePersonControlMapper;
	
	/**
	 *
	 * selectFaceByPage:分页查询白名单信息. <br/>
	 *
	 * @author:杨维龙 Date: 2016年9月27日 下午8:04:29
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<WhitePersonCheckQueryVo> selectWhitePersonCheckByPage(Map<String, Object> map) {
		List<WhitePersonCheckQueryVo> list=this.whitePersonCheckMapper.selectWhitePersonCheckByPage(map);
		//查询渠道和交易类型
		for(WhitePersonCheckQueryVo whitePersonCheckQueryVo:list){
	     //渠道
		 List<String> listChannel=whitePersonCheckControlMapper.selectChannelByCheckWhiteId(whitePersonCheckQueryVo.getId());
		 StringBuffer channelTemp=new StringBuffer("");
		 for(String channel:listChannel){
			 channelTemp.append(channel+" ");
		 }
		 whitePersonCheckQueryVo.setChannels(channelTemp.toString());
		 //交易类型
		 List<String> listTrading=whitePersonCheckControlMapper.selectTradingByCheckWhiteId(whitePersonCheckQueryVo.getId());
		 StringBuffer tradingTemp=new StringBuffer("");
		 for(String trading:listTrading){
			 tradingTemp.append(trading+" ");
		 }
		 whitePersonCheckQueryVo.setTradingCodes(tradingTemp.toString());
		}
		
		return list;
	}
	
	/**
	 * insertWhitePerson:(插入白名单). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 2:02:06 PM
	 * @param whitePerson
	 * @return
	 * @since JDK 1.7
	 */
	public void insertWhitePerson(WhitePersonCheck whitePersonCheck,User user){
		if(whitePersonCheck !=null){
		    	//
				WhitePerson whitePerson=new WhitePerson();
				whitePerson.setWhiteType(whitePersonCheck.getWhiteType());
				whitePerson.setCtfname(whitePersonCheck.getCtfname());
				whitePerson.setCtfno(whitePersonCheck.getCtfno());
				whitePerson.setCustomerId(whitePersonCheck.getCustomerId());
				whitePerson.setCreateTime(new Date());
				whitePerson.setCreator(user.getUserId());
				whitePerson.setCtftype(whitePersonCheck.getCtftype());
				whitePerson.setScore(whitePersonCheck.getScore());
				whitePerson.setEngineCode(whitePersonCheck.getEngineCode());
				whitePerson.setEngineverCode(whitePersonCheck.getEngineverCode());
				whitePerson.setOrgCode(user.getOrgCodePath());
				whitePerson.setLegalOrgCode(whitePersonCheck.getLegalOrgCode());
				//插入白名单信息表
			    this.whitePersonMapper.insertSelective(whitePerson);
			    
			    List<String> channels=whitePersonCheckControlMapper.selectChannelCodeByCheckWhiteId(whitePersonCheck.getId());
			    List<String> tradings=whitePersonCheckControlMapper.selectTradingCodeByCheckWhiteId(whitePersonCheck.getId());
			    //插入白名单渠道交易控制表
			    insertWhitePersonControl(channels,tradings,whitePerson);
			    //更新白名单审核信息表
			    whitePersonCheck.setWhiteId(whitePerson.getId());
			    whitePersonCheck.setStatus(Short.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_PASS.getValue()));
			    whitePersonCheckMapper.updateByPrimaryKeySelective(whitePersonCheck);
		}
	}

/**
 * insertWhitePersonControl:(插入白名单渠道交易信息). <br/>
 * TODO(这里描述这个方法适用条件 – 可选).<br/>
 *
 *  Date: Sep 30, 2016 2:34:06 PM
 * @since JDK 1.7
 */
private void insertWhitePersonControl(List<String> channels,
		List<String> tradingCodes, WhitePerson whitePerson) {
	WhitePersonControl whitePersonControl = null;

	if (!ObjectUtils.isEmpty(channels) && !ObjectUtils.isEmpty(tradingCodes)) {
		for (String channel : channels) {
			for (String tradingCode : tradingCodes) {
				whitePersonControl = new WhitePersonControl();
				whitePersonControl.setWhiteId(whitePerson.getId());
				whitePersonControl.setChannel(channel);
				whitePersonControl.setTradingCode(tradingCode);
				whitePersonControl.setLegalOrgCode(whitePerson
						.getLegalOrgCode());
				// 插入数据库
				whitePersonControlMapper.insert(whitePersonControl);
			}

		}
	} else if (!ObjectUtils.isEmpty(channels) && ObjectUtils.isEmpty(tradingCodes)) {

		for (String channel : channels) {
			whitePersonControl = new WhitePersonControl();
			whitePersonControl.setWhiteId(whitePerson.getId());
			whitePersonControl.setChannel(channel);
			whitePersonControl.setLegalOrgCode(whitePerson
					.getLegalOrgCode());
			// 插入数据库
			whitePersonControlMapper.insert(whitePersonControl);
		}
	} else if (ObjectUtils.isEmpty(channels) && !ObjectUtils.isEmpty(tradingCodes)) {
		for (String tradingCode : tradingCodes) {
			whitePersonControl = new WhitePersonControl();
			whitePersonControl.setWhiteId(whitePerson.getId());
			whitePersonControl.setTradingCode(tradingCode);
			whitePersonControl.setLegalOrgCode(whitePerson
					.getLegalOrgCode());
			// 插入数据库
			whitePersonControlMapper.insert(whitePersonControl);
		}
	}

	}

/**
 * insertWhitePerson:(更新白名单). <br/>
 * TODO(这里描述这个方法适用条件 – 可选).<br/>
 *
 *  Date: Sep 29, 2016 2:02:06 PM
 * @param whitePerson
 * @return
 * @since JDK 1.7
 */
public void updateWhitePerson(WhitePersonCheck whitePersonCheck,User user){
	if(whitePersonCheck !=null){
	    	//
			WhitePerson whitePerson=new WhitePerson();
			whitePerson.setId(whitePersonCheck.getWhiteId());
			whitePerson.setWhiteType(whitePersonCheck.getWhiteType());
			whitePerson.setCtfname(whitePersonCheck.getCtfname());
			whitePerson.setCtfno(whitePersonCheck.getCtfno());
			whitePerson.setCustomerId(whitePersonCheck.getCustomerId());
			whitePerson.setCtftype(whitePersonCheck.getCtftype());
			whitePerson.setOrgCode(user.getOrgCodePath());
			whitePerson.setUpdateTime(new Date());
			whitePerson.setScore(whitePersonCheck.getScore());
			whitePerson.setEngineCode(whitePersonCheck.getEngineCode());
			whitePerson.setEngineverCode(whitePersonCheck.getEngineverCode());
			whitePerson.setUpdator(user.getUserId());
			whitePerson.setLegalOrgCode(whitePersonCheck.getLegalOrgCode());
			//插入白名单信息表
		    this.whitePersonMapper.updateByPrimaryKeySelective(whitePerson);
		    
		    //删除之前的白名单渠道交易控制表记录
		    whitePersonControlMapper.deleteByWhiteID(whitePerson.getId());
		    
		    List<String> channels=whitePersonCheckControlMapper.selectChannelCodeByCheckWhiteId(whitePersonCheck.getId());
		    List<String> tradings=whitePersonCheckControlMapper.selectTradingCodeByCheckWhiteId(whitePersonCheck.getId());
		    //插入白名单渠道交易控制表
		    //插入白名单渠道交易控制表
		    insertWhitePersonControl(channels,tradings,whitePerson);
		    //更新白名单审核信息表
		    whitePersonCheck.setWhiteId(whitePerson.getId());
		    whitePersonCheck.setStatus(Short.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_PASS.getValue()));
		    whitePersonCheckMapper.updateByPrimaryKeySelective(whitePersonCheck);
	}
}
/**
 * deleteWhitePersonByID:(通过ID删除白名单). <br/>
 * TODO(这里描述这个方法适用条件 – 可选).<br/>
 *
 *  Date: Oct 9, 2016 3:00:57 PM
 * @param id
 * @return
 * @since JDK 1.7
 */
	public int deleteWhitePersonByID(String id) {
		return whitePersonMapper.deleteByPrimaryKey(id);
	}
/**
 * deleteWhitePersonControlByWhiteId:(通过白名单ID删除渠道交易信息). <br/>
 * TODO(这里描述这个方法适用条件 – 可选).<br/>
 *
 *  Date: Oct 9, 2016 3:01:19 PM
 * @param id
 * @return
 * @since JDK 1.7
 */
	public int deleteWhitePersonControlByWhiteId(String id) {
		return whitePersonControlMapper.deleteByWhiteID(id);
	}
}
