package com.hna.testNosql.aerospike;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.async.AsyncClient;
import com.aerospike.client.listener.RecordListener;
import com.aerospike.client.listener.WriteListener;
import com.aerospike.client.policy.WritePolicy;

public final class AsyncTest {
	public static void main(String[] args) {
		try {
			AsyncTest test = new AsyncTest();
			test.runTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AsyncClient client;
	private WritePolicy policy;
	private boolean completed;

	public AsyncTest() {
		policy = new WritePolicy();
		policy.timeout = 50; // 50 millisecond timeout.
	}

	public void runTest() throws AerospikeException {
		client = new AsyncClient("172.16.0.15", 3000);
		try {
			// Write a single value.
			Key key = new Key("test", "myset", "mykey");
			Bin bin = new Bin("mybin", "myvalue");
			System.out.println(String.format(
					"Write: namespace=%s set=%s key=%s value=%s",
					key.namespace, key.setName, key.userKey, bin.value));
			client.put(policy, new WriteHandler(), key, bin);
			waitTillComplete();
		} finally {
			client.close();
		}
	}

	private class WriteHandler implements WriteListener {
		//@Override
		public void onSuccess(Key key) {
			try {
				// Write succeeded. Now call read.
				client.get(policy, new ReadHandler(), key);
			} catch (Exception e) {
				System.out
						.println(String
								.format("Failed to get: namespace=%s set=%s key=%s exception=%s",
										key.namespace, key.setName,
										key.userKey, e.getMessage()));
			}
		}

		//@Override
		public void onFailure(AerospikeException e) {
			e.printStackTrace();
			notifyCompleted();
		}
	}

	private class ReadHandler implements RecordListener {
		//@Override
		public void onSuccess(Key key, Record record) {
			// Read completed.
			Object received = (record == null) ? null : record
					.getValue("mybin");
			System.out.println(String.format(
					"Received: namespace=%s set=%s key=%s value=%s",
					key.namespace, key.setName, key.userKey, received));
			// Notify application that read is complete.
			notifyCompleted();
		}

		//@Override
		public void onFailure(AerospikeException e) {
			e.printStackTrace();
			notifyCompleted();
		}
	}

	private synchronized void waitTillComplete() {
		while (!completed) {
			try {
				super.wait();
			} catch (InterruptedException ie) {
			}
		}
	}

	private synchronized void notifyCompleted() {
		completed = true;
		super.notify();
	}
}
