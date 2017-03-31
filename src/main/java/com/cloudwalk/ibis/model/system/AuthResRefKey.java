/**
 * Copyright @ 2015-2018 重庆中科云丛科技有限公司  All Rights Reserved.
 *
 * This file is automatically generated by MyBatis Generator, do not modify.
 * MyBatis Generator was modified by FUNDSTAR team, if you have any questions please contact with author.
 * Project Name: FundStar
 * File Name: AuthResRefKey.java
 * Creat Date: 2015-08-15 11:30:16
 */
package com.cloudwalk.ibis.model.system;

import java.io.Serializable;

public class AuthResRefKey implements Serializable {
    /**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = -7447673607785607387L;

    /**
     * FND_AUTH_RES_REF.AUTHORITY_ID
     * 权限ID
     */
    private String authorityId;

    /**
     * FND_AUTH_RES_REF.RESOURCE_ID
     * 资源ID
     */
    private String resourceId;

    /**
     * This method returns the value of the database column
	 *	FND_AUTH_RES_REF.AUTHORITY_ID (权限ID)
     * @return the value of FND_AUTH_RES_REF.AUTHORITY_ID
     */
    public String getAuthorityId() {
        return authorityId;
    }

    /**
     * This method sets the value of the database column
	 *	FND_AUTH_RES_REF.AUTHORITY_ID (权限ID)
     * @param authorityId the value for FND_AUTH_RES_REF.AUTHORITY_ID
     */
    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId == null ? null : authorityId.trim();
    }

    /**
     * This method returns the value of the database column
	 *	FND_AUTH_RES_REF.RESOURCE_ID (资源ID)
     * @return the value of FND_AUTH_RES_REF.RESOURCE_ID
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * This method sets the value of the database column
	 *	FND_AUTH_RES_REF.RESOURCE_ID (资源ID)
     * @param resourceId the value for FND_AUTH_RES_REF.RESOURCE_ID
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId == null ? null : resourceId.trim();
    }
}