package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.recogSet.WhitePersonCheck;
import com.cloudwalk.ibis.model.recogSet.vo.WhitePersonCheckQueryVo;

public interface WhitePersonCheckMapper {


    int deleteByPrimaryKey(String id);

    int insert(WhitePersonCheck record);

    int insertSelective(WhitePersonCheck record);

    WhitePersonCheck selectByPrimaryKey(String id);
    
    /**
     * 根据已审核的白名单ID获取审核的白名单信息
     * @param id
     * @return
     */
    WhitePersonCheck selectByWhileId(String id);
    
    /**
     * 查询已审核表的白名单信息
     * @param id
     * @return
     */
    WhitePersonCheck selectWpByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WhitePersonCheck record);

    int updateByPrimaryKey(WhitePersonCheck record);
    
    List<WhitePersonCheckQueryVo> searchAllByPage (Map<String, Object> map) ;
    
    /**
     * 查询审核表的白名单信息
     * @param map
     * @return
     */
    List<WhitePersonCheckQueryVo> selectWhitePersonCheckByPage(Map<String, Object> map);
    
    /**
     * 获取白名单信息
     * @param record
     * @return
     */
    List<WhitePersonCheck> selectWhitePersonChecks(WhitePersonCheck record);
}