package com.cloudwalk.common.engine.face.recg;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.util.Base64;

import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.engine.face.consts.EngineConst;
import com.cloudwalk.common.engine.face.eoc.Attribute;
import com.cloudwalk.common.engine.face.eoc.BankCard;
import com.cloudwalk.common.engine.face.eoc.Compare;
import com.cloudwalk.common.engine.face.eoc.Detect;
import com.cloudwalk.common.engine.face.eoc.EOCResult;
import com.cloudwalk.common.engine.face.eoc.Feature;
import com.cloudwalk.common.engine.face.eoc.IDCard;
import com.cloudwalk.common.engine.face.eoc.Identify;
import com.cloudwalk.common.engine.face.eoc.Liveness;
import com.cloudwalk.common.engine.face.eoc.MultimodelFeature;
import com.cloudwalk.common.engine.face.eoc.Quality;
import com.cloudwalk.common.engine.face.eoc.Query;
import com.cloudwalk.common.engine.face.eoc.Reticulate;
import com.cloudwalk.common.util.HttpRequestProxy;

/**
 * 云之眼请求工具类
 *
 */
public class RecogniseUtils {

	/**
	 * 云之眼APPID
	 */
	private static String EOC_APP_ID;
	
	/**
	 * 云之眼APPS SECRET
	 */
	private static String EOC_APP_SECRET;
	
	/**
	 * 云之眼基础服务地址
	 */
	private static String EOC_SERVICE_URL;
	
	/**
	 * 初始化参数值
	 */
	static {
		EOC_APP_ID = Constants.Config.APP_ID;
		EOC_APP_SECRET = Constants.Config.APP_SECRET;
		EOC_SERVICE_URL = Constants.Config.FACE_SERVER_URL;
	}
	
	/**
	 * 将字节数组进行base64编码，然后替换换行和回车等字符
	 * @param str
	 * @return
	 */
	public static String clearBase64String(String str) {
		return str.replaceAll("\r\n","").replaceAll("\\s", "+");
	}
	
	/**
	 * 将字节数组进行base64编码，然后替换换行和回车等字符
	 * @param b
	 * @return
	 */
	public static String encodeBase64String(byte[] b) {
		return clearBase64String(Base64.encodeBase64String(b));
	}
	
	/**
	 * 对base64编码后的字符串进行反解
	 * @param str
	 * @return
	 */
	public static byte[] decodeBase64(String str) {
		return Base64.decodeBase64(clearBase64String(str));
	}
	
	/**
	 * 发送HTTP的POST请求
	 * @param url
	 * @param params
	 * @return 
	 * @return
	 */
	private static <T> T sendPostRequest(String url, Map<String, String> params, Class<T> clz) {
		String ret = HttpRequestProxy.post(EOC_SERVICE_URL + url, params, "UTF-8");
		return JSONArray.parseObject(ret, clz);
	}
	
