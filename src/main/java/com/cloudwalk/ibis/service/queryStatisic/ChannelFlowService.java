package com.cloudwalk.ibis.service.queryStatisic;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.cloudwalk.ibis.model.queryStatisic.FlowStatisic;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowBankMapper;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowCardMapper;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowCertificateFeatureMapper;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowCompareStrengthenMapper;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowDetectLiveMapper;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowFaceCompareMapper;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowMapper;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowRegisterMapper;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowSearchImgMapper;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowSearchPersonMapper;
import com.cloudwalk.ibis.service.featurelib.PersonFeatureService;
import com.cloudwalk.ibis.service.system.DicService;
import com.google.common.collect.Maps;


@Service("channelFlowService")
public class ChannelFlowService {
	@Resource(name = "channelFlowMapper")
	private ChannelFlowMapper channelFlowMapper;
	
	@Resource(name="dicService")
	private DicService dicService;
	
	@Resource(name="personFeatureService")
	private PersonFeatureService personFeatureService;
	
	@Resource(name = "personRecogFlowService")
	private PersonRecogFlowService personRecogFlowService;
	
	@Resource(name = "channelFlowCompareStrengthenMapper")
	private ChannelFlowCompareStrengthenMapper channelFlowCompareStrengthenMapper;
	
	@Resource(name = "channelFlowCardMapper")
	private ChannelFlowCardMapper channelFlowCardMapper;
	
	@Resource(name = "channelFlowBankMapper")
	private ChannelFlowBankMapper channelFlowBankMapper;
	
	@Resource(name = "channelFlowSearchPersonMapper")
	private ChannelFlowSearchPersonMapper channelFlowSearchPersonMapper;
	
	@Resource(name = "channelFlowSearchImgMapper")
	private ChannelFlowSearchImgMapper channelFlowSearchImgMapper;
	
	@Resource(name = "channelFlowRegisterMapper")
	private ChannelFlowRegisterMapper channelFlowRegisterMapper; 
	
	@Resource(name = "channelFlowCertificateFeatureMapper")
	private ChannelFlowCertificateFeatureMapper channelFlowCertificateFeatureMapper; 
	
	@Resource(name = "channelFlowFaceCompareMapper")
	private ChannelFlowFaceCompareMapper channelFlowFaceCompareMapper;
	
	@Resource(name = "channelFlowDetectLiveMapper")
	private ChannelFlowDetectLiveMapper channelFlowDetectLiveMapper;
	
	protected final Logger log = Logger.getLogger(this.getClass());
	/**
	 * 分页查询流水表基本信息
	 * @param map
	 * @return
	 */
		public List<ChannelFlow> selectAllByPage(Map<String, Object> map) {
			List<ChannelFlow> list = channelFlowMapper.selectAllByPage(map);
			if(list != null && list.size()>0){
				return changeList(list);
			}
			return list;
		}
	
