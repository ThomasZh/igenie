package com.oct.ga.monitor.agent;

public class PkgRecvCounter {
	
	
	private long sum = 0;

	public long getCount() {
		return sum;
	}

	public void increase(int count) {
		this.sum += count;
	}
}
