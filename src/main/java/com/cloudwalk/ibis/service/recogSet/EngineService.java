package com.cloudwalk.ibis.service.recogSet;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.ibis.model.recogSet.Engine;
import com.cloudwalk.ibis.repository.recogSet.EngineMapper;
import com.cloudwalk.ibis.service.base.CacheService;

/**
 * 算法引擎业务逻辑服务
 * @author 白乐
 *
 */
@Service("engineService")
public class EngineService {
	@Resource(name = "engineMapper")
	EngineMapper engineMapper;
	
	@Resource(name = "cacheService")
	private CacheService cacheService;
	
	public int saveEngine(Engine record) throws Exception {
	 	return engineMapper.insert(record);
	}
	/**
	 * 检查引擎代码是否重复
	 * @param engineCode
	 * @return
	 */
	public int checkEngineCode(String engineCode){
		return engineMapper.selectCountByEngineCode(engineCode);
	}
	
	/**
	 * 算法引擎列表分页查询
	 * @param map
	 * @return
	 */
	public List<Engine> selectEnginePage(Map<String, Object> map){
		return engineMapper.selectEnginePage(map);
	}
	
	public Engine selectEngineById(String id){
		return engineMapper.selectByPrimaryKey(id);
	}
	
	public Engine selectByEngineCode(String engineCode) {
		return engineMapper.selectByEngineCode(engineCode);
	}
	
	/**
	 * 修改算法引擎
	 * @param record
	 * @return
	 */
	public int updateEngine(Engine record) throws Exception{
		int ret = engineMapper.updateByPrimaryKey(record);
		if(ret > 0) {
			this.cacheService.cacheEngineVers();
			this.cacheService.cacheRecogSteps();
		}
		return ret;
	}
	/**
	 * 删除算法引擎
	 * @param id
	 * @return
	 */
	public int deleteEngine(String id) throws Exception{
		return engineMapper.deleteByPrimaryKey(id);
	}
	/**
	 * 查询策略中是否存在此引擎
	 * @param engineId
	 * @return
	 */
	public int selectEngineHaveStep(String engineId){
		return engineMapper.selectEngineHaveStep(engineId);
	}
	
	
}
