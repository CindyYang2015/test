package com.cloudwalk.ibis.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.spring.SpringBeanUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.system.DicTypes;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.repository.system.DicMapper;
import com.cloudwalk.ibis.repository.system.DicTypesMapper;
import com.cloudwalk.ibis.service.base.CacheService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("dicService")
public class DicService {
	
	@Resource(name = "dicMapper")
	private DicMapper dicMapper;
	
	@Resource(name = "dicTypesMapper")
	private DicTypesMapper dicTypesMapper;		
	

	/**
	 * 删除字典对象
	 *
	 * @author 张强
	 * @param record
	 * @return
	 */
	public int deleteDicValues(DicValues key) {
		int ret = dicMapper.deleteByPrimaryKey(key);
		if(ret > 0) {
			CacheService cacheService = SpringBeanUtil.getBean("cacheService", CacheService.class);
			cacheService.cacheDics();
		}
		return ret;
	}
	
	/**
	 * 获取数据字典类型数据
	 * @return
	 */
	public String getDicTypes() {
		List<DicTypes> dicTypeList = this.dicMapper.selectAllDicTypes(null);
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		if (dicTypeList != null && dicTypeList.size() > 0) {

			for (int i = 0; i < dicTypeList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				DicTypes d = dicTypeList.get(i);
				map.put("id", d.getDicType());
				map.put("text", d.getDescription());
				l.add(map);
			}
		}
		String str = JSONArray.toJSONString(l);
		return str;
	}

	/**
	 *
	 * getgetDicTypesMap:获取字典类型并且转化成map. <br/>
	 *
	 * @author:朱云飞 Date: 2015年9月11日 下午2:16:31
	 * @param dicTypes
	 * @return
	 * @since JDK 1.7
	 */
	public Map<String, String> getDicMap() {
		Map<String, String> dicTypeMap = Maps.newHashMap();
		List<DicValues> list = dicMapper.selectAll(null);
		if (!ObjectUtils.isEmpty(list)) {
			for (DicValues dValues : list) {
				dicTypeMap.put(dValues.getMeaning(), dValues.getDicCode());
			}
		}
		return dicTypeMap;
	}
	
	/**
	 * 获取数据字典键值对数据
	 * @return
	 */
	public Map<String, String> getDicValueMap() {
		Map<String, String> dicValueMap = Maps.newHashMap();
		List<DicValues> list = dicMapper.selectAll(null);
		if (!ObjectUtils.isEmpty(list)) {
			for (DicValues dValues : list) {
				dicValueMap.put( dValues.getDicCode(),dValues.getMeaning());
			}
		}
		return dicValueMap;
	}
	
	/**
	 * 获取数据字典，按照类型分类
	 * @return
	 */
	public Map<String, List<DicValues>> getDicsByTypeMap() {		
		List<DicValues> list = dicMapper.selectAll(null);		
		//根据字典类型设置
		Map<String, List<DicValues>> dicsMap = Maps.newHashMap();
		if (!ObjectUtils.isEmpty(list)) {
			for (DicValues dValues : list) {
				if(dicsMap.containsKey(dValues.getDicType())) {
					dicsMap.get(dValues.getDicType()).add(dValues);
				} else {
					List<DicValues> dValuesList = Lists.newArrayList();
					dValuesList.add(dValues);
					dicsMap.put(dValues.getDicType(), dValuesList);
				}
			}
		}		
		return dicsMap;
	}

	/**
	 * 根据数据字典编码获取含义
	 * @param dicCode 字典编码
	 * @return
	 */
	public String getMeaningByCode(String dicCode) {
		DicValues dicValues = new DicValues();
		dicValues.setDicCode(dicCode);
		List<DicValues> list = dicMapper.selectAll(dicValues);
		if (list.size() > 0) {
			return list.get(0).getMeaning();
		} else {
			return "";
		}
	}

	/**
	 * 新增字典对象
	 *
	 * @author:张强
	 * @param record
	 * @since JDK 1.7
	 */
	public int insertSelective(DicValues record) {
		int ret = dicMapper.insertSelective(record);
		if(ret > 0) {
			CacheService cacheService = SpringBeanUtil.getBean("cacheService", CacheService.class);
			cacheService.cacheDics();
		}		
		return ret;
		
	}

	/**
	 * 根据字典类型+字典编码查询字典对象
	 *
	 * @author:张强
	 * @param key
	 * @since JDK 1.7
	 */
	public DicValues selectByPrimaryKey(DicValues key) {
		return dicMapper.selectByPrimaryKey(key);
	}

