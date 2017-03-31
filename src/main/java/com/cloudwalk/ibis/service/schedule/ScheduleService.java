package com.cloudwalk.ibis.service.schedule;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.schedule.ScheduleEntity;
import com.cloudwalk.ibis.model.schedule.ScheduleRecordEntity;
import com.cloudwalk.ibis.repository.schedule.ScheduleMapper;
import com.google.common.collect.Maps;

/**
 * 定时任务服务类
 * @author zhuyf
 *
 */
@Service("scheduleService")
public class ScheduleService {
	
	protected final Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "scheduleMapper")
	private ScheduleMapper scheduleMapper;
	
	/**
	 * 添加定时任务信息
	 * @taskCode 定时任务编码
	 * @return 
	 */
	public ScheduleRecordEntity addScheduleRecordInfo(String taskCode) {
		
		ScheduleRecordEntity se = null;
		
		//查询当天是否存在正在执行的任务
		Map<String,Object> paramMap = Maps.newHashMap();		
		paramMap.put("taskCode", taskCode);
		
		List<ScheduleRecordEntity> scheduleRecordList = this.scheduleMapper.selectScheduleRecordByDate(paramMap);
		if(!ObjectUtils.isEmpty(scheduleRecordList)) {
			//存在执行的任务，直接返回
			return scheduleRecordList.get(0);
		}
		
		//插入待执行的定时任务信息
		String serverIp = ObjectUtils.getServerIp();		
		se = new ScheduleRecordEntity();
		se.setTaskCode(taskCode);
		se.setServerIp(serverIp);
		se.setRemark("服务器"+serverIp+"正在执行");
		se.setHeartTime(new Date());
		se.setCreateTime(new Date());
		se.setTaskStatus(ScheduleEntity.TASK_STATUS_1);
		int ret = this.scheduleMapper.addScheduleRecord(se);
		if(ret <= 0) {
			se = null;
		}
		
		return se;
	}
	
	/**
	 * 查询当前正在执行的任务信息
	 * @param taskCode 任务编码
	 * @return
	 */
	public ScheduleRecordEntity selectCurSchedule(String taskCode) {		
		
		//查询当天是否存在正在执行的任务
		Map<String,Object> paramMap = Maps.newHashMap();		
		paramMap.put("taskCode", taskCode);
		
		List<ScheduleRecordEntity> scheduleRecordList = this.scheduleMapper.selectScheduleRecordByDate(paramMap);
		if(!ObjectUtils.isEmpty(scheduleRecordList)) {
			//存在执行的任务，直接返回
			return scheduleRecordList.get(0);
		}
		
		return null;
	}
	
	/**
	 * 通知定时任务已完成
	 * @param se
	 */
	public int finishSchedule(ScheduleRecordEntity se) {
		if(se == null) return 0;
		return this.scheduleMapper.finishSchedule(se);
	}
	
	/**
	 * 更新定时任务信息
	 * @param se
	 */
	public int updateScheduleRecord(ScheduleRecordEntity se) {
		if(se == null) return 0;
		return this.scheduleMapper.updateScheduleRecord(se);
	}
	
	/**
	 * 添加定时任务
	 * @param se
	 * @return
	 */
	public int addSchedule(ScheduleEntity se) {
		
		//返回状态
		int retStatus = 0;
		if(se == null) return retStatus;
		try {
			List<ScheduleEntity> seList = this.scheduleMapper.selectScheduleByCode(se.getTaskCode());
			if(ObjectUtils.isEmpty(seList)) {
				retStatus = this.scheduleMapper.addSchedule(se);
			} else {
				retStatus = this.scheduleMapper.updateScheduleStatus(se);
			}
		} catch(Exception e) {
			retStatus = 0;
		}
		
		return retStatus;
	}
	
	/**
	 * 查询定时任务信息
	 * @param taskCode 任务编码
	 * @return
	 */
	public ScheduleEntity getScheduleInfo(String taskCode) {
		if(StringUtils.isBlank(taskCode)) return null;
		List<ScheduleEntity> scheduleEntitys = this.scheduleMapper.selectScheduleByCode(taskCode);
		if(!ObjectUtils.isEmpty(scheduleEntitys)) {
			return scheduleEntitys.get(0);
		}
		return null;
	}
	
	/**
	 * 查询定时任务列表
	 * @param se
	 * @return
	 */
	public List<ScheduleEntity> selectScheduleList(Map<String, Object> map) {
		return this.scheduleMapper.selectSchedulePage(map);
	}
	
	/**
	 * 查询定时任务记录列表
	 * @param se
	 * @return
	 */
	public List<ScheduleRecordEntity> selectScheduleRecordList(Map<String, Object> map) {
		return this.scheduleMapper.selectScheduleRecordPage(map);
	}
	
	/**
	 * 更新任务状态
	 * @param se
	 * @return
	 */
	public int updateScheduleStatus(ScheduleEntity se){
		return this.scheduleMapper.updateScheduleStatus(se);
	}
	
}
