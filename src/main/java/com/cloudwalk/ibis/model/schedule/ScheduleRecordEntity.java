package com.cloudwalk.ibis.model.schedule;

import java.io.Serializable;
import java.util.Date;

import com.cloudwalk.common.util.ObjectUtils;

/**
 * 定时任务执行记录实体对象
 * @author zhuyf
 *
 */
public class ScheduleRecordEntity implements Serializable{      

	/**
	 * 
	 */
	private static final long serialVersionUID = -1691511863814345001L;

	public ScheduleRecordEntity() {
    	String uuid = ObjectUtils.createUUID();
    	this.id = uuid;
    }
	
	/**
	 * 进行中
	 */
	public static int TASK_STATUS_1 = 1;
	/**
	 * 已完成
	 */
	public static int TASK_STATUS_2 = 2;
	/**
	 * 超时
	 */
	public static int TASK_STATUS_3 = 3;
    
    /**
     * ID
     * id 主键
     */
    private String id;
    
    /**
     * 任务编码
     */
    private String taskCode;
    
    /**
	 * SERVER_IP
	 * 服务器ip
	 */
	private String serverIp;
    /**
     * TASK_STATUS
     * 任务状态 1进行中 2完成 3超时
     */
	private Integer taskStatus;
	/**
     * REMARK
     * 备注
     */
	private String remark;    
	
	/**
	 * 心跳时间
	 */
	private Date heartTime;
	
	/**
     * CREATE_TIME
     * 创建日期
     */
	private Date createTime;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public Integer getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getHeartTime() {
		return heartTime;
	}
	public void setHeartTime(Date heartTime) {
		this.heartTime = heartTime;
	}
	
	
}
