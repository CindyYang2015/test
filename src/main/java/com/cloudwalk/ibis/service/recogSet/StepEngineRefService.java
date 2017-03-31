package com.cloudwalk.ibis.service.recogSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloudwalk.ibis.model.recogSet.StepEngineRef;
import com.cloudwalk.ibis.repository.recogSet.StepEngineRefMapper;

@Service("stepEngineRefService")
public class StepEngineRefService {
	@Resource(name = "stepEngineRefMapper")
	private StepEngineRefMapper stepEngineRefMapper;
	
	public void save(StepEngineRef record) throws Exception{
		stepEngineRefMapper.insert(record);
	}
	
}
