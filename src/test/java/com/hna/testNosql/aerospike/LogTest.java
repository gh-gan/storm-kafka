package com.hna.testNosql.aerospike;

import java.util.Date;
import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Log;

public final class LogTest implements Log.Callback {
	public static void main(String[] args) {
		try {
			new LogTest().runTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void runTest() throws AerospikeException {
		Log.setLevel(Log.Level.INFO);
		Log.setCallback(this);
		AerospikeClient client = new AerospikeClient("172.16.0.15", 3000);
		try {
			// Perform database operations.
		} finally {
			client.close();
		}
	}

	//@Override
	public void log(Log.Level level, String message) {
		Date date = new Date();
		System.out.println(date.toString() + ' ' + level + ' ' + message);
	}
}
