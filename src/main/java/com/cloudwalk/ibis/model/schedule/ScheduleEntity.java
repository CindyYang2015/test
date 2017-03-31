package com.cloudwalk.ibis.model.schedule;

import java.io.Serializable;

/**
 * 定时任务实体对象
 * @author zhuyf
 *
 */
public class ScheduleEntity implements Serializable{    
    
	private static final long serialVersionUID = 1551504509769034045L;
	
	/**
	 * 开启
	 */
	public static int TASK_STATUS_1 = 1;
	/**
	 * 关闭
	 */
	public static int TASK_STATUS_0 = 0;
	    
    /**
     * 任务编码
     */
    private String taskCode;
    
    /**
     * 任务名称
     */
    private String taskName;    
    
    /**
     * TASK_STATUS
     * 任务状态 1开启 0关闭
     */
	private Integer taskStatus;

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}
	
}
