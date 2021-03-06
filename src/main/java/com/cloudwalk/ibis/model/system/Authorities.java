/**
 * Copyright @ 2015-2018 重庆中科云丛科技有限公司  All Rights Reserved.
 *
 * This file is automatically generated by MyBatis Generator, do not modify.
 * MyBatis Generator was modified by FUNDSTAR team, if you have any questions please contact with author.
 * Project Name: FundStar
 * File Name: Authorities.java
 * Creat Date: 2015-08-15 11:30:16
 */
package com.cloudwalk.ibis.model.system;

import java.io.Serializable;
import java.util.Date;

import com.cloudwalk.common.util.ObjectUtils;

/**
 * Class Name: Authorities<br/>
 * Description: This class corresponds to the table - FND_AUTHORITIES<br>
 * Create Date: 2015-08-15 11:30:16 <br/>
 */
public class Authorities implements Serializable {
	

	@Override
	public String toString() {
		return "Authorities [authorityId=" + authorityId + ", legalOrgCode="
				+ legalOrgCode + ", authorityName=" + authorityName
				+ ", authorityDesc=" + authorityDesc + ", issys=" + issys
				+ ", groupId=" + groupId + ", createDate=" + createDate + "]";
	}

	/**
	 * serialVersionUID:序列化ID,缓存需要
	 */
	private static final long serialVersionUID = 5022265714750881894L;
	
	public Authorities() {
		String uuid = ObjectUtils.createUUID();
		this.authorityId = uuid;
	}

	/**
	 * FND_AUTHORITIES.AUTHORITY_ID 权限ID
	 */
	private String authorityId;
	
	/**
	 * LEGAL_ORG_CODE 机构代码（法人机构）全路径
	 */
	private String legalOrgCode;
	
	/**
	 * 法人机构名称
	 */
	private String orgName;
	
	/**
	 * FND_AUTHORITIES.AUTHORITY_NAME 权限名称
	 */
	private String authorityName;

	/**
	 * FND_AUTHORITIES.AUTHORITY_DESC 权限描述
	 */
	private String authorityDesc;

	/**
	 * FND_AUTHORITIES.ISSYS 是否是超级权限(1-不是，2-是)
	 */
	private Integer issys;

	private String groupId;
	
	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * This method returns the value of the database column
	 * FND_AUTHORITIES.AUTHORITY_DESC (权限描述)
	 *
	 * @return the value of FND_AUTHORITIES.AUTHORITY_DESC
	 */
	public String getAuthorityDesc() {
		return authorityDesc;
	}

	/**
	 * This method returns the value of the database column
	 * FND_AUTHORITIES.AUTHORITY_ID (权限ID)
	 *
	 * @return the value of FND_AUTHORITIES.AUTHORITY_ID
	 */
	public String getAuthorityId() {
		return authorityId;
	}

	/**
	 * This method returns the value of the database column
	 * FND_AUTHORITIES.AUTHORITY_NAME (权限名称)
	 *
	 * @return the value of FND_AUTHORITIES.AUTHORITY_NAME
	 */
	public String getAuthorityName() {
		return authorityName;
	}

	public String getGroupId() {
		return groupId;
	}

	/**
	 * This method returns the value of the database column
	 * FND_AUTHORITIES.ISSYS (是否是超级权限(1-不是，2-是))
	 *
	 * @return the value of FND_AUTHORITIES.ISSYS
	 */
	public Integer getIssys() {
		return issys;
	}

	/**
	 * This method sets the value of the database column
	 * FND_AUTHORITIES.AUTHORITY_DESC (权限描述)
	 *
	 * @param authorityDesc
	 *            the value for FND_AUTHORITIES.AUTHORITY_DESC
	 */
	public void setAuthorityDesc(String authorityDesc) {
		this.authorityDesc = authorityDesc == null ? null : authorityDesc
				.trim();
	}

	/**
	 * This method sets the value of the database column
	 * FND_AUTHORITIES.AUTHORITY_ID (权限ID)
	 *
	 * @param authorityId
	 *            the value for FND_AUTHORITIES.AUTHORITY_ID
	 */
	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId == null ? null : authorityId.trim();
	}

	/**
	 * This method sets the value of the database column
	 * FND_AUTHORITIES.AUTHORITY_NAME (权限名称)
	 *
	 * @param authorityName
	 *            the value for FND_AUTHORITIES.AUTHORITY_NAME
	 */
	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName == null ? null : authorityName
				.trim();
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * This method sets the value of the database column FND_AUTHORITIES.ISSYS
	 * (是否是超级权限(1-不是，2-是))
	 *
	 * @param issys
	 *            the value for FND_AUTHORITIES.ISSYS
	 */
	public void setIssys(Integer issys) {
		this.issys = issys;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getLegalOrgCode() {
		return legalOrgCode;
	}

	public void setLegalOrgCode(String legalOrgCode) {
		this.legalOrgCode = legalOrgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}	

}