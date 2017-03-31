package com.cloudwalk.ibis.service.base;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.recogSet.InterfaceVer;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.service.base.cache.CacheMange;
import com.cloudwalk.ibis.service.base.cache.CacheTemplate;
import com.cloudwalk.ibis.service.recogSet.EngineVerService;
import com.cloudwalk.ibis.service.recogSet.InterfaceVerService;
import com.cloudwalk.ibis.service.system.DicService;
import com.cloudwalk.ibis.service.system.OrganizationService;

/**
 * 缓存服务，用于缓存字典等基础数据
 * 
 * @author zhuyf
 *
 */
@Service("cacheService")
public class CacheService {
	
	@Resource(name="dicService")
	private DicService dicService;
	
	@Resource(name="engineVerService")
	private EngineVerService engineVerService;
	
	@Resource(name="interfaceVerService")
	private InterfaceVerService interfaceVerService;
	
	@Resource(name="organizationService")
	private OrganizationService orgService;
	
	@Resource(name="cacheMange")
	private CacheMange cacheMange;
	
	/**
	 * 缓存操作对象
	 */
	private CacheTemplate template;
	
	/**
	 * 缓存状态 true表示缓存 false表示不缓存
	 */
	private boolean isCache = false;
	
	@PostConstruct
	public void init() {
		
		//初始化缓存模板对象
		cacheMange.init();
//		template = SpringContextHolder.getBean(Constants.Config.IBIS_CACHE_NAME);
		String cacheName = Constants.Config.IBIS_CACHE_NAME;
		template = cacheMange.getCacheTemplate(cacheName);
		template.init();
		
		//判断是否缓存
		if(!EnumClass.CacheTypeEnum.NO.getValue().equals(cacheName)) {
			
			isCache = true;
			//初始化字典缓存
			this.cacheDics();
			//初始化算法引擎版本信息
			this.cacheEngineVers();
			//初始化接口版本信息
			this.cacheInterfaceVers();
			//缓存识别策略
			this.cacheRecogSteps();
			//缓存机构代码
			this.cacheOrgCodes();
		}
		
	}

	public void cacheDics() {
		if(!this.isCache) return;
		Map<String,List<DicValues>> dicMap = this.dicService.getDicsByTypeMap();
		template.cacheDics(dicMap);
	}

	public void cacheDics(String key, List<DicValues> dics) {
		if(!this.isCache) return;
		template.cacheDics(key, dics);
	}

	public void cacheDics(String key, String value) {
		if(!this.isCache) return;
		template.cacheDics(key, value);
	}

	public void cacheEngineVers() {
		if(!this.isCache) return;
		Map<String,EngineVer> engineVerMap = this.engineVerService.getAllEngineVersMap();
		template.cacheEngineVers(engineVerMap);		
	}

	public void cacheEngineVers(String key, EngineVer ev) {
		if(!this.isCache) return;
		template.cacheEngineVers(key, ev);		
	}

	
	public void cacheRecogSteps() {
		if(!this.isCache) return;
		Map<String, Map<String, Set<String>>> temRecogStepMap = this.engineVerService.getRecogStepEngineMap();
		template.cacheRecogSteps(temRecogStepMap);		
	}

	
	public void cacheRecogSteps(String key, Map<String, Set<String>> value) {
		if(!this.isCache) return;
		template.cacheRecogSteps(key, value);		
	}

	
	public void cacheOrgCodes() {
		if(!this.isCache) return;
		Map<String,String> orgMap = this.orgService.getLegalOrgMap();
		template.cacheOrgCodes(orgMap);		
	}

	
	public void cacheOrgCodes(String key, String orgCodePath) {
		if(!this.isCache) return;
		template.cacheOrgCodes(key, orgCodePath);		
	}

	
	public void cacheInterfaceVers() {
		if(!this.isCache) return;
		Map<String, InterfaceVer> tempMap = this.interfaceVerService.getAllInterfaceVersMap();
		template.cacheInterfaceVers(tempMap);		
	}

	
	public void cacheInterfaceVers(String key, InterfaceVer iv) {
		if(!this.isCache) return;
		template.cacheInterfaceVers(key, iv);		
	}

	
	public List<DicValues> getDicValuesByType(String type) {
		return template.getDicValuesByType(type);
	}

	
	public String getDicNameByCode(String code) {
		return template.getDicNameByCode(code);
	}

	
	public EngineVer getEngineVer(String orgCode, String channel,
			String tradingCode, String engineCode) {
		return template.getEngineVer(orgCode, channel, tradingCode, engineCode);
	}

	
	public InterfaceVer getInterfaceVer(String code, String verCode) {
		return template.getInterfaceVer(code, verCode);
	}

	
	public Map<String, Set<String>> getStepMap(String recogStepId) {
		return template.getStepMap(recogStepId);
	}

	
	public String getLegalOrgCodePath(String legalOrgCode) {
		return template.getLegalOrgCodePath(legalOrgCode);
	}	
	
}
