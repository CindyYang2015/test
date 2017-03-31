/**
 * Project Name:ufsp
 * File Name:Constants.java
 * Package Name:com.linc.reports.common
 * Date:2015年03月30日下午13:48:38
 * Copyright @ 2010-2014 上海企垠信息科技有限公司  All Rights Reserved.
 *
 */

package com.cloudwalk.common.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.cloudwalk.common.util.PropsUtil;

/**
 * ClassName:Constants <br/>
 * Description: 下面用到的一些常量. <br/>
 * Date: 2015年03月30日下午13:48:38 <br/>
 *
 * @author Jackson He
 * @version 1.0
 * @since JDK 1.7
 */

public class Constants {
	
	/**
	 * PAGE_ERROR 页面错误
	 */
	public static final String PAGE_ERROR = "/html/error.html";
	
	/**
	 * SERVICE_EXCE 业务异常代码
	 */
	public static final Integer SERVICE_EXCE = 300;

	/**
	 * PARAMETER_ERROR 参数错误
	 */
	public static final Integer PARAMETER_ERROR = 100;

	/**
	 * INSERT_FAILURED 添加失败.
	 */
	public static final Integer INSERT_FAILURED = -1;

	/**
	 * INSERT_FAILURED_WORKID_EXIST 数据已存在，添加失败.
	 */
	public static final Integer INSERT_FAILURED_DATA_EXIST = 3;

	/**
	 * INSERT_SUCCESSED 添加成功.
	 */
	public static final Integer INSERT_SUCCESSED = 1;

	/**
	 * DELETE_FAILURED 删除失败.
	 */
	public static final Integer DELETE_FAILURED = -1;

	/**
	 * DELETE_REF_FAILURED 因为存在关联表，删除失败.
	 */
	public static final Integer DELETE_FAILURED_REF = 3;

	/**
	 * DELETE_SUCCESSED 删除成功.
	 */
	public static final Integer DELETE_SUCCESSED = -3;

	/**
	 * DELETE_FAILURED_CHILD 因为存在子数据，删除失败.
	 */
	public static final Integer DELETE_FAILURED_CHILD = 2;

	/**
	 * UPDATE_FAILURED 更新失败.
	 */
	public static final Integer UPDATE_FAILURED = -1;

	/**
	 * UPDATE_SUCCESSED 更新成功.
	 */
	public static final Integer UPDATE_SUCCESSED = 1;

	// 工作流变量
	public static List<String> PARENT_GROUP = null;

	/**
	 * 用户默认密码：!1qazxsw@3Edcvfr$5RFVbgt6yhnDFGui*
	 */
//	public static final String USER_DEFAULT_PWD = "!1qazxsw@3Edcvfr$5RFVbgt6yhnDFGui*";
	public static final String USER_DEFAULT_PWD = "123456";

	/**
	 * webservice模式
	 */
	public static final int WEBSERVICE = 1;

	/**
	 * socket模式
	 */
	public static final int SOCKET = 2;

	/**
	 * http模式
	 */
	public static final int HTTP = 3;

	/**
	 * socket服务器IP
	 */
	public static final String SOCKET_IP = "127.0.0.1";
	
	/**
	 * socket请求超时时间
	 */
	public static final int SOCKET_TIMEOUT = 30000;
	
	/**
	 * socket请求头长度
	 */
	public static final int SOCKET_HEAD_LENGTH = 7;
	
	/**
	 * 表示接口参数需要加密 1
	 */
	public static final String PARAM_ISENCRYPT = "1";
	
	/**
	 * 新建人脸库
	 */
	public static final String BUSINESS_CODE_ADD_FACE = "addFaces";
	
	/**
	 * 证脸比对
	 */
	public static final String BUSINESS_CODE_CHECK_PERSON = "checkPerson";
	
	/**
	 * 脸脸对比
	 */
	public static final String BUSINESS_CODE_COMPARE_FACES = "compareFaces";
	
	/**
	 * 按脸检索
	 */
	public static final String BUSINESS_CODE_SEARCH_BY_IMAGE = "searchCustByImg";
	
	/**
	 * 按证检索
	 */
	public static final String BUSINESS_CODE_SEARCH_BY_INFO = "searchCustByInfo";
	
	/**
	 * 图片审核通知
	 */
	public static final String BUSINESS_CODE_POST_AUDIT_RESULT = "postAuditResult";
	
