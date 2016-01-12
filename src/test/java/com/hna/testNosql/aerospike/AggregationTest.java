package com.hna.testNosql.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Language;
import com.aerospike.client.Value;
import com.aerospike.client.lua.LuaConfig;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.query.ResultSet;
import com.aerospike.client.query.Statement;
import com.aerospike.client.task.IndexTask;
import com.aerospike.client.task.RegisterTask;
import com.aerospike.client.util.Util;

public final class AggregationTest {
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
			String indexName = "aggindex";
			String binName = "aggbin";
			// Initialize aggregation function and create secondary index.
			System.out.println("Register UDF");
			RegisterTask rt = client.register(null, "udf/sum_example.lua",
					"sum_example.lua", Language.LUA);
			rt.waitTillComplete();
			System.out.println("Create Index");
			IndexTask it = client.createIndex(null, namespace, setName,
					indexName, binName, IndexType.NUMERIC);
			it.waitTillComplete();
			// Write 10 records.
			for (int i = 1; i <= 10; i++) {
				Key key = new Key(namespace, setName, "key" + i);
				Bin bin = new Bin(binName, i);
				client.put(null, key, bin);
			}
			// Query on secondary index.
			System.out.println("Run Query");
			Statement stmt = new Statement();
			stmt.setNamespace(namespace);
			stmt.setSetName(setName);
			stmt.setBinNames(binName);
			stmt.setFilters(Filter.range(binName, 4, 7));

			ResultSet rs = client.queryAggregate(null, stmt, "sum_example",
					"sum_single_bin", Value.get(binName));
			try {
				if (rs.next()) {
					Object sum = rs.getObject();
					System.out.println("Sum = " + sum);
				}
			} finally {
				rs.close();
			}
		} finally {
			client.close();

		}
	}
}
