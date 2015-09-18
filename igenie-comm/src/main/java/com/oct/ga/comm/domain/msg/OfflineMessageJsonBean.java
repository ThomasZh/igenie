package com.oct.ga.comm.domain.msg;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class OfflineMessageJsonBean
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2388966475414982429L;
	/**
	 * UUID.randomUUID().toString()
	 */
	private String msgId;
	private String fromAccountId;
	private String fromAccountName;
	private String fromAccountAvatarUrl;
	/**
	 * 0:txt
	 */
	private short contentType;
	/**
	 * 101:activity, 102:create_personal, 103:personal
	 */
	private short channelType;
	/**
	 * activityId
	 */
	private String channelId;
	/**
	 * 101:activity=acitivtyId, 102:create_personal=toAccountId,
	 * 103:personal=md5(fromAccountId+toAccountId)
	 */
	private String channelName;
	private String chatId;
	private String content;
	private int timestamp;

	public String getMsgId()
	{
		return msgId;
	}

	public void setMsgId(String msgId)
	{
		this.msgId = msgId;
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

	public short getContentType()
	{
		return contentType;
	}

	public void setContentType(short contentType)
	{
		this.contentType = contentType;
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

	public String getChatId()
	{
		return chatId;
	}

	public void setChatId(String chatId)
	{
		this.chatId = chatId;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
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