	/**
	 * 与人脸识别算法服务器通信模式(http)
	 */
	public static final String FACE_SERVER_MODEL_HTTP = "http";
	
	/**
	 * 随机码编码
	 */
	public static final String RAND_CHECK_CODE = "randCheckCode";
	
	/**
	 * 添加水印
	 */
	public static final String BUSINESS_CODE_ADD_WATERMARK = "addwatermark";
	
	/**
	 * 验证水印
	 */
	public static final String BUSINESS_CODE_VALIDATE_WATERMARK = "validatewatermark";
	
	/**
	 * 分隔符
	 */
	public static final String SEPARATOR = "_";
	
	
	/**
	 * 文件分隔符
	 */
	public static final String FILE_SPARATOR = "/";
	
	/**
	 * 图片文件限制大小(单位：kb)
	 */
	public static final long IMAGE_SIZE = 1024;
	
	/**
	 * 身份证类型编码0200
	 */
	public static final String IDCARD_TYPE = "0200";
	
	//身份证号码分区数组
	public static final String[] IDCARD_NUM_ARRAY = {"11","12","13","14","15","21","22","23","31",
												"32","33","34","35","36","37","41","42","43",
												"44","45","46","50","51","52","53","54","61",
												"62","63","64","65","71","81","82"};
	////身份证号码分区集合
	public static final HashSet<String> IDCARD_NUM_SET = new HashSet<String>(Arrays.asList(IDCARD_NUM_ARRAY));
	
	//非身份证号码默认分区id
	public static final int NOT_IDCARD_NUM = 99;
	
	/**
	 * 配置文件信息
	 * @author zhuyf
	 *
	 */
	public interface Config {		
		
		public static final String IBIS_FILE_PATH = PropsUtil.getProperty("ibis.file.path");
		public static final String IBIS_LOG_LIST = PropsUtil.getProperty("ibis.log.list");
		public static final String FACE_SERVER_URL = PropsUtil.getProperty("face.server.url");
		public static final String APP_ID = PropsUtil.getProperty("app.id");
		public static final String APP_SECRET = PropsUtil.getProperty("app.secret");
		public static final String FACE_GROUP_ID = PropsUtil.getProperty("face.group.id");
		public static final String DATA_TYPE = PropsUtil.getProperty("data.type");
		public static final String PERM_EXCLUDE_URLS = PropsUtil.getProperty("perm.exclude.urls");
		public static final String IBIS_SOCKET_PORT = PropsUtil.getProperty("ibis.socket.port");
		public static final String IBIS_HTTP_REQURL = PropsUtil.getProperty("ibis.http.requrl");
		public static final String IBIS_WEBSERVICE_HOST = PropsUtil.getProperty("ibis.webservice.host");
		public static final String IBIS_REQUEST_KEY = PropsUtil.getProperty("ibis.request.key");
		public static final String IBIS_REQUEST_ISENCRYPT = PropsUtil.getProperty("ibis.request.isencrypt");
		public static final String IBIS_ENGINE_CODE = PropsUtil.getProperty("ibis.engine.code");
		public static final String IBIS_INTERFACE_VERCODE = PropsUtil.getProperty("ibis.interface.vercode");
		public static final String FILE_STORAGE_WAY = PropsUtil.getProperty("file.storage.way");
		public static final String HTTPS_PASSWORD = PropsUtil.getProperty("https.password");
		public static final String HTTPS_KEYSTOREPATH = PropsUtil.getProperty("https.keyStorePath");
		public static final String HTTPS_TRUSTSTOREPATH = PropsUtil.getProperty("https.trustStorePath");
		public static final String BIOLOGY_SIZE = PropsUtil.getProperty("file.biology.size");
		public static final String IBIS_FILE_PATH_CHANNEL_DEFAULT = PropsUtil.getProperty("ibis.file.path.channel.default");
		public static final String IBIS_FILE_PATH_TRADING_DEFAULT = PropsUtil.getProperty("ibis.file.path.trading.default");
		public static final String FACE_ENGINE_ISREG = PropsUtil.getProperty("face.engine.isreg");
		public static final String IBIS_CACHE_NAME = PropsUtil.getProperty("ibis.cache.name");
		public static final String FLOW_HANDLE_THREAD_NUM = PropsUtil.getProperty("flow.handle.thread.num");

		

	}
	
	
}
