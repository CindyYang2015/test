package com.cloudwalk.common.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 定时任务属性配置
 * @author zhuyf
 *
 */
public class ScheduleConfig {

	private static Properties props = new Properties();
	
	static{
		InputStream in = ScheduleConfig.class.getClassLoader().getResourceAsStream("spring/schedule-config.properties");
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key){
		return props.getProperty(key);
	}
	
	public static String TASK_ON = "on";//开启
	public static String TASK_OFF = "off";//关闭
	
	public static String FILE_SEPARATOR = "/";//文件分隔符
	public static String FILE_ROOT_DIR = getProperty("file.root.dir");//文件根目录
	public static String ON_OFF = getProperty("on.off");//开关
	public static String SLEEP_RANDOM_TIME = getProperty("sleep.random.time");//睡眠随机时间
	
	/**
	 * 流水属性配置
	 */
	public static String FLOW_TRANSFER_WAY_REMOVE = "remove";//删除
	public static String FLOW_TRANSFER_WAY_FTP = "ftp";//ftp转移	
	public static String FLOW_TRANSFER_DAYS = getProperty("flow.transfer.days");//转移天数
	public static String FLOW_TRANSFER_WAY = getProperty("flow.transfer.way");//文件转移方式
	public static String FLOW_FILE_SOURCE_DIR = getProperty("flow.file.source.dir");//待转移的流水文件目录
	
	//ftp配置
	public static String FLOW_FTP_PATH = getProperty("flow.ftp.path");//ftp服务器保存的根路径
	public static String FLOW_FTP_ADDR = getProperty("flow.ftp.addr");//ftp服务器的ip
	public static String FLOW_FTP_PORT = getProperty("flow.ftp.port");//ftp服务器的端口
	public static String FLOW_FTP_USERNAME = getProperty("flow.ftp.username");//ftp服务器的用户名
	public static String FLOW_FTP_PASSWORD = getProperty("flow.ftp.password");//ftp服务器的密码

}
