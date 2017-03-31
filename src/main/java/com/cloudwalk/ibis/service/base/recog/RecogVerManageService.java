package com.cloudwalk.ibis.service.base.recog;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;


/**
 * 识别认证接口版本管理服务
 * @author zhuyf
 *
 */
public class RecogVerManageService {
	
	//分隔符
	public static final String SEPARATE = "_";
	
	/**
	 * 识别服务集合,key：版本代码_接口代码，value:具体的识别服务
	 */
	private Map<String,RecogService> recogMap;		
	
	/**
	 * 根据接口代码和版本代码获取对应的实现类
	 * @param interCode 接口代码
	 * @param verCode 版本代码
	 * @return
	 */
	public RecogService getRecogService(String interCode,String verCode) {
		if(StringUtils.isBlank(interCode) || StringUtils.isBlank(verCode)) return null;
		if(recogMap == null || recogMap.isEmpty()) return null;		
		String key = verCode+SEPARATE+interCode;		
		if(recogMap.containsKey(key)) {
			return recogMap.get(key);
		} else {
			return null;
		}
	}

	public Map<String, RecogService> getRecogMap() {
		return recogMap;
	}

	public void setRecogMap(Map<String, RecogService> recogMap) {
		this.recogMap = recogMap;
	}	

}
