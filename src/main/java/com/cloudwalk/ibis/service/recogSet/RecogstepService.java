package com.cloudwalk.ibis.service.recogSet;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.ibis.model.recogSet.Recogstep;
import com.cloudwalk.ibis.repository.recogSet.RecogstepMapper;
import com.cloudwalk.ibis.service.base.CacheService;



/**
 * 识别策略逻辑服务
 * @author 白乐
 *
 */
@Service("recogstepService")
public class RecogstepService {
	@Resource(name = "recogstepMapper")
	private RecogstepMapper recogstepMapper;
	
	@Resource(name = "cacheService")
	private CacheService cacheService;
	
	/**
	 * 保存识别策略
	 * @param record
	 * @throws Exception
	 */
	public int saveRecogstep(Recogstep record) throws Exception 
	{
		int ret = recogstepMapper.insert(record);
		if(ret > 0) {
			this.cacheService.cacheEngineVers();
			this.cacheService.cacheRecogSteps();
		}
		return ret;
	}
	
	/**
	 * 识别策略分页查询
	 * @param map
	 * @return
	 */
	public List<Recogstep>  selectRecogstepPage(Map<String, Object> map){
		return recogstepMapper.selectRecogstepPage(map);
	}
	
	/**
	 * 修改识别策略
	 * @param record
	 * @return
	 */
	public int updateRecogstep(Recogstep record) throws Exception{
		int ret = recogstepMapper.updateByPrimaryKey(record);
		if(ret > 0) {
			this.cacheService.cacheEngineVers();
			this.cacheService.cacheRecogSteps();
		}
		return ret;
	}
	
	/**
	 * 根据ID得到识别策略
	 * @param recogStepId
	 * @return
	 */
	public Recogstep selectRecogstepById(String recogStepId){
		return recogstepMapper.selectByPrimaryKey(recogStepId);
	}
	
	/**
	 * 删除识别策略
	 * @param recogStepId
	 * @return
	 * @throws Exception
	 */
	public int deleteRecogstepById(String recogStepId) throws Exception{
		int ret = recogstepMapper.deleteByPrimaryKey(recogStepId);
		if(ret > 0) {
			this.cacheService.cacheEngineVers();
			this.cacheService.cacheRecogSteps();
		}
		return ret;
	}
	
	/**
	 * 根据识别策略ID得到在识别规则中的数量
	 * @param recogStepId
	 * @return
	 */
	public int selectRecogStepHaveRule(String recogStepId){
		return recogstepMapper.selectRecogStepHaveRule(recogStepId);
	}
	
	/**
	 * 
	* @Title: selectRecogstepByRecogstep 
	* @Description: 根据
	* @param @param recogstep
	* @param @return    设定文件 
	* @return List<Recogstep>    返回类型 
	* @author huyuxin
	* @throws
	 */
	public List<Recogstep> selectRecogstepByRecogstep(Recogstep recogstep){
		
		return recogstepMapper.selectRecogstepByRecogstep(recogstep);
	}
}
