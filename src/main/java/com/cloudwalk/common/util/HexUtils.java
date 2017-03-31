package com.cloudwalk.common.util;

public class HexUtils {

	/**
	 * byte数组转换成16进制字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHex(byte[] src) {
		StringBuilder str = new StringBuilder(); 
		
        if (src == null || src.length <= 0) {     
            return null;     
        }
        
        String hv = null;
        for (int i = 0; i < src.length; i++) {
        	hv = Integer.toHexString(src[i] & 0xFF);
            if (hv.length() < 2) {
            	str.append("0");
            }
            
            str.append(hv);     
        }
        
        return str.toString();     
	}
}
