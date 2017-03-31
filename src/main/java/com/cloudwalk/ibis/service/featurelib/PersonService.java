package com.cloudwalk.ibis.service.featurelib;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.net.util.Base64;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.file.FileUtils;
import com.cloudwalk.ibis.model.featurelib.Person;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.repository.featurelib.PersonFeatureMapper;
import com.cloudwalk.ibis.repository.featurelib.PersonMapper;
import com.cloudwalk.ibis.service.base.engine.EngineService;
import com.cloudwalk.ibis.service.base.engine.EngineVerManageService;
import com.cloudwalk.ibis.service.system.DicService;

@Service("personService")
public class PersonService {
	@Resource(name = "personMapper")
	private PersonMapper personMapper;
	
	@Resource(name="dicService")
	private DicService dicService;
	
	@Resource(name = "personFeatureMapper")
	private PersonFeatureMapper personFeatureMapper;
	
	/**
	 * 引擎版本管理服务
	 */
	@Resource(name="engineVerManage")
	protected EngineVerManageService engineVerManageService;

	/**
	 * 分页查询客户信息
	 * @param map
	 * @return
	 */
	public List<Person> selectAllByPage(Map<String, Object> map) {
		List<Person> list  = personMapper.selectAllByPage(map);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	
	/**
	 * 根据客户信息查询满足条件的客户列表
	 * @param record
	 * @return
	 */
	public List<Person> searchAll(Person record){
		List<Person> list  = personMapper.searchAll(record);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	
	/**
	 * 根据机构代码，证件类型，证件号码判断客户是否存在
	 * @param record
	 * @return
	 */
	public List<Person> selectPersonBykey(Person record){
		if(record == null) return null;
		Person p = new Person();
		p.setPersonId(null);
		p.setLegalOrgCode(record.getLegalOrgCode());
		p.setCtftype(record.getCtftype());
		p.setCtfno(record.getCtfno());
		List<Person> list  = personMapper.searchAll(p);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	
	/**
	 * 根据客户信息查询满足条件的客户列表
	 * @param record
	 * @return
	 */
	public List<Person> searchAllByPrimaryKey(Person record){
		List<Person> list  = personMapper.searchAll(record);
		return list;
	}
	
	/**
	 * 根据客户ID获取客户信息
	 * @param personId 客户ID
	 * @return
	 */
	public Person selectByPrimaryKey(String personId){
		Person person = new Person();
		person.setPersonId(personId);
		List<Person> list  = personMapper.searchAll(person);
		if(!ObjectUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 逻辑删除客户信息
	 * @param record
	 * @return
	 */
	public int removePersonByPrimaryKey(Person record){
		return personMapper.removePersonByPrimaryKey(record);
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
	 * 新增客户信息
	 * @param record
	 * @return
	 */
	public int insertSelective(Person record){
		return personMapper.insertSelective(record);
	}
	
	/**
	 * 修改客户信息
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(Person record){
		return personMapper.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 根据输入的客户信息查询是否有满足条件的客户进行新增或者修改操作
	 * @param record
	 * @return null表示插入或者保存失败，非null返回插入或保存的personId
	 */
	public String insertOrUpdatePerson(Person record){
		String personId = record.getPersonId();
		record.setPersonId(null);
		List<Person> list = personMapper.searchAll(record);
		if(list != null){
			if(list.size() > 0){
				record.setPersonId(list.get(0).getPersonId());
				int i = personMapper.updateByPrimaryKeySelective(record);
				if(i != 0){
					return record.getPersonId();
				}
			}
		}
		record.setPersonId(personId);
		int j = personMapper.insertSelective(record);
		if(j != 0){
			return personId;
		}
		return null;
	}
	
	/**
	 * 根据personId以及引擎类型版本删除特征信息
	 * @param record
	 * @return
	 */
	public int deleteByPersonIdAndEngine(PersonFeature record){
		return personFeatureMapper.deleteByPersonIdAndEngine(record);
	}
	
	public List<Person> changeList(List<Person> list){
		String channel = dicService.selectDicValuesByDicType(null);
		JSONArray json = JSON.parseArray(channel);
		json.remove(0);
		if(list != null){
			for(int j=0;j<json.size();j++){
				for(int i=0;i<list.size();i++){
					if(json.getJSONObject(j).getString("id").equals(list.get(i).getProperty())){//客户类型
						list.get(i).setProperty(json.getJSONObject(j).getString("text"));
					}
					if(json.getJSONObject(j).getString("id").equals(list.get(i).getCtftype())){//证件类型
						list.get(i).setCtftype(json.getJSONObject(j).getString("text"));
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据特征ID获取客户信息
	 * @param pf
	 * @return
	 */
	public PersonFeature selectPersonByFeatureId(String featureId) {
		PersonFeature pf = new PersonFeature();
		pf.setFeatureId(featureId);
		List<PersonFeature>  pfList = this.personMapper.selectPersonByFeatureId(pf);
		if(!ObjectUtils.isEmpty(pfList)) {
			return pfList.get(0);
		}
		return null;
	}
	
	/**
	 * 新增客户特征信息
	 * @param record
	 * @return
	 */
	public int insertSelectiveByFile(PersonFeature record,byte[] outBuf,String org){
		int ret = 0;
		try {			
			String feature = null;
			if(outBuf != null) {
				String path = this.saveFile(outBuf,record.getEngineCode(),org);
				record.setFilePath(path);
				/** 提取特征 **/
				EngineService engine = this.engineVerManageService.getRecogService(record.getEngineCode(), record.getEngineVerCode());
				if(engine != null){			
					feature = engine.getFeature(Base64.encodeBase64String(outBuf),EnumClass.FileTypeEnum.HDTV.getValue()) == null ? null : engine.getFeature(Base64.encodeBase64String(outBuf),EnumClass.FileTypeEnum.HDTV.getValue());
				}
				record.setFeature(Base64.decodeBase64(feature));
				record.setFeatureType(EnumClass.FileTypeEnum.HDTV.getValue());
				ret = personFeatureMapper.insertSelective(record);
				if(ret > 0) {
					engine.regFeature(Base64.encodeBase64String(outBuf), EnumClass.FileTypeEnum.HDTV.getValue(), record.getFeatureId(), 1);
				}
			} else {
				ret = 1;
			}
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return ret;
	}
	/**
	 * 更新客户特征信息
	 * @param record
	 * @return
	 */
	public int updateSelectiveByFile(PersonFeature record,byte[] outBuf,String org){
		int ret = 0;
		try {			
			String feature = null;
			if(outBuf != null) {
				String path = this.saveFile(outBuf,record.getEngineCode(),org);
				record.setFilePath(path);
				/** 提取特征 **/
				EngineService engine = this.engineVerManageService.getRecogService(record.getEngineCode(), record.getEngineVerCode());
				if(engine != null){			
					feature = engine.getFeature(Base64.encodeBase64String(outBuf),EnumClass.FileTypeEnum.HDTV.getValue()) == null ? null : engine.getFeature(Base64.encodeBase64String(outBuf),EnumClass.FileTypeEnum.HDTV.getValue());
				}
				record.setFeature(Base64.decodeBase64(feature));
				record.setFeatureType(EnumClass.FileTypeEnum.HDTV.getValue());	
				ret = insertOrUpdatePersonFeature(record);
				if(ret > 0) {
					engine.regFeature(Base64.encodeBase64String(outBuf), EnumClass.FileTypeEnum.HDTV.getValue(), record.getFeatureId(), 1);
				}
			} else {
				ret = 1;
			}
			
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return ret;
	}
	
	/**
	 * 根据输入的客户特征信息查询是否有满足条件的客户特征进行新增或者修改操作
	 * @param record
	 * @return
	 */
	private int insertOrUpdatePersonFeature(PersonFeature record){
		String feature = record.getFeatureId();
		record.setFeatureId("");
		List<PersonFeature> list = personFeatureMapper.searchAll(record);
		if(list != null){
			if(list.size() > 0){
				record.setFeatureId(list.get(0).getFeatureId());
				record.setPersonId(list.get(0).getPersonId());
				record.setUpdateTime(record.getCreateTime());
				record.setUpdator(record.getCreator());
				return personFeatureMapper.updateByPrimaryKeySelective(record);
			}
		}
		record.setFeatureId(feature);
		return personFeatureMapper.insertSelective(record);
	}
	
	/**
	 * 保存图片
	 * @param imgData
	 * @return
	 */
	public String saveFile(byte[] imgData,String engineCode,String orgCode) {
		String saveFilePath = "";
		try {
			String channel = Constants.Config.IBIS_FILE_PATH_CHANNEL_DEFAULT;
			String trading = Constants.Config.IBIS_FILE_PATH_TRADING_DEFAULT;
			String sp = Constants.FILE_SPARATOR;
			String curdate = FileUtils.getCurDatePath();
			String libType = EnumClass.LibTypeEnum.LIB_TYPE.getValue();
			saveFilePath = sp+engineCode+sp+libType+curdate+sp
					+orgCode+sp+channel+sp+trading;
			//保存文件
			saveFilePath = FileUtils.saveFileData(imgData, saveFilePath, null);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return saveFilePath;
	}
}
