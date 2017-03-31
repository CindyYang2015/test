package com.cloudwalk.ibis.controller.queryStatisic;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.file.CsvFileUtil;
import com.cloudwalk.ibis.model.featurelib.Person;
import com.cloudwalk.ibis.model.queryStatisic.PersonRecogFlow;
import com.cloudwalk.ibis.model.recogSet.BlackPersonCheck;
import com.cloudwalk.ibis.model.recogSet.WhitePersonCheck;
import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonVo;
import com.cloudwalk.ibis.model.recogSet.vo.WhitePersonCheckVo;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.featurelib.PersonService;
import com.cloudwalk.ibis.service.queryStatisic.PersonRecogFlowService;
import com.cloudwalk.ibis.service.recogSet.BlackPersonService;
import com.cloudwalk.ibis.service.recogSet.WhitePersonService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
*
* ClassName: channelFlowController <br/>
* Description: 识别认证统计表. <br/>
* date: 2016年9月27日 下午2:13:16 <br/>
*
* @author 方凯
* @version
* @since JDK 1.7
*/
@Controller
@RequestMapping("/personRecogFlow")
public class PersonRecogFlowController extends BaseController{
	
	@Resource(name = "personRecogFlowService")
	private PersonRecogFlowService personRecogFlowService;
	
	//日志操作
	@Resource(name = "logOperService")
	private LogOperService logOperService;
	
	/**
	 * 客户信息服务
	 */
	@Resource(name = "personService")
	private PersonService personService;
	
	/**
	 * 白名单服务
	 */
	@Resource(name = "whitePersonService")
	private WhitePersonService whitePersonService;
	
	/**
	 * 黑名单服务
	 */
	@Resource(name = "blackPersonService")
	private BlackPersonService blackPersonService;
	
	
	
	/**
	 *
	 * selectchannelFlowFeatureByPage:查询识别认证统计表. <br/>
	 *
	 * @author:方凯 Date: 2015年8月21日 下午2:37:20
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectPersonRecogFlowByPage")
	public void selectPersonRecogFlowByPage(HttpServletRequest request,
			HttpServletResponse response, PersonRecogFlow channelFlow, PageInfo pageInfo) {
		this.setPageInfo(request, pageInfo);
		JsonListResult<PersonRecogFlow> result = new JsonListResult<PersonRecogFlow>();
		String retJson = "";
		
		try {
			channelFlow.setLegalOrgCode(BaseUtil.getCurrentUser(request).getLegalOrgCode());
			// 设置分页查询
			JSONObject map = new JSONObject();
			// 设置分页信息
			map.put("obj", channelFlow);
			map.put("page", pageInfo);
			List<PersonRecogFlow> list = this.personRecogFlowService.selectAllByPage(map);
	
			// 设置返回结果
			result.setRows(list);
			result.setTotal(Long.valueOf(Long.valueOf(pageInfo.getTotalCount())));
			retJson = JsonUtil.toJSON(result);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		getPrintWriter(response, retJson);

	}
	
	/**
	 *
	 * selectPersonRecogFlowById:根据识别认证统计表id获取信息. <br/>
	 *
	 * @author:方凯 Date: 2016年9月29日16:11:00
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectPersonRecogFlowById")
	public void selectPersonRecogFlowById(HttpServletRequest request,
			HttpServletResponse response, PersonRecogFlow channelFlow) {

		JsonEntityResult<PersonRecogFlow> result = new JsonEntityResult<PersonRecogFlow>();
		String retJson = "";
		try {
			List<PersonRecogFlow> entity = personRecogFlowService.searchAll(channelFlow);
			// 设置返回结果
			result.setEntity(entity.get(0));
			retJson = JsonUtil.toJSON(result);	
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}
		getPrintWriter(response, retJson);
	}
	
	
	@RequestMapping(value = "/isBlackExist")
	public void isBlackExist(HttpServletRequest request,
			HttpServletResponse response, PersonRecogFlow channelFlow,String blackType){
		
			//返回结果
			String retJson = "";
			Person person = this.personService.selectByPrimaryKey(channelFlow.getPersonId());
			if(person != null){
				BlackPersonCheck blackPersonCheck = new BlackPersonCheck();
				blackPersonCheck.setLegalOrgCode(person.getLegalOrgCode());
				blackPersonCheck.setCtftype(person.getCtftype());
				blackPersonCheck.setCtfno(person.getCtfno());
				blackPersonCheck.setCtfname(person.getCtfname());
				blackPersonCheck.setCustomerId(person.getCustomerId());
				
				blackPersonCheck.setChannel(channelFlow.getChannel());
				blackPersonCheck.setTradingCode(channelFlow.getTradingCode());
				blackPersonCheck = blackPersonService.getBlackPersonCheck(blackPersonCheck);
				if(blackPersonCheck != null) {
					retJson = ControllerUtil.getFailRetJson("该客户黑名单已存在");
					getPrintWriter(response, retJson);
					return;
				}else{
					retJson = ControllerUtil.getSuccessRetJson("");
					getPrintWriter(response, retJson);
					return;
				}
			}
		}
	
	@RequestMapping(value = "/isWhiteExist")
	public void isWhiteExist(HttpServletRequest request,
			HttpServletResponse response, PersonRecogFlow channelFlow,String blackType){
		//返回结果
		String retJson = "";
			Person person = this.personService.selectByPrimaryKey(channelFlow.getPersonId());
			if(person != null){	
				//判断该客户白名单是否已存在
				WhitePersonCheck  whitePersonCheck = new WhitePersonCheck();
				whitePersonCheck.setCtftype(person.getCtftype());
				whitePersonCheck.setCustomerId(person.getCustomerId());
				whitePersonCheck.setCtfno(person.getCtfno());
				whitePersonCheck.setLegalOrgCode(person.getLegalOrgCode());
				whitePersonCheck.setCtfname(person.getCtfname());
				
				whitePersonCheck.setTradingCode(channelFlow.getTradingCode());
				whitePersonCheck.setChannel(channelFlow.getChannel());
				whitePersonCheck = whitePersonService.getWhitePersonCheck(whitePersonCheck);
				if(whitePersonCheck != null) {					
					retJson = ControllerUtil.getFailRetJson("该客户白名单已存在");
					getPrintWriter(response, retJson);
					return;
				}else{
					retJson = ControllerUtil.getSuccessRetJson("");
					getPrintWriter(response, retJson);
					return;
				}
			}
		}
	
	/**
	 *
	 * addBlack:根据识别认证统计id将客户拉入黑名单. <br/>
	 *
	 * @author:方凯 Date: 2016年9月29日16:11:00
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */

