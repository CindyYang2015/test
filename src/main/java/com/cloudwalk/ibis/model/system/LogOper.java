package com.cloudwalk.ibis.model.system;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.cloudwalk.common.util.ObjectUtils;

/**
 * Class Name: LogOper<br/>
 * Description: This class corresponds to the table - FND_LOG_OPER<br>
 * Create Date: 2015-08-31 17:50:30 <br/>
 */
public class LogOper implements Serializable {
	/**
	 * serialVersionUID:序列化ID,缓存需要
	 */
	private static final long serialVersionUID = -8709319732982751346L;
	
	public LogOper() {
		String uuid = ObjectUtils.createUUID();
		this.logId = uuid;
	}

	/**
	 * FND_LOG_OPER.LOG_ID 主键
	 */
	private String logId;

	/**
	 * FND_LOG_OPER.LOGIN_NAME 登录账号
	 */
	private String loginName;

	/**
	 * FND_LOG_OPER.USER_NAME 登录姓名
	 */
	private String userName;

	/**
	 * FND_LOG_OPER.IP_ADDRESS 请求IP地址
	 */
	private String ipAddress;

	/**
	 * FND_LOG_OPER.OPER_NAME 操作类型
	 */
	private String operName;

	/**
	 * FND_LOG_OPER.REMARK 描述
	 */
	private String remark;

	/**
	 * FND_LOG_OPER.STATUS 操作状态(0：失败，1：成功)
	 */
	private Short status;

	/**
	 * FND_LOG_OPER.CREATE_TIME 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	/**
	 * 部门代码
	 */
	private String deptCode;
	
	/**
	 * 部门名称
	 */
	private String deptName;
	
	/**
	 * 机构代码
	 */
	private String orgCode;
	
	/**
	 * 机构名称
	 */
	private String orgName;

	/**
	 * This method returns the value of the database column
	 * FND_LOG_OPER.CREATE_TIME (创建时间)
	 *
	 * @return the value of FND_LOG_OPER.CREATE_TIME
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method returns the value of the database column
	 * FND_LOG_OPER.IP_ADDRESS (请求IP地址)
	 *
	 * @return the value of FND_LOG_OPER.IP_ADDRESS
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * This method returns the value of the database column FND_LOG_OPER.LOG_ID
	 * (主键)
	 *
	 * @return the value of FND_LOG_OPER.LOG_ID
	 */
	public String getLogId() {
		return logId;
	}

	/**
	 * This method returns the value of the database column
	 * FND_LOG_OPER.LOGIN_NAME (登录账号)
	 *
	 * @return the value of FND_LOG_OPER.LOGIN_NAME
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * This method returns the value of the database column
	 * FND_LOG_OPER.OPER_NAME (操作类型)
	 *
	 * @return the value of FND_LOG_OPER.OPER_NAME
	 */
	public String getOperName() {
		return operName;
	}

	/**
	 * This method returns the value of the database column FND_LOG_OPER.REMARK
	 * (描述)
	 *
	 * @return the value of FND_LOG_OPER.REMARK
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * This method returns the value of the database column FND_LOG_OPER.STATUS
	 * (操作状态(0：失败，1：成功))
	 *
	 * @return the value of FND_LOG_OPER.STATUS
	 */
	public Short getStatus() {
		return status;
	}

	/**
	 * This method returns the value of the database column
	 * FND_LOG_OPER.USER_NAME (登录姓名)
	 *
	 * @return the value of FND_LOG_OPER.USER_NAME
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * This method sets the value of the database column
	 * FND_LOG_OPER.CREATE_TIME (创建时间)
	 *
	 * @param createTime
	 *            the value for FND_LOG_OPER.CREATE_TIME
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method sets the value of the database column FND_LOG_OPER.IP_ADDRESS
	 * (请求IP地址)
	 *
	 * @param ipAddress
	 *            the value for FND_LOG_OPER.IP_ADDRESS
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress == null ? null : ipAddress.trim();
	}

	/**
	 * This method sets the value of the database column FND_LOG_OPER.LOG_ID
	 * (主键)
	 *
	 * @param logId
	 *            the value for FND_LOG_OPER.LOG_ID
	 */
	public void setLogId(String logId) {
		this.logId = logId;
	}

	/**
	 * This method sets the value of the database column FND_LOG_OPER.LOGIN_NAME
	 * (登录账号)
	 *
	 * @param loginName
	 *            the value for FND_LOG_OPER.LOGIN_NAME
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName == null ? null : loginName.trim();
	}

	/**
	 * This method sets the value of the database column FND_LOG_OPER.OPER_NAME
	 * (操作类型)
	 *
	 * @param operName
	 *            the value for FND_LOG_OPER.OPER_NAME
	 */
	public void setOperName(String operName) {
		this.operName = operName == null ? null : operName.trim();
	}

	/**
	 * This method sets the value of the database column FND_LOG_OPER.REMARK
	 * (描述)
	 *
	 * @param remark
	 *            the value for FND_LOG_OPER.REMARK
	 */
	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	/**
	 * This method sets the value of the database column FND_LOG_OPER.STATUS
	 * (操作状态(0：失败，1：成功))
	 *
	 * @param status
	 *            the value for FND_LOG_OPER.STATUS
	 */
	public void setStatus(Short status) {
		this.status = status;
	}

	/**
	 * This method sets the value of the database column FND_LOG_OPER.USER_NAME
	 * (登录姓名)
	 *
	 * @param userName
	 *            the value for FND_LOG_OPER.USER_NAME
	 */
	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
	
}