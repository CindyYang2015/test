package com.cloudwalk.common.spring;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * spring对象工具类
 * @author zhuyf
 *
 */
public class SpringBeanUtil {

	/**
	 * 获取spring上下文容器的对象
	 * 该方法不能在对象初始化时使用
	 * @param beanName 对象名称
	 * @param classType 类型
	 * @return
	 */
	public static <T> T getBean(String beanName,Class<T> classType) {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		return context.getBean(beanName, classType);
	}
}
