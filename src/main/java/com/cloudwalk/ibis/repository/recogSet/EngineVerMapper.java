package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.google.code.ssm.api.ReadThroughSingleCache;

public interface EngineVerMapper {
    

    int deleteByPrimaryKey(String id);

    int insert(EngineVer record);

    EngineVer selectByPrimaryKey(String id);

    int updateByPrimaryKey(EngineVer record);
    
    List<EngineVer> selectEngineVerPage(Map<String,Object> map);
    
    List<EngineVer> selectByEngineVer(Map<String,String> map);
    
    List<EngineVer> selectByCode(String code);
    List<EngineVer> selectByCodeFromFeature(PersonFeature code);
    
    /**
	 * 根据机构代码，渠道，交易代码，引擎代码获取算法引擎版本信息
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
    @ReadThroughSingleCache(namespace ="EngineVer", expiration = 30)
    List<EngineVer> selectEngineVer(Map<String,String> map) throws DataAccessException;
    
    /**
     * 获取所有的算法引擎版本信息
     * @return
     * @throws DataAccessException
     */
    List<EngineVer> selectAllEngineVer() throws DataAccessException;
    
    
    /**
     * 查询策略中是否存在此引擎版本
     * @param engineVerId
     * @return
     */
    int selectEngineVerHaveStep(String engineVerId);
    
}