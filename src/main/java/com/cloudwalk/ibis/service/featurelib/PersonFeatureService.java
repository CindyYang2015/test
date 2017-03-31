package com.cloudwalk.ibis.service.featurelib;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.recogSet.Engine;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.repository.featurelib.PersonFeatureMapper;
import com.cloudwalk.ibis.service.recogSet.EngineVerService;
import com.cloudwalk.ibis.service.system.DicService;

@Service("personFeatureService")
public class PersonFeatureService {
	@Resource(name = "personFeatureMapper")
	private PersonFeatureMapper personFeatureMapper;
	
	@Resource(name="dicService")
	private DicService dicService;
	
	@Resource(name = "engineVerService")
	private EngineVerService engineVerService;

	/**
	 * 分页查询客户特征信息
	 * @param map
	 * @return
	 */
	public List<PersonFeature> selectAllByPage(Map<String, Object> map) {
		List<PersonFeature> list = personFeatureMapper.selectAllByPage(map);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	
	/**
	 * 根据客户输入的特征信息查询特征信息列表
	 * @param record
	 * @return
	 */
	public List<PersonFeature> searchAll(PersonFeature record){
		List<PersonFeature> list = personFeatureMapper.searchAll(record);
		return list;
	}
	
	/**
	 * 查询带特征值的客户特征信息
	 * @param record
	 * @return
	 */
	public PersonFeature selectPersonFeature(PersonFeature record){
		List<PersonFeature> list = personFeatureMapper.selectPersonFeature(record);
		if(!ObjectUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据客户输入的特征信息查询特征信息列表
	 * @param record
	 * @return
	 */
	public List<PersonFeature> searchAllByPrimaryKey(PersonFeature record){
		List<PersonFeature> list = personFeatureMapper.searchAllByPrimaryKey(record);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	
	/**
	 * 新增客户特征信息
	 * @param record
	 * @return
	 */
	public int insertSelective(PersonFeature record){
		return personFeatureMapper.insertSelective(record);
	}
	
	/**
	 * 修改客户特征信息
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(PersonFeature record){
		return personFeatureMapper.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 根据输入的客户特征信息查询是否有满足条件的客户特征进行新增或者修改操作
	 * @param record
	 * @return
	 */
	public int insertOrUpdatePersonFeature(PersonFeature record){
		String feature = record.getFeatureId();
		record.setFeatureId("");
		List<PersonFeature> list = personFeatureMapper.searchAll(record);
		if(list != null){
			if(list.size() > 0){
				record.setFeatureId(list.get(0).getFeatureId());
				record.setPersonId(list.get(0).getPersonId());
				return personFeatureMapper.updateByPrimaryKeySelective(record);
			}
		}
		record.setFeatureId(feature);
		return personFeatureMapper.insertSelective(record);
	}
	
	/**
	 * 根据featureId查询filePath
	 * @param record
	 * @return
	 */
	public String selectPathByFeatureId(PersonFeature record){
		if(record.getPersonId() == null || "".equals(record.getPersonId())){
			return null;
		}
		return personFeatureMapper.selectPathByFeatureId(record);
	}
	
	/**
	 * 根据特征信息得到算法引擎信息
	 * @param personId
	 * @return
	 */
	public List<Engine> selectValidEngineAllByFeature(Integer partitionId,String personId){
		return engineVerService.selectValidEngineAllByFeature(partitionId,personId);
	}
	
	/**
	 * 根据特征信息得到版本
	 * @param code
	 * @return
	 */
	public List<EngineVer> selectByCodeFromFeature(PersonFeature code){
		return engineVerService.selectByCodeFromFeature(code);
	}
	
	public List<PersonFeature> changeList(List<PersonFeature> list){
		String channel = dicService.selectDicValuesByDicType(null);
		JSONArray json = JSON.parseArray(channel);
		json.remove(0);
		if(list != null){
			for(int j=0;j<json.size();j++){
				for(int i=0;i<list.size();i++){
					if(json.getJSONObject(j).getString("id").equals(list.get(i).getEngineType())){//算法引擎类型
						list.get(i).setEngineType(json.getJSONObject(j).getString("text"));
					}
				}
			}
		}
		return list;
	}
}
