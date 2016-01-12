package com.hna.testNosql.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Language;
import com.aerospike.client.Value;
import com.aerospike.client.lua.LuaConfig;
import com.aerospike.client.task.RegisterTask;

public final class UDFTest {
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
			RegisterTask task = client.register(null, "record_example.lua",
					"record_example.lua", Language.LUA);
			task.waitTillComplete();
			Key key = new Key("test", "myset", "my-udf-key");
			Bin bin = new Bin("mybin", "string value");
			client.put(null, key, bin);
			String received = (String) client.execute(null, key,
					"record_example", "readBin", Value.get(bin.name));
			System.out.println("Received: " + received);
		} finally {
			client.close();
		}
	}
}