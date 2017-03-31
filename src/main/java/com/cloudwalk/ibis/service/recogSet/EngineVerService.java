package com.cloudwalk.ibis.service.recogSet;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.spring.SpringBeanUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.recogSet.Engine;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.repository.recogSet.EngineMapper;
import com.cloudwalk.ibis.repository.recogSet.EngineVerMapper;
import com.cloudwalk.ibis.service.base.CacheService;
import com.google.common.collect.Maps;

/**
 * 算法引擎业版本务逻辑服务
 * @author 白乐
 *
 */
@Service("engineVerService")
public class EngineVerService {
	@Resource(name = "engineVerMapper")
	EngineVerMapper engineVerMapper;
	
	@Resource(name = "engineMapper")
	EngineMapper engineMapper;
	
	public int saveEngineVer(EngineVer record) throws Exception {
	 	return engineVerMapper.insert(record);
	}
	
	public List<EngineVer> selectEngineVerPage(Map<String,Object> map){
		return engineVerMapper.selectEngineVerPage(map);
	}
	
	public EngineVer selectEngineVerById(String id){
		return engineVerMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 修改算法引擎版本
	 * @param record
	 * @return
	 */
	public int updateEngineVer(EngineVer record) throws Exception{
		int ret = engineVerMapper.updateByPrimaryKey(record);
		if(ret > 0) {
			CacheService cacheService = SpringBeanUtil.getBean("cacheService", CacheService.class);
			cacheService.cacheEngineVers();
			cacheService.cacheRecogSteps();
		}
		return ret;
	}
	
	/**
	 * 删除算法引擎版本
	 * @param id
	 * @return
	 */
	public int deleteEngineVer(String id) throws Exception{
		return engineVerMapper.deleteByPrimaryKey(id);
	}	
	
	/**
	 * 得到所有的算法引擎
	 * @return
	 */
	public List<Engine> selectValidEngineAll(){
		return engineMapper.selectValidEngineAll();
	}
	
	/**
	 * 根据特征信息得到算法引擎信息
	 * @param partitionId 分区ID
	 * @param personId
	 * @return
	 */
	public List<Engine> selectValidEngineAllByFeature(Integer partitionId,String personId){
		Map<String,Object> param = Maps.newHashMap();
		param.put("partitionId", partitionId);
		param.put("personId", personId);
		return engineMapper.selectValidEngineAllByFeature(param);
	}
	
	
	public List<Engine> selectEngineAll(){
		return engineMapper.selectEngineAll();
	}
	
	
	/**
	 * 根据算法引擎代码得到版本
	 * @param code
	 * @return
	 */
	public List<EngineVer> selectByCode(String code){
		return engineVerMapper.selectByCode(code);
	}
	
	/*
	 * 根据算法引擎版本自身信息进行查询
	 */
	public List<EngineVer> selectByEngineVer(Map<String,String> map){
		return engineVerMapper.selectByEngineVer(map);
	}
	/**
	 * 根据特征信息得到版本
	 * @param code
	 * @return
	 */
	public List<EngineVer> selectByCodeFromFeature(PersonFeature code){
		return engineVerMapper.selectByCodeFromFeature(code);
	}
	
	/**
	 * 根据机构代码，渠道，交易代码，引擎代码获取引擎版本信息
	 * @param map 机构代码，渠道，交易代码，引擎代码
	 * @return
	 */
	public EngineVer selectEngineVer(Map<String,String> map) {		
		List<EngineVer> engineVerList = this.engineVerMapper.selectEngineVer(map);
		if(ObjectUtils.isEmpty(engineVerList)) {
			return null;
		}
		return engineVerList.get(0);
	}
	
	/**
	 * 获取所有的算法引擎版本信息
	 * @return
	 */
	public Map<String,EngineVer> getAllEngineVersMap() {
		Map<String,EngineVer> engineVerMap = Maps.newHashMap();
		List<EngineVer> engineVerList = this.engineVerMapper.selectAllEngineVer();
		if(ObjectUtils.isEmpty(engineVerList)) {
			return engineVerMap;
		}
		for(EngineVer ev:engineVerList) {
			String key = ev.getOrgCode()+Constants.SEPARATOR+ev.getChannel()+Constants.SEPARATOR+ev.getTradingCode()
							+Constants.SEPARATOR+ev.getEngineCode();
			engineVerMap.put(key, ev);
		}
		return engineVerMap;
	}
	
	/**
	 * 根据机构代码，渠道，交易代码，引擎代码获取算法引擎版本信息 
	 * key 机构代码+渠道+交易代码+引擎代码
	 * @param orgCode 机构代码
	 * @param channel 渠道
	 * @param tradingCode 交易代码
	 * @param engineCode 引擎代码
	 * @return
	 */
	public EngineVer getEngineVerBykey(String orgCode, String channel,
			String tradingCode, String engineCode) {		
		
		//拼接查询参数
		Map<String,String> paramMap = Maps.newHashMap();
		paramMap.put("orgCode", orgCode);
		paramMap.put("channel", channel);
		paramMap.put("tradingCode", tradingCode);
		paramMap.put("engineCode", engineCode);
		List<EngineVer> engineVerList = this.engineVerMapper.selectEngineVer(paramMap);
		if(ObjectUtils.isEmpty(engineVerList)) {
			return null;
		}
		
		return engineVerList.get(0);
		
	}
	
	/**
	 * 获取所有识别策略对应的策略
	 * @return
	 */
	public Map<String,Map<String,Set<String>>> getRecogStepEngineMap() {
		Map<String,Map<String,Set<String>>> recogStepMap = Maps.newHashMap();
		List<EngineVer> evList = this.engineVerMapper.selectEngineVer(null);
		if(ObjectUtils.isEmpty(evList)) {
			return recogStepMap;
		}
		for(EngineVer ev:evList) {	
			String evString = ev.getEngineCode()+Constants.SEPARATOR+ev.getVerCode();
			if(recogStepMap.containsKey(ev.getRecogstepId())) {
				Map<String,Set<String>> tempstepMap = recogStepMap.get(ev.getRecogstepId());
				if(tempstepMap.containsKey(ev.getStepId())) {					
					tempstepMap.get(ev.getStepId()).add(evString);
				} else {
					Set<String> tempevSet = new HashSet<String>();
					tempevSet.add(evString);
					tempstepMap.put(ev.getStepId(), tempevSet);
				}				
			} else {
				Map<String,Set<String>> tempstepMap = Maps.newHashMap();
				Set<String> tempevSet = new HashSet<String>();
				tempevSet.add(evString);
				tempstepMap.put(ev.getStepId(), tempevSet);
				recogStepMap.put(ev.getRecogstepId(), tempstepMap);
			}
		}
		return recogStepMap;
	}
	
	/**
	 * 根据识别策略ID获取策略信息
	 * @param rsId 识别策略ID
	 * @return
	 */
	public Map<String,Set<String>> getStepEngineMapByrsid(String rsId) {
		
		//策略map信息
		Map<String,Set<String>> stepMap = Maps.newHashMap();		
		if(StringUtils.isBlank(rsId)) return stepMap;
		
		//根据识别策略ID获取对应的策略组信息
		Map<String,String> paramMap = Maps.newHashMap();
		paramMap.put("recogstepId", rsId);
		List<EngineVer> evList = this.engineVerMapper.selectEngineVer(paramMap);
		if(ObjectUtils.isEmpty(evList)) {
			return stepMap;
		}
		
		//根据策略ID分组引擎信息
		for(EngineVer ev:evList) {	
			String evString = ev.getEngineCode()+Constants.SEPARATOR+ev.getVerCode();		
			if(stepMap.containsKey(ev.getStepId())) {					
				stepMap.get(ev.getStepId()).add(evString);
			} else {
				Set<String> tempevSet = new HashSet<String>();
				tempevSet.add(evString);
				stepMap.put(ev.getStepId(), tempevSet);
			}				
		}
		return stepMap;
	}
	
	/**
	 * 查询策略中是否存在此引擎版本
	 * @param engineVerId
	 * @return
	 */
	public int selectEngineVerHaveStep(String engineVerId){
		return   engineVerMapper.selectEngineVerHaveStep(engineVerId);
	}
}
