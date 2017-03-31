package com.cloudwalk.common.util;

/**
 * config的参数的Key值
 * 参见：config.properties
 * 
 * 
 */
public class PropsParam {
	
	public static final String UPLOAD_FILE_PATH = "upload_file_path";

	public static final String UPLOAD_IMG_PATH = "upload_img_path";
	
	//人脸识别服务器http地址
	public static final String FACE_SERVER_URL = "faceServer_url";
	
	//人脸识别服务器app_id
	public static final String APP_ID = "app_id";
	
	//人脸识别服务器app_secret
	public static final String APP_SECRET = "app_secret";
	
	//#1:N服务，初始化组别ID
	public static final String ONETOMANY_SERVICE_INITGROUPID = "oneToMany_service_initGroupId";
}
