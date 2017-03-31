package com.cloudwalk.ibis.service.recogSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.StringUtil;
import com.cloudwalk.ibis.model.recogSet.Engine;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.recogSet.Step;
import com.cloudwalk.ibis.model.recogSet.StepEngineRef;
import com.cloudwalk.ibis.model.recogSet.vo.StepVo;
import com.cloudwalk.ibis.repository.recogSet.EngineMapper;
import com.cloudwalk.ibis.repository.recogSet.EngineVerMapper;
import com.cloudwalk.ibis.repository.recogSet.StepEngineRefMapper;
import com.cloudwalk.ibis.repository.recogSet.StepMapper;
import com.cloudwalk.ibis.service.base.CacheService;

/**
 * 策略逻辑服务
 * @author 白乐
 *
 */
@Service("stepService")
public class StepService {
	@Resource(name = "stepMapper")
	private StepMapper stepMapper;
	
	@Resource(name = "stepEngineRefMapper")
	private StepEngineRefMapper stepEngineRefMapper;
	
	@Resource(name = "engineMapper")
	private EngineMapper engineMapper;
	
	@Resource(name = "engineVerMapper")
	private EngineVerMapper engineVerMapper;
	
	@Resource(name = "cacheService")
	private CacheService cacheService;
	
	/**
	 * 策略列表分页查询
	 * @param map 必须包含用户的机构代码（法人机构）全路径
	 * @return
	 */
	public List<StepVo> selectStepPage(Map<String, Object> map){
		List<Step> stepList = stepMapper.selectStepPage(map);
		List<StepVo> resultList = new ArrayList<StepVo>();
		for(int i=0;i<stepList.size();i++){
			Step step = stepList.get(i);
			List<StepEngineRef> sfrList = stepEngineRefMapper.selectStepEngineByStepId(step.getId());
			StepVo  sv = new StepVo();
			StringBuffer engNameStrs = new StringBuffer();
			for(int n=0;n<sfrList.size();n++){
				Engine engine = engineMapper.selectByPrimaryKey(sfrList.get(n).getEngineId());
				EngineVer engineVer = engineVerMapper.selectByPrimaryKey(sfrList.get(n).getEngineverId());
				String engineName = "";
				String engineVerNo = "";
				if(engine != null) engineName = engine.getEngineName();
				if(engineVer != null) engineVerNo = engineVer.getVerNo();
				if(engNameStrs.length() > 0) {
					engNameStrs.append(",");
				}
				engNameStrs.append(engineName).append("(").append(engineVerNo).append(")");
				
			}
			sv.setId(step.getId());
			sv.setStepName(step.getStepName());
			sv.setEngineNames(engNameStrs.toString());
			sv.setEngineNum(sfrList.size()+"");
			sv.setRemark(step.getRemark());
			resultList.add(sv);
		}
		return resultList;
	}
	
	/**
	 * 保存策略
	 * @param step  策略实体类
	 * @param idarr 策略与引擎关联数据
	 * @param saveflag 0:保存 1:修改
	 * @return
	 * @throws Exception
	 */
	public int saveStep(Step step,String[] idarr,int saveflag) throws Exception{
		int returnNum = 0;
		if(saveflag==0){
			returnNum = stepMapper.insert(step);
		}else if(saveflag==1){
			this.checkStep(idarr, step.getId());			
			returnNum =  stepMapper.updateByPrimaryKey(step);
			stepEngineRefMapper.deleteByStepId(step.getId());
		}
		if(returnNum>0){
			for(int i=0;i<idarr.length;i++){
				String[] engarr = idarr[i].split("\\_");
				if(engarr.length==2){					
					StepEngineRef stepEngineRef = new StepEngineRef();
					stepEngineRef.setStepId(step.getId());
					stepEngineRef.setEngineId(engarr[0]);
					stepEngineRef.setEngineverId(engarr[1]);
					stepEngineRefMapper.insert(stepEngineRef);
				}
			}
		}	
		//更新时刷新缓存
		if(saveflag==1){
			this.cacheService.cacheEngineVers();
			this.cacheService.cacheRecogSteps();
		}
		return returnNum;
	}
	
	/**
	 * 判断策略组中不能存在一种算法引擎对应不同的版本
	 * @param engines 引擎ID和版本ID的数组
	 * @param stepId 策略ID
	 * @throws ServiceException 
	 */
	private void checkStep(String[] engines,String stepId) throws ServiceException {
		if(engines == null || engines.length < 1 || StringUtil.isBlank(stepId)) return;
		//查询当前策略ID对应策略组的其他策略对应的引擎信息
		List<EngineVer> evList = this.stepMapper.selectOtherEnginesById(stepId);
		if(!ObjectUtils.isEmpty(evList)) {			
			for(String engine:engines) {
				String[] engarr = engine.split("\\_");
				if(engarr.length==2){
					for(EngineVer ev:evList) {
						if(engarr[0].equals(ev.getEngineId()) && !engarr[0].equals(ev.getId())) {
							//当前策略对应的策略组已存在该算法引擎的其他版本，不能修改
							throw new ServiceException("该策略对应的策略组已存在‘"+ev.getEngineName()+"-"+ev.getVerNo()+"’版本");
						}
					}
				}
				
			}			
		}
		
	}
	/**
	 * 得到更新策略的显示数据
	 * @param stepid
	 * @return
	 */
	public StepVo selectUpdateStep(String stepid){
		Step step = stepMapper.selectByPrimaryKey(stepid);
		StepVo vo  = new StepVo();
		List<StepEngineRef> serList = stepEngineRefMapper.selectStepEngineByStepId(step.getId());
		String engIds = "";
		for(int i=0;i<serList.size();i++){
			StepEngineRef ser = serList.get(i);
			if(i==0){
				engIds = engIds + ser.getEngineId()+"_"+ser.getEngineverId();
			}else{
				engIds = engIds + "|"+ser.getEngineId()+"_"+ser.getEngineverId();
			}
		}
		vo.setId(stepid);
		vo.setStepName(step.getStepName());
		vo.setEngineIds(engIds);
		vo.setRemark(step.getRemark());
		return vo;
	}
	
	
	/**
	 * 删除策略
	 * @param stepid
	 * @return
	 * @throws Exception
	 */
	public int	deleteStep(String stepid)  throws Exception {
		int ret = this.stepMapper.deleteByPrimaryKey(stepid);
		if(ret > 0) {
			this.stepEngineRefMapper.deleteByStepId(stepid);
		}	
		return ret;
	}
	
	
	
	
	
	/**
	 * 根据策略查询在策略中的数量
	 * @param stepId
	 * @return
	 */
	public int selectStepHaveGroup(String stepId){
		return stepMapper.selectStepHaveGroup(stepId);
	}
	
	/**
	 * 
	* @Title: selectStepVoByName 
	* @Description: 根据策略的名字进行查询
	* @param @param name
	* @param @return    设定文件 
	* @return Step    返回类型 
	* @author huyuxin
	* @throws
	 */
	public List<Step> selectStepByName(String name){
		return stepMapper.selectStepByName(name);
	}
	
	
}
