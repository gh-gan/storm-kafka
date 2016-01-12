package com.kafka.spout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class ReadFileSpout extends BaseRichSpout{

	private SpoutOutputCollector spoutCollector;
	private BufferedReader buff = null;
	private String line = null;
	public static double num = 0;
	
	@Override
	public void nextTuple() {
		if(buff==null){
			return;
		}
		
		if(num>1000){
			return;
		}
		
		try {
			line=buff.readLine();
			if(line==null){
				System.exit(0);
			}
			num++;
			this.spoutCollector.emit(new Values(line,num));
			
			Thread.sleep(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}

	@Override
	public void open(Map map, TopologyContext arg1, SpoutOutputCollector spoutCollector) {
		this.spoutCollector = spoutCollector;
		try {
			buff = new BufferedReader(new FileReader(new File("H:/access_nginx.log")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("nginx-log","num"));
	}
	
}
