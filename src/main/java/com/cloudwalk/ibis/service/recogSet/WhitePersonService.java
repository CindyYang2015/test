/**
 * Project Name:ibisv2
 * File Name:WhitePersonService.java
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
import com.cloudwalk.ibis.model.recogSet.WhitePerson;
import com.cloudwalk.ibis.model.recogSet.WhitePersonCheck;
import com.cloudwalk.ibis.model.recogSet.WhitePersonCheckControl;
import com.cloudwalk.ibis.model.recogSet.vo.WhitePersonCheckQueryVo;
import com.cloudwalk.ibis.model.recogSet.vo.WhitePersonCheckVo;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.repository.recogSet.WhitePersonCheckControlMapper;
import com.cloudwalk.ibis.repository.recogSet.WhitePersonCheckMapper;
import com.cloudwalk.ibis.repository.recogSet.WhitePersonMapper;
import com.cloudwalk.ibis.repository.system.DicMapper;
import com.google.common.collect.Maps;

/**
 * ClassName:WhitePersonService <br/>
 * Description: 白名单服务类. <br/>
 * Date:     Sep 27, 2016 2:25:16 PM <br/>
 * @author   杨维龙
 * @version  1.0.0
 * @since    JDK 1.7	 
 */
@Service("whitePersonService")
public class WhitePersonService {
	@Resource(name = "whitePersonCheckMapper")
	private WhitePersonCheckMapper whitePersonCheckMapper;
	
	@Resource(name = "whitePersonCheckControlMapper")
	private WhitePersonCheckControlMapper whitePersonCheckControlMapper;
	
	@Resource(name = "whitePersonMapper")
	private WhitePersonMapper whitePersonMapper;
	
	@Resource(name = "dicMapper")
	private DicMapper dicMapper;
	
	
	/**
	 * 根据已审核的白名单ID获取审核的白名单信息
	 * @param id 已审核的白名单ID
	 * @return
	 */
	public WhitePersonCheck selectWpcByWhileId(String id) {
		return this.whitePersonCheckMapper.selectByWhileId(id);
	}
	
