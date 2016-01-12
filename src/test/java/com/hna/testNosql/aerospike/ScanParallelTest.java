package com.hna.testNosql.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.ScanCallback;
import com.aerospike.client.policy.Priority;
import com.aerospike.client.policy.ScanPolicy;

public final class ScanParallelTest implements ScanCallback {
	public static void main(String[] args) {
		try {
			new ScanParallelTest().runTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int recordCount;

	public void runTest() throws AerospikeException {
		AerospikeClient client = new AerospikeClient("172.16.0.15", 3000);
		try {
			ScanPolicy policy = new ScanPolicy();
			policy.concurrentNodes = true;
			policy.priority = Priority.LOW;
			policy.includeBinData = false;
			client.scanAll(policy, "test", "demoset", this);
			System.out.println("Records " + recordCount);
		} finally {
			client.close();
		}
	}

	public void scanCallback(Key key, Record record) {
		recordCount++;
		if ((recordCount % 10000) == 0) {
			System.out.println("Records " + recordCount);
		}
	}
}