	@RequestMapping(value = "/addBlack")
	public void addBlack(HttpServletRequest request,
			HttpServletResponse response, PersonRecogFlow channelFlow,String blackType) {

		//返回结果
		String retJson = "";
		
		try {
			Person person = this.personService.selectByPrimaryKey(channelFlow.getPersonId());
			if(person != null){	
				User user = BaseUtil.getCurrentUser(request);
				BlackPersonVo blackPersonVo = new BlackPersonVo();
				List<String> channels = new ArrayList<String>();
				List<String> tradingCodes = new ArrayList<String>();
				channels.add(channelFlow.getChannel());
				tradingCodes.add(channelFlow.getTradingCode());
				blackPersonVo.setCtftype(person.getCtftype());
				blackPersonVo.setCtfname(person.getCtfname());
				blackPersonVo.setCtfno(person.getCtfno());
				blackPersonVo.setCustomerId(person.getCustomerId());
				blackPersonVo.setLegalOrgCode(person.getLegalOrgCode());
				blackPersonVo.setOrgCode(user.getOrgCodePath());
				blackPersonVo.setCreator(user.getOrgCodePath());
				blackPersonVo.setLegalOrgCode(person.getLegalOrgCode());
				blackPersonVo.setChannels(channels);
				blackPersonVo.setTradingCodes(tradingCodes);
				blackPersonVo.setBlackType(blackType);
				
				//若白名单中存在则先删除白名单中信息
				WhitePersonCheck  whitePersonCheck = new WhitePersonCheck();
				whitePersonCheck.setCtftype(person.getCtftype());
				whitePersonCheck.setCustomerId(person.getCustomerId());
				whitePersonCheck.setCtfno(person.getCtfno());
				whitePersonCheck.setLegalOrgCode(person.getLegalOrgCode());
				whitePersonCheck.setCtfname(person.getCtfname());
				whitePersonCheck = whitePersonService.getWhitePersonCheck(whitePersonCheck);
				if(whitePersonCheck != null){
					whitePersonService.deleteWhitePersonCheck(whitePersonCheck.getId());
					whitePersonService.deleteWhitePersonCheckControlByWhiteID(whitePersonCheck.getId());
					//TODO 删除审核过的信息
					String whiteId=whitePersonCheck.getWhiteId();
					if ( null != whiteId && !("").equals(whiteId) ) {
						whitePersonService.deleteWhitePerson(whiteId);
					 }
				}
				
				int record = personRecogFlowService.createBlackPerson(request, blackPersonVo);
				if(record > 0){
					logOperService.insertLogOper(request, 1, "将客户拉入黑名单,详情:"+blackPersonVo.toString());
					retJson = ControllerUtil.getSuccessRetJson("该客户拉入黑名单成功");
					getPrintWriter(response, retJson);
					return;
					} 
			}		
			retJson = ControllerUtil.getFailRetJson("该客户拉入黑名单失败");
			getPrintWriter(response, retJson);
		
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
			getPrintWriter(response, retJson);
		}
	}
		
