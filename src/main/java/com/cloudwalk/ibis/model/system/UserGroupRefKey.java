/**
 * Copyright @ 2015-2018 重庆中科云丛科技有限公司  All Rights Reserved.
 *
 * This file is automatically generated by MyBatis Generator, do not modify.
 * MyBatis Generator was modified by FUNDSTAR team, if you have any questions please contact with author.
 * Project Name: FundStar
 * File Name: UserGroupRefKey.java
 * Creat Date: 2015-08-15 11:30:16
 */
package com.cloudwalk.ibis.model.system;

import java.io.Serializable;

public class UserGroupRefKey implements Serializable {
    /**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = -8727606433602645149L;

    /**
     * FND_USER_GROUP_REF.USER_ID
     * 用户ID
     */
    private String userId;

    /**
     * FND_USER_GROUP_REF.GROUP_ID
     * 角色ID
     */
    private String groupId;

    /**
     * This method returns the value of the database column
	 *	FND_USER_GROUP_REF.USER_ID (用户ID)
     * @return the value of FND_USER_GROUP_REF.USER_ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method sets the value of the database column
	 *	FND_USER_GROUP_REF.USER_ID (用户ID)
     * @param userId the value for FND_USER_GROUP_REF.USER_ID
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method returns the value of the database column
	 *	FND_USER_GROUP_REF.GROUP_ID (角色ID)
     * @return the value of FND_USER_GROUP_REF.GROUP_ID
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * This method sets the value of the database column
	 *	FND_USER_GROUP_REF.GROUP_ID (角色ID)
     * @param groupId the value for FND_USER_GROUP_REF.GROUP_ID
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }
}