package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;

import com.cloudwalk.ibis.model.recogSet.WhitePerson;

public interface WhitePersonMapper {


    int deleteByPrimaryKey(String id);

    int insert(WhitePerson record);

    int insertSelective(WhitePerson record);

    WhitePerson selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WhitePerson record);

    int updateByPrimaryKey(WhitePerson record);
    
    /**
     * 根据机构，渠道，交易代码，引擎信息，客户信息获取白名单信息
     * @param record
     * @return
     */
    public List<WhitePerson> selectWhitePersonBykey(WhitePerson record);
}