package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.recogSet.BlackPersonCheck;
import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonCheckQueryVo;
import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonVo;

public interface BlackPersonCheckMapper {

	int deleteByPrimaryKey(String id);

	int insert(BlackPersonCheck record);

	int insertSelective(BlackPersonCheck record);

	BlackPersonCheck selectByPrimaryKey(String id);
	
	/**
	 * 根据已审核的黑名单ID获取审核的黑名单信息
	 * @param id 已审核的黑名单ID
	 * @return
	 */
	BlackPersonCheck selectByBlackId(String id);	

	BlackPersonVo selectBlackPersonVoByPrimaryKey(String id);
	
	int updateByPrimaryKeySelective(BlackPersonCheck record);

	int updateByPrimaryKey(BlackPersonCheck record);
	
    List<BlackPersonCheckQueryVo> searchAllByPage (Map<String, Object> map) ;
    
    List<BlackPersonCheckQueryVo> searchBlackPersonCheckByPage (Map<String, Object> map) ;
    
    /**
     * 获取黑名单信息
     * @param record
     * @return
     */
    List<BlackPersonCheck> selectBlackPersonChecks(BlackPersonCheck record);
}