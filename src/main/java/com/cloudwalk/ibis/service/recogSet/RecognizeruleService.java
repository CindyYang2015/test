package com.cloudwalk.ibis.service.recogSet;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.Engine;
import com.cloudwalk.ibis.model.recogSet.Recognizerule;
import com.cloudwalk.ibis.model.recogSet.Recogstep;
import com.cloudwalk.ibis.repository.recogSet.EngineMapper;
import com.cloudwalk.ibis.repository.recogSet.RecognizeruleMapper;
import com.cloudwalk.ibis.repository.recogSet.RecogstepMapper;
/**
 * 渠道规则信息业务逻辑服务类
 * @author 白乐
 *
 */
@Service("recognizeruleService")
public class RecognizeruleService {
	
	@Resource(name = "recognizeruleMapper")
	private RecognizeruleMapper recognizeruleMapper;
	@Resource(name = "recogstepMapper")
	private RecogstepMapper recogstepMapper;
	@Resource(name = "engineMapper")
	private EngineMapper engineMapper;
	
	/**
	 * 根据识别策略ID和引擎代码获取识别规则信息
	 * @param rule
	 * @return
	 */
	public Recognizerule selectRecogsetpRule(Recognizerule rule) {
		List<Recognizerule> ruleList = this.recognizeruleMapper.selectRecogsetpRule(rule);
		if(!ObjectUtils.isEmpty(ruleList)) {
			return ruleList.get(0);
		}
		return null;
	}
	/**
	 * 
	* @Title: selectRecogsetpRuleList 
	* @Description:  根据识别策略ID和引擎代码获取识别规则信息
	* @param @param rule
	* @param @return    设定文件 
	* @return List<Recognizerule>    返回类型 
	* @author huyuxin
	* @throws
	 */
	public List<Recognizerule> selectRecogsetpRuleList(Recognizerule rule){
		return this.recognizeruleMapper.selectRecogsetpRule(rule);
	}
	/**
	 * 得到识别策略列表
	 * @param legalOrgCode
	 * @return
	 */
	public List<Recogstep> selectRecogsetpList(String legalOrgCode){
		return recogstepMapper.selectRecogstepAll(legalOrgCode);
	}
	
	/**
	 * 根据识别策略得到算法引擎
	 * @param recogStepId
	 * @return
	 */
	public List<Engine> selectEngineByRecogStepId(String recogStepId){
		return engineMapper.selectEngineByRecogStepId(recogStepId);
	}
	
	/**
	 * 保存更新
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public int saveRecognizerule(Recognizerule record) throws Exception{
		return recognizeruleMapper.insert(record);
	}
	
	/**
	 * 识别规则信息列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Recognizerule> selectRecognizerulePage(Map<String,Object> map){
		return recognizeruleMapper.selectRecognizerulePage(map);
	}
	
	/**
	 * 根据ID得到规则
	 * @param id
	 * @return
	 */
	public Recognizerule selectRecognizeruleById(String id){
		return recognizeruleMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 识别规则修改
	 * @param record
	 * @return
	 */
	public int updateRecognizerule(Recognizerule record) throws Exception{
		return recognizeruleMapper.updateByPrimaryKey(record);
	}
	
	/**
	 * 删除识别规则
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteRecognizerule(String id) throws Exception{
		return recognizeruleMapper.deleteByPrimaryKey(id);
	}
	
	
}
