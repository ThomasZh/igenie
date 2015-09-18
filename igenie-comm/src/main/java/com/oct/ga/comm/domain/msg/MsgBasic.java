package com.oct.ga.comm.domain.msg;

import java.io.Serializable;

public class MsgBasic
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1919546322526588094L;
	private String fromAccountId;
	private String fromAccountName;
	private String fromAccountAvatarUrl;
	/**
	 * 101:task_message, 102:chat_message, 103:personal_message, 104:invite,
	 * 105:task_log, 106:news, 107:vote, 108:gomoku
	 */
	private short channelType;
	/**
	 * taskId,gameId
	 */
	private String channelId;
	private String channelName;

	public String getFromAccountId()
	{
		return fromAccountId;
	}

	public void setFromAccountId(String fromAccountId)
	{
		this.fromAccountId = fromAccountId;
	}

	public String getFromAccountName()
	{
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName)
	{
		this.fromAccountName = fromAccountName;
	}

	public String getFromAccountAvatarUrl()
	{
		return fromAccountAvatarUrl;
	}

	public void setFromAccountAvatarUrl(String fromAccountAvatarUrl)
	{
		this.fromAccountAvatarUrl = fromAccountAvatarUrl;
	}

	public short getChannelType()
	{
		return channelType;
	}

	public void setChannelType(short channelType)
	{
		this.channelType = channelType;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getChannelName()
	{
		return channelName;
	}

	public void setChannelName(String channelName)
	{
		this.channelName = channelName;
	}

}