	/**
	 *
	 * addBlack:根据识别认证统计id将客户拉入白名单. <br/>
	 *
	 * @author:方凯 Date: 2016年9月29日16:11:00
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/addWhite")
	public void addWhite(HttpServletRequest request,
			HttpServletResponse response, PersonRecogFlow channelFlow,String whiteType,double score) {
		
		//返回结果
		String retJson = "";
		
		try {
			Person person = this.personService.selectByPrimaryKey(channelFlow.getPersonId());
			if(person != null){			
				User user = BaseUtil.getCurrentUser(request);
				List<String> channels = new ArrayList<String>();
				List<String> tradingCodes = new ArrayList<String>();
				WhitePersonCheckVo white = new WhitePersonCheckVo();
				channels.add(channelFlow.getChannel());
				tradingCodes.add(channelFlow.getTradingCode());
				white.setCtftype(person.getCtftype());
				white.setCtfname(person.getCtfname());
				white.setCtfno(person.getCtfno());
				white.setCustomerId(person.getCustomerId());
				white.setLegalOrgCode(person.getLegalOrgCode());
				white.setOrgCode(user.getOrgCodePath());
				white.setCreator(user.getUserId());
				white.setEngineCode(channelFlow.getEngineCode());
				white.setEngineverCode(channelFlow.getEngineVerCode());
				white.setScore(BigDecimal.valueOf(score));
				white.setChannels(channels);
				white.setTradingCodes(tradingCodes);
				white.setWhiteType(whiteType);
				//若黑名单中存在则先删除黑名单中信息
				BlackPersonCheck blackPersonCheck = new BlackPersonCheck();
				blackPersonCheck.setLegalOrgCode(person.getLegalOrgCode());
				blackPersonCheck.setCtftype(person.getCtftype());
				blackPersonCheck.setCtfno(person.getCtfno());
				blackPersonCheck.setCustomerId(person.getCustomerId());
				blackPersonCheck.setCtfname(person.getCtfname());
				blackPersonCheck = blackPersonService.getBlackPersonCheck(blackPersonCheck);
				if(blackPersonCheck != null) {
					blackPersonService.deleteBlackPersonCheck(blackPersonCheck.getId());
					blackPersonService.deleteByCheckBlackPersonID(blackPersonCheck.getId());
					String blackId=blackPersonCheck.getBlackId();
					if (null!=blackId && !("").equals(blackId)){
						blackPersonService.deleteBlackPerson(blackId);
					}
				}
				int record = personRecogFlowService.createWhitePersonCheck(request, white);
				if(record > 0){
					logOperService.insertLogOper(request, 1, "将客户拉入白名单,详情:"+white.toString());
					retJson = ControllerUtil.getSuccessRetJson("该客户拉入白名单成功");
					getPrintWriter(response, retJson);
					return;
				} 
			}		
			retJson = ControllerUtil.getSuccessRetJson("该客户拉入白名单失败");
			getPrintWriter(response, retJson);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
			getPrintWriter(response, retJson);
		}
	}
	
	/**
	 * 
	 * exportCsv:(导出cvs). <br/>
	 *
	 * @author:冯德志   Date: 2016年10月18日 下午5:55:17
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 * @since JDK 1.7
	 */
  @RequestMapping("/exportCsv")
  public void exportCsv(HttpServletRequest request,
      HttpServletResponse response, PersonRecogFlow record,
      PageInfo pageInfo) {
	  
	try {
	    record.setLegalOrgCode(BaseUtil.getCurrentUser(request).getLegalOrgCode());
	    List<PersonRecogFlow> entityList = this.personRecogFlowService.searchAll(record);
	    // 导出csv
	    String fileName = "高危人员管理统计";
	    String fieldString = "ctftype,ctfno,ctfname,customerId,channel,tradingCode,sucessCount,failCount";
	    CsvFileUtil.doExportCsvBody(entityList, fileName, fieldString,
	        PersonRecogFlow.class, request, response);
	} catch(Exception e) {
		log.error(e.getLocalizedMessage());
	}
  }

}
