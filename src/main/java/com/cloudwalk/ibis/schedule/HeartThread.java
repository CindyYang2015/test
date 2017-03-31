package com.cloudwalk.ibis.schedule;

import java.util.Date;

import com.cloudwalk.ibis.model.schedule.ScheduleRecordEntity;
import com.cloudwalk.ibis.service.schedule.ScheduleService;

/**
 * 心跳线程，用于检测定时任务中途挂掉
 * @author zhuyf
 *
 */
public class HeartThread implements Runnable {
	
	/**
	 * 定时任务服务
	 */
	private CWExecutorService executorService;
	
	/**
	 * 任务状态，true表示任务已执行完成，false任务执行中
	 */
	private boolean taskStatus = false;	
	
	/**
	 * 定时任务信息
	 */
	private ScheduleRecordEntity se;
	
	/**
	 * 心跳间隔时间，单位为：s
	 */
	private int heartCheckTimes;

	public boolean isTaskStatus() {
		return taskStatus;
	}
	
	public void setTaskStatus(boolean taskStatus) {
		this.taskStatus = taskStatus;
	}

	public CWExecutorService getExecutorService() {
		return executorService;
	}

	/**
	 * 构造方法
	 */
	public HeartThread(CWExecutorService executorService,ScheduleRecordEntity se) {
		this.executorService = executorService;
		this.heartCheckTimes = executorService.getHeartCheckTimes();
		this.se = se;
	}

	@Override
	public void run() {

		//根据任务配置的检测心跳时间发生任务心跳
		while(true) {
			
			//任务执行完成后，退出心跳发送
			if(this.taskStatus) {
				break;
			}
			
			//睡眠心跳间隔时间，然后发送心跳
			try {
				Thread.sleep(this.heartCheckTimes*1000);
			} catch (InterruptedException e) {
			}
			
			//发送心跳
			se.setHeartTime(new Date());
			se.setTaskStatus(null);
			ScheduleService scheduleService = this.getExecutorService().getScheduleService();
			scheduleService.updateScheduleRecord(se);
		}
	}

}
