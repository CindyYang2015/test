package com.cloudwalk.ibis.controller.faceTest;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.common.BaseConstants;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.common.JsonResult;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.DesUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.result.ver001.Response;
import com.cloudwalk.ibis.service.base.RecogRequestService;
import com.cloudwalk.ibis.service.base.recog.DataParse;

/**
 * 人脸识别模拟测试controller
 *
 */
@Controller
@RequestMapping("/faceTest")
public class FaceTestController extends BaseController {

	/**
	 * 人脸注册页面
	 */
	private static final String FACE_ADD_PAGE = "platform/faceTest/faceAdd";

	/**
	 * 证脸比对页面
	 */
	private static final String FACE_CARD_COMPARE_PAGE = "platform/faceTest/idcardFaceCompare";

	/**
	 * 脸脸比对页面
	 */
	private static final String FACE_FACE_COMPARE_PAGE = "platform/faceTest/faceFaceCompare";

	/**
	 * 通过客户信息检索页面
	 */
	private static final String SEARCH_BY_PERSON_PAGE = "platform/faceTest/searchByPerson";

	/**
	 * 通过图片数据检索页面
	 */
	private static final String SEARCH_BY_IMG_PAGE = "platform/faceTest/searchByImg";

	/**
	 * 两证一脸比对页面
	 */
	private static final String IDCARD_FACE_COMPARE_EX_PAGE = "platform/faceTest/idcardFaceCompareEx";

	/**
	 * 身份证OCR识别页面
	 */
	private static final String OCR_RECOG_PAGE = "platform/faceTest/ocrRecog";
	
	/**
	 *	银行卡OCR识别页面 
	 */
	private static final String BANKCARD_OCR_RECOG_PAGE="platform/faceTest/bankCardOcrRecog";

	/**
	 * 数据解析器
	 */
	private DataParse dataParse;

	@Resource(name = "recogRequestService")
	RecogRequestService recogRequestService;

	@PostConstruct
	public void init() {
		dataParse = new DataParse();
	}