	/**
	 * 根据流水相关信息查询流水表基本信息
	 * @param record
	 * @return
	 */
	public List<ChannelFlow> searchAll(ChannelFlow record){
		List<ChannelFlow> list = channelFlowMapper.searchAll(record);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	/**
	 * 根据流水相关信息查询两证一脸流水表基本信息
	 * @param record
	 * @return
	 */
	public List<ChannelFlow> searchCheckPersonExAll(ChannelFlow record){
		List<ChannelFlow> list = channelFlowCompareStrengthenMapper.searchCheckPersonExAll(record);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	
	
	/**
	 * 根据流水相关信息查询人脸活体流水表基本信息
	 * @param record
	 * @return
	 */
	public List<ChannelFlow> searchChannelFlowDetectLiveAll(ChannelFlow record){
		List<ChannelFlow> list = channelFlowDetectLiveMapper.searchChannelFlowDetectLiveAll(record);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	/**
	 * 
	* @Title: searchRegisterFlow 
	* @Description: 根据流水信息查询注册人脸流水基本信息（主表+人脸注册流水表）
	* @param record
	* @return      
	* @author:huyuxin
	 */
	public List<ChannelFlow> searchRegisterFlow(ChannelFlow record){
		List<ChannelFlow> list = channelFlowRegisterMapper.searchRegisterFlowAll(record);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	/**
	 * 
	* @Title: searchCertificateFeatureFlow 
	* @Description: 根据流水信息查询证脸对比流水基本信息（主表+证脸对比流水表）
	* @param record
	* @return      
	* @author:huyuxin
	 */
	public List<ChannelFlow> searchCertificateFeatureFlow(ChannelFlow record){
		List<ChannelFlow> list = channelFlowCertificateFeatureMapper.searchCertificateFeatureFlowAll(record);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	/**
	 * 
	* @Title: searchFaceCompareFlow 
	* @Description: 根据流水信息查询脸脸对比流水基本信息（主表+脸脸对比流水表）
	* @param record
	* @return      
	* @author:huyuxin
	 */
	public List<ChannelFlow> searchFaceCompareFlow(ChannelFlow record){
		List<ChannelFlow> list = channelFlowFaceCompareMapper.searchFaceCompareFlowAll(record);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	
	/**
	 * 根据流水ID查询流水表基本信息
	 * @param record
	 * @return
	 */
	public List<ChannelFlow> selectPFLowById(String flowId){
		List<ChannelFlow> list = channelFlowMapper.selectPersonFlowById(flowId);
		if(list != null && list.size()>0){
			return changeList(list);
		}
		return list;
	}
	/**
	 * 根据流水ID查询流水表查询两证一脸分表中的数据
	 * @param record
	 * @return
	 */
	public ChannelFlowCompareStrengthen selectChannelFlowCompareStrengthenById(String flowId){
		return  channelFlowCompareStrengthenMapper.selectChannelFlowCompareStrengthenById(flowId);
	}
	
	
	/**
	 * 根据流水ID查询流水表查询活体检测分表中的数据
	 * @param record
	 * @return
	 */
	public ChannelFlowDetectLive selectChannelFlowDetectLiveById(String flowId){
		return  channelFlowDetectLiveMapper.selectChannelFlowDetectLiveById(flowId);
	}
	/**
	 * 
	* @Title: selectChannelFlowRegisterById 
	* @Description: 根据流水ID查询注册流水表中的信息
	* @param flowId
	* @return      
	* @author:huyuxin
	 */
	public ChannelFlowRegister selectChannelFlowRegisterById(String flowId){
		
		return channelFlowRegisterMapper.selectRegisterFlowById(flowId);
	}
	/**
	 * 
	* @Title: selectChannelFlowCertificateFeatureById 
	* @Description:  根据流水ID查询证脸对比流水表中的信息
	* @param flowId
	* @return      
	* @author:huyuxin
	 */
	public ChannelFlowCertificateFeature selectChannelFlowCertificateFeatureById(String flowId){
		
		return channelFlowCertificateFeatureMapper.selectCertificateFeatureFlowById(flowId);
	}
	/**
	 * 
	* @Title: selectChannelFlowFaceToFaceById 
	* @Description: 根据流水ID查询脸脸对比流水表中的信息
	* @param flowId
	* @return      
	* @author:huyuxin
	 */
	public ChannelFlowFaceToFace selectChannelFlowFaceToFaceById(String flowId){
		
		return channelFlowFaceCompareMapper.selectFaceCompareFlowById(flowId);
	}
	/**
	 * 根据流水相关信息查询流水表基本信息
	 * @param record
	 * @return
	 */
	public List<ChannelFlow> selectFlows(ChannelFlow record){
		List<ChannelFlow> list = channelFlowMapper.selectFlows(record);
		return list;
	}
	
	/**
	 * 根据流水相关信息查询请求报文以及返回报文
	 * @param record
	 * @return
	 */
	public List<ChannelFlow> searchAllBLOB(ChannelFlow record){
		return channelFlowMapper.searchAllBLOB(record);
	}
	
	/**
	 * 新增流水信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	public int insertSelective(ChannelFlow record){
		int ret = channelFlowMapper.insertSelective(record);
		if(ret > 0) {
			//客户流水统计
			this.personRecogFlowService.savePersonRecogFlow(record);
		}
		return ret;
	}
	
	/**
	 * 根据featureId查询filePath
	 * @param record
	 * @return
	 */
	public String selectPathByFeatureId(PersonFeature record){
		return personFeatureService.selectPathByFeatureId(record);
	}
	
	public List<ChannelFlow> changeList(List<ChannelFlow> list){
		String channel = dicService.selectDicValuesByDicType(null);
		JSONArray json = JSON.parseArray(channel);
		json.remove(0);
		if(list != null){
			for(int j=0;j<json.size();j++){
				for(int i=0;i<list.size();i++){
					if(json.getJSONObject(j).getString("id").equals(list.get(i).getChannel())){//渠道
						list.get(i).setChannel(json.getJSONObject(j).getString("text"));
					}
					if(json.getJSONObject(j).getString("id").equals(list.get(i).getTradingCode())){//交易代码
						list.get(i).setTradingCode(json.getJSONObject(j).getString("text"));
					}
					if(json.getJSONObject(j).getString("id").equals(list.get(i).getCtftype())){//证件类型
						list.get(i).setCtftype(json.getJSONObject(j).getString("text"));
					}
					if(json.getJSONObject(j).getString("id").equals(list.get(i).getEngineType())){//算法引擎类型
						list.get(i).setEngineType(json.getJSONObject(j).getString("text"));
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * resultCount:(result结果统计). <br/>
	 */
	public List<FlowStatisic> resultCount(Map<String, Object> map){
	  return channelFlowMapper.resultCount(map);
	}
	
	/**
	 * 根据业务交易流水号查询已成功的交易流水信息
	 * @param record
	 * @return
	 */
	public List<ChannelFlow> selectTradingFlows(ChannelFlow record){
		return this.channelFlowMapper.selectTradingFlows(record);
	}
	
	/**
	 * 插入已认证成功的交易信息
	 * @param record
	 * @return
	 */
	public int insertTradingFlow(ChannelFlow record){
		return this.channelFlowMapper.insertTradingFlow(record);
	}
	
	/**
	 * 根据流水ID查询流水表查询身份证OCR分表中的数据
	 * @param flowId
	 * @return
	 */
	public ChannelFlowCard selectChannelFlowCardById(String flowId) {
		return channelFlowCardMapper.selectChannelFlowCardById(flowId);
	}

	/**
	 * 根据流水ID查询流水表查询银行卡OCR分表中的数据
	 * @param flowId
	 * @return
	 */
	public ChannelFlowBank selectChannelFlowBankById(String flowId) {
		return channelFlowBankMapper.selectChannelFlowBankById(flowId);
	}

	/**
	 * 根据流水ID查询流水表查询按证检索人分表中的数据
	 * @param flowId
	 * @return
	 */
	public ChannelFlowSearchPerson selectChannelFlowSearchPersonById(
			String flowId) {
		return channelFlowSearchPersonMapper.selectChannelFlowSearchPersonById(flowId);
	}
	
	/**
	 * 根据流水ID查询流水表查询按脸检索人分表中的数据
	 * @param flowId
	 * @return
	 */
	public ChannelFlowSearchImg selectChannelFlowSearchImgById(String flowId) {
		return channelFlowSearchImgMapper.selectChannelFlowSearchImgById(flowId);
	}

	public List<ChannelFlow> selectAllFlows(ChannelFlow record) {
		// TODO 自动生成的方法存根
		return channelFlowMapper.selectAllFlows(record);
	}
	
	/**
	 * 根据日期删除流水数据
	 * @param date
	 * @return
	 */
	public int deleteFlowsByDate(Date date) {
		
		//拼接删除条件
		Map<String,Object> paramMap = Maps.newHashMap();
		paramMap.put("createTime", date);
		
		//删除银行卡流水
		this.channelFlowBankMapper.deleterFlowsByDate(paramMap);
		//删除身份证流水
		this.channelFlowCardMapper.deleterFlowsByDate(paramMap);
		//删除证特征比对流水
		this.channelFlowCertificateFeatureMapper.deleterFlowsByDate(paramMap);
		//删除两证特征比对流水
		this.channelFlowCompareStrengthenMapper.deleterFlowsByDate(paramMap);
		//删除注册流水
		this.channelFlowRegisterMapper.deleterFlowsByDate(paramMap);
		//删除按脸检索流水
		this.channelFlowSearchImgMapper.deleterFlowsByDate(paramMap);
		//删除按证检索流水
		this.channelFlowSearchPersonMapper.deleterFlowsByDate(paramMap);
		//删除主表流水
		return this.channelFlowMapper.deleterFlowsByDate(paramMap);				
	}
	
}
