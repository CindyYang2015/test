package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.recogSet.StepGroup;

public interface StepGroupMapper {

    int deleteByPrimaryKey(String id);

    int insert(StepGroup record);

    StepGroup selectByPrimaryKey(String id);

    int updateByPrimaryKey(StepGroup record);
    
    List<StepGroup> selectStepGroupPage(Map<String,Object> map);
    
    int selectGroupHaveRecogstep(String groupid);
    
    List<StepGroup> selectStepGroupByLegalOrgCode(String legalOrgCode);
}