package com.cloudwalk.common.base.token;
/**
 * Project Name:wams-service
 * File Name:Token.java
 * Package Name:com.cloudwalk.core.shire
 * Date:2016年4月1日下午2:47:02
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 *
*/
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:Token <br/>
 * Description: TODO Description. <br/>
 * Date:     2016年4月1日 下午2:47:02 <br/>
 * @author   Hechunjie 
 * @version  
 * @since    JDK 1.6
 */

@Target(ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface Token {
 
     boolean save() default false ;
 
     boolean remove() default false ;
}

