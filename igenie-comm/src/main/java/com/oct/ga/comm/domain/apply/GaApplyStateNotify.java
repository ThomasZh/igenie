package com.oct.ga.comm.domain.apply;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaApplyStateNotify
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4447774621945296750L;

	private String msgId;
	private String fromAccountId;
	private String fromAccountName;
	private String fromAccountAvatarUrl;
	private String toAccountId;
	private String channelId;
	private String chatId;
	private String channelName;
	private String txt;
	private int timestamp;
	private short action;

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

	public String getToAccountId()
	{
		return toAccountId;
	}

	public void setToAccountId(String toAccountId)
	{
		this.toAccountId = toAccountId;
	}

	public String getTxt()
	{
		return txt;
	}

	public void setTxt(String txt)
	{
		this.txt = txt;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public short getAction()
	{
		return action;
	}

	public void setAction(short action)
	{
		this.action = action;
	}

	public String getChatId()
	{
		return chatId;
	}

	public void setChatId(String chatId)
	{
		this.chatId = chatId;
	}

	public String getMsgId()
	{
		return msgId;
	}

	public void setMsgId(String msgId)
	{
		this.msgId = msgId;
	}

}
