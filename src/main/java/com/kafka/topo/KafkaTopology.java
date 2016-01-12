package com.kafka.topo;

import java.util.ArrayList;
import java.util.List;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import com.kafka.bolt.MySqlBolt;
import com.kafka.spout.ReadFileSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

public class KafkaTopology {
	public static void main(String args[]) throws Exception{
		
		List<String> zkServers = new ArrayList<String>();
     	zkServers.add("data25");
     	zkServers.add("data26");
     	zkServers.add("data27");
    	BrokerHosts brokerHosts = new ZkHosts("data25:2181,data26:2181,data27:2181");

    	String topic = "test"; 
    	SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, topic, "/ghkafka", "task"); 
    	spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());  // 字符串
    	
    	//KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
//    	kafkaConfig.zkServers = zkServers;    // 这里可不设置，为空时会从storm中 Config 获取
//    	kafkaConfig.zkPort = 2181;
    	spoutConfig.socketTimeoutMs = 600 * 1000;
    	spoutConfig.forceFromStart = false;   //true从头消费
    	spoutConfig.zkPort = 2181;
    	
    	
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kafka", new KafkaSpout(spoutConfig), 2);
		//builder.setSpout("readFile", new ReadFileSpout(), 1);
		builder.setBolt("mysql", new MySqlBolt(),5).shuffleGrouping("kafka");
		
		Config conf = new Config();
		
		conf.setNumWorkers(5);
		
//       config.setMaxSpoutPending(5000);
      
//       config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 2000);
		conf.put(Config.NIMBUS_HOST, "data05");
		conf.put(Config.NIMBUS_THRIFT_PORT, 6627);
		conf.put(Config.STORM_ZOOKEEPER_PORT, 2181);
		conf.put(Config.STORM_ZOOKEEPER_SERVERS, zkServers);
		conf.put(Config.NIMBUS_THRIFT_MAX_BUFFER_SIZE, 100000);
      
//       config.put(Config.TOPOLOGY_ACKER_EXECUTORS, 50);
		conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 600);   
		conf.setDebug(false);
		
		StormSubmitter.submitTopology("storm-kafka", conf, builder.createTopology());
		
//		LocalCluster cluster = new LocalCluster();
//		cluster.submitTopology("nginxLogTest", conf, builder.createTopology());
		
	}
}
