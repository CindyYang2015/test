/**
 * Copyright @ 2015-2018 重庆中科云丛科技有限公司  All Rights Reserved.
 *
 * This file is automatically generated by MyBatis Generator, do not modify.
 * MyBatis Generator was modified by FUNDSTAR team, if you have any questions please contact with author.
 * Project Name: FundStar
 * File Name: GroupAuthRefKey.java
 * Creat Date: 2015-08-15 11:30:16
 */
package com.cloudwalk.ibis.model.system;

import java.io.Serializable;

public class GroupAuthRefKey implements Serializable {
    /**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = -2473586802958787488L;

    /**
     * FND_GROUP_AUTH_REF.GROUP_ID
     * 角色ID
     */
    private String groupId;

    /**
     * FND_GROUP_AUTH_REF.AUTHORITY_ID
     * 权限ID
     */
    private String authorityId;

    /**
     * This method returns the value of the database column
	 *	FND_GROUP_AUTH_REF.GROUP_ID (角色ID)
     * @return the value of FND_GROUP_AUTH_REF.GROUP_ID
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * This method sets the value of the database column
	 *	FND_GROUP_AUTH_REF.GROUP_ID (角色ID)
     * @param groupId the value for FND_GROUP_AUTH_REF.GROUP_ID
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    /**
     * This method returns the value of the database column
	 *	FND_GROUP_AUTH_REF.AUTHORITY_ID (权限ID)
     * @return the value of FND_GROUP_AUTH_REF.AUTHORITY_ID
     */
    public String getAuthorityId() {
        return authorityId;
    }

    /**
     * This method sets the value of the database column
	 *	FND_GROUP_AUTH_REF.AUTHORITY_ID (权限ID)
     * @param authorityId the value for FND_GROUP_AUTH_REF.AUTHORITY_ID
     */
    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId == null ? null : authorityId.trim();
    }
}