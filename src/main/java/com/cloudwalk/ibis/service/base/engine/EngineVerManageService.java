package com.cloudwalk.ibis.service.base.engine;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 算法引擎接口版本管理对象
 * @author zhuyf
 *
 */
public class EngineVerManageService {

	//分隔符
	public static final String SEPARATE = "_";
	
	/**
	 * 引擎服务集合,key：版本代码_引擎代码，value:具体的引擎服务
	 */
	private Map<String,EngineService> engineMap;	
	
	/**
	 * 根据引擎代码和版本代码获取对应的实现类
	 * @param interCode 引擎代码
	 * @param verCode 版本代码
	 * @return
	 */
	public EngineService getRecogService(String engineCode,String verCode) {
		if(StringUtils.isBlank(engineCode) || StringUtils.isBlank(verCode)) return null;
		if(engineMap == null || engineMap.isEmpty()) return null;
		String key = verCode+SEPARATE+engineCode;
		if(engineMap.containsKey(key)) {
			return engineMap.get(key);
		} else {
			return null;
		}
	}

	public Map<String, EngineService> getEngineMap() {
		return engineMap;
	}

	public void setEngineMap(Map<String, EngineService> engineMap) {
		this.engineMap = engineMap;
	}	
		
}
