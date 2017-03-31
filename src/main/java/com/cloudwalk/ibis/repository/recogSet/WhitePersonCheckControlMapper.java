package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;

import com.cloudwalk.ibis.model.recogSet.WhitePersonCheckControl;

public interface WhitePersonCheckControlMapper {

    int deleteByPrimaryKey(String id);

    int insert(WhitePersonCheckControl record);

    int insertSelective(WhitePersonCheckControl record);

    WhitePersonCheckControl selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WhitePersonCheckControl record);

    int updateByPrimaryKey(WhitePersonCheckControl record);
    
    /**
     * 查询审核的渠道信息
     * @param id
     * @return
     */
    List<String>  selectChannelByCheckWhiteId(String  id);
    
    /**
     * 查询已审核的渠道信息
     * @param id
     * @return
     */
    List<String>  selectChannelByWhiteId(String  id);
    
    /**
     * 查询审核的交易信息
     * @param id
     * @return
     */
    List<String>  selectTradingByCheckWhiteId(String id); 
   
    
    /**
     * 查询已审核的交易信息
     * @param id
     * @return
     */
    List<String>  selectTradingByWhiteId(String  id);
    
    /**
     * 查询审核的白名单渠道信息
     * @param id
     * @return
     */
    List<String>  selectChannelCodeByCheckWhiteId(String  id);
    
    /**
     * 查询已审核的白名单渠道信息
     * @param id
     * @return
     */
    List<String>  selectChannelCodeByWhiteId(String  id);
    
    /**
     * 查询已审核的白名单交易信息
     * @param id
     * @return
     */
    List<String>  selectTradingCodeByWhiteId(String  id);
    
    /**
     * 查询审核的白名单交易信息
     * @param id
     * @return
     */
    List<String>  selectTradingCodeByCheckWhiteId(String  id);
    
    int deleteByWhitePersonID(String id);
}