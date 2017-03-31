package com.cloudwalk.ibis.socket;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 循环自动扩展队列
 */
public final class CycleQueue<T> {
    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<T>();
    
    public CycleQueue() {
        this(20, 5);
    }
    
    public CycleQueue(int initSize) {
    	this(initSize, 5);
    }
    
    public CycleQueue(int initSize, int ExpSize) {
    }
    
    /**
     * 添加数据对象到队列尾部
     * @param obj 数据对象
     */
    public boolean add(T obj) {
    	return queue.add(obj);    	
    }
    
    /**
     * 获取并删除队列头的数据对象
     * @return 返回对队头的数据对象，如果没有，则为null
     */
    public T get() {
    	return queue.poll();
    }
    
    /**
     * 检索，但是不移除此队列的头，如果此队列为空，则返回 null。
     */
    public T peek() {
    	return queue.peek();
    }
    
    /**
     * 获取对队中当前包括数据对象的个数
     * @return
     */
    public int count()  {
    	return queue.size();
    }
    
    public ConcurrentLinkedQueue<T> getQueue() {
    	return this.queue;
    }
}