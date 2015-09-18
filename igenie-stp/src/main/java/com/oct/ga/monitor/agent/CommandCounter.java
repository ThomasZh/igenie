package com.oct.ga.monitor.agent;

import java.util.Hashtable;

public class CommandCounter {

	private final Hashtable<Short, Long> commandMap = new Hashtable<Short, Long>();

	public Hashtable<Short, Long> getCommandMap() {
		return commandMap;
	}

	public void increase(Short cmdTag, int count) {
		Long sum = commandMap.get(cmdTag);
		if (sum == null)
			sum = 0L;
		commandMap.put(cmdTag, sum + count);
	}
}
