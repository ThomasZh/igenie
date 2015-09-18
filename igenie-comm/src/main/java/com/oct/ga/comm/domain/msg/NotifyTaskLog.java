package com.oct.ga.comm.domain.msg;

public class NotifyTaskLog
		extends Notify
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5470926140456195426L;
	private short commandTag;
	private String taskPid;
	private short depth;
	private String sendToAccountId;
	private String sendToAccountName;
	/**
	 * 121:apply,122: Accept,123: Reject
	 */
	private short activityState;

	public NotifyTaskLog copy()
	{
		NotifyTaskLog message = new NotifyTaskLog();

		message.set_id(this.get_id());
		message.setContent(this.getContent());
		message.setContentType(this.getContentType());
		message.setCurrentTimestamp(this.getCurrentTimestamp());
		message.setFromAccountId(this.getFromAccountId());
		message.setFromAccountName(this.getFromAccountName());
		message.setReciverIoSessionId(this.getReciverIoSessionId());
		message.setSenderDeviceId(this.getSenderDeviceId());
		message.setSyncState(this.getSyncState());
		message.setTimestamp(this.getTimestamp());
		message.setToAccountId(this.getToAccountId());
		message.setToAccountName(this.getToAccountName());
		message.setChannelId(this.getChannelId());
		message.setChannelName(this.getChannelName());
		message.setChannelType(this.getChannelType());
		message.setCommandTag(commandTag);
		message.setTaskPid(taskPid);
		message.setDepth(depth);
		message.setSendToAccountId(sendToAccountId);
		message.setSendToAccountName(sendToAccountName);

		return message;
	}

	public short getCommandTag()
	{
		return commandTag;
	}

	public void setCommandTag(short commandTag)
	{
		this.commandTag = commandTag;
	}

	public String getTaskPid()
	{
		return taskPid;
	}

	public void setTaskPid(String taskPid)
	{
		this.taskPid = taskPid;
	}

	public String getSendToAccountId()
	{
		return sendToAccountId;
	}

	public void setSendToAccountId(String sendToAccountId)
	{
		this.sendToAccountId = sendToAccountId;
	}

	public String getSendToAccountName()
	{
		return sendToAccountName;
	}

	public void setSendToAccountName(String sendToAccountName)
	{
		this.sendToAccountName = sendToAccountName;
	}

	public short getActivityState()
	{
		return activityState;
	}

	public void setActivityState(short activityState)
	{
		this.activityState = activityState;
	}

	public short getDepth()
	{
		return depth;
	}

	public void setDepth(short depth)
	{
		this.depth = depth;
	}
}
