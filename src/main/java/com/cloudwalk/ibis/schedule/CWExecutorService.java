package com.cloudwalk.ibis.schedule;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.cloudwalk.common.common.ScheduleConfig;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.schedule.ScheduleEntity;
import com.cloudwalk.ibis.model.schedule.ScheduleRecordEntity;
import com.cloudwalk.ibis.service.schedule.ScheduleService;

/**
 * ClassName: ExecutorService<br/>
 * Description:定时任务执行器. <br/>
 * Date: 2017年08月12日 上午10:18:03 <br/>
 *
 * @author Chunjie He
 * @version 1.0.0
 * @since 1.7
 */
public class CWExecutorService {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 执行完成的结果消息
	 */
	public static String resultMsg = "";
	
	/**
	 * 执行任务数
	 */	
	public static CountDownLatch taskCount = null;
	
	
	/**
	 * 定时任务对象
	 */
	public static ThreadLocal<ScheduleRecordEntity> scheduleRecordEntity = new ThreadLocal<ScheduleRecordEntity>();
	
	/**
	 * 定时任务计划列表
	 */
	protected List<CWExecutor> taskList;	
	
	/**
	 * 定义定时任务编码
	 */
	private String code;	
	
	/**
	 * 定义定时任务名称
	 */
	private String name;		
	
	/**
	 * 心跳检测次数
	 */
	private int heartCheckCounts = 1;
	
	/**
	 * 心跳检测时间,单位s,默认为10s
	 */
	private int heartCheckTimes = 10;
	
	/**
	 * 心跳线程
	 */
	private HeartThread heartThread;
	
	@Resource(name = "scheduleService")
	private ScheduleService scheduleService;
	
	/**
	 * 初始化定时任务信息
	 */
	public void initTask(){
		ScheduleEntity se = new ScheduleEntity();
		se.setTaskCode(this.code);
		se.setTaskName(this.name);
		this.scheduleService.addSchedule(se);
	}
	
	/**
	 * 执行任务计划
	 */
	public void start() {
		
		//判断定时任务是开启
		if(!ScheduleConfig.TASK_ON.equals(ScheduleConfig.ON_OFF)) {
			logger.info("@@@ 计划任务未开启...");
			return;
		}
		
		//判断该任务组是否开启
		ScheduleEntity task = this.scheduleService.getScheduleInfo(this.code);
		if(task == null || task.getTaskStatus() != ScheduleEntity.TASK_STATUS_1) {
			logger.info("@@@ 计划任务<<"+this.name+">>未开启...");
			return;
		}
		
		//随机睡眠一段时间，防止多台服务器部署时，同时启动。
		this.sleep();
		
		//添加定时任务信息
		ScheduleRecordEntity se =this.scheduleService.addScheduleRecordInfo(this.getCode());
		if(se == null) {
			logger.info("该任务已有服务器正在执行，正在监听任务完成和超时");
			this.checkHeart();
			return;
		}
		//设置当前执行线程的定时任务信息
		scheduleRecordEntity.set(se);
		
		//初始化同步计数器
		this.init();
		
		//初始化心跳发送线程
		this.initHeartThread(se);
				
		logger.info("开始执行" + this.getCode() + "定时任务...");
		
		if (!CollectionUtils.isEmpty(this.getTaskList())) {
			ExecutorService exec = Executors.newCachedThreadPool(); 
			for (CWExecutor item : this.getTaskList()) {
				item.setCode(this.getCode());
				exec.execute(item);
			}
		}		
		
		//定时任务完成
		this.finish();
	}
	
	/**
	 * 设置定时任务编码
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 获取定时任务编码
	 * @return
	 */
	public String getCode() {
		return code;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置定时任务执行计划列表
	 * @param taskList
	 */
	public void setTaskList(List<CWExecutor> taskList) {
		this.taskList = taskList;
	}
	
	/**
	 * 获取定时任务执行计划列表
	 * @return
	 */
	public List<CWExecutor> getTaskList() {
		return taskList;
	}	
	
	
	public int getHeartCheckCounts() {
		return heartCheckCounts;
	}

	public void setHeartCheckCounts(int heartCheckCounts) {
		this.heartCheckCounts = heartCheckCounts;
	}

	public int getHeartCheckTimes() {
		return heartCheckTimes;
	}

	public void setHeartCheckTimes(int heartCheckTimes) {
		this.heartCheckTimes = heartCheckTimes;
	}

	public ScheduleService getScheduleService() {
		return scheduleService;
	}

	/**
	 * 每个具体任务执行完成后，调用该方法
	 */
	public static synchronized void updateTaskInfo(String msg) {	
		
		//更新执行结果信息
		if(!StringUtils.isBlank(resultMsg)) {
			resultMsg += ";";
		}
		resultMsg += msg;		
		
		//更新计数器
		taskCount.countDown();
	}
	
	/**
	 * 监听该定时任务是否全部完成
	 */
	public void finish() {
		
		//等待计数器为0时，执行后面的步骤
		try {
			taskCount.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		//通知心跳线程任务已完成
		this.heartThread.setTaskStatus(true);
		
		//更新定时任务信息
		ScheduleRecordEntity se = scheduleRecordEntity.get();		
		se.setRemark("服务器"+se.getServerIp()+"执行完成,具体结果信息如下："+resultMsg);
		int ret = this.scheduleService.finishSchedule(se);
		if(ret < 1) {
			//如果更新失败，延迟1s重试一次
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			} finally {
				this.scheduleService.finishSchedule(se);
			}
		}		
		
		logger.info("结束" + this.getCode() + "定时任务...");
		
	}
	
	/**
	 * 初始化计数器和结果消息
	 */
	public void init() {
		//初始化计数器和结果消息
		taskCount = new CountDownLatch(this.taskList.size());
		resultMsg = "";	
	}
	
	/**
	 * 当前定时任务执行线程随机睡眠一段时间
	 */
	public void sleep() {
		Random random = new Random();
		int randomTime = ObjectUtils.objToInt(ScheduleConfig.SLEEP_RANDOM_TIME,100);
		int sleepTime = random.nextInt(10)*randomTime;
		logger.info("定时任务睡眠时间："+sleepTime);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			logger.error("定时任务启动时随机睡眠发生异常",e);
			return;
		}
	}
	
	/**
	 * 初始化心跳发送线程
	 * @param se
	 */
	public void initHeartThread(ScheduleRecordEntity se) {
		this.heartThread = new HeartThread(this,se);
		Thread thread = new Thread(this.heartThread);
		thread.start();				
	}
	
	/**
	 * 检测心跳
	 */
	public void checkHeart() {
		
		//任务超时标识,true超时 
		boolean taskFlag = false;
		//任务对象
		ScheduleRecordEntity se = null;
		for(int i=0;i<this.getHeartCheckCounts();i++) {
			
			//睡眠心跳间隔时间，然后检测心跳
			try {
				Thread.sleep(this.heartCheckTimes*1000);
			} catch (InterruptedException e) {
			}
			
			//查询正在执行的定时任务的心跳时间
			se = this.scheduleService.selectCurSchedule(this.getCode());
			if(se == null) {
				//监听的任务已完成，直接返回
				return;
			}
			
			//判断心跳时间是否存在
			Date heartTime = se.getHeartTime();
			Date curDate = new Date();			
			if(heartTime == null || ((curDate.getTime()-heartTime.getTime())/1000)>this.heartCheckTimes) {
				taskFlag = true;
			} else {
				taskFlag = false;
			}
		}
		
		//如果任务连续heartCheckCounts次数都超时，则更新任务为超时，并尝试接替该任务重新执行
		if(taskFlag) {
			se.setTaskStatus(ScheduleRecordEntity.TASK_STATUS_3);
			se.setHeartTime(null);
			this.scheduleService.updateScheduleRecord(se);
			this.start();
			return;
		}
		
		//如果任务心跳存在，则一直监听
		this.checkHeart();		
	}
}
