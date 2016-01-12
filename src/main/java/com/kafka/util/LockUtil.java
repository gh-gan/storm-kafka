package com.kafka.util;

import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;

public class LockUtil {
	public static String ZK_LOCK_MYSQL = "/zklocks/ggh/mysql";
	public static String ZK_LOCK_REDIS = "/zklocks/ggh/redis";
	// 分布式锁
	public static CuratorFramework client = null;
	private static String zkList = "data25:2181,data26:2181,data27:2181";
	public synchronized  static CuratorFramework getCuratorFramework(){
		if(client==null){
			try {
				RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);  
				client = CuratorFrameworkFactory.newClient(zkList, retryPolicy);
				client.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}
}
