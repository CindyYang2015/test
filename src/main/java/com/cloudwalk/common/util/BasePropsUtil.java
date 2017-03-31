package com.cloudwalk.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * config工具类 读取base-config.properties
 *
 * @author
 * @createTime
 * @history 1.修改时间,修改;修改内容：
 *
 */
public class BasePropsUtil {

	private static Properties props = new Properties();

	static {
		InputStream in = BasePropsUtil.class.getClassLoader().getResourceAsStream("spring/base-config.properties");
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return props.getProperty(key);
	}
}