	/**
	 * createWhitePerson:(创建白名单). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 28, 2016 1:15:40 PM
	 * @param whitePersonVo
	 * @return
	 * @since JDK 1.7
	 */
	public int createWhitePersonCheck(WhitePersonCheckVo whitePersonVo){
		//影响条数初始化
		int result=0;
		if(whitePersonVo !=null){
			WhitePersonCheck whitePersonCheck=new WhitePersonCheck();
			whitePersonCheck.setWhiteType(whitePersonVo.getWhiteType());
			whitePersonCheck.setCtfname(whitePersonVo.getCtfname());
			whitePersonCheck.setCtfno(whitePersonVo.getCtfno());
			whitePersonCheck.setCustomerId(whitePersonVo.getCustomerId());
			whitePersonCheck.setCreateTime(new Date());
			whitePersonCheck.setCreator(whitePersonVo.getCreator());
			whitePersonCheck.setCtftype(whitePersonVo.getCtftype());
			whitePersonCheck.setOrgCode(whitePersonVo.getOrgCode());
			whitePersonCheck.setLegalOrgCode(whitePersonVo.getLegalOrgCode());
			whitePersonCheck.setEngineCode(whitePersonVo.getEngineCode());
			whitePersonCheck.setEngineverCode(whitePersonVo.getEngineverCode());
			whitePersonCheck.setScore(whitePersonVo.getScore());
			whitePersonCheck.setStatus(Short.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_WAITING.getValue()));
			whitePersonCheck.setOperateStatus(Short.parseShort(EnumClass.DicOptTypeEnum.OPERATE_STATUS_NEW.getValue()));
			//插入新的白名单数据，
	         result=whitePersonCheckMapper.insertSelective(whitePersonCheck);
			//插入白名单渠道交易控制表
			if(result>0){
				//渠道
				List<String> channels=whitePersonVo.getChannels();
				//交易类型
				List<String> tradingCodes=whitePersonVo.getTradingCodes();
				//保存数据
				insertWhitePersonCheckControl(channels,tradingCodes,whitePersonCheck);	
			}
		
		}
            return result;		
	}
	/**
	 * editWhitePerson:(白名单编辑). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 10:39:43 AM
	 * @param whitePersonVo
	 * @return
	 * @since JDK 1.7
	 */
	public int editWhitePerson(WhitePersonCheckVo whitePersonVo){
		//影响条数初始化
		int result=0;
		if(whitePersonVo !=null){
			WhitePersonCheck whitePersonCheck=new WhitePersonCheck();
			whitePersonCheck.setId(whitePersonVo.getId());
			whitePersonCheck.setWhiteType(whitePersonVo.getWhiteType());
			whitePersonCheck.setCtfname(whitePersonVo.getCtfname());
			whitePersonCheck.setCtfno(whitePersonVo.getCtfno());
			whitePersonCheck.setCustomerId(whitePersonVo.getCustomerId());
			whitePersonCheck.setUpdator(whitePersonVo.getCreator());
			whitePersonCheck.setCtftype(whitePersonVo.getCtftype());
			whitePersonCheck.setUpdateTime(new Date());
			whitePersonCheck.setScore(whitePersonVo.getScore());
			whitePersonCheck.setOrgCode(whitePersonVo.getOrgCode());
			whitePersonCheck.setLegalOrgCode(whitePersonVo.getLegalOrgCode());
			whitePersonCheck.setEngineCode(whitePersonVo.getEngineCode());
			whitePersonCheck.setEngineverCode(whitePersonVo.getEngineverCode());
			whitePersonCheck.setStatus(Short.parseShort(EnumClass.DicCheckTypeEnum.CHECK_STATUS_WAITING.getValue()));
			whitePersonCheck.setOperateStatus(Short.parseShort(EnumClass.DicOptTypeEnum.OPERATE_STATUS_MODIFY.getValue()));
			//插入新的白名单数据，
		     result=whitePersonCheckMapper.updateByPrimaryKeySelective(whitePersonCheck);
		    //删除之前的白名单渠道交易控制表记录
		     whitePersonCheckControlMapper.deleteByWhitePersonID(whitePersonVo.getId());
			//插入白名单渠道交易控制表
			if(result>0){
				//渠道
				List<String> channels=whitePersonVo.getChannels();
				//交易类型
				List<String> tradingCodes=whitePersonVo.getTradingCodes();
			    //保存数据
				insertWhitePersonCheckControl(channels,tradingCodes,whitePersonCheck);
			}
		
		}
            return result;	
            
	}
	/**
	 * insertWhitePersonCheckControl:(插入白名单审核渠道交易信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 30, 2016 2:34:06 PM
	 * @since JDK 1.7
	 */
	private void insertWhitePersonCheckControl(List<String> channels, List<String> tradingCodes,WhitePersonCheck whitePersonCheck ){
		
		WhitePersonCheckControl whitePersonCheckControl=null;
		
		if(!ObjectUtils.isEmpty(channels) && !ObjectUtils.isEmpty(tradingCodes)){
			for(String channel:channels){
				for(String tradingCode:tradingCodes){
					whitePersonCheckControl=new WhitePersonCheckControl();
					whitePersonCheckControl.setWhiteId(whitePersonCheck.getId());
					whitePersonCheckControl.setChannel(channel);
					whitePersonCheckControl.setTradingCode(tradingCode);
					whitePersonCheckControl.setLegalOrgCode(whitePersonCheck.getLegalOrgCode());
					//插入数据库
					whitePersonCheckControlMapper.insert(whitePersonCheckControl);
				}
			
		}
			}else if(!ObjectUtils.isEmpty(channels) && ObjectUtils.isEmpty(tradingCodes)){
				
				for(String channel:channels){
						whitePersonCheckControl=new WhitePersonCheckControl();
						whitePersonCheckControl.setWhiteId(whitePersonCheck.getId());
						whitePersonCheckControl.setChannel(channel);
						whitePersonCheckControl.setLegalOrgCode(whitePersonCheck.getLegalOrgCode());
						//插入数据库
						whitePersonCheckControlMapper.insert(whitePersonCheckControl);
			}
		}else if(ObjectUtils.isEmpty(channels) && !ObjectUtils.isEmpty(tradingCodes)){
				for(String tradingCode:tradingCodes){
					whitePersonCheckControl=new WhitePersonCheckControl();
					whitePersonCheckControl.setWhiteId(whitePersonCheck.getId());
					whitePersonCheckControl.setTradingCode(tradingCode);
					whitePersonCheckControl.setLegalOrgCode(whitePersonCheck.getLegalOrgCode());
					//插入数据库
					whitePersonCheckControlMapper.insert(whitePersonCheckControl);
				}
	  }
		
	}
	
	
	/**
	 *
	 * selectFaceByPage:分页查询白名单信息. <br/>
	 *
	 * @author:杨维龙 Date: 2016年9月27日 下午8:04:29
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<WhitePersonCheckQueryVo> selectWhitePersonByPage(Map<String, Object> map) {
		List<WhitePersonCheckQueryVo> list=this.whitePersonCheckMapper.searchAllByPage(map);
		//查询渠道和交易类型
		for(WhitePersonCheckQueryVo whitePersonCheckQueryVo:list){
	     //渠道
		 List<String> listChannel=whitePersonCheckControlMapper.selectChannelByWhiteId(whitePersonCheckQueryVo.getId());
		 StringBuffer channelTemp=new StringBuffer("");
		 for(String channel:listChannel){
			 channelTemp.append(channel+" ");
		 }
		 whitePersonCheckQueryVo.setChannels(channelTemp.toString());
		 //交易类型
		 List<String> listTrading=whitePersonCheckControlMapper.selectTradingByWhiteId(whitePersonCheckQueryVo.getId());
		 StringBuffer tradingTemp=new StringBuffer("");
		 for(String trading:listTrading){
			 tradingTemp.append(trading+" ");
		 }
		 whitePersonCheckQueryVo.setTradingCodes(tradingTemp.toString());
		}
		
		return list;
	}
	/**
	 * getWhitePersonCheckById:(通过主键获取白名单审核信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Oct 8, 2016 4:36:04 PM
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	
	public WhitePersonCheck getWhitePersonCheckById(String id){		
	      return whitePersonCheckMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 查询已审核的白名单信息
	 * @param id
	 * @return
	 */
	public WhitePersonCheck getWhitePersonkById(String id){		
	      return whitePersonCheckMapper.selectWpByPrimaryKey(id);
	}
	
	/**
	 * 获取白名单信息
	 * @param record
	 * @return
	 */
	public WhitePersonCheck getWhitePersonCheck(WhitePersonCheck record){		
	      List<WhitePersonCheck> records = whitePersonCheckMapper.selectWhitePersonChecks(record);
	      if(!ObjectUtils.isEmpty(records)) {
	    	  return records.get(0);
	      }
	      return null;
	}
	
	/**
	 * getChannels:(获取白名单审批表中，关联的渠道). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 4:16:48 PM
	 * @param whitePersonCheckId
	 * @return
	 * @since JDK 1.7
	 */
	public List<String> getChannelCodes(String whitePersonCheckId){
		return whitePersonCheckControlMapper.selectChannelCodeByWhiteId(whitePersonCheckId);
	}
	
	/**
	 * getTradings:(获取白名单审批表中，关联的交易代码). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 29, 2016 4:15:55 PM
	 * @param whitePersonCheckId
	 * @return
	 * @since JDK 1.7
	 */
	public List<String> getTradingCodes(String whitePersonCheckId){
		 //交易类型
		return whitePersonCheckControlMapper.selectTradingCodeByWhiteId(whitePersonCheckId);
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
					if(code.equals(d.getDicCode())){
						map.put("checkbox", true);
					}
				}
				l.add(map);
			}
		}
		String str = JSONArray.toJSONString(l);
		return str;
	}
	/**
	 * deleteWhitePersonCheck:(通过主键删除白名单数据). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Oct 8, 2016 5:27:39 PM
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	public int deleteWhitePersonCheck(String id){
		return whitePersonCheckMapper.deleteByPrimaryKey(id);
	}
	
	public int deleteWhitePersonCheckControlByWhiteID(String id){
		return whitePersonCheckControlMapper.deleteByWhitePersonID(id);
	}
	public int deleteWhitePerson(String id){
		return whitePersonMapper.deleteByPrimaryKey(id);
	}
	/**
	 * updateWhitePersonCheck:(更新白名单信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Oct 8, 2016 5:28:51 PM
	 * @param whitePersonCheck
	 * @return
	 * @since JDK 1.7
	 */
	public int updateWhitePersonCheck(WhitePersonCheck whitePersonCheck){
		return  whitePersonCheckMapper.updateByPrimaryKeySelective(whitePersonCheck);
	}
	
	/**
	 * 获取白名单阈值
	 * @param record
	 * @return
	 */
	public WhitePerson getWhitePersonBykey(WhitePerson record) {
		List<WhitePerson> wpList = this.whitePersonMapper.selectWhitePersonBykey(record);
		if(!ObjectUtils.isEmpty(wpList)) {
			return wpList.get(0);
		}
		return null;
	}
}

