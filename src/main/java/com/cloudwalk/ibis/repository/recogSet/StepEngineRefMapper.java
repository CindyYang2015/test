package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;

import com.cloudwalk.ibis.model.recogSet.StepEngineRef;


public interface StepEngineRefMapper {


    int deleteByPrimaryKey(String id);

    int insert(StepEngineRef record);

    StepEngineRef selectByPrimaryKey(String id);

    int updateByPrimaryKey(StepEngineRef record);
    
    public List<StepEngineRef> selectStepEngineByStepId(String stepid);
    
    int deleteByStepId(String stepId);
}