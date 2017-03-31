/**
 * Project Name:IBIS-commons
 * File Name:DateFormatter.java
 * Package Name:com.cloudwalk.common.common
 * Date:2015年9月8日下午4:41:54
 * Copyright @ 2015-2015 重庆中科云丛科技有限公司  All Rights Reserved.
 *
 */

package com.cloudwalk.common.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.format.Formatter;

/**
 * ClassName:DateFormatter <br/>
 * Description: TODO Description. <br/>
 * Date: 2015年9月8日 下午4:41:54 <br/>
 *
 * @author zhuyf
 * @version
 * @since JDK 1.7
 * @see
 */
public class DateFormatter implements Formatter<Date> {

	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(text);
		} catch (Exception e) {
			format = new SimpleDateFormat("yyyy-MM-dd");
			date = format.parse(text);
		}
		
		return date;
	}

	@Override
	public String print(Date object, Locale locale) {
		return null;
	}
}
