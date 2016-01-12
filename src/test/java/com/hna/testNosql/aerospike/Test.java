package com.hna.testNosql.aerospike;

import java.util.Map;
import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;

public final class Test {
	public static void main(String[] args) {
		try {
			runTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void runTest() throws AerospikeException {
		AerospikeClient client = new AerospikeClient("172.16.0.15", 3000);
		try {
			// Initialize policy.
			WritePolicy policy = new WritePolicy();
			policy.timeout = 50; // 50 millisecond timeout.
			// Write a single value.
			Key key = new Key("test", "myset", "mykey");
			Bin bin = new Bin("mybin", "myvalue");
			client.put(policy, key, bin);
			// Write multiple values.
			Bin bin1 = new Bin("name", "John");
			Bin bin2 = new Bin("age", 25);
			client.put(policy, key, bin1, bin2);
			// Read a single value.
			Record record = client.get(policy, key, "name");
			if (record != null) {
				System.out.println("Got name: " + record.getValue("name"));
			}
			// Read multiple values.
			record = client.get(policy, key);
			if (record != null) {
				System.out.println("Found record: Expiration="
						+ record.expiration + " Generation="
						+ record.generation);
				for (Map.Entry<String, Object> entry : record.bins.entrySet()) {
					System.out.println("Name=" + entry.getKey() + " Value="
							+ entry.getValue());
				}
			}
			// Delete record.
			client.delete(policy, key);
		} finally {
			client.close();

		}
	}
}
