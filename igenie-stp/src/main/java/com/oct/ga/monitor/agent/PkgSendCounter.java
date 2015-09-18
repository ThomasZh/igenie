package com.oct.ga.monitor.agent;

public class PkgSendCounter {

	/**
	 * 自启动以来，发送的TLV数据包数
	 */
	private long sum = 0;

	public long getCount() {
		return sum;
	}

	public void increase(int count) {
		this.sum += count;
	}
}
