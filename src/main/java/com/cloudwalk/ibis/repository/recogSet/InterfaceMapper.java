
package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.recogSet.Interface;
/**
 * 
 * ClassName: InterfaceMapper 
 * Description: 接口信息管理mapper.
 * date: 2016年9月29日 上午11:00:45 
 *
 * @author 胡钰鑫
 * @version 
 * @since JDK 1.7
 */
public interface InterfaceMapper {
	/**
	 * 
	 * selectByPrimaryKey:根据主键查询.
	 * @author:胡钰鑫   Date: 2016年9月27日 下午4:15:24
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	public Interface selectByPrimaryKey(String id);
	/**
	 * 
	 * selectAll:根据条件精确查询.
	 * @author:胡钰鑫   Date: 2016年9月27日 下午4:17:07
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<Interface> selectAll(Interface interf);
	/**
	 * 
	 * selectAllByPage:精准分页查询.
	 * @author:胡钰鑫   Date: 2016年9月27日 下午5:51:18
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<Interface> selectAllByPage(Map<String, Object> map);
	/**
	 * 
	 * searchAll:根据条件模糊查询.
	 * @author:胡钰鑫   Date: 2016年9月27日 下午4:17:46
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<Interface> searchAll(Interface interf);
	/**
	 * 
	 * searchAllByPage:模糊分页查询.
	 * @author:胡钰鑫   Date: 2016年9月27日 下午5:53:00
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<Interface> searchAllByPage(Map<String, Object> map);
	/**
	 * 查询结果的总计
	 * selectCount:(这里用一句话描述这个方法的作用).
	 * @author:胡钰鑫   Date: 2016年9月27日 下午4:19:48
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public int selectCount(Interface interf);
	/**
	 * 
	 * deleteByPrimaryKey:根据主键删除.
	 * @author:胡钰鑫   Date: 2016年9月27日 下午4:21:19
	 * @param interf
	 * @return
	 * @since JDK 1.7
	 */
	public int deleteByPrimaryKey(String id);
	/**
	 * 
	 * deleteBatch:批量删除.
	 * @author:胡钰鑫   Date: 2016年9月27日 下午4:29:57
	 * @param list
	 * @return
	 * @since JDK 1.7
	 */
	public int deleteBatch(List<String> list);
	/**
	 * 
	 * insertSelective:新增一条数据.
	 * @author:胡钰鑫   Date: 2016年9月27日 下午4:22:14
	 * @param interf
	 * @return
	 * @since JDK 1.7
	 */
	public int insertSelective(Interface interf);
	/**
	 * 
	 * updateByPrimaryKeySelective:修改一条数据.
	 * @author:胡钰鑫   Date: 2016年9月27日 下午4:22:52
	 * @param interf
	 * @return
	 * @since JDK 1.7
	 */
	public int updateByPrimaryKeySelective(Interface interf);
	
	
	
	
}