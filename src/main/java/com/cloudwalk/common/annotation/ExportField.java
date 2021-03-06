package com.cloudwalk.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于实体类字段上
 * @author zhuyf
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportField {
	String name() default "";

	// 该属性用于属性值通过形如1:成功 2:失败的情况
	String[] valType() default {};
}
