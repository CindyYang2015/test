package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.recogSet.Recogstep;

public interface RecogstepMapper {
    

    int deleteByPrimaryKey(String id);

    int insert(Recogstep record);

    Recogstep selectByPrimaryKey(String id);

    int updateByPrimaryKey(Recogstep record);
    
    List<Recogstep> selectRecogstepPage(Map<String,Object> map);
    
    List<Recogstep> selectRecogstepAll(String legalOrgCode);    
    
    int selectRecogStepHaveRule(String recogStepId);
    /**
     * 
    * @Title: selectRecogstepByRecogstep 
    * @Description: 根据条件查询识别策略
    * @param @return    设定文件 
    * @return List<Recogstep>    返回类型 
    * @author huyuxin
    * @throws
     */
    List<Recogstep> selectRecogstepByRecogstep(Recogstep recogstep);
}