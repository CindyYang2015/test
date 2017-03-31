package com.cloudwalk.ibis.service.queryStatisic;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.queryStatisic.PersonRecogFlow;
import com.cloudwalk.ibis.model.recogSet.vo.BlackPersonVo;
import com.cloudwalk.ibis.model.recogSet.vo.WhitePersonCheckVo;
import com.cloudwalk.ibis.repository.queryStatisic.PersonRecogFlowMapper;
import com.cloudwalk.ibis.service.base.CacheService;
import com.cloudwalk.ibis.service.recogSet.BlackPersonService;
import com.cloudwalk.ibis.service.recogSet.WhitePersonService;
import com.cloudwalk.ibis.service.system.DicService;


@Service("personRecogFlowService")
public class PersonRecogFlowService {
	@Resource(name = "personRecogFlowMapper")
	private PersonRecogFlowMapper personRecogFlowMapper;
	
	@Resource(name="dicService")
	private DicService dicService;
	
	@Resource(name = "blackPersonService")
	private BlackPersonService blackPersonService;
	
	@Resource(name = "whitePersonService")
	private WhitePersonService whitePersonService;
	
	@Resource(name = "cacheService")
	private CacheService cacheService;
	
	/**
	 * 分页查询流水表基本信息
	 * @param map
	 * @return
	 */
	public List<PersonRecogFlow> selectAllByPage(Map<String, Object> map) {
		List<PersonRecogFlow> list = personRecogFlowMapper.selectAllByPage(map);
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
	public List<PersonRecogFlow> searchAll(PersonRecogFlow record){
		List<PersonRecogFlow> list = personRecogFlowMapper.searchAll(record);
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
	public List<PersonRecogFlow> searchAllByPrimaryKey(PersonRecogFlow record){
		return personRecogFlowMapper.searchAllByPrimaryKey(record);
	}
	
	/**
	 * 新增流水信息
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	public int insertSelective(PersonRecogFlow record){
		return personRecogFlowMapper.insertSelective(record);
	}
	/**
	 * createBlackPerson:(创建黑名单). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 28, 2016 1:15:40 PM
	 * @param request
	 * @param blackPersonVo
	 * @return
	 * @since JDK 1.7
	 */
	public int createBlackPerson(HttpServletRequest request,BlackPersonVo blackPersonVo){
		return blackPersonService.createBlackPerson(blackPersonVo);
	}
	/**
	 * createWhitePerson:(创建白名单). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 *  Date: Sep 28, 2016 1:15:40 PM
	 * @param request
	 * @param whitePersonVo
	 * @return
	 * @since JDK 1.7
	 */
	public int createWhitePersonCheck(HttpServletRequest request,WhitePersonCheckVo whitePersonVo){
		return whitePersonService.createWhitePersonCheck(whitePersonVo);
	}
	
	public List<PersonRecogFlow> changeList(List<PersonRecogFlow> list){
		//从数据字典设置类型名称
		if(!ObjectUtils.isEmpty(list)){
			for(PersonRecogFlow prf:list){
				prf.setCtftypeName(this.cacheService.getDicNameByCode(prf.getCtftype()));
				prf.setEngineTypeName(this.cacheService.getDicNameByCode(prf.getEngineType()));
				prf.setChannelName(this.cacheService.getDicNameByCode(prf.getChannel()));
				prf.setTradingCodeName(this.cacheService.getDicNameByCode(prf.getTradingCode()));
			}
		}		
		return list;
	}
	
	/**
	 * 根据流水信息统计客户识别认证信息
	 * @param flow
	 */
	public void savePersonRecogFlow(ChannelFlow flow) {
		if(flow == null || StringUtils.isBlank(flow.getPersonId())) return;
		//根据客户ID，算法引擎代码，引擎版本，渠道，接口代码，接口版本，交易代码获取客户识别流水信息
		PersonRecogFlow personRecogFlow = new PersonRecogFlow();
		personRecogFlow.setPersonId(flow.getPersonId());
		personRecogFlow.setEngineCode(flow.getEngineCode());
		personRecogFlow.setEngineVerCode(flow.getEngineVerCode());
		personRecogFlow.setChannel(flow.getChannel());
		personRecogFlow.setBusCode(flow.getBusCode());
		personRecogFlow.setInterVerCode(flow.getInterVerCode());
		personRecogFlow.setTradingCode(flow.getTradingCode());
		List<PersonRecogFlow> personRecogFlows = this.personRecogFlowMapper.selectPersonRecogFlows(personRecogFlow);
		if(ObjectUtils.isEmpty(personRecogFlows) || personRecogFlows.get(0) == null) {
			//新增客户流水统计信息
			Date curDate = new Date();
			personRecogFlow.setId(ObjectUtils.createUUID());
			personRecogFlow.setLegalOrgCode(flow.getLegalOrgCode());
			personRecogFlow.setCtfno(flow.getCtfno());
			personRecogFlow.setCtfname(flow.getCtfname());
			personRecogFlow.setCtftype(flow.getCtftype());
			personRecogFlow.setCustomerId(flow.getCustomerId());
			personRecogFlow.setCreateTime(curDate);
			if(EnumClass.FlowResultEnum.SUCESS.getValue() == flow.getResult()) {
				personRecogFlow.setSucessCount(1);
				personRecogFlow.setFailCount(0);
			} else {
				personRecogFlow.setSucessCount(0);
				personRecogFlow.setFailCount(1);
			}
			this.personRecogFlowMapper.insertSelective(personRecogFlow);
			
		} else {
			//更新统计信息
			PersonRecogFlow updatePersonRecogFlow = personRecogFlows.get(0);
			if(EnumClass.FlowResultEnum.SUCESS.getValue() == flow.getResult()) {
				updatePersonRecogFlow.setSucessCount(updatePersonRecogFlow.getSucessCount()+1);
			} else {
				updatePersonRecogFlow.setFailCount(updatePersonRecogFlow.getFailCount()+1);
			}
			this.personRecogFlowMapper.updatePersonRecogFlowBykey(updatePersonRecogFlow);
		}
		
	}
}
