package com.kafka.topo;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

import com.kafka.bolt.MySqlBolt;
import com.kafka.spout.ReadFileSpout;

public class KafkaTest {
    
     public static void main(String[] args) throws Exception {
    	List<String> zkServers = new ArrayList<String>();
      	zkServers.add("data25");
      	zkServers.add("data26");
      	zkServers.add("data27");
      	
 		TopologyBuilder builder = new TopologyBuilder();
 		builder.setSpout("readFile", new ReadFileSpout(), 1);
 		builder.setBolt("log2", new MySqlBolt(),5).shuffleGrouping("readFile");
 		
 		Config conf = new Config();
//        config.setMaxSpoutPending(5000);
       
//        config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 2000);
 		conf.put(Config.NIMBUS_HOST, "data05");
 		conf.put(Config.NIMBUS_THRIFT_PORT, 6627);
 		conf.put(Config.STORM_ZOOKEEPER_PORT, 2181);
 		conf.put(Config.STORM_ZOOKEEPER_SERVERS, zkServers);
 		conf.put(Config.NIMBUS_THRIFT_MAX_BUFFER_SIZE, 100000);
       
//        config.put(Config.TOPOLOGY_ACKER_EXECUTORS, 50);
 		conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 600);   
 		conf.setDebug(false);
 		
// 		StormSubmitter.submitTopology("storm-kafka", conf, builder.createTopology());
 		
 		LocalCluster cluster = new LocalCluster();
 		cluster.submitTopology("nginxLogTest", conf, builder.createTopology());
    	
    }
}
