package com.redoct.ga.sup.client.socket.pool2;

import java.util.concurrent.ConcurrentHashMap;

import com.redoct.ga.sup.SupRespCommand;

public class RespCmdQueue
{
	private ConcurrentHashMap<Long, SupRespCommand> map = new ConcurrentHashMap<Long, SupRespCommand>();

	public void put(SupRespCommand respCmd)
	{
		map.put(respCmd.getSequence(), respCmd);
	}

	public SupRespCommand pop(long sequence)
	{
		SupRespCommand respCmd = map.get(sequence);
		if (respCmd != null) {
			map.remove(sequence);
		}

		return respCmd;
	}
}
