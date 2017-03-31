package com.cloudwalk.ibis.service.recogSet;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.spring.SpringBeanUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.InterfaceVer;
import com.cloudwalk.ibis.model.recogSet.vo.InterfaceVerVo;
import com.cloudwalk.ibis.repository.recogSet.InterfaceVerMapper;
import com.cloudwalk.ibis.service.base.CacheService;
import com.google.common.collect.Maps;
/**
 * 
 * ClassName: InterfaceVerServiceImpl 
 * Description: 接口版本管理业务实现层.
 * date: 2016年9月27日 下午4:38:29 
 *
 * @author 胡钰鑫
 * @version 
 * @since JDK 1.7
 */
@Service("interfaceVerService")
public class InterfaceVerService{
	
	@Resource(name = "interfaceVerMapper")
	private InterfaceVerMapper interfaceVerMapper;
	
	
	public List<InterfaceVer> searchInterfaceVer(InterfaceVer interfaceVer) {
		return this.interfaceVerMapper.searchAll(interfaceVer);
	}

	
	public List<InterfaceVer> searchInterfaceVerByPage(Map<String, Object> map) {
		return this.interfaceVerMapper.searchAllByPage(map);
	}

	
	public List<InterfaceVer> selectInterfaceVer(InterfaceVer interfaceVer) {
		return this.interfaceVerMapper.selectAll(interfaceVer);
	}

	
	public List<InterfaceVerVo> selectInterfaceVerByPage(Map<String, Object> map) {
		return this.interfaceVerMapper.selectAllByPage(map);
	}

	
	public InterfaceVer getInterfaceVer(String id) {
		return this.interfaceVerMapper.selectByPrimaryKey(id);
	}

	
	public int selectCount(InterfaceVer interfaceVer) {
		return 0;
	}

	
	public int insertInterfaceVer(InterfaceVer interfaceVer) {
		if (interfaceVer == null) {
			return Constants.PARAMETER_ERROR;
		}
		//自增id
		interfaceVer.setId(ObjectUtils.createUUID());
		//新增时间（修改时间）
		interfaceVer.setCreateTime(new Date());
		interfaceVer.setUpdateTime(new Date());
		if(interfaceVerMapper.insertSelective(interfaceVer)>0){
			CacheService cacheService = SpringBeanUtil.getBean("cacheService", CacheService.class);
			cacheService.cacheInterfaceVers();
			return Constants.INSERT_SUCCESSED;
		} else {
			return Constants.INSERT_FAILURED;
		}
	}

	
	public int deleteInterfaceVer(String id) {
		int ret = interfaceVerMapper.deleteByPrimaryKey(id);
		if(ret > 0) {
			CacheService cacheService = SpringBeanUtil.getBean("cacheService", CacheService.class);
			cacheService.cacheInterfaceVers();
		}
		return ret;
	}

	
	public int deleteInterfaceVerBatch(List<String> list) {
		return 0;
	}

	
	public int updateInterfaceVer(InterfaceVer interfaceVer) {
		if(interfaceVer==null){
			return Constants.PARAMETER_ERROR; // 参数错误
		}
		if(interfaceVerMapper.updateByPrimaryKeySelective(interfaceVer)>0){
			CacheService cacheService = SpringBeanUtil.getBean("cacheService", CacheService.class);
			cacheService.cacheInterfaceVers();
			return Constants.UPDATE_SUCCESSED; // 修改成功
		}else{
			return Constants.UPDATE_FAILURED; // 修改失败
		}
	}

	/**
	 * 获取所有的接口版本信息
	 * @return
	 */
	
	public Map<String, InterfaceVer> getAllInterfaceVersMap() {
		Map<String,InterfaceVer> interfaceVerMap = Maps.newHashMap();
		InterfaceVer interfaceVer = new InterfaceVer();
		interfaceVer.setStatus(EnumClass.StatusEnum.YES.getValue());
		List<InterfaceVer> interfaceVerList = this.interfaceVerMapper.selectAll(interfaceVer);
		if(ObjectUtils.isEmpty(interfaceVerList)) {
			return interfaceVerMap;
		}
		for(InterfaceVer iv:interfaceVerList) {
			String key = iv.getInterfaceCode()+Constants.SEPARATOR+iv.getVerCode();
			interfaceVerMap.put(key, iv);
		}
		return interfaceVerMap;	
	}
	
	
	
	

}
