package com.cloudwalk.ibis.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * 线程池服务
 * @author zhuyf
 *
 */
public class ThreadpoolService {
	
	private static final Logger logger = Logger.getLogger(ThreadpoolService.class);

	//线程池对象
	ExecutorService executorService = null;	
	
	/**
	 * 初始化线程池对象
	 */
	public void init() {
		try{
//			int threadCount = Runtime.getRuntime().availableProcessors();
//			logger.info("socket线程池初始化"+threadCount+"个线程");
			executorService = Executors.newCachedThreadPool();
		} catch(Exception e) {
			logger.info("socket线程池初始化失败,异常："+e);
		}
	}
	
	/**
	 * 执行操作
	 * @param runnable
	 */
	public void execute(Runnable command) {
		if(command != null && executorService != null) {
			this.executorService.execute(command);
		} else {
			logger.info("command为NULL");
		}
	}
	
	/**
	 * 关闭线程池对象
	 */
	public void close() {
		logger.info("线程池关闭中。。。");
		if(executorService != null) {
			executorService.shutdown();
			executorService = null;
		}
	}
}
