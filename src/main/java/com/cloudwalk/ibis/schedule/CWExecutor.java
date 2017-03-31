package com.cloudwalk.ibis.schedule;


import org.apache.log4j.Logger;

import com.cloudwalk.common.exception.ServiceException;

/**
 * ClassName: CWExecutor<br/>
 * Description:定时任务执行器. <br/>
 * 该定时器被实现时，不能异步启动其他线程，直接返回，只能当前任务的所有执行线程完成后才返回. <br/>
 * Date: 2017年08月12日 上午10:18:03 <br/>
 *
 * @author Chunjie He
 * @version 1.0.0
 * @since 1.7
 */
public abstract class CWExecutor implements Runnable {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 定时任务执行方法，此方法需要被覆盖	 * 
	 * @throws ServiceException
	 */
	public abstract void execute() throws Exception;
	
	@Override
	public void run() {	
		
		logger.info("@@@ " + this.getName() + " @@@ 计划任务开始执行...");
		//任务执行结果消息
		String resultMsg = "";
		
		try {
			this.execute();
			resultMsg = this.name+"任务执行成功";
			logger.info("@@@ " + this.getName() + " @@@ 计划任务执行成功");
		} catch (Exception e) {
			resultMsg = this.name+"任务执行发生异常，详情查看对应服务器的日志信息";
			logger.error("执行***" + this.getName() + "***任务计划失败***",e);
		} finally {
			//更新完成状态
			CWExecutorService.updateTaskInfo(resultMsg);
		}
		
	}
	
	/**
	 * 计划任务类型
	 */
	protected String code;
	
	/**
	 * 定时任务名称
	 */
	protected String name;
	
	/**
	 * 获取计划任务类型
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置计划任务类型
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取定时任务名称
	 * @return
	 * @throws ServiceException
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置定时计划名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
