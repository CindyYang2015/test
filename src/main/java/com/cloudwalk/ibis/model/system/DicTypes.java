/**
 * Copyright @ 2015-2018 重庆中科云丛科技有限公司  All Rights Reserved.
 *
 * This file is automatically generated by MyBatis Generator, do not modify.
 * MyBatis Generator was modified by FUNDSTAR team, if you have any questions please contact with author.
 * Project Name: FundStar
 * File Name: DicTypes.java
 * Creat Date: 2015-08-15 13:47:27
 */
package com.cloudwalk.ibis.model.system;

import java.io.Serializable;

/**
 * Class Name: DicTypes<br/>
 * Description: This class corresponds to the table - FND_DIC_TYPES<br>
 * Create Date: 2015-08-15 13:47:27 <br/>
 */
public class DicTypes implements Serializable {
    /**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = 6363728121408673491L;

    /**
     * FND_DIC_TYPES.DIC_TYPE
     * 类型
     */
    private String dicType;

    /**
     * FND_DIC_TYPES.DESCRIPTION
     * 描述
     */
    private String description;

    /**
     * FND_DIC_TYPES.CUSTOMIZATION_LEVEL
     * 自定义级别
     */
    private Integer customizationLevel;

    public String getDicType() {
		return dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

	/**
     * This method returns the value of the database column
	 *	FND_DIC_TYPES.DESCRIPTION (描述)
     * @return the value of FND_DIC_TYPES.DESCRIPTION
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method sets the value of the database column
	 *	FND_DIC_TYPES.DESCRIPTION (描述)
     * @param description the value for FND_DIC_TYPES.DESCRIPTION
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public Integer getCustomizationLevel() {
		return customizationLevel;
	}

	public void setCustomizationLevel(Integer customizationLevel) {
		this.customizationLevel = customizationLevel;
	}    
}