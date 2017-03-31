package com.cloudwalk.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.commons.net.util.Base64;

public class ImgUtil {
	private static String BASE_PATH = PropsUtil.getProperty(PropsParam.UPLOAD_IMG_PATH);
	private static String separator = "/"; // 图片路劲层级分隔符

	/**
	 * 获取base64图片数据
	 * @param imgUrl
	 * @return
	 */
	public static String getBase64Img(String imgUrl) {
		byte[] imgbyte = ImgConvertByteArray.img2byteArray(imgUrl);
		if(imgbyte == null) return "";
		return org.apache.commons.net.util.Base64.encodeBase64String(imgbyte);
	}

	/**
	 * 创建图片名称
	 * @param imageFormat
	 * @return
	 */
	public static String getImgName(String imageFormat) {
		return ObjectUtils.createUUID().toLowerCase() + "." + imageFormat;
	}

	/**
	 * 创建图片保存路径
	 * @return
	 */
	public static String getPath() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		String strMonth = month < 10 ? "0" + month : String.valueOf(month);
		String strDay = day < 10 ? "0" + day : String.valueOf(day);

		return separator + year + separator + strMonth + separator + strDay;
	}

	/**
	 * 保存图片
	 * @param imgData
	 * @param path
	 * @param imgName
	 * @return
	 */
	public static String saveImg(byte[] imgData, String path, String imgName) {
		String imgUrl = BASE_PATH + path + separator + imgName;
		ImgConvertByteArray.byteArray2Img(imgData, imgUrl);
		
		return path + separator + imgName;
	}
	
	/**
	 * 保存图片
	 * @param imgData
	 * @param subfix
	 * @return
	 */
	public static String saveImg(byte[] imgData, String subfix) {
		return ImgUtil.saveImg(imgData, getPath(), getImgName(subfix));
	}
	/**
	 * imageForBase64:(从本地获取图片). <br/>
	 *  Date: Jun 15, 2016 9:44:51 AM
	 * @param path
	 * @return
	 * @since JDK 1.7
	 */
	public static String imageForBase64(String path) {
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(BASE_PATH+separator +path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		String result = Base64.encodeBase64String(data);
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(getBase64Img("D:/img/addface/01D1.jpg"));
	}
}
