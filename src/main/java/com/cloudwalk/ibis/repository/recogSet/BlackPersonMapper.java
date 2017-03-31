package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;

import com.cloudwalk.ibis.model.recogSet.BlackPerson;
import com.cloudwalk.ibis.model.recogSet.BlackPersonControl;
/**
 * ClassName: BlackPersonMapper <br/>
 * Description: 黑名单信息. <br/>
 * date: Sep 27, 2016 1:52:43 PM <br/>
 *
 * @author 杨维龙
 * @version 
 * @since JDK 1.7
 */
public interface BlackPersonMapper {
	
    int deleteByPrimaryKey(String id);
    
    int insert(BlackPerson record);

    int insertSelective(BlackPerson record);
    
    BlackPerson selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BlackPerson record);

    int updateByPrimaryKey(BlackPerson record);
    
    /**
     * 获取黑名单控制信息
     * @param bpc
     * @return
     */
    public List<BlackPersonControl> selectBlackPersonBykey(BlackPersonControl bpc);

}