package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.recogSet.Engine;

public interface EngineMapper {
    

    int deleteByPrimaryKey(String id);

    int insert(Engine record);

    Engine selectByPrimaryKey(String id);
    
    Engine selectByEngineCode(String engineCode);
  
    int updateByPrimaryKey(Engine record);
    
    int selectCountByEngineCode(String engineCode);
    
    List<Engine> selectEnginePage(Map<String,Object> map);
    
    List<Engine> selectValidEngineAll();
    
    List<Engine> selectValidEngineAllByFeature(Map<String,Object> map);
    
    List<Engine> selectEngineAll();
    
    List<String> selectEngineRepeatByStepIds(List<String> list);
    /**
     * 根据识别策略得到算法引擎
     * @param recogStepId
     * @return
     */
    List<Engine> selectEngineByRecogStepId(String recogStepId);
    /**
     * 查询策略中是否存在此引擎
     * @param engineId
     * @return
     */
    int selectEngineHaveStep(String engineId);
}