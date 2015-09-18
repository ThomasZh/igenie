package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class SyncTaskTimestampResp
		extends RespCommand
{
	public SyncTaskTimestampResp()
	{
		this.setTag(Command.SYNC_TASK_TIMESTAMP_RESP);
	}

	public SyncTaskTimestampResp(String taskId, int timestamp)
	{
		this();

		this.setTaskId(taskId);
		this.setTimestamp(timestamp);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tTaskId = new TlvObject(1, this.getTaskId());
		TlvObject tTimestamp = new TlvObject(2, 4, TlvByteUtil.int2Byte(this.getTimestamp()));

		// 6 + taskId + 6 + 4
		int pkgLen = 16 + tTaskId.getLength();
		logger.info("from command to tlv package:(tag=" + Command.SYNC_TASK_TIMESTAMP_RESP + ", child=2, length="
				+ pkgLen + ")");
		TlvObject tlv = new TlvObject(Command.SYNC_TASK_TIMESTAMP_RESP, pkgLen);
		tlv.add(tTaskId);
		tlv.add(tTimestamp);
		return tlv;
	}

	private String taskId;
	private int timestamp;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskTimestampResp.class);

}
