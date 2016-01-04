package com.base.wwmm.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 请求工具类
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class RequestExecutor {

	/** 请求线程池队列，同时允许5个线程操作 */
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);

	/**
	 * 往线程池添加网络请求事务，带数据库缓存
	 */
	public static void addTask(RequestDBTask task) {
		executorService.submit(task);
	}

	/**
	 * 往线程池添加网络请求事务
	 */
	public static void addTask(BaseTask task) {
		executorService.submit(task);
	}

	/**
	 * 往线程池添加线程
	 */
	public static void addTask(Runnable task) {
		executorService.submit(task);
	}

	/**
	 * 关闭线程池
	 */
	public static void shutdown() {
		executorService.shutdown();
	}

}