	/**
	 * 人脸检测
	 * @param img
	 * @param mode
	 * @return
	 */
	public static Detect detect(String img, boolean mode) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("img", clearBase64String(img));
		params.put("mode", mode ? "true" : "false");
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_DETECT, params, Detect.class);
	}
	
	/**
	 * 人脸属性分析
	 * @param img
	 * @return
	 */
	public static Attribute attribute(String img) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("img", clearBase64String(img));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_ATTRIBUTE, params, Attribute.class);
	}
	
	/**
	 * 人脸质量分析
	 * @param img
	 * @return
	 */
	public static Quality quality(String img) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("img", clearBase64String(img));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_QUALITY, params, Quality.class);
	}
	
	/**
	 * 人脸比对
	 * @param imageA
	 * @param imageB
	 * @return
	 */
	public static Compare compare(String imageA, String imageB) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("imgA", clearBase64String(imageA));
		params.put("imgB", clearBase64String(imageB));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_COMPARE, params, Compare.class);
	}
	
	/**
	 * 人脸比对扩展方法
	 * @param imageA
	 * @param imageB
	 * @return
	 */
	public static Compare compareExt1(String imageA, String imageB) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("imgA", clearBase64String(imageA));
		params.put("imgB", clearBase64String(imageB));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_COMPARE_EXT1, params, Compare.class);
	}
	
	/**
	 * 人脸相似度比较(扩展方式一)
	 * @param img
	 * @return
	 */
	public static Reticulate removeWater(String img) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("img", clearBase64String(img));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_RMWATER, params, Reticulate.class);
	}
	
	/**
	 * 人脸特征提取
	 * @param img
	 * @return
	 */
	public static Feature feature(String img) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("img", clearBase64String(img));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_FEATURE, params, Feature.class);
	}
	
	/**
	 * 人脸特征合并(作废)
	 * @param featureA
	 * @param weightA
	 * @param featureB
	 * @param weightB
	 * @return
	 
	public static Feature mergeFeature(String featureA, double weightA, String featureB, double weightB) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("featureA", clearBase64String(featureA));
		params.put("featureB", String.valueOf(weightA));
		params.put("weightA",  clearBase64String(featureB));
		params.put("weightB",  String.valueOf(weightB));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_MERGE_FEATURE, params, Feature.class);
	}*/
	
	/**
	 * 特征比对
	 * @param featureA
	 * @param featureB
	 * @return
	 */
	public static Compare similarityByFeature(String featureA, String featureB) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("featureA", clearBase64String(featureA));
		params.put("featureB", clearBase64String(featureB));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_SIMILARITYBYFEATURE, params, Compare.class);
	}
	
	/**
	 * 添加人脸
	 * @param faceId
	 * @param img
	 * @param tag
	 */
	public static EOCResult faceCreate(String faceId, String img, String tag) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("tag", tag);
		params.put("faceId", faceId);
		params.put("img", clearBase64String(img));
		
		return sendPostRequest(EngineConst.Service.FACE_CLUSTERIING_FACE_CREATE, params, EOCResult.class);
	}
	
	/**
	 * 删除人脸
	 * @param faceId
	 * @return
	 */
	public static EOCResult faceDelete(String faceId) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("faceId", faceId);
		
		return sendPostRequest(EngineConst.Service.FACE_CLUSTERING_FACE_DELETE, params, EOCResult.class);
	}
	
	/**
	 * 添加组
	 * @param groupId
	 * @param tag
	 * @return
	 */
	public static EOCResult groupCreate(String groupId, String tag) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("groupId", groupId);
		params.put("tag", tag);
		
		return sendPostRequest(EngineConst.Service.FACE_CLUSTERING_GROUP_CREATE, params, EOCResult.class);
	}
	
	/**
	 * 删除组名
	 * @param groupId
	 * @return
	 */
	public static EOCResult groupDelete(String groupId) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("groupId",    groupId);
		
		return sendPostRequest(EngineConst.Service.FACE_CLUSTERING_GROUP_DELETE, params, EOCResult.class);
	}
	
	/**
	 * 组中添加人脸
	 * @param groupId
	 * @param faceId
	 * @return
	 */
	public static EOCResult addFace(String groupId, String faceId) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("groupId",    groupId);
		params.put("faceId",     faceId);
		
		return sendPostRequest(EngineConst.Service.FACE_CLUSTERING_GROUP_ADDFACE, params, EOCResult.class);
	}
	
	/**
	 * 组中删除人脸
	 * @param groupId
	 * @param faceId
	 * @return
	 */
	public static EOCResult removeFace(String groupId, String faceId) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("groupId",    groupId);
		params.put("faceId",     faceId);
		
		return sendPostRequest(EngineConst.Service.FACE_CLUSTERING_GROUP_RMFACE, params, EOCResult.class);
	}
	
	/**
	 * 查询组中人脸数目
	 * @param groupId
	 * @return
	 */
	public static Query query(String groupId) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("type", "queryFaceCount");
		params.put("groupId",    groupId);
		
		return sendPostRequest(EngineConst.Service.FACE_CLUSTERING_GROUP_QUERY, params, Query.class);
	}
	
	/**
	 * 人脸识别-图片
	 * @param groupId
	 * @param img
	 * @param topN
	 * @return
	 */
	public static Identify identifyByImage(String groupId, String img, long topN) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("groupId", groupId);
		params.put("img", clearBase64String(img));
		params.put("topN", String.valueOf(topN));
		
		return sendPostRequest(EngineConst.Service.FACE_RECOG_GROUP_IDENTIFY, params, Identify.class);
	}
	
	/**
	 * 人脸识别-特征
	 * @param groupId
	 * @param feature
	 * @param topN
	 * @return
	 */
	public static Identify identifyByFeature(String groupId, String feature, long topN) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("groupId", groupId);
		params.put("feature", clearBase64String(feature));
		params.put("topN", String.valueOf(topN));
		
		return sendPostRequest(EngineConst.Service.FACE_RECOG_GROUP_IDENTIFY, params, Identify.class);
	}
	
	/**
	 * 组识别(扩展方式一)
	 * @param groupId
	 * @param img
	 * @return
	 */
	public static Identify identifyExt1(String groupId, String img) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("groupId", groupId);
		params.put("img", clearBase64String(img));
		
		return sendPostRequest(EngineConst.Service.FACE_RECOG_GROUP_IDENTIFY_EXT1, params, Identify.class);
	}
	
	/**
	 * 特征提取(模型可选)
	 * @param img   图片数据
	 * @param type  图片类型 空-高清, 0-程序自动判断, 1-高清, 2-水印
	 * @return
	 */
	public static MultimodelFeature mulFeature(String img, int type) {
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("img", clearBase64String(img));
		params.put("type", String.valueOf(type));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_MULTIMODEL_FEATURE, params, MultimodelFeature.class);

	}
	
	/**
	 *图片和图片比对相似度(模型可选)
	 * @param imgA   图片数据
	 * @param typeA  图片类型 空-高清, 0-程序自动判断, 1-高清, 2-水印
	 * @param imgB   图片数据
	 * @param typeB  图片类型 空-高清, 0-程序自动判断, 1-高清, 2-水印
	 * @return
	 */
	public static Compare mulSimilarityByImage(String imgA, int typeA, String imgB, int typeB) {
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("imgA", clearBase64String(imgA));
		params.put("typeA", String.valueOf(typeA));
		params.put("imgB", clearBase64String(imgA));
		params.put("typeB", String.valueOf(typeA));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_MULTIMODEL_SIMILARITYBYIMAGE, params, Compare.class);

	}
	
	/**
	 *特征和图片比对相似度(模型可选)
	 * @param feature      特征数据
	 * @param typeFeature  提取特征使用模型类型 1-高清, 2-水印
	 * @param img          图片数据
	 * @param typeImage    图片类型 空-高清, 0-程序自动判断, 1-高清, 2-水印
	 * @return
	 */
	public static Compare mulSimilarityByFeature(String feature, int typeFeature, String img, int typeImage) {
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("feature", clearBase64String(feature));
		params.put("typeFeature", String.valueOf(typeFeature));
		params.put("img", clearBase64String(img));
		params.put("typeImage", String.valueOf(typeImage));
		
		return sendPostRequest(EngineConst.Service.FACE_TOOL_MULTIMODEL_SIMILARITYBYFEATURE, params, Compare.class);

	}
	
	/**
	 * 身份证识别
	 * @param idcardImg 身份证图片数据
	 * @param type 1正面 0反面
	 * @return
	 */
	public static IDCard ocrIDCard(String idcardImg,int type) {
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("img", clearBase64String(idcardImg));
		params.put("getFace", String.valueOf(type));
		
		return sendPostRequest(EngineConst.Service.URL_OCR, params, IDCard.class);

	}
	
	/**
	 * 活体检测
	 * @param json 活体数据
	 * @return
	 */
	public static Liveness jdugeLiveness(String json) {

		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("param", clearBase64String(json));
		return sendPostRequest(EngineConst.Service.FACE_RECOG_LIVE_NESS, params, Liveness.class);

	}
	
	/**
	 * 银行卡识别
	 * 65792	银行卡识别错误
	 * @param img 银行卡图片
	 * @return
	 */
	public static BankCard ocrBankCard(String img) {
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("app_id",     EOC_APP_ID);
		params.put("app_secret", EOC_APP_SECRET);
		params.put("img", clearBase64String(img));
		
		return sendPostRequest(EngineConst.Service.URL_OCR_BANKCARD, params, BankCard.class);

	}
	
}
