package com.cloudwalk.ibis.service.recogSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.Interface;
import com.cloudwalk.ibis.repository.recogSet.InterfaceMapper;
import com.cloudwalk.ibis.service.base.CacheService;
import com.google.common.collect.Maps;
/**
 * 
 * ClassName: InterfaceServiceImpl 
 * Description: 接口管理业务实现层.
 * date: 2016年9月27日 下午4:38:29 
 *
 * @author 胡钰鑫
 * @version 
 * @since JDK 1.7
 */
@Service("interfaceService")
public class InterfaceService{
	
	@Resource(name = "interfaceMapper")
	private InterfaceMapper interfaceMapper;
	
	@Resource(name = "cacheService")
	private CacheService cacheService;
	
	
	public List<Interface> searchInterface(Interface interf) {
		return this.interfaceMapper.searchAll(interf);
	}

	
	public List<Interface> searchInterfaceByPage(Map<String, Object> map) {
		return this.interfaceMapper.searchAllByPage(map);
	}

	
	public String selectInterface(Interface interf) {
		List<Interface> list=interfaceMapper.selectAll(interf);
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = Maps.newHashMap();
		if (list != null && list.size() > 0) {			
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, Object>();
				Interface inter=list.get(i);
				map.put("interfaceCode", inter.getInterfaceCode());
				map.put("interfaceName", inter.getInterfaceName());
				l.add(map);
			}
			}
		String str = JSONArray.toJSONString(l);
		String subStr ="";
		if(list.size()>0){
		      String nullStr = "{\"interfaceCode\":\"\",\"interfaceName\":\"--请选择--\"},";//选项前加入一个空行
		      subStr = str.substring(str.indexOf("[")+"[".length(), str.length());
		      subStr = "["+subStr;
		    
		    }
		//String str = JSONArray.toJSONString(l);
		return subStr;
	}

	
	public List<Interface> selectInterfaceByPage(Map<String, Object> map) {
		return this.interfaceMapper.selectAllByPage(map);
	}

	
	public Interface getInterface(String id) {
		return this.interfaceMapper.selectByPrimaryKey(id);
	}

	
	public int selectCount(Interface interf) {
		return this.interfaceMapper.selectCount(interf);
	}

	
	public int insertInterface(Interface interf) {
		if (interf == null) {
			return Constants.PARAMETER_ERROR;
		}
		//自增id
		interf.setId(ObjectUtils.createUUID());
		//新增时间（修改时间）
		interf.setCreateTime(new Date());
		interf.setUpdateTime(new Date());
		if(interfaceMapper.insertSelective(interf)>0){
			this.cacheService.cacheInterfaceVers();
			return Constants.INSERT_SUCCESSED;
		} else {
			return Constants.INSERT_FAILURED;
		}
	}

	
	public int deleteInterface(String id) {
		int ret = interfaceMapper.deleteByPrimaryKey(id);
		if(ret > 0) {
			this.cacheService.cacheInterfaceVers();
		}
		return ret;
	}

	
	public int deleteInterfaceBatch(List<String> list) {
		// TODO 自动生成的方法存根
		return 0;
	}

	
	public int updateInterface(Interface interf) {
		if(interf==null){
			return Constants.PARAMETER_ERROR; // 参数错误
		}
		if(interfaceMapper.updateByPrimaryKeySelective(interf)>0){
			this.cacheService.cacheInterfaceVers();
			return Constants.UPDATE_SUCCESSED; // 修改成功
		}else{
			return Constants.UPDATE_FAILURED; // 修改失败
		}
	}
	

}
