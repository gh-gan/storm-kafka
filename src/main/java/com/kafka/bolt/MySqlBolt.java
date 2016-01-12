package com.kafka.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.hna.dao.MysqlDao;
import com.hna.domain.RequestLog;
import com.kafka.util.LockUtil;

public class MySqlBolt extends BaseBasicBolt{

	private static ObjectMapper mapper  = new ObjectMapper();
	private static Logger log = LoggerFactory.getLogger(MySqlBolt.class);
	private static MysqlDao mysqlDao = null;
	private static boolean flg = true;
	private static CopyOnWriteArrayList<RequestLog> cache1 = new CopyOnWriteArrayList<RequestLog>(); // flg=true
	private static CopyOnWriteArrayList<RequestLog> cache2 = new CopyOnWriteArrayList<RequestLog>(); // flg=false
	public static List<RequestLog> sqlError = new ArrayList<RequestLog>();
	private static List<String> parserError = new ArrayList<String>();
	
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		mysqlDao = new MysqlDao();
		
		ScheduledExecutorService newSes = Executors.newScheduledThreadPool(1);
		newSes.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				cacheTomysql();
			}
		}, 5, 5, TimeUnit.SECONDS);
		
	}
	
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String line = input.getString(0);
//		log.info(line);
		RequestLog requestLog = null;
		try {
			requestLog = mapper.readValue(line, RequestLog.class);
		} catch (Exception e) {
			//e.printStackTrace();
//			parserError.add(line);
			log.info("parserError-->json error !!! "+line);
			return;
		}
		
		if(flg){
			cache1.add(requestLog);
		}else{
			cache2.add(requestLog);
		}

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

	public void cacheTomysql(){
		List<RequestLog> list = new ArrayList<RequestLog>();
		
		synchronized (MySqlBolt.class) {
			if(flg){
				flg = false;
				list.addAll(cache1);
				cache1.clear();
			}else{
				flg = true;
				list.addAll(cache2);
				cache2.clear();
			}
		}
		
		if(list==null || list.size()==0){
			return;
		}
		
		mysqlDao.save(list);
	}

	@Override
	public void cleanup() {
		LockUtil.client.close();
		
		log.error("---------------------------------");
		for(String line : parserError){
			log.error(line);
		}
		log.error("---------------------------------");
	}
	
}
