package com.kafka.spout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class RodSpout extends BaseRichSpout{

	private SpoutOutputCollector spoutCollector;
	private String [] line = {"hello ggh1","hello ggh2","hello ggh3","hello ggh4","hello ggh5","hello ggh6"};
	private Random ran = new Random();
	
	@Override
	public void nextTuple() {
		int i = ran.nextInt(line.length);
		this.spoutCollector.emit(new Values(line[i]));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void open(Map map, TopologyContext arg1, SpoutOutputCollector spoutCollector) {
		this.spoutCollector = spoutCollector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("nginx-log"));
	}
	
}
