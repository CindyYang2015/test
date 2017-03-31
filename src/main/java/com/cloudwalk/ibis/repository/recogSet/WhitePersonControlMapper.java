package com.cloudwalk.ibis.repository.recogSet;

import com.cloudwalk.ibis.model.recogSet.WhitePersonControl;

public interface WhitePersonControlMapper {


    int deleteByPrimaryKey(String id);

    int insert(WhitePersonControl record);

    int insertSelective(WhitePersonControl record);
    
    int deleteByWhiteID(String id);

    WhitePersonControl selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WhitePersonControl record);

    int updateByPrimaryKey(WhitePersonControl record);
}