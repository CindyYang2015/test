package com.cloudwalk.common.util;

import com.cloudwalk.common.common.Constants;

/**
 * 对请求数据进行解密操作
 * @author 何春节
 * @version 1.0
 */
public class DataEncryptUtils {
	
	public final static String IBIS_FACE_REQ_KEY = "ibis.faceRequest.key";
	public final static String IBIS_FACE_REQ_ENCRYPT = "ibis.faceRequest.isencrypt";
	
	
	/**
	 * DES解密接受的字符串
	 * @param requestData 请求的报文
	 * @return
	 * @throws Exception 
	 */
	public static String decrypt(String requestData) throws Exception {
		
		if (DataEncryptUtils.isEncrypt()) {
			String encryptKey = BasePropsUtil.getProperty(IBIS_FACE_REQ_KEY);
			return DataEncryptUtils.decrypt(requestData, encryptKey);
		}
		
		return requestData;
	}
	
	/**
	 * 解码
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws Exception {
		return DesUtil.decrypt(data, key);
	}
	
	/**
	 * 判断是否需要进行解码
	 * @return
	 */
	private static boolean isEncrypt() {
		return Constants.PARAM_ISENCRYPT.equals(BasePropsUtil.getProperty(IBIS_FACE_REQ_ENCRYPT));
	}
}
