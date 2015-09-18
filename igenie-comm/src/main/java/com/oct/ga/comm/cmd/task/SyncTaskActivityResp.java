package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class SyncTaskActivityResp
		extends RespCommand
{
	public SyncTaskActivityResp()
	{
		super();
		this.setTag(Command.SYNC_TASK_ACTIVITY_RESP);
	}

	public SyncTaskActivityResp(NotifyTaskLog activity)
	{
		this();
		this.setTaskActivity(activity);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tActivityId = new TlvObject(1, activity.get_id());
		TlvObject tTaskId = new TlvObject(2, activity.getChannelId());// taskId
		TlvObject tTaskName = new TlvObject(3, activity.getChannelName());// taskName
		TlvObject tFromAccountId = new TlvObject(4, activity.getFromAccountId());
		TlvObject tFromAccountName = new TlvObject(5, activity.getFromAccountName());
		TlvObject tToAccountId = new TlvObject(6, activity.getToAccountId());
		TlvObject tToAccountName = new TlvObject(7, activity.getToAccountName());
		TlvObject tTimestamp = new TlvObject(8, 4, TlvByteUtil.int2Byte(activity.getTimestamp()));
		TlvObject tState = new TlvObject(9, 2, TlvByteUtil.short2Byte(activity.getActivityState()));
		TlvObject tCommandTag = new TlvObject(10, 2, TlvByteUtil.short2Byte(activity.getCommandTag()));
		String pid = activity.getTaskPid();
		if (pid == null || pid.length() == 0)
			pid = "";
		TlvObject tTaskPid = new TlvObject(11, pid);
		TlvObject tSyncState = new TlvObject(12, 2, TlvByteUtil.short2Byte(activity.getSyncState()));
		TlvObject tlv = new TlvObject(Command.SYNC_TASK_ACTIVITY_RESP);
		tlv.push(tActivityId);
		tlv.push(tTaskId);
		tlv.push(tTaskName);
		tlv.push(tFromAccountId);
		tlv.push(tFromAccountName);
		tlv.push(tToAccountId);
		tlv.push(tToAccountName);
		tlv.push(tTimestamp);
		tlv.push(tState);
		tlv.push(tCommandTag);
		tlv.push(tTaskPid);
		tlv.push(tSyncState);
		logger.info("from command to tlv package:(tag=" + Command.SYNC_TASK_ACTIVITY_RESP + ", child=12, length="
				+ tlv.getLength() + ")");
		return tlv;
	}

	private NotifyTaskLog activity = new NotifyTaskLog();

	public NotifyTaskLog getTaskActivity()
	{
		return activity;
	}

	public void setTaskActivity(NotifyTaskLog activity)
	{
		this.activity = activity;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskActivityResp.class);

}
