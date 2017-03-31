package com.cloudwalk.ibis.service.base.cache;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.cache.JedisUtils;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.recogSet.InterfaceVer;
import com.cloudwalk.ibis.model.system.DicValues;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * redis缓存服务
 * @author zhuyf
 *
 */
@Service("jedisCache")
public class JedisCache implements CacheTemplate {
	
	/**
	 * 缓存名称前缀
	 */
	public static String CACHE_NAME_PREFIX = "redis.ibis";
	
	public static String CACHE_NAME_DICTYPE = CACHE_NAME_PREFIX+".dictype";
	
	public static String CACHE_NAME_DICVALUE = CACHE_NAME_PREFIX+".dicvalue";
	
	public static String CACHE_NAME_ENGINEVER = CACHE_NAME_PREFIX+".enginever";
	
	public static String CACHE_NAME_RECOGSTEP = CACHE_NAME_PREFIX+".recogstep";
	
	public static String CACHE_NAME_ORG = CACHE_NAME_PREFIX+".org";
	
	public static String CACHE_NAME_INTERFACEVER = CACHE_NAME_PREFIX+".interfacever";
	
	/**
	 * 初始化
	 */
	public void init() {		
	}
	
	@Override
	public void cacheDics(Map<String,List<DicValues>> dicMap) {
		if(dicMap != null && !dicMap.isEmpty()) {
			for(Entry<String, List<DicValues>> entry:dicMap.entrySet()) {
				if(!ObjectUtils.isEmpty(entry.getValue())) {
					String key = CACHE_NAME_DICTYPE+"."+entry.getKey();
					JedisUtils.setObjectList(key, entry.getValue(), 0);
					for(DicValues dic:entry.getValue()) {
						JedisUtils.set(CACHE_NAME_DICVALUE+"."+dic.getDicCode(), dic.getMeaning(), 0);
					}
				}				
			}
		}
	}	

	@Override
	public void cacheDics(String key, List<DicValues> dics) {
		String tempkey = CACHE_NAME_DICTYPE+"."+key;
		JedisUtils.setObjectList(tempkey, dics, 0);
	}	

	@Override
	public void cacheDics(String key, String value) {
		String tempkey = CACHE_NAME_DICVALUE+"."+key;
		JedisUtils.set(tempkey, value, 0);
	}

	@Override
	public void cacheEngineVers(Map<String,EngineVer> engineVerMap) {
		if(engineVerMap != null && !engineVerMap.isEmpty()){
			for(Entry<String, EngineVer> entry:engineVerMap.entrySet()) {
				String key = CACHE_NAME_ENGINEVER+"."+entry.getKey();
				JedisUtils.setObject(key, entry.getValue(), 0);
			}			
		}	
	}	

	@Override
	public void cacheEngineVers(String key, EngineVer ev) {
		String tempkey = CACHE_NAME_ENGINEVER+"."+key;
		JedisUtils.setObject(tempkey, ev, 0);
	}

	@Override
	public void cacheRecogSteps(Map<String, Map<String, Set<String>>> temRecogStepMap) {
		if(temRecogStepMap != null && !temRecogStepMap.isEmpty()) {
			for(Entry<String, Map<String, Set<String>>> entry:temRecogStepMap.entrySet()) {
				String tempkey = CACHE_NAME_RECOGSTEP+"."+entry.getKey();
				JedisUtils.setObjectMap(tempkey, entry.getValue(), 0);
			}
		}	
	}	

	@Override
	public void cacheRecogSteps(String key, Map<String, Set<String>> value) {
		String tempkey = CACHE_NAME_RECOGSTEP+"."+key;
		JedisUtils.setObjectMap(tempkey, value, 0);
	}

	@Override
	public void cacheOrgCodes(Map<String,String> orgMap) {
		if(orgMap != null && !orgMap.isEmpty()) {
			for(Entry<String, String> entry:orgMap.entrySet()) {
				String tempkey = CACHE_NAME_ORG+"."+entry.getKey();
				JedisUtils.set(tempkey, entry.getValue(), 0);
			}
		}	
	}	

	@Override
	public void cacheOrgCodes(String key, String orgCodePath) {
		String tempkey = CACHE_NAME_ORG+"."+key;
		JedisUtils.set(tempkey, orgCodePath, 0);
	}

	@Override
	public void cacheInterfaceVers(Map<String, InterfaceVer> tempMap) {
		if(tempMap != null && !tempMap.isEmpty()) {
			for(Entry<String, InterfaceVer> entry:tempMap.entrySet()) {
				String tempkey = CACHE_NAME_INTERFACEVER+"."+entry.getKey();
				JedisUtils.setObject(tempkey, entry.getValue(), 0);
			}
		}
	}
	

	@Override
	public void cacheInterfaceVers(String key, InterfaceVer iv) {
		String tempkey = CACHE_NAME_INTERFACEVER+"."+key;
		JedisUtils.setObject(tempkey, iv, 0);
	}

	@Override
	public List<DicValues> getDicValuesByType(String type) {
		String tempkey = CACHE_NAME_DICTYPE+"."+type;
		List<Object> objList = JedisUtils.getObjectList(tempkey);
		List<DicValues> dicValuesList = Lists.newArrayList();
		for(Object obj:objList) {
			dicValuesList.add((DicValues)obj);
		}
		return dicValuesList;
	}

	@Override
	public String getDicNameByCode(String code) {
		String tempkey = CACHE_NAME_DICVALUE+"."+code;
		return JedisUtils.get(tempkey);
	}

	@Override
	public EngineVer getEngineVer(String orgCode, String channel,
			String tradingCode, String engineCode) {
		String key = orgCode+Constants.SEPARATOR+channel+Constants.SEPARATOR+tradingCode+Constants.SEPARATOR+engineCode;
		String tempkey = CACHE_NAME_ENGINEVER+"."+key;
		Object obj = JedisUtils.getObject(tempkey);
		if(obj != null) {
			return (EngineVer)obj;
		}
		return null;
	}

	@Override
	public InterfaceVer getInterfaceVer(String code, String verCode) {
		String key = code+Constants.SEPARATOR+verCode;
		String tempkey = CACHE_NAME_INTERFACEVER+"."+key;
		Object obj = JedisUtils.getObject(tempkey);
		if(obj != null) {
			return (InterfaceVer)obj;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Set<String>> getStepMap(String recogStepId) {
		Map<String, Set<String>> setMap = Maps.newHashMap();
		String tempkey = CACHE_NAME_RECOGSTEP+"."+recogStepId;
		Map<String, Object> objMap = JedisUtils.getObjectMap(tempkey);		
		if(objMap != null && !objMap.isEmpty()) {
			for(Entry<String, Object> entry:objMap.entrySet()) {
				setMap.put(entry.getKey(), (Set<String>)entry.getValue());
			}
		}
		return setMap;
	}

	@Override
	public String getLegalOrgCodePath(String legalOrgCode) {
		String tempkey = CACHE_NAME_ORG+"."+legalOrgCode;
		return JedisUtils.get(tempkey);
	}

}
