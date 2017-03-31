package com.cloudwalk.ibis.repository.recogSet;

import com.cloudwalk.ibis.model.recogSet.BlackPersonControl;

public interface BlackPersonControlMapper {


    int deleteByPrimaryKey(String id);

    int insert(BlackPersonControl record);

    int insertSelective(BlackPersonControl record);

    BlackPersonControl selectByPrimaryKey(String id);
    
    int deleteByBlackId(String id);

    int updateByPrimaryKeySelective(BlackPersonControl record);

    int updateByPrimaryKey(BlackPersonControl record);    
}