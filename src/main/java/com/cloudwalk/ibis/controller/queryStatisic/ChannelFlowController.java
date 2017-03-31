package com.cloudwalk.ibis.controller.queryStatisic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonEntityResult;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.file.CsvFileUtil;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowBank;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowCard;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowCertificateFeature;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowCompareStrengthen;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowDetectLive;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowFaceToFace;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowRegister;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowSearchImg;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowSearchPerson;
import com.cloudwalk.ibis.service.queryStatisic.ChannelFlowService;
/**
*
* ClassName: channelFlowController <br/>
* Description: 流水信息. <br/>
* date: 2016年9月27日 下午2:13:16 <br/>
*
* @author 方凯
* @version
* @since JDK 1.7
*/
@Controller
@RequestMapping("/channelFlow")
public class ChannelFlowController extends BaseController{
	
	@Resource(name = "channelFlowService")
	private ChannelFlowService channelFlowService;

	/**
	 *
	 * selectchannelFlowFeatureByPage:查询生物特征库维护. <br/>
	 *
	 * @author:方凯 Date: 2015年8月21日 下午2:37:20
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectChannelFlowByPage")
	public void selectChannelFlowByPage(HttpServletRequest request,
			HttpServletResponse response, ChannelFlow channelFlow, PageInfo pageInfo) {
		this.setPageInfo(request, pageInfo);
		JsonListResult<ChannelFlow> result = new JsonListResult<ChannelFlow>();
		String retJson = "";
		
		try{
			channelFlow.setLegalOrgCode(BaseUtil.getCurrentUser(request).getLegalOrgCode());			
			// 设置分页查询
			JSONObject map = new JSONObject();
			//查询条件中结束日期需要加1天，才可以查到如2015-10-17至2015-10-17的数据，否则，查不到
			if(channelFlow.getBeginTime()!=null&&!"".equals(channelFlow.getEndTime())){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(channelFlow.getEndTime());
				calendar.add(Calendar.DATE, 1);
				channelFlow.setEndTime(calendar.getTime());
			}
			// 设置分页信息
			map.put("obj", channelFlow);
			map.put("page", pageInfo);
			
			List<ChannelFlow> list = this.channelFlowService.selectAllByPage(map);
			
			//统计结果成功是失败的结果,并添加到查询结果后面
//			int success = 0;
///			int fail = 0;
//			List<FlowStatisic> flowStatisics = this.channelFlowService.resultCount(map);
//			if(!ObjectUtils.isEmpty(flowStatisics)) {
//				for(FlowStatisic flowStatisic :flowStatisics){	
//				  if(EnumClass.StatusEnum.YES.getValue() == flowStatisic.getStatus()){
//				    //成功
//				    success=flowStatisic.getCounts();
//				  } else if(EnumClass.StatusEnum.NO.getValue() == flowStatisic.getStatus()){
//				    //失败
//				    fail=flowStatisic.getCounts();
//				  }
//				}
//			}
		
			// 设置返回结果
			result.setRows(list);
			result.setTotal(Long.valueOf(pageInfo.getTotalCount()));
			result.getData().put("success",""+pageInfo.getTotalCount());
			result.getData().put("fail", ""+0);
			retJson = JsonUtil.toJSON(result);						
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		getPrintWriter(response, retJson);

	}
	
	/**
	 *
	 * selectChannelBLOBFlowById:根据接口流水id获取请求报文返回报文信息. <br/>
	 *
	 * @author:方凯 Date: 2016年9月29日16:11:00
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectChannelBLOBFlowById")
	public void selectChannelBLOBFlowById(HttpServletRequest request,
			HttpServletResponse response, ChannelFlow channelFlow) {

		JsonEntityResult<ChannelFlow> result = new JsonEntityResult<ChannelFlow>();
		String retJson = "";
		
		try {
			List<ChannelFlow> entity = channelFlowService.searchAllBLOB(channelFlow);
			// 设置返回结果
			result.setEntity(entity.get(0));
			retJson = JsonUtil.toJSON(result);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		
		getPrintWriter(response, retJson);
	}
	
	/**
	 *
	 * selectChannelFlowById:根据接口流水id获取信息. <br/>
	 *
	 * @author:方凯 Date: 2016年9月29日16:11:00
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectChannelFlowById")
	public void selectChannelFlowById(HttpServletRequest request,
			HttpServletResponse response, ChannelFlow channelFlow) {

		JsonEntityResult<ChannelFlow> result = new JsonEntityResult<ChannelFlow>();
		String retJson = "";
		try{
			//获取主表信息
			List<ChannelFlow> entity = channelFlowService.selectPFLowById(channelFlow.getFlowId());
			
			//获取分表信息
			ChannelFlow channelFlowEntiy=entity.get(0);
			if(channelFlowEntiy !=null){
				//根据busscode查询相应的分表，获取相应的查询数据
				if(EnumClass.InterFaceEnum.REG.getValue().equals(channelFlowEntiy.getBusCode())){
					//人脸注册
					ChannelFlowRegister channelFlowRegister=channelFlowService.selectChannelFlowRegisterById(channelFlow.getFlowId());
					channelFlowEntiySetChannelFlowRegister(channelFlowEntiy, channelFlowRegister);
				}else if(EnumClass.InterFaceEnum.CHECK_PERSON.getValue().equals(channelFlowEntiy.getBusCode())){
					//证脸对比
					ChannelFlowCertificateFeature channelFlowCertificateFeature =channelFlowService.selectChannelFlowCertificateFeatureById(channelFlow.getFlowId());
					channelFlowEntiySetChannelFlowCertificateFeature(channelFlowEntiy, channelFlowCertificateFeature);
				}else if(EnumClass.InterFaceEnum.COMPARE.getValue().equals(channelFlowEntiy.getBusCode())){
					//脸脸对比
					ChannelFlowFaceToFace channelFlowFaceToFace=channelFlowService.selectChannelFlowFaceToFaceById(channelFlow.getFlowId());
					channelFlowEntiySetChannelFlowFaceToFace(channelFlowEntiy, channelFlowFaceToFace);
				}else if(EnumClass.InterFaceEnum.CHECK_PERSON_EX.getValue().equals(channelFlowEntiy.getBusCode())){
					//两证一脸
					ChannelFlowCompareStrengthen channelFlowCompareStrengthen=channelFlowService.selectChannelFlowCompareStrengthenById(channelFlow.getFlowId());
					channelFlowEntiySetChannelFlowCompareStrengthen(channelFlowEntiy, channelFlowCompareStrengthen);
				}else if(EnumClass.InterFaceEnum.OCR.getValue().equals(channelFlowEntiy.getBusCode())){
					//身份证OCR
					ChannelFlowCard channelFlowCard = channelFlowService.selectChannelFlowCardById(channelFlow.getFlowId());
					channelFlowEntiySetChannelFlowCard(channelFlowEntiy, channelFlowCard);
				}else if(EnumClass.InterFaceEnum.OCR_BANK_CARD.getValue().equals(channelFlowEntiy.getBusCode())){
					//银行卡OCR
					ChannelFlowBank channelFlowBank = channelFlowService.selectChannelFlowBankById(channelFlow.getFlowId());
					channelFlowEntiySetChannelFlowBank(channelFlowEntiy,channelFlowBank);
				}else if(EnumClass.InterFaceEnum.SEARCH_BY_PERSON.getValue().equals(channelFlowEntiy.getBusCode())){
					//按证检索人
					ChannelFlowSearchPerson channelFlowSearchPerson = channelFlowService.selectChannelFlowSearchPersonById(channelFlow.getFlowId());
					channelFlowEntiySetChannelFlowSearchPerson(channelFlowEntiy,channelFlowSearchPerson);
				}else if(EnumClass.InterFaceEnum.SEARCH_BY_IMG.getValue().equals(channelFlowEntiy.getBusCode())){
					//按脸检索人
					ChannelFlowSearchImg channelFlowSearchImg = channelFlowService.selectChannelFlowSearchImgById(channelFlow.getFlowId());
					channelFlowEntiySetChannelFlowSearchImg(channelFlowEntiy,channelFlowSearchImg);
				}else if(EnumClass.InterFaceEnum.CHECK_LIVENESS.getValue().equals(channelFlowEntiy.getBusCode())){
					//活体检测
					ChannelFlowDetectLive channelFlowDetectLive=channelFlowService.selectChannelFlowDetectLiveById(channelFlow.getFlowId());
					channelFlowEntiySetChannelFlowDetectLive(channelFlowEntiy,channelFlowDetectLive);
				}
				
			}
			// 设置返回结果
			result.setEntity(channelFlowEntiy);
			retJson = JsonUtil.toJSON(result);		
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		
		getPrintWriter(response, retJson);
	}
	
	private void channelFlowEntiySetChannelFlowRegister(ChannelFlow channelFlowEntiy,
					ChannelFlowRegister channelFlowRegister) {
		channelFlowEntiy.setFeatureId(channelFlowRegister.getFeatureId());
		channelFlowEntiy.setRegFilePath(channelFlowRegister.getFilePath());
		channelFlowEntiy.setPersonId(channelFlowRegister.getPersonId());
	}
	
	/**
	 * channelFlowEntiySetChannelFlowDetectLive:(人脸活体数据封装). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Feb 27, 2017 1:50:07 PM
	 * @param channelFlowEntiy
	 * @param channelFlowDetectLive
	 * @since JDK 1.7
	 */
	private void channelFlowEntiySetChannelFlowDetectLive(ChannelFlow channelFlowEntiy,
			ChannelFlowDetectLive channelFlowDetectLive) {
		channelFlowEntiy.setFileOnePath(channelFlowDetectLive.getImgpath());
	}

	private void channelFlowEntiySetChannelFlowCertificateFeature(ChannelFlow channelFlowEntiy,
				ChannelFlowCertificateFeature channelFlowCertificateFeature) {
		channelFlowEntiy.setFeatureId(channelFlowCertificateFeature.getFeatureId());
		channelFlowEntiy.setPersonId(channelFlowCertificateFeature.getPersonId());
		channelFlowEntiy.setFpOneCmpScore(channelFlowCertificateFeature.getCmpscore()+"");
		
		//注册文件
		channelFlowEntiy.setRegFilePath(channelFlowCertificateFeature.getFeatureFilePath());
		//对比文件
		channelFlowEntiy.setFileOnePath(channelFlowCertificateFeature.getFilePath());
	}

	private void channelFlowEntiySetChannelFlowFaceToFace(
			ChannelFlow channelFlowEntiy,
			ChannelFlowFaceToFace channelFlowFaceToFace) {
		channelFlowEntiy.setFileOnePath(channelFlowFaceToFace.getFileonePath());
		channelFlowEntiy.setFileTwoPath(channelFlowFaceToFace.getFiletwoPath());
		channelFlowEntiy.setCmpScore(channelFlowFaceToFace.getCmpscore()+"");
	}

	private void channelFlowEntiySetChannelFlowSearchImg(
			ChannelFlow channelFlowEntiy,
			ChannelFlowSearchImg channelFlowSearchImg) {
		channelFlowEntiy.setFileOnePath(channelFlowSearchImg.getFilePath());
//		channelFlowEntiy.setFlowId(channelFlowSearchImg.getFlowId());
//		channelFlowEntiy.setRemark(channelFlowSearchImg.getRemark());
		
	}

	private void channelFlowEntiySetChannelFlowSearchPerson(
			ChannelFlow channelFlowEntiy,
			ChannelFlowSearchPerson channelFlowSearchPerson) {
		channelFlowEntiy.setFeatureId(channelFlowSearchPerson.getFeatureId());
//		channelFlowEntiy.setFlowId(channelFlowSearchPerson.getFlowId());
		channelFlowEntiy.setPersonId(channelFlowSearchPerson.getPersonId());
//		channelFlowEntiy.setRemark(channelFlowSearchPerson.getRemark());
		
	}

	private void channelFlowEntiySetChannelFlowBank(
			ChannelFlow channelFlowEntiy, ChannelFlowBank channelFlowBank) {
		
		channelFlowEntiy.setBankcardNo(channelFlowBank.getBankCardNo());
//		channelFlowEntiy.set(channelFlowBank.getBankCardType());
//		channelFlowEntiy.set(channelFlowBank.getBankName());
//		channelFlowEntiy.set(channelFlowBank.getCardName());
//		channelFlowEntiy.setFlowId(channelFlowBank.getFlowId());
		channelFlowEntiy.setFileOnePath(channelFlowBank.getImgpath());
//		channelFlowEntiy.setRemark(channelFlowBank.getRemark());
		
	}

	private void channelFlowEntiySetChannelFlowCard(
			ChannelFlow channelFlowEntiy, ChannelFlowCard channelFlowCard) {
//		channelFlowEntiy.set(channelFlowCard.getAddress());
//		channelFlowEntiy.set(channelFlowCard.getAuthority());
//		channelFlowEntiy.set(channelFlowCard.getBirthday());
		
		channelFlowEntiy.setFileTwoPath(channelFlowCard.getBlackImgpath());
		
//		channelFlowEntiy.set(channelFlowCard.getFaceImgpath());
//		channelFlowEntiy.set(channelFlowCard.getFlag());
//		channelFlowEntiy.set(channelFlowCard.getFlowId());
//		channelFlowEntiy.set(channelFlowCard.getFolk());
		
		channelFlowEntiy.setFileOnePath(channelFlowCard.getFrontImgpath());
		channelFlowEntiy.setCtftype("身份证");
		channelFlowEntiy.setCtfname(channelFlowCard.getIdcardName());
		channelFlowEntiy.setCtfno(channelFlowCard.getIdcardNo());
//		channelFlowEntiy.setRemark(channelFlowCard.getRemark());
		
//		channelFlowEntiy.set(channelFlowCard.getSex());
//		channelFlowEntiy.set(channelFlowCard.getValiddate1());
//		channelFlowEntiy.set(channelFlowCard.getValiddate2());
	}
	
	
	/**
	 * channelFlowEntiySetChannelFlowCompareStrengthen:(两证一脸详情页面数据封装). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Feb 27, 2017 1:50:44 PM
	 * @param channelFlowEntiy
	 * @param channelFlowCompareStrengthen
	 * @since JDK 1.7
	 */
	private void channelFlowEntiySetChannelFlowCompareStrengthen(
			ChannelFlow channelFlowEntiy,
			ChannelFlowCompareStrengthen channelFlowCompareStrengthen) {
		channelFlowEntiy.setFpOneCmpScore(channelFlowCompareStrengthen.getFpOneCmpscore()+"");
		channelFlowEntiy.setFpTwoCmpScore(channelFlowCompareStrengthen.getFpTwoCmpscore()+"");
		channelFlowEntiy.setFileOnePath(channelFlowCompareStrengthen.getFileOnePath());
		channelFlowEntiy.setFileTwoPath(channelFlowCompareStrengthen.getFileTwoPath());
		channelFlowEntiy.setRegFilePath(channelFlowCompareStrengthen.getFeatureFilePath());
	}
	
	/**
	 *
	 * exportCvs:导出cvs1. <br/>
	 *
	 * @author:方凯 Date: 2016年9月29日16:24:30
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 * @since JDK 1.7
	 */
	@RequestMapping("/exportCsv")
	public void exportCsv(HttpServletRequest request,
			HttpServletResponse response, ChannelFlow record,
			PageInfo pageInfo) {		
		record.setFlowId("");					
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String bussCode=request.getParameter("busCode");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			
			if(beginTime != null && !"".equals(beginTime)){
				Date d = sd.parse(beginTime); 
				record.setBeginTime(d);
			}
			if(endTime != null&&!"".equals(endTime)){
				Date d = sd.parse(endTime); 
				record.setEndTime(d);
			}
			if(bussCode != null&&!"".equals(bussCode)){
				record.setBusCode(bussCode);
			}
			List<ChannelFlow> entityList=null;
			//根据busscode查询相应的分表，获取相应的查询数据
			if(EnumClass.InterFaceEnum.CHECK_PERSON_EX.getValue().equals(bussCode)){
				//两证一脸
				entityList = channelFlowService.searchCheckPersonExAll(record);
			}else if(EnumClass.InterFaceEnum.OCR.getValue().equals(bussCode)){
				//身份证OCR
				entityList = channelFlowService.selectAllFlows(record);
			}else if(EnumClass.InterFaceEnum.OCR_BANK_CARD.getValue().equals(bussCode)){
				//银行卡OCR
				entityList = channelFlowService.selectAllFlows(record);
			}else if(EnumClass.InterFaceEnum.SEARCH_BY_PERSON.getValue().equals(bussCode)){
				//按证检索人
				entityList = channelFlowService.selectAllFlows(record);
			}else if(EnumClass.InterFaceEnum.SEARCH_BY_IMG.getValue().equals(bussCode)){
				//按脸检索人
				entityList = channelFlowService.selectAllFlows(record);
			}else if(EnumClass.InterFaceEnum.REG.getValue().equals(bussCode)){
				//人脸注册
				entityList = channelFlowService.searchRegisterFlow(record);
			}else if(EnumClass.InterFaceEnum.CHECK_PERSON.getValue().equals(bussCode)){
				//证脸对比
				entityList = channelFlowService.searchCertificateFeatureFlow(record);
			}else if(EnumClass.InterFaceEnum.COMPARE.getValue().equals(bussCode)){
				//脸脸对比
				entityList = channelFlowService.searchFaceCompareFlow(record);
			}else if(EnumClass.InterFaceEnum.CHECK_LIVENESS.getValue().equals(bussCode)){
				//活体检测
				entityList = channelFlowService.searchChannelFlowDetectLiveAll(record);
			}
			// 导出csv
			String fileName = "生物识别比对结果";
			String fieldString = "flowId,tradingCode,channel,busCode,engineCode,engineVerCode,result,cmpScore,ctftype,ctfno,ctfname,customerId";
			CsvFileUtil.doExportCsvBody(entityList, fileName, fieldString,
					ChannelFlow.class, request, response);

		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}
	
	/**
	 * 根据featureId查询注册人脸地址
	 * @param request
	 * @param response
	 * @param channelFlow
	 */
	@RequestMapping(value = "/selectPathByFeatureId")
	public void selectPathByFeatureId(HttpServletRequest request,
			HttpServletResponse response, PersonFeature feature) {

//		String path = channelFlowService.selectPathByFeatureId(feature);

		//FileUtils.loadImg(path,request,response);
	}
}
