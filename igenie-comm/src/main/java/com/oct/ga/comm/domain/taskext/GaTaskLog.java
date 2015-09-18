package com.oct.ga.comm.domain.taskext;

import java.io.Serializable;

public class GaTaskLog
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2550225374284559964L;
	private String logId;
	private String channelId;
	private String fromAccountId;
	private short actionTag;
	private String toActionId;
	private int timestamp;

	public String getLogId()
	{
		return logId;
	}

	public void setLogId(String logId)
	{
		this.logId = logId;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getFromAccountId()
	{
		return fromAccountId;
	}

	public void setFromAccountId(String fromAccountId)
	{
		this.fromAccountId = fromAccountId;
	}

	public short getActionTag()
	{
		return actionTag;
	}

	public void setActionTag(short actionTag)
	{
		this.actionTag = actionTag;
	}

	public String getToActionId()
	{
		return toActionId;
	}

	public void setToActionId(String toActionId)
	{
		this.toActionId = toActionId;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

}
