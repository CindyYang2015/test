package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;

import com.cloudwalk.ibis.model.recogSet.StepgroupRef;

public interface StepgroupRefMapper {

    int deleteByPrimaryKey(String id);

    int insert(StepgroupRef record);

    StepgroupRef selectByPrimaryKey(String id);

    int updateByPrimaryKey(StepgroupRef record);
    
    List<StepgroupRef> selectStepByGroupId(String groupId);
    
    int deleteStepGroupByGroupId(String groupId);
    
}