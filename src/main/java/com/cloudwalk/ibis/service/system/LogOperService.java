package com.cloudwalk.ibis.service.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.ibis.model.system.LogOper;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.repository.system.LogOperMapper;

/**
 *
 * ClassName: LogOperService <br/>
 * Description: 操作日志service. <br/>
 * date: 2015年8月31日 下午2:23:48 <br/>
 *
 * @author zhangqiang
 * @version
 * @since JDK 1.7
 */
@Service("logOperService")
public class LogOperService {

	@Resource(name = "logOperMapper")
	private LogOperMapper logOperMapper;

	/**
	 *
	 * insertSelective:添加操作日志信息. <br/>
	 *
	 * @author:张强 Date: 2015年8月31日 下午1:47:32
	 * @param request
	 * @param status
	 *            0：失败，1：成功
	 * @param remark
	 * @return
	 * @since JDK 1.7
	 */
	public int insertLogOper(HttpServletRequest request, int status,
			String remark) {
		
		String operName = null;
		String uri = request.getServletPath();
		LogOper logOper = new LogOper();
		User user = BaseUtil.getCurrentUser(request);
		Map<String, String> authUrlMap = user.getAuthUrlMap();
		operName = this.getOperName(authUrlMap, uri);
		operName = operName.equals("/login/login") ? "用户登录" : operName;
		logOper.setLoginName(user.getWorkCode());
		logOper.setUserName(user.getUserName());
		logOper.setIpAddress(request.getRemoteAddr());
		logOper.setOperName(operName);
		logOper.setRemark(remark);
		logOper.setStatus((short) status);
		logOper.setCreateTime(new Date());
		logOper.setDeptCode(user.getDeptCode());
		logOper.setOrgCode(user.getOrgCode());
		logOper.setDeptName(user.getDeptName());
		logOper.setOrgName(user.getOrgAName());
		
		if (logOperMapper.insertSelective(logOper) > 0) {
			return Constants.INSERT_SUCCESSED;
		} else {
			return Constants.INSERT_FAILURED;
		}
	}

	/**
	 * insertSelective:添加操作日志信息. <br/>
	 * @param logOper
	 * @return
	 */
	public int insertLogOper(LogOper logOper){
		if (logOperMapper.insertSelective(logOper) > 0) {
			return Constants.INSERT_SUCCESSED;
		} else {
			return Constants.INSERT_FAILURED;
		}
	}
	/**
	 * 根据uri获取对应的操作类型
	 * @param authUrlMap
	 * @param uri
	 * @return
	 */
	private String getOperName(Map<String, String> authUrlMap, String uri){
		
		String keyIterate = null;
		if(authUrlMap.isEmpty()){
			return uri;
		}
		
		for (Map.Entry<String, String> entry : authUrlMap.entrySet()) {
			keyIterate = entry.getKey();
			if(keyIterate.contains(uri)){
				return entry.getValue();
			}
		} 
		
		return uri;
	}
	/**
	 *
	 * searchAllLogOper:搜素系统操作日志. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月31日 下午5:02:03
	 * @param logOper
	 * @return
	 * @since JDK 1.7
	 */
	public List<LogOper> searchAllLogOper(LogOper logOper) {
		return this.logOperMapper.searchAll(logOper);
	}

	/**
	 *
	 * selectAllLogOper:获取系统操作日志. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月31日 下午5:03:01
	 * @param logOper
	 * @return
	 * @since JDK 1.7
	 */
	public List<LogOper> selectAllLogOper(LogOper logOper) {
		return this.logOperMapper.selectAll(logOper);
	}

	/**
	 *
	 * selectLogOperByPage:分页查询系统操作日志. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月31日 下午5:11:00
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	public List<LogOper> selectLogOperByPage(Map<String, Object> map) {
		return this.logOperMapper.searchAllByPage(map);
	}

}
