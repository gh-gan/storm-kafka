package com.hna.testNosql.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.Value;
import com.aerospike.client.command.Buffer;
import com.aerospike.client.lua.LuaConfig;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.aerospike.client.task.IndexTask;
import com.aerospike.client.util.Util;

public final class QueryTest {
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
			String namespace = "test";
			String setName = "demoset";
			String indexName = "queryindex";
			String binName = "querybin";
			System.out.println("Create index");
			IndexTask task = client.createIndex(null, namespace, setName,
					indexName, binName, IndexType.NUMERIC);
			task.waitTillComplete();
			// Write 10 records.
			System.out.println("Write records");
			for (int i = 1; i <= 10; i++) {
				Key key = new Key(namespace, setName, "key" + i);
				Bin bin = new Bin(binName, i);
				client.put(null, key, bin);
			}
			// Query on secondary index.
			Statement stmt = new Statement();
			stmt.setNamespace(namespace);
			stmt.setSetName(setName);
			stmt.setBinNames(binName);
			stmt.setFilters(Filter.range(binName, 4, 7));
			RecordSet rs = client.query(null, stmt);
			try {
				while (rs.next()) {
					Key key = rs.getKey();
					Record record = rs.getRecord();
					Object result = record.getValue(binName);
					System.out.println("Record: ns=" + key.namespace + " set="
							+ key.setName + " bin=" + binName + " digest="
							+ Buffer.bytesToHexString(key.digest) + " value="
							+ result);
				}
			} finally {
				rs.close();
			}
		} finally {
			client.close();
		}
	}
}
