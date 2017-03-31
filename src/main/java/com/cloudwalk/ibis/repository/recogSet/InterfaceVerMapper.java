
 
package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.recogSet.InterfaceVer;
import com.cloudwalk.ibis.model.recogSet.vo.InterfaceVerVo;

/**
 * 
 * ClassName: InterfaceVerMapper 
 * Description:接口版本信息管理.
 * date: 2016年9月29日 上午11:01:05 
 *
 * @author 胡钰鑫
 * @version 
 * @since JDK 1.7
 */
public interface InterfaceVerMapper {
	/**
	 * 
	 * selectByPrimaryKey:根据主键查询.
	 * @author:胡钰鑫   Date: 2016年9月29日 上午11:01:45
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	public InterfaceVer selectByPrimaryKey(String id);
	/**
	 * 
	 * selectAll:根据条件精确查询.
	 * @author:胡钰鑫   Date: 2016年9月29日 上午11:02:37
	 * @param interfaceVer
	 * @return
	 * @since JDK 1.7
	 */
	public List<InterfaceVer> selectAll(InterfaceVer interfaceVer);
	/**
	 * 
	 * selectAllByPage:精准分页查询.
	 * @author:胡钰鑫   Date: 2016年9月29日 上午11:03:14
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<InterfaceVerVo> selectAllByPage(Map<String, Object> map);
	/**
	 * 
	 * searchAll:根据条件模糊查询.
	 * @author:胡钰鑫   Date: 2016年9月29日 上午11:03:55
	 * @param interfaceVer
	 * @return
	 * @since JDK 1.7
	 */
	public List<InterfaceVer> searchAll(InterfaceVer interfaceVer);
	/**
	 * 
	 * searchAllByPage:模糊分页查询.
	 * @author:胡钰鑫   Date: 2016年9月29日 上午11:04:24
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<InterfaceVer> searchAllByPage(Map<String, Object> map);
	/**
	 * 
	 * deleteByPrimaryKey:根据主键删除.
	 * @author:胡钰鑫   Date: 2016年9月29日 上午11:05:03
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	public int deleteByPrimaryKey(String id);
	/**
	 * 
	 * insertSelective:新增一条数据.
	 * @author:胡钰鑫   Date: 2016年9月29日 上午11:06:10
	 * @param interfaceVer
	 * @return
	 * @since JDK 1.7
	 */
	public int insertSelective(InterfaceVer interfaceVer);
	/**
	 * 
	 * updateByPrimaryKeySelective:修改一条数据.
	 * @author:胡钰鑫   Date: 2016年9月29日 上午11:06:38
	 * @param interfaceVer
	 * @return
	 * @since JDK 1.7
	 */
	public int updateByPrimaryKeySelective(InterfaceVer interfaceVer);
	
}