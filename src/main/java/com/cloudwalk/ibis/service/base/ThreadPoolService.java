package com.cloudwalk.ibis.service.base;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

/**
 * 线程池服务
 * @author zhuyf
 *
 */
@Service("threadPoolService")
public class ThreadPoolService {

	//线程池
	private ThreadPoolExecutor threadPoolExecutor;
	
	/**
	 * 从数据库获取静态数据，缓存
	 */
	@PostConstruct
	public void init() {		
		//初始化线程池
		threadPoolExecutor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
	}
	
	@PreDestroy
	public void destory() {
		threadPoolExecutor.shutdown();
	}
	
	/**
	 * 执行线程
	 * @param task
	 */
	public void addTask(Runnable task) {
		if(threadPoolExecutor != null) {
			threadPoolExecutor.execute(task);
		}
	}

	public ThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}

	public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;
	}
	
	
	
		
}