	public String selectDicValuesByDicType(DicValues dicValues) {
		List<DicValues> list = dicMapper.selectAll(dicValues);
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = Maps.newHashMap();
		/*map.put("id", "");
		map.put("text", "--请选择--");*/
		l.add(map);
		if (list != null && list.size() > 0) {			
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, Object>();
				DicValues d = list.get(i);
				map.put("id", d.getDicCode());
				map.put("text", d.getMeaning());
				l.add(map);
			}
		}
		String str = JSONArray.toJSONString(l);
		return str;
	}
	/**
	 * 
	* @Title: selectDicValuesByDicTypeAndDicCode 
	* @Description:获取DicCode比传入DicCode大的值
	* @param @return    设定文件 
	* @return String    返回类型 
	* @author huyuxin
	* @throws
	 */
	public List<DicValues> selectDicValuesByDicTypeAndDicCode(DicValues dicValues){
		DicValues newDicValues=new DicValues();
		newDicValues.setDicType(dicValues.getDicType());
		List<DicValues> list = dicMapper.selectAll(newDicValues);
		List<DicValues> newList=new ArrayList<DicValues>();
		for(int i=0;i<list.size();i++){
			//如果查出来的dicCode比传入的dicCode小，则从list中移出
			if(Integer.parseInt(list.get(i).getDicCode())>Integer.parseInt(dicValues.getDicCode())){
				newList.add(list.get(i));
			}
		}
		
		/*List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", "");
		map.put("text", "--请选择--");
		l.add(map);
		if (newList != null && newList.size() > 0) {			
			for (int i = 0; i < newList.size(); i++) {
				map = new HashMap<String, Object>();
				DicValues d = newList.get(i);
				map.put("id", d.getDicCode());
				map.put("text", d.getMeaning());
				l.add(map);
			}
		}
		String str = JSONArray.toJSONString(l);*/
		return newList;
	}
	
	public String selectDicValuesCheckBoxByDicType(DicValues dicValues) {
		List<DicValues> list = dicMapper.selectAll(dicValues);
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = Maps.newHashMap();
		/*map.put("id", "");
		map.put("text", "--请选择--");*/
		l.add(map);
		if (list != null && list.size() > 0) {			
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, Object>();
				DicValues d = list.get(i);
				map.put("id", d.getDicCode());
				map.put("text", d.getMeaning());
				l.add(map);
			}
		}
		String str = JSONArray.toJSONString(l);
		return str;
	}

	public List<DicValues> selectPageDicValues(Map<String, Object> map) {
		return this.dicMapper.selectAllByPage(map);
	}

	/**
	 * 更新字典对象
	 *
	 * @author 张强
	 * @param record
	 * @return
	 */
	public int updateDicValues(DicValues record) {
		int ret = dicMapper.updateByPrimaryKeySelective(record);
		if(ret > 0) {
			CacheService cacheService = SpringBeanUtil.getBean("cacheService", CacheService.class);
			cacheService.cacheDics();
		}
		return ret;
	}
	
	/**
	 * 
	 * batchImportDicValues:(批量导入数据字典). <br/>
	 *
	 * @author:冯德志   Date: 2016年10月13日 上午9:06:17
	 * @param list
	 * @return
	 * @since JDK 1.7
	 */
	public int batchImportDicValues(List<DicValues> list){

	   int count = 0;
	   List<DicValues> dicValuesList = new ArrayList<DicValues>();
	   for(DicValues dicValues:list){
	      //判断字典类型是否存在,存在则不能插入
		 DicTypes dicType = dicMapper.selectDicTypesByKey(dicValues.getDicType());
		 DicValues dicValue = dicMapper.selectByPrimaryKey(dicValues);
	     if(dicType != null && dicValue == null){
	       dicValuesList.add(dicValues);
	     }
	   }
	   
	   if(dicValuesList.size()!=0){
	     count= dicMapper.insertBatch(dicValuesList); 
	     if(count > 0) {
			 CacheService cacheService = SpringBeanUtil.getBean("cacheService", CacheService.class);
	    	 cacheService.cacheDics();
	     }
	   }	   
	   return count;
	}
	
	/**
	 * 根据字典类型查询数据字典
	 * @param type 字典类型
	 * @return
	 */
	public List<DicValues> selectDvsBytype(String type) {
		DicValues dv = new DicValues();
		dv.setDicType(type);
		List<DicValues> list = dicMapper.selectAll(dv);
		return list;
	}	
	
}
