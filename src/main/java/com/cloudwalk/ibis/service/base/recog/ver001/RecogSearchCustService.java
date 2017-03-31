package com.cloudwalk.ibis.service.base.recog.ver001;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlowSearchPerson;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.model.result.ver001.SearchData;
import com.cloudwalk.ibis.repository.queryStatisic.ChannelFlowSearchPersonMapper;
import com.cloudwalk.ibis.service.base.engine.SearchFeature;
import com.cloudwalk.ibis.service.base.recog.RecogServiceImpl;
import com.google.common.collect.Lists;

/**
 * 人脸识别按照客户信息检索相似客户
 * @author zhuyf
 *
 */
public class RecogSearchCustService extends RecogServiceImpl {
	
	@Resource(name = "channelFlowSearchPersonMapper")
	private ChannelFlowSearchPersonMapper channelFlowSearchPersonMapper;
	
	@Override
	public void execute(TransferObj transferObj) throws ServiceException {
		//请求对象
		RecogRequest recogRequest = transferObj.getRecogRequest();
		//参数验证
		this.checkParam(recogRequest);
		//初始化算法引擎服务
		this.initEngineService(transferObj);
		//验证黑名单信息
		this.checkBlackPerson(transferObj);
		//获取客户信息
		this.getPerson(transferObj);
		if(transferObj.getP() == null || transferObj.getP().getPf() == null) {
			//客户不存在，返回检索为空
			throw new ServiceException("客户不存在，检索失败");
		}
		//根据客户特征检索
		PersonFeature pf = transferObj.getP().getPf();
		List<SearchFeature> sfList = transferObj.getEngineService().searchPersonByFeature(Base64.encodeBase64String(pf.getFeature()), pf.getFeatureType(), Constants.Config.FACE_GROUP_ID, recogRequest.getNumber());
		if(ObjectUtils.isEmpty(sfList)) {
			throw new ServiceException("检索结果为空");
		}
		//获取客户信息
		List<PersonFeature> pfList = Lists.newArrayList();
		for(SearchFeature sf:sfList) {			
			PersonFeature temppf = this.personService.selectPersonByFeatureId(sf.getFeatureId());
			if(temppf == null) continue;
			temppf.setScore(sf.getScore());
			pfList.add(temppf);
		}
		if(pfList.isEmpty()) {
			throw new ServiceException("未找到匹配的客户信息");
		}
		SearchData data = new SearchData(pfList);
		
		//设置返回结果
		this.setRetResult(transferObj);		
		transferObj.getResult().setData(data);
		this.setTradingResult(transferObj);
		
		
	}
	
	@Override
	public boolean checkParam(RecogRequest recogRequest)
			throws ServiceException {
		if(super.checkParam(recogRequest)) {
			if(StringUtils.isBlank(recogRequest.getCtfno())
					|| StringUtils.isBlank(recogRequest.getCtfname())
					|| StringUtils.isBlank(recogRequest.getCtftype())
					) {
					throw new ServiceException("参数不完整");
				}	
			
		}
		return true;
	}

	@Override
	public void saveRecogFlowInfo(TransferObj transferObj) {			
		super.saveRecogFlowInfo(transferObj);
		this.saveChannelFlowSearchPerson(transferObj);
	}

	/**
	 * 保存按照客户信息检索流水分表信息
	 * @param transferObj
	 */
	private void saveChannelFlowSearchPerson(TransferObj transferObj) {
		
		//组装按证检索流水对象
		ChannelFlow flow=transferObj.getFlow();
		ChannelFlowSearchPerson channelFlowSearchPerson = new ChannelFlowSearchPerson();
		channelFlowSearchPerson.setFlowId(flow.getFlowId());
		channelFlowSearchPerson.setFeatureId(flow.getFeatureId());
		channelFlowSearchPerson.setPersonId(flow.getPersonId());
		channelFlowSearchPerson.setRemark(flow.getRemark());
		
		this.channelFlowSearchPersonMapper.insertSelective(channelFlowSearchPerson);
	}

	@Override
	public void setRetResult(TransferObj transferObj) throws ServiceException {
		Result result = Result.initSuccessResult();		
		transferObj.setResult(result);
		super.setRetResult(transferObj);
	}
	
}
