package com.cloudwalk.ibis.service.recogSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cloudwalk.ibis.model.recogSet.Step;
import com.cloudwalk.ibis.model.recogSet.StepGroup;
import com.cloudwalk.ibis.model.recogSet.StepgroupRef;
import com.cloudwalk.ibis.repository.recogSet.EngineMapper;
import com.cloudwalk.ibis.repository.recogSet.StepGroupMapper;
import com.cloudwalk.ibis.repository.recogSet.StepMapper;
import com.cloudwalk.ibis.repository.recogSet.StepgroupRefMapper;
import com.cloudwalk.ibis.service.base.CacheService;

/**
 * 策略组服务
 * @author 白乐
 *
 */
@Service("stepGroupService")
public class StepGroupService {
	
	@Resource(name = "engineMapper")
	private EngineMapper engineMapper;
	@Resource(name = "stepMapper")
	private StepMapper stepMapper;
	@Resource(name = "stepGroupMapper")
	private StepGroupMapper stepGroupMapper;
	@Resource(name = "stepgroupRefMapper")
	private StepgroupRefMapper stepgroupRefMapper;
	
	@Resource(name = "cacheService")
	private CacheService cacheService;
	
	/**
	 * 根据机构代码得到策略列表
	 * @param legalOrgCode
	 * @return
	 */
	public List<Step> selectStepList(String legalOrgCode){
		return stepMapper.selectStepsByLegal(legalOrgCode);
	}
	
	/**
	 * 根据机构代码得到策略组列表
	 * @param legalOrgCode
	 * @return
	 */
	public List<StepGroup> selectStepGroupList(String legalOrgCode){
		return stepGroupMapper.selectStepGroupByLegalOrgCode(legalOrgCode);
	}
	
	/**
	 * 保存策略组
	 * @param record
	 * @param saveFlag 0:新建 1:修改
	 * @throws Exception
	 */
	public void saveStepGroup(StepGroup record,int saveFlag) throws Exception {
		String stepids = record.getSteps();
		int sgnum = 0;
		if(saveFlag == 0) {//新增
			sgnum = stepGroupMapper.insert(record);
		} else if(saveFlag == 1) {//修改
			//删除策略组和关联数据之后再添加
			stepgroupRefMapper.deleteStepGroupByGroupId(record.getId());
			sgnum =  stepGroupMapper.updateByPrimaryKey(record);
		}
		
		if(sgnum > 0 && !StringUtils.isBlank(stepids)){
			String[] idarr = stepids.split(",");
			for(int i=0;i<idarr.length;i++){
				StepgroupRef sr = new StepgroupRef(); 
				sr.setStepgroupId(record.getId());
				sr.setStepId(idarr[i]);
				stepgroupRefMapper.insert(sr);
			}
		}
		
		//更新时刷新缓存
		if(saveFlag == 1) {
			this.cacheService.cacheEngineVers();
			this.cacheService.cacheRecogSteps();
		}
		
	}
	
	/**
	 * 检测算法引擎是否重复
	 * @param stepids 逗号隔开的多个策略ID
	 * @return 重复引擎的名称以,隔开，如果没有重复的则返回空字符串
	 */
	public String selectEngineRepeat(String stepids){
		String[] idarr = stepids.split(",");
		List<String> list = new ArrayList<String>();
		for(int i=0;i<idarr.length;i++){
			list.add(idarr[i]); 
		}
		List<String> engList = engineMapper.selectEngineRepeatByStepIds(list);
		String returnStr = "";
		for(int n=0;n<engList.size();n++){
			if(n==0){
				returnStr = returnStr + engList.get(n);
			}else{
				returnStr = returnStr +","+engList.get(n);
			}
		}
		return returnStr;
	}
	
	/**
	 * 查询是否可以删除本策略组，如果被识别策略应用则不能删除
	 * @param groupid
	 * @return
	 */
	public boolean selectIsDelete(String groupid){
		int i = stepGroupMapper.selectGroupHaveRecogstep(groupid);
		if(i>0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 删除策略组包括关联的数据
	 * @param groupid
	 * @return
	 * @throws Exception
	 */
	public int deleteStepGroup(String groupid) throws Exception {
		int ret = stepGroupMapper.deleteByPrimaryKey(groupid);
		if(ret > 0) {
			this.stepgroupRefMapper.deleteStepGroupByGroupId(groupid);
		}
		return ret;
	}
	
	
	/**
	 * 策略组分页查询
	 * @param map
	 * @return
	 */
	public List<StepGroup> selectStepGroupPage(Map<String, Object> map){
		List<StepGroup> resultList = new ArrayList<StepGroup>();
		List<StepGroup> grouplist = stepGroupMapper.selectStepGroupPage(map);
		for(int i=0;i<grouplist.size();i++){
			StepGroup group = grouplist.get(i);
			List<StepgroupRef> sgrList = stepgroupRefMapper.selectStepByGroupId(group.getId());
			StepGroup  sg = new StepGroup();
			String stepNameStrs = "";
			for(int n=0;n<sgrList.size();n++){
				String stepName =  stepMapper.selectByPrimaryKey(sgrList.get(n).getStepId()).getStepName();
				if(n==0){
					stepNameStrs = stepNameStrs + stepName;
				}else{
					stepNameStrs = stepNameStrs +","+stepName;
				}
			}
			sg.setStepgroupName(group.getStepgroupName());
			sg.setSteps(stepNameStrs);
			sg.setRemark(group.getRemark());
			sg.setId(group.getId());
			resultList.add(sg);
		}
		return resultList;		
	}
	
	/**
	 * 根据策略组id查询策略组包括关联的策略信息
	 * @param groupId
	 * @return
	 */
	public StepGroup selectStepGroupById(String groupId){
		StepGroup stepGroup = stepGroupMapper.selectByPrimaryKey(groupId);
		List<StepgroupRef> sgrList =  stepgroupRefMapper.selectStepByGroupId(groupId);
		String stepIdStrs = "";
		for(int n=0;n<sgrList.size();n++){
			if(n==0){
				stepIdStrs = stepIdStrs + sgrList.get(n).getStepId();
			}else{
				stepIdStrs = stepIdStrs +","+sgrList.get(n).getStepId();
			}
		}
		stepGroup.setStepsTemp(stepIdStrs);
		return stepGroup;
	}
	
	
	
	
	
}
