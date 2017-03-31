/**
 * Copyright @ 2015-2018 重庆中科云丛科技有限公司  All Rights Reserved.
 *
 * This file is automatically generated by MyBatis Generator, do not modify.
 * MyBatis Generator was modified by FUNDSTAR team, if you have any questions please contact with author.
 * Project Name: FundStar
 * File Name: Group.java
 * Creat Date: 2015-08-15 17:22:16
 */
package com.cloudwalk.ibis.model.system;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Class Name: AuthoritiesDto<br/>
 * Description: This class corresponds to the table - FND_GROUP<br>
 * Create Date: 2015-08-15 17:22:16 <br/>
 */
public class AuthoritiesDto implements Serializable {
	@Override
	public String toString() {
		return "AuthoritiesDto [authorityId=" + Arrays.toString(authorityId)
				+ ", authorityName=" + Arrays.toString(authorityName) + "]";
	}

	/**
	 * serialVersionUID:序列化ID,缓存需要
	 */
	private static final long serialVersionUID = -5738940223795910265L;

	/**
	 * FND_AUTHORITIES.AUTHORITY_ID 权限ID
	 */
	private String[] authorityId;

	/**
	 * FND_AUTHORITIES.AUTHORITY_NAME 权限名称
	 */
	private String[] authorityName;
	
	/**
	 * LEGAL_ORG_CODE 法人机构编码
	 */
	private String legalOrgCode; 

	public String[] getAuthorityId() {
		return authorityId;
	}

	public String[] getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityId(String[] authorityId) {
		this.authorityId = authorityId;
	}

	public void setAuthorityName(String[] authorityName) {
		this.authorityName = authorityName;
	}

	public String getLegalOrgCode() {
		return legalOrgCode;
	}

	public void setLegalOrgCode(String legalOrgCode) {
		this.legalOrgCode = legalOrgCode;
	}

	
}