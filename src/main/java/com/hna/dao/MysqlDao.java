package com.hna.dao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hna.domain.RequestLog;
import com.kafka.util.LockUtil;
import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import com.netflix.curator.retry.ExponentialBackoffRetry;

public class MysqlDao {
	private static Logger log = LoggerFactory.getLogger(MysqlDao.class);
	private static Connection con = null;
	private String url;
	private String userName;
	private String userPasswd;
	public MysqlDao(){
		init();
	}
	public void init(){
		InputStream in = MysqlDao.class.getResourceAsStream("/conf.properties");
		Properties prop = new Properties();
		try{
			prop.load(in);
			url = prop.getProperty("jdbcUrl");
			userName = prop.getProperty("userName");
			userPasswd = prop.getProperty("userPasswd");
		}catch(Exception e){
			log.error("conf file load error !!!");
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, userName, userPasswd);
		} catch (Exception e) {
			log.error("mysql connection error!!!");
			e.printStackTrace();
		}
	}
	public boolean save(List<RequestLog> list){
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO request_log (website,time_local,remote_addr,remote_port,request,request_body,response_time,request_time,status,body_bytes_sent,http_referer,http_user_agent,headers,jsession,http_cookie,resp_body)");
		sql.append("VALUES");
		for(int i=0;i<list.size();i++){
			if(i>0){
				sql.append(",");
			}
			sql.append("(");
			RequestLog rl = list.get(i);
			sql.append("\""+rl.getWebsite()+"\"");
			sql.append(",\""+rl.getTime_local()+"\"");
			sql.append(",\""+rl.getRemote_addr()+"\"");
			sql.append(",\""+rl.getRemote_port()+"\"");
			sql.append(",\""+rl.getRequest()+"\"");
			sql.append(",\""+rl.getRequest_body()+"\"");
			sql.append(",\""+rl.getResponse_time()+"\"");
			sql.append(",\""+rl.getRequest_time()+"\"");
			sql.append(",\""+rl.getStatus()+"\"");
			sql.append(",\""+rl.getBody_bytes_sent()+"\"");
			sql.append(",\""+rl.getHttp_referer()+"\"");
			sql.append(",\""+rl.getHttp_user_agent()+"\"");
			sql.append(",\""+rl.getHeaders()+"\"");
			sql.append(",\""+rl.getJsession()+"\"");
			sql.append(",\""+rl.getHttp_cookie()+"\"");
			sql.append(",\""+rl.getResp_body()+"\"");
			sql.append(")");
		}
		
		return lockSave(sql.toString());
	}
	
	public boolean saveToMysql(String sql){
		try {
//			con.setAutoCommit(false);
			PreparedStatement prepareStatement = con.prepareStatement(sql);
			prepareStatement.executeUpdate();
//			con.commit();
		} catch (SQLException e) {
			/*try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}*/
			e.printStackTrace();
		}
		return true;
	}
	public boolean lockSave(String sql){
		InterProcessMutex lock = new InterProcessMutex(LockUtil.getCuratorFramework(), LockUtil.ZK_LOCK_MYSQL); 
		boolean save  = true;
		try{
			while (lock.acquire(5, TimeUnit.MINUTES) )  
			{
			    try {
			    	save = saveToMysql(sql);
			    }
			    finally
			    {
			        lock.release();
			        break;  		 
			    }
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return save;
	}
	
}
