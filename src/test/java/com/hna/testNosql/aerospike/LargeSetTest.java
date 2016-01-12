package com.hna.testNosql.aerospike;

import java.util.Map;
import java.util.Map.Entry;
import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Key;
import com.aerospike.client.Value;
import com.aerospike.client.large.LargeSet;
import com.aerospike.client.lua.LuaConfig;
import com.aerospike.client.policy.WritePolicy;

public final class LargeSetTest {
	public static void main(String[] args) {
		try {
			runTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void runTest() throws AerospikeException {
		LuaConfig.SourceDirectory = "udf";
		AerospikeClient client = new AerospikeClient("172.16.0.15", 3000);
		try {
			WritePolicy policy = new WritePolicy();
			Key key = new Key("test", "demoset", "setkey");
			String binName = "setbin";
			// Delete record if it already exists.
			client.delete(policy, key);
			// Initialize large set operator.
			LargeSet set = client.getLargeSet(policy, key, binName, null);
			// Write values.
			set.add(Value.get("setvalue1"));
			set.add(Value.get("setvalue2"));
			// Verify large set was created with default configuration.
			Map<?, ?> map = set.getConfig();
			for (Entry<?, ?> entry : map.entrySet()) {
				System.out.println(entry.getKey().toString() + ','
						+ entry.getValue());
			}
			System.out.println("Size: " + set.size());
			Object received = set.get(Value.get("setvalue2"));
			System.out.println("Found: " + received);
		} finally {
			client.close();
		}
	}
}
