package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;

import com.cloudwalk.ibis.model.recogSet.BlackPersonCheckControl;

public interface BlackPersonCheckControlMapper {


    int deleteByPrimaryKey(String id);

    int insert(BlackPersonCheckControl record);

    int insertSelective(BlackPersonCheckControl record);
    
    int deleteByCheckBlackPersonID(String id);

    BlackPersonCheckControl selectByPrimaryKey(String id);


    int updateByPrimaryKeySelective(BlackPersonCheckControl record);

    int updateByPrimaryKey(BlackPersonCheckControl record);
    
    /**
     * 查询已审核过的渠道信息
     * @param id
     * @return
     */
    List<String>  selectChannelByBlackId(String  id);
    
    /**
     * 查询审核表的渠道信息
     * @param id
     * @return
     */
    List<String>  selectChannelByCheckBlackId(String  id);    
    
    /**
     * 查询已审核过的交易信息
     * @param id
     * @return
     */
    List<String>  selectTradingByBlackId(String  id);
    
    /**
     * 查询审核表的交易信息
     * @param id
     * @return
     */
    List<String>  selectTradingByCheckBlackId(String  id);
    
    List<String>  selectChannelCodeByBlackId(String  id);
    
    List<String>  selectTradingCodeByBlackId(String  id);
    
    /**
     * 查询审核表的渠道信息
     * @param id
     * @return
     */
    List<String>  selectChannelCodeByCheckBlackId(String  id);
    
    /**
     * 查询审核表的交易信息
     * @param id
     * @return
     */
    List<String>  selectTradingCodeByCheckBlackId(String  id);
}