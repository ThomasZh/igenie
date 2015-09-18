package com.oct.ga.comm.cmd.inlinecast;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class InlinecastTaskNotifyReq
		extends ReqCommand
{
	public InlinecastTaskNotifyReq()
	{
		super();

		this.setTag(Command.INLINECAST_TASK_ACTIVITY_REQ);
	}

	public InlinecastTaskNotifyReq(NotifyTaskLog taskActivity)
	{
		this();

		this.setTaskActivity(taskActivity);
	}

	@Override
	public InlinecastTaskNotifyReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.INLINECAST_TASK_ACTIVITY_REQ + ", child=12) to command");

		TlvParser.decodeChildren(tlv, 12);

		TlvObject tActivityId = tlv.getChild(0);
		String activityId = new String(tActivityId.getValue(), "UTF-8");
		logger.debug("activityId: " + activityId);

		TlvObject tTaskId = tlv.getChild(1);
		String taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tTaskName = tlv.getChild(2);
		String taskName = new String(tTaskName.getValue(), "UTF-8");
		logger.debug("taskName: " + taskName);

		TlvObject tFromAccountId = tlv.getChild(3);
		String fromAccountId = new String(tFromAccountId.getValue(), "UTF-8");
		logger.debug("fromAccountId: " + fromAccountId);

		TlvObject tFromAccountName = tlv.getChild(4);
		String fromAccountName = new String(tFromAccountName.getValue(), "UTF-8");
		logger.debug("fromAccountName: " + fromAccountName);

		TlvObject tToAccountId = tlv.getChild(5);
		String toAccountId = new String(tToAccountId.getValue(), "UTF-8");
		logger.debug("toAccountId: " + toAccountId);

		TlvObject tToAccountName = tlv.getChild(6);
		String toAccountName = new String(tToAccountName.getValue(), "UTF-8");
		logger.debug("toAccountName: " + toAccountName);

		TlvObject tTimestamp = tlv.getChild(7);
		int timestamp = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("timestamp: " + timestamp);

		TlvObject tState = tlv.getChild(8);
		short state = TlvByteUtil.byte2Short(tState.getValue());
		logger.debug("state: " + state);

		TlvObject tCommandTag = tlv.getChild(9);
		short commandTag = TlvByteUtil.byte2Short(tCommandTag.getValue());
		logger.debug("commandTag: " + commandTag);

		TlvObject tIoSessionId = tlv.getChild(10);
		long ioSessionId = TlvByteUtil.byte2Long(tIoSessionId.getValue());
		logger.debug("ioSessionId: " + ioSessionId);

		TlvObject tTaskPid = tlv.getChild(11);
		String taskPid = new String(tTaskPid.getValue(), "UTF-8");
		logger.debug("taskPid: " + taskPid);

		activity.set_id(activityId);
		activity.setChannelId(taskId);// taskId
		activity.setChannelName(taskName);// taskName
		activity.setFromAccountId(fromAccountId);
		activity.setFromAccountName(fromAccountName);
		activity.setToAccountId(toAccountId);
		activity.setToAccountName(toAccountName);
		activity.setTimestamp(timestamp);
		activity.setActivityState(state);
		activity.setCommandTag(commandTag);
		activity.setReciverIoSessionId(ioSessionId);
		activity.setTaskPid(taskPid);

		return this;
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
		TlvObject tIoSessionId = new TlvObject(11, 8, TlvByteUtil.long2Byte(activity.getReciverIoSessionId()));
		TlvObject tTaskPid = new TlvObject(12, activity.getTaskPid());

		TlvObject tlv = new TlvObject(Command.INLINECAST_TASK_ACTIVITY_REQ);
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
		tlv.push(tIoSessionId);
		tlv.push(tTaskPid);

		logger.info("from command to tlv package:(tag=" + Command.INLINECAST_TASK_ACTIVITY_REQ + ", child=12, length="
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

	private final static Logger logger = LoggerFactory.getLogger(InlinecastTaskNotifyReq.class);

}
