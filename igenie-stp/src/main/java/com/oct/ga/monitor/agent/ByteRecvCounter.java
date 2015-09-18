package com.oct.ga.monitor.agent;

public class ByteRecvCounter {

	
	private long sum = 0;

	public long getCount() {
		return sum;
	}

	public void increase(int count) {
		this.sum += count;
	}
}
