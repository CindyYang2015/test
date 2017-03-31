package com.cloudwalk.ibis.service.base.cache;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

/**
 * 缓存管理服务
 * @author zhuyf
 *
 */
@Service("cacheMange")
public class CacheMange {

	@Resource(name="localCache")
	private CacheTemplate localCache;
	
	@Resource(name="jedisCache")
	private CacheTemplate jedisCache;	
	
	@Resource(name="noCache")
	private CacheTemplate noCache;
	
	
	private Map<String,CacheTemplate> cacheTemplateMap = Maps.newHashMap();
	
	public void init() {
		cacheTemplateMap.put("localCache", localCache);
		cacheTemplateMap.put("jedisCache", jedisCache);
		cacheTemplateMap.put("noCache", noCache);
	}
	
	public CacheTemplate getCacheTemplate(String cacheName) {
		return this.cacheTemplateMap.get(cacheName);
	}
}