	/**
	 * 人脸注册
	 * 
	 * @param request
	 * @param response
	 * @param recogRequest
	 */
	@RequestMapping("/faceAdd")
	public void addFace(HttpServletRequest request,
			HttpServletResponse response, RecogRequest recogRequest) {
		// 返回结果
		String result = "";
		// 通信协议，webservice：1，socket:2,Http:3
		int requestWay = ObjectUtils.objToInt(
				request.getParameter("requestWay"), Constants.WEBSERVICE);
		// 获取注册的图片数据
		String faceImgName = EnumClass.ImgNameEnum.FACE_IMG.getValue();
		try {

			Map<String, String> imgMap = ControllerUtil
					.getRequestBase64Data(request,
							new String[] { faceImgName });
			if (imgMap.isEmpty()) {
				result = BaseConstants.NULL_UPLOADFILE_ERROR;
			} else {
				// 设置服务接口代码和图片数据
				recogRequest.setBuscode(EnumClass.InterFaceEnum.REG.getValue());
				recogRequest.setEngineCode(Constants.Config.IBIS_ENGINE_CODE);
				recogRequest
						.setVerCode(Constants.Config.IBIS_INTERFACE_VERCODE);

				if (imgMap.containsKey(faceImgName)) {
					recogRequest.setRegfileData(imgMap.get(faceImgName));
				}

				// 请求报文对象解析成系统对应的数据格式
				String requestData = this.dataParse.parseDataStr(recogRequest);
				// 请求报文加密
				requestData = this.encrypt(requestData);
				// 发送请求报文
				result = recogRequestService.invoke(requestWay, requestData);
			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			result = this.getExceptionMsg("人脸接口模拟请求发生异常，详情查看后台日志");
		}
		getPrintWriter(response, result);

	}

	/**
	 * 证脸比对
	 * 
	 * @param request
	 * @param response
	 * @param recogRequest
	 */
	@RequestMapping("/idcardFaceCompare")
	public void idcardFaceCompare(HttpServletRequest request,
			HttpServletResponse response, RecogRequest recogRequest) {
		// 返回结果
		String result = "";
		// 通信协议，webservice：1，socket:2,Http:3
		int requestWay = ObjectUtils.objToInt(
				request.getParameter("requestWay"), Constants.WEBSERVICE);
		// 获取证件图片和核查图片数据
		String faceImgName = EnumClass.ImgNameEnum.FACE_IMG.getValue();
		String netCheckImgName = EnumClass.ImgNameEnum.NET_CHECK_IMG.getValue();
		try {
			Map<String, String> imgMap = ControllerUtil
					.getRequestBase64Data(request, new String[] {
							faceImgName, netCheckImgName });
			if (imgMap.isEmpty()) {
				result = BaseConstants.NULL_UPLOADFILE_ERROR;
			} else {
				// 设置服务接口代码和图片数据
				recogRequest.setBuscode(EnumClass.InterFaceEnum.CHECK_PERSON
						.getValue());
				recogRequest.setEngineCode(Constants.Config.IBIS_ENGINE_CODE);
				recogRequest
						.setVerCode(Constants.Config.IBIS_INTERFACE_VERCODE);

				if (imgMap.containsKey(faceImgName)) {
					recogRequest.setFileDataone(imgMap.get(faceImgName));
				}
				if (imgMap.containsKey(netCheckImgName)) {
					recogRequest.setNetCheckFileData(imgMap
							.get(netCheckImgName));
				}
				// 请求报文对象解析成系统对应的数据格式
				String requestData = this.dataParse.parseDataStr(recogRequest);
				// this.sysoRequestNoFile(requestData);
				// 请求报文加密
				requestData = this.encrypt(requestData);
				// 发送请求报文
				result = recogRequestService.invoke(requestWay, requestData);
			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			result = this.getExceptionMsg("人脸接口模拟请求发生异常，详情查看后台日志");
		}
		getPrintWriter(response, result);
	}

	/**
	 * 两证一脸比对
	 * 
	 * @param request
	 * @param response
	 * @param recogRequest
	 */
	@RequestMapping("/idcardFaceCompareEx")
	public void idcardFaceCompareEx(HttpServletRequest request,
			HttpServletResponse response, RecogRequest recogRequest) {
		// 返回结果
		String result = "";
		// 通信协议，webservice：1，socket:2,Http:3
		int requestWay = ObjectUtils.objToInt(
				request.getParameter("requestWay"), Constants.WEBSERVICE);
		// 获取图片数据
		String faceImgName = EnumClass.ImgNameEnum.FACE_IMG.getValue();
		String idcardImgName = EnumClass.ImgNameEnum.IDCARD_IMG.getValue();
		String netCheckImgName = EnumClass.ImgNameEnum.NET_CHECK_IMG.getValue();
		try {
			Map<String, String> imgMap = ControllerUtil
					.getRequestBase64Data(request, new String[] {
							faceImgName, idcardImgName, netCheckImgName });
			if (imgMap.isEmpty()) {
				result = BaseConstants.NULL_UPLOADFILE_ERROR;
			} else {
				// 设置服务接口代码和图片数据
				recogRequest.setBuscode(EnumClass.InterFaceEnum.CHECK_PERSON_EX
						.getValue());
				recogRequest.setEngineCode(Constants.Config.IBIS_ENGINE_CODE);
				recogRequest
						.setVerCode(Constants.Config.IBIS_INTERFACE_VERCODE);

				if (imgMap.containsKey(faceImgName)) {
					recogRequest.setFileDataone(imgMap.get(faceImgName));
				}
				if (imgMap.containsKey(idcardImgName)) {
					recogRequest.setFileDatatwo(imgMap.get(idcardImgName));
				}
				if (imgMap.containsKey(netCheckImgName)) {
					recogRequest.setNetCheckFileData(imgMap
							.get(netCheckImgName));
				}
				// 请求报文对象解析成系统对应的数据格式
				String requestData = this.dataParse.parseDataStr(recogRequest);
				// this.sysoRequestNoFile(requestData);
				// 请求报文加密
				requestData = this.encrypt(requestData);
				// 发送请求报文
				result = recogRequestService.invoke(requestWay, requestData);
			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			result = this.getExceptionMsg("人脸接口模拟请求发生异常，详情查看后台日志");
		}
		getPrintWriter(response, result);

	}

	/**
	 * 脸脸比对
	 * 
	 * @param request
	 * @param response
	 * @param recogRequest
	 */
	@RequestMapping("/faceFaceCompare")
	public void faceFaceCompare(HttpServletRequest request,
			HttpServletResponse response, RecogRequest recogRequest) {
		// 返回结果
		String result = "";
		// 通信协议，webservice：1，socket:2,Http:3
		int requestWay = ObjectUtils.objToInt(
				request.getParameter("requestWay"), Constants.WEBSERVICE);
		// 获取图片数据
		String faceImgName = EnumClass.ImgNameEnum.FACE_IMG.getValue();
		String idcardImgName = EnumClass.ImgNameEnum.IDCARD_IMG.getValue();
		try {

			Map<String, String> imgMap = ControllerUtil
					.getRequestBase64Data(request, new String[] {
							faceImgName, idcardImgName });
			if (imgMap.isEmpty()) {
				result = BaseConstants.NULL_UPLOADFILE_ERROR;
			} else {
				// 设置服务接口代码和图片数据
				recogRequest.setBuscode(EnumClass.InterFaceEnum.COMPARE
						.getValue());
				recogRequest.setEngineCode(Constants.Config.IBIS_ENGINE_CODE);
				recogRequest
						.setVerCode(Constants.Config.IBIS_INTERFACE_VERCODE);

				if (imgMap.containsKey(faceImgName)) {
					recogRequest.setFileDataone(imgMap.get(faceImgName));
				}
				if (imgMap.containsKey(idcardImgName)) {
					recogRequest.setFileDatatwo(imgMap.get(idcardImgName));
				}
				// 请求报文对象解析成系统对应的数据格式
				String requestData = this.dataParse.parseDataStr(recogRequest);
				// this.sysoRequestNoFile(requestData);
				// 请求报文加密
				requestData = this.encrypt(requestData);
				// 发送请求报文
				result = recogRequestService.invoke(requestWay, requestData);
			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			result = this.getExceptionMsg("人脸接口模拟请求发生异常，详情查看后台日志");
		}
		getPrintWriter(response, result);

	}

	/**
	 * 按照客户信息检索
	 * 
	 * @param request
	 * @param response
	 * @param recogRequest
	 */
	@RequestMapping("/searchByPerson")
	public void searchByPerson(HttpServletRequest request,
			HttpServletResponse response, RecogRequest recogRequest) {
		// 返回结果
		String result = "";
		// 通信协议，webservice：1，socket:2,Http:3
		int requestWay = ObjectUtils.objToInt(
				request.getParameter("requestWay"), Constants.WEBSERVICE);

		// 设置服务接口代码和图片数据
		recogRequest.setBuscode(EnumClass.InterFaceEnum.SEARCH_BY_PERSON
				.getValue());
		recogRequest.setEngineCode(Constants.Config.IBIS_ENGINE_CODE);
		recogRequest.setVerCode(Constants.Config.IBIS_INTERFACE_VERCODE);
		try {
			// 请求报文对象解析成系统对应的数据格式
			String requestData = this.dataParse.parseDataStr(recogRequest);
			// 请求报文加密
			requestData = this.encrypt(requestData);
			// this.sysoRequestNoFile(requestData);
			// 发送请求报文
			result = recogRequestService.invoke(requestWay, requestData);
			// 如果响应的消息非json格式，需要转成json数据
			result = this.dataParse.toJson(result, Response.class);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			result = this.getExceptionJsonMsg("人脸接口模拟请求发生异常，详情查看后台日志");
		}
		getPrintWriter(response, result);

	}

	/**
	 * 按照图片检索
	 * 
	 * @param request
	 * @param response
	 * @param recogRequest
	 */
	@RequestMapping("/searchByImg")
	public void searchByImg(HttpServletRequest request,
			HttpServletResponse response, RecogRequest recogRequest) {
		// 返回结果
		String result = "";
		// 通信协议，webservice：1，socket:2,Http:3
		int requestWay = ObjectUtils.objToInt(
				request.getParameter("requestWay"), Constants.WEBSERVICE);
		// 获取图片数据
		String faceImgName = EnumClass.ImgNameEnum.FACE_IMG.getValue();
		try {

			Map<String, String> imgMap = ControllerUtil
					.getRequestBase64Data(request,
							new String[] { faceImgName });
			if (imgMap.isEmpty()) {
				result = BaseConstants.NULL_UPLOADFILE_ERROR;
			} else {
				// 设置服务接口代码和图片数据
				recogRequest.setBuscode(EnumClass.InterFaceEnum.SEARCH_BY_IMG
						.getValue());
				recogRequest.setEngineCode(Constants.Config.IBIS_ENGINE_CODE);
				recogRequest
						.setVerCode(Constants.Config.IBIS_INTERFACE_VERCODE);

				if (imgMap.containsKey(faceImgName)) {
					recogRequest.setFileDataone(imgMap.get(faceImgName));
				}

				// 请求报文对象解析成系统对应的数据格式
				String requestData = this.dataParse.parseDataStr(recogRequest);
				// 请求报文加密
				requestData = this.encrypt(requestData);
				// this.sysoRequestNoFile(requestData);
				// 发送请求报文
				result = recogRequestService.invoke(requestWay, requestData);
				// 如果响应的消息非json格式，需要转成json数据
				result = this.dataParse.toJson(result, Response.class);
			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			result = this.getExceptionJsonMsg("人脸接口模拟请求发生异常，详情查看后台日志");
		}
		getPrintWriter(response, result);

	}

	/**
	 * 身份证识别
	 * 
	 * @param request
	 * @param response
	 * @param recogRequest
	 */
	@RequestMapping("/ocrRecog")
	public void ocrRecog(HttpServletRequest request,
			HttpServletResponse response, RecogRequest recogRequest) {
		// 返回结果
		String result = "";
		// 通信协议，webservice：1，socket:2,Http:3
		int requestWay = ObjectUtils.objToInt(
				request.getParameter("requestWay"), Constants.WEBSERVICE);
		// 获取注册的图片数据
		String frontImgName = EnumClass.ImgNameEnum.FRONT_IMG.getValue();
		String backImgName = EnumClass.ImgNameEnum.BLACK_IMG.getValue();
		try {

			Map<String, String> imgMap = ControllerUtil
					.getRequestBase64Data(request, new String[] {
							frontImgName, backImgName });
			if (imgMap.isEmpty()) {
				result = BaseConstants.NULL_UPLOADFILE_ERROR;
			} else {
				// 设置服务接口代码和图片数据
				recogRequest.setBuscode(EnumClass.InterFaceEnum.OCR.getValue());
				recogRequest.setEngineCode(Constants.Config.IBIS_ENGINE_CODE);
				recogRequest
						.setVerCode(Constants.Config.IBIS_INTERFACE_VERCODE);

				if (imgMap.containsKey(frontImgName)) {
					recogRequest.setFrontImge(imgMap.get(frontImgName));
				}
				if (imgMap.containsKey(backImgName)) {
					recogRequest.setBackImge(imgMap.get(backImgName));
				}
				// 请求报文对象解析成系统对应的数据格式
				String requestData = this.dataParse.parseDataStr(recogRequest);
				// this.sysoRequestNoFile(requestData);
				// 请求报文加密
				requestData = this.encrypt(requestData);
				// 发送请求报文
				result = recogRequestService.invoke(requestWay, requestData);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			result = this.getExceptionMsg("人脸接口模拟请求发生异常，详情查看后台日志");
		}
		getPrintWriter(response, result);
	}
	
	/**
	 * 
	* @Title: bankCardOcrRecog 
	* @Description: 银行卡OCR识别
	* @param request
	* @param response
	* @param recogRequest      
	* @author:huyuxin
	 */
	@RequestMapping("/bankCardOcrRecog")
	public void bankCardOcrRecog(HttpServletRequest request,
					HttpServletResponse response, RecogRequest recogRequest){
		// 返回结果
		String result = "";
		// 通信协议，webservice：1，socket:2,Http:3
		int requestWay = ObjectUtils.objToInt(
				request.getParameter("requestWay"), Constants.WEBSERVICE);
		// 获取图片数据
		String bankCardImgName= EnumClass.ImgNameEnum.BANK_IMG.getValue();
		try {
			
			Map<String, String> imgMap = ControllerUtil
					.getRequestBase64Data(request,new String[] {bankCardImgName});
			if (imgMap.isEmpty()) {
				result = BaseConstants.NULL_UPLOADFILE_ERROR;
			} else {
				// 设置服务接口代码和图片数据
				recogRequest.setBuscode(EnumClass.InterFaceEnum.OCR_BANK_CARD.getValue());
				recogRequest.setEngineCode(Constants.Config.IBIS_ENGINE_CODE);
				recogRequest.setVerCode(Constants.Config.IBIS_INTERFACE_VERCODE);

				if (imgMap.containsKey(bankCardImgName)) {
					recogRequest.setBankCardImg(imgMap.get(bankCardImgName));
				}
				// 请求报文对象解析成系统对应的数据格式
				String requestData = this.dataParse.parseDataStr(recogRequest);
				// 请求报文加密
				requestData = this.encrypt(requestData);
				// 发送请求报文
				result = recogRequestService.invoke(requestWay, requestData);
				// 如果响应的消息非json格式，需要转成json数据
				result = this.dataParse.toJson(result, Response.class);
			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			result = this.getExceptionJsonMsg("人脸接口模拟请求发生异常，详情查看后台日志");
		}
		getPrintWriter(response, result);
	}
	
	@RequestMapping("/toFaceAdd")
	public String toAddFace(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return FACE_ADD_PAGE;
	}

	@RequestMapping("/toIdcardFaceCompare")
	public String toFaceCard(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return FACE_CARD_COMPARE_PAGE;
	}

	@RequestMapping("/toFaceFaceCompare")
	public String toFaceFace(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return FACE_FACE_COMPARE_PAGE;
	}

	@RequestMapping("/toSearchByPerson")
	public String toCardSearchUsr(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return SEARCH_BY_PERSON_PAGE;
	}

	@RequestMapping("/toSearchByImg")
	public String toFaceSearchUsr(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return SEARCH_BY_IMG_PAGE;
	}

	@RequestMapping("/toIdcardFaceCompareEx")
	public String toTwoCardOneFace(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return IDCARD_FACE_COMPARE_EX_PAGE;
	}

	@RequestMapping("/toOcrRecog")
	public String toOcrRecog(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return OCR_RECOG_PAGE;
	}
	
	@RequestMapping("/toBankCardOcrRecog")
	public String toBankCardOcrRecog(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return BANKCARD_OCR_RECOG_PAGE;
	}

	/**
	 * 请求报文加密
	 * 
	 * @param requestData
	 *            加密字符串
	 * @throws Exception
	 */
	private String encrypt(String requestData) throws Exception {
		String isencrypt = Constants.Config.IBIS_REQUEST_ISENCRYPT;
		if (Constants.PARAM_ISENCRYPT.equals(isencrypt)) {
			// 加密接口输入json，采用des和base64加密
			requestData = DesUtil.encrypt(requestData,
					Constants.Config.IBIS_REQUEST_KEY);
		}
		return requestData;
	}

	/**
	 * 获取人脸识别接口模拟异常的返回信息
	 * 
	 * @param msg
	 * @return
	 */
	private String getExceptionMsg(String msg) {
		Response result = Response.getFailResponse(null);
		result.setMessage(msg);
		return this.dataParse.parseDataStr(result);
	}

	/**
	 * 获取人脸识别接口模拟异常的json返回信息
	 * 
	 * @param msg
	 * @return
	 */
	private String getExceptionJsonMsg(String msg) {
		Response result = Response.getFailResponse(null);
		result.setMessage(msg);
		return JsonUtil.toJSON(result);
	}
	
	/**
	 * 获取图片base64字符串
	 * @param request
	 * @param response
	 */
	@RequestMapping("uploadImage")
	public void uploadImage(HttpServletRequest request, HttpServletResponse response)  {
		JsonResult resultEntity = ControllerUtil.uploadImage(request, response);	
		getPrintWriter(response, JsonUtil.toJSON(resultEntity));
	}

}
