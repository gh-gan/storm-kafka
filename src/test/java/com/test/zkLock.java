package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import com.netflix.curator.retry.ExponentialBackoffRetry;

public class zkLock {
	public static void main(String [] args){
		
		// 多线程执行
		ExecutorService threedPool = Executors.newCachedThreadPool();
		for(int i=0 ;i<100;i++){
			final int index = i;
			threedPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
//						handleLockData(index);   
//						handleLockData2(index);
						handleLockData3(index);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	// 输出功能
	public static void out(int index){
		System.out.print("--------index = "+index+"------");
		for(int i=0;i<10;i++){
			System.out.print("---"+i);
		}
		System.out.println();
	}
	
	// 不带锁 输出是乱序的
	public static void handleLockData(int index){
		out(index);
	}
	
	// 带锁
	public static synchronized void handleLockData2(int index){
		out(index);
	}
	
	
	// 下面使用 zookeeper 的分布式锁   测试
	public static void handleLockData3(int index)throws Exception{
		InterProcessMutex lock = new InterProcessMutex(getCuratorFramework(), "/zklocks/ggh"); // /zklocks/ggh 为zk上锁使用的路径
		// lock.acquire(1, TimeUnit.MINUTES)  获取锁，返回是否获取成功。   参数设置表示:超时1s内没返回结果表示失败
		while (lock.acquire(5, TimeUnit.MINUTES) )  // 直到获取到锁为止，while 是为了拿到锁才往下执行
		{
		    try {
		    	out(index); // 业务代码
		    }
		    finally
		    {
		        lock.release();  // 释放锁
		        break;  		 // 跳出循环
		    }
		}
	}
	
	// 使用 Curator 完成 zookeeper 的分布式锁
	private static CuratorFramework client = null;
	private static String zkList = "data25:2181,data26:2181,data27:2181";
	public synchronized  static CuratorFramework getCuratorFramework(){
		if(client==null){
			try {
				// 重试策略
				RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);  // 最多重试3次
				// CuratorFramework 是线程安全的，在一个应用中共享一个zk集群的
				client = CuratorFrameworkFactory.newClient(zkList, retryPolicy);
				client.start();  // 在应用结束的时候调用 close() 方法
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}
}
