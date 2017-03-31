package com.cloudwalk.ibis.service.base.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.recogSet.InterfaceVer;
import com.cloudwalk.ibis.model.system.DicValues;

/**
 * 缓存服务，用于缓存字典等基础数据
 * 
 * @author zhuyf
 *
 */
public interface CacheTemplate {
	
	public void init();
	
	/**
	 * 缓存数据字典
	 */
	public void cacheDics(Map<String,List<DicValues>> dicMap);
	
	/**
	 * 缓存数据字典
	 * @param key 字典类型
	 * @param dics 字典列表
	 */
	public void cacheDics(String key,List<DicValues> dics);
	
	/**
	 * 缓存数据字典
	 * @param key 字典类型
	 * @param value 字典名称
	 */
	public void cacheDics(String key,String value);
	
	/**
	 * 缓存算法引擎版本信息
	 */
	public void cacheEngineVers(Map<String,EngineVer> engineVerMap);
	
	/**
	 * 缓存算法引擎版本信息
	 * @param key 机构代码+渠道+交易代码+引擎代码
	 * @param ev 引擎版本对象
	 */
	public void cacheEngineVers(String key,EngineVer ev);
	
	/**
	 * 缓存识别策略
	 */
	public void cacheRecogSteps(Map<String, Map<String, Set<String>>> temRecogStepMap);
	
	/**
	 * 缓存识别策略
	 * @param key 识别策略ID
	 * @param value 策略map
	 */
	public void cacheRecogSteps(String key,Map<String,Set<String>> value);
	
	/**
	 * 缓存机构代码
	 */
	public void cacheOrgCodes(Map<String,String> orgMap);
	
	/**
	 * 缓存机构代码
	 * @param key 机构代码
	 * @param orgCodePath 机构代码全路径
	 */
	public void cacheOrgCodes(String key,String orgCodePath);
	
	/**
	 * 缓存接口版本信息
	 */
	public void cacheInterfaceVers(Map<String, InterfaceVer> tempMap);
	
	/**
	 * 缓存接口版本信息
	 * @param key 接口代码
	 * @param iv 接口版本对象
	 */
	public void cacheInterfaceVers(String key,InterfaceVer iv);
	
	
	/**
	 * 根据字典类型获取数据字典
	 * @param type 字典类型
	 * @return
	 */
	public List<DicValues> getDicValuesByType(String type);
	
	/**
	 * 根据字典编码获取名称
	 * @param code 字典编码
	 * @return
	 */
	public String getDicNameByCode(String code);
	
	/**
	 * 根据机构代码，渠道，交易代码，引擎代码获取引擎版本信息
	 * @param orgCode 机构代码
	 * @param channel 渠道
	 * @param tradingCode 交易代码
	 * @param engineCode 引擎代码
	 * @return
	 */
	public EngineVer getEngineVer(String orgCode,String channel,String tradingCode,String engineCode);
	
	/**
	 * 根据接口代码和版本代码获取接口版本信息
	 * @param code 接口代码
	 * @param verCode 版本代码
	 * @return
	 */
	public InterfaceVer getInterfaceVer(String code,String verCode);
	
	/**
	 * 根据识别策略ID获取策略信息
	 * @param recogStepId 识别策略ID
	 * @return
	 */
	public Map<String, Set<String>> getStepMap(String recogStepId);
	
	/**
	 * 根据机构代码获取机构代码全路径
	 * @param legalOrgCode
	 * @return
	 */
	public String getLegalOrgCodePath(String legalOrgCode);
}
