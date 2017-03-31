package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.recogSet.Step;

public interface StepMapper {

    int deleteByPrimaryKey(String id);

    int insert(Step record);

    Step selectByPrimaryKey(String id);

    int updateByPrimaryKey(Step record);
    
    List<Step> selectStepPage(Map<String,Object> map);
    
    List<Step> selectStepsByLegal(String legalOrgCode);
    
    int selectStepHaveGroup(String stepId);
    
    /**
     * 根据策略ID获取对应策略组的其他策略对应的引擎信息
     * @param stepId 策略ID
     * @return
     */
    public List<EngineVer> selectOtherEnginesById(String stepId);
    /**
     * 
    * @Title: selectStepByName 
    * @Description: 根据策略名称查询
    * @param @param name
    * @param @return    设定文件 
    * @return List<Step>    返回类型 
    * @author huyuxin
    * @throws
     */
    public List<Step> selectStepByName(String stepName);
}