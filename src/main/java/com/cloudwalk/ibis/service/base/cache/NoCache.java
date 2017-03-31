package com.cloudwalk.ibis.service.base.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.recogSet.InterfaceVer;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.model.system.Organization;
import com.cloudwalk.ibis.service.recogSet.EngineVerService;
import com.cloudwalk.ibis.service.recogSet.InterfaceVerService;
import com.cloudwalk.ibis.service.system.DicService;
import com.cloudwalk.ibis.service.system.OrganizationService;

/**
 * 当前服务器基础数据不缓存查询
 * @author zhuyf
 *
 */
@Service("noCache")
public class NoCache implements CacheTemplate {

	@Resource(name="dicService")
	private DicService dicService;
	
	@Resource(name="engineVerService")
	private EngineVerService engineVerService;
	
	@Resource(name="interfaceVerService")
	private InterfaceVerService interfaceVerService;
	
	@Resource(name="organizationService")
	private OrganizationService orgService;	
	
	@Override
	public void init() {		
	}

	@Override
	public void cacheDics(Map<String, List<DicValues>> dicMap) {		
	}

	@Override
	public void cacheDics(String key, List<DicValues> dics) {		
	}

	@Override
	public void cacheDics(String key, String value) {		
	}

	@Override
	public void cacheEngineVers(Map<String, EngineVer> engineVerMap) {		
	}

	@Override
	public void cacheEngineVers(String key, EngineVer ev) {		
	}

	@Override
	public void cacheRecogSteps(
			Map<String, Map<String, Set<String>>> temRecogStepMap) {		
	}

	@Override
	public void cacheRecogSteps(String key, Map<String, Set<String>> value) {		
	}

	@Override
	public void cacheOrgCodes(Map<String, String> orgMap) {		
	}

	@Override
	public void cacheOrgCodes(String key, String orgCodePath) {
	}

	@Override
	public void cacheInterfaceVers(Map<String, InterfaceVer> tempMap) {
	}

	@Override
	public void cacheInterfaceVers(String key, InterfaceVer iv) {
	}

	@Override
	public List<DicValues> getDicValuesByType(String type) {
		return this.dicService.selectDvsBytype(type);
	}

	@Override
	public String getDicNameByCode(String code) {		
		return this.dicService.getMeaningByCode(code);
	}

	@Override
	public EngineVer getEngineVer(String orgCode, String channel,
			String tradingCode, String engineCode) {
		return this.engineVerService.getEngineVerBykey(orgCode, channel, tradingCode, engineCode);
	}

	@Override
	public InterfaceVer getInterfaceVer(String code, String verCode) {
		
		//根据接口代码，版本代码获取接口信息
		InterfaceVer interfaceVer = new InterfaceVer();
		interfaceVer.setInterfaceCode(code);
		interfaceVer.setVerCode(verCode);
		interfaceVer.setStatus(EnumClass.StatusEnum.YES.getValue());
		List<InterfaceVer> interfaceVerList = this.interfaceVerService.selectInterfaceVer(interfaceVer);
		if(ObjectUtils.isEmpty(interfaceVerList)) {
			return null;
		}
		
		return interfaceVerList.get(0);
	}

	@Override
	public Map<String, Set<String>> getStepMap(String recogStepId) {
		return this.engineVerService.getStepEngineMapByrsid(recogStepId);
	}

	@Override
	public String getLegalOrgCodePath(String legalOrgCode) {
		 Organization org = this.orgService.getOrgByOrgCode(legalOrgCode);
		 if(org == null) return "";
		 return org.getOrgCodePath();
	}		

	

}
