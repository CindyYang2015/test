package com.cloudwalk.ibis.service.base.cache;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.recogSet.InterfaceVer;
import com.cloudwalk.ibis.model.system.DicValues;
import com.google.common.collect.Maps;

/**
 * 当前服务器缓存
 * @author zhuyf
 *
 */
@Service("localCache")
public class LocalCache implements CacheTemplate {	

	/**
	 * 数据字典缓存，按照类型分类
	 */
	private Map<String,List<DicValues>> dicMap;
	
	/**
	 * 数据字典缓存，key:字典编码 value:名称
	 */
	private Map<String,String> dicValueMap;
	
	/**
	 * 算法引擎版本缓存，按照机构，渠道，交易类型，引擎代码分类
	 */
	private Map<String,EngineVer> engineVerMap;
	
	/**
	 * 接口版本信息缓存，key：接口代码_接口版本代码，vaule:接口版本信息
	 */
	private Map<String,InterfaceVer> interfaceVerMap;
	
	/**
	 * 识别策略缓存，key：识别策略ID，value：策略
	 * 策略，key:策略ID,value:算法引擎版本列表
	 */
	private Map<String,Map<String,Set<String>>> recogStepMap;
	
	/**
	 * 法人机构代码缓存，key:机构代码 value:机构代码全路径
	 */
	private Map<String,String> legalOrgMap;

	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {		
		dicMap = Maps.newHashMap();
		engineVerMap = Maps.newHashMap();
		interfaceVerMap = Maps.newHashMap();
		recogStepMap = Maps.newHashMap();
		legalOrgMap = Maps.newHashMap();
		dicValueMap = Maps.newHashMap();		
	}	
	

	@Override
	public void cacheDics(String key, List<DicValues> dics) {
	
	}



	@Override
	public void cacheEngineVers(String key, EngineVer ev) {
		
	}


	@Override
	public void cacheRecogSteps(String key, Map<String, Set<String>> value) {
		
	}


	@Override
	public void cacheOrgCodes(String key, String orgCodePath) {
		
	}


	@Override
	public void cacheInterfaceVers(String key, InterfaceVer iv) {
		
	}

	@Override
	public void cacheDics(String key, String value) {
		
	}


	@Override
	public void cacheDics(Map<String,List<DicValues>> dicMap) {		
		
		if(dicMap != null) {
			
			this.dicMap.clear();
			this.dicMap.putAll(dicMap);	
			
			this.dicValueMap.clear();
			if(!dicMap.isEmpty()) {
				for(Entry<String, List<DicValues>> entry:dicMap.entrySet()) {
					List<DicValues> dicValues = entry.getValue();
					for(DicValues dicValue:dicValues) {
						this.dicValueMap.put(dicValue.getDicCode(), dicValue.getMeaning());
					}
				}
			}	
			
		}	
	}

	@Override
	public void cacheEngineVers(Map<String,EngineVer> engineVerMap) {
		if(engineVerMap != null) {
			this.engineVerMap.clear();
			this.engineVerMap.putAll(engineVerMap);
		}	
	}

	@Override
	public void cacheRecogSteps(Map<String, Map<String, Set<String>>> temRecogStepMap) {
		if(temRecogStepMap != null) {
			this.recogStepMap.clear();
			this.recogStepMap.putAll(temRecogStepMap);
		}	
	}

	@Override
	public void cacheOrgCodes(Map<String,String> orgMap) {
		if(orgMap != null) {
			this.legalOrgMap.clear();
			this.legalOrgMap.putAll(orgMap);
		}		
	
	}

	@Override
	public void cacheInterfaceVers(Map<String, InterfaceVer> tempMap) {
		if(tempMap != null) {
			this.interfaceVerMap.clear();
			this.interfaceVerMap.putAll(tempMap);
		}
	
	}

	@Override
	public List<DicValues> getDicValuesByType(String type) {
		if(this.dicMap.containsKey(type)) {
			return this.dicMap.get(type);
		}
		return null;
	
	}

	@Override
	public String getDicNameByCode(String code) {
		if(this.dicValueMap.containsKey(code)) {
			return this.dicValueMap.get(code);
		}
		return null;
	}

	@Override
	public EngineVer getEngineVer(String orgCode, String channel,
			String tradingCode, String engineCode) {
		String key = orgCode+Constants.SEPARATOR+channel+Constants.SEPARATOR+tradingCode+Constants.SEPARATOR+engineCode;
		if(this.engineVerMap.containsKey(key)) {
			return this.engineVerMap.get(key);
		}
		return null;
	}

	@Override
	public InterfaceVer getInterfaceVer(String code, String verCode) {
		String key = code+Constants.SEPARATOR+verCode;
		if(this.interfaceVerMap.containsKey(key)) {
			return this.interfaceVerMap.get(key);
		}
		return null;
	}

	@Override
	public Map<String, Set<String>> getStepMap(String recogStepId) {
		if(this.recogStepMap.containsKey(recogStepId)) {
			return this.recogStepMap.get(recogStepId);
		}
		return null;
	}

	@Override
	public String getLegalOrgCodePath(String legalOrgCode) {
		if(this.legalOrgMap.containsKey(legalOrgCode)) {
			return this.legalOrgMap.get(legalOrgCode);
		}
		return null;
	}

}
