package com.oct.ga.comm.domain.msg;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class MsgOriginal
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1000850188917897949L;
	/**
	 * UUID.randomUUID().toString()
	 */
	private String msgId;
	private String fromAccountId;
	/**
	 * 0:txt
	 */
	private short contentType;
	/**
	 * 101:activity, 103:personal
	 */
	private short channelType;
	/**
	 * activityId
	 */
	private String channelId;
	/**
	 * in activity=acitivtyId; in personal=md5(fromAccountId+toAccountId)
	 */
	private String chatId;
	private String content;
	private String attachUrl;

	public String getMsgId()
	{
		return msgId;
	}

	public void setMsgId(String msgId)
	{
		this.msgId = msgId;
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

	public String getAttachUrl()
	{
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl)
	{
		this.attachUrl = attachUrl;
	}

	public String getFromAccountId()
	{
		return fromAccountId;
	}

	public void setFromAccountId(String fromAccountId)
	{
		this.fromAccountId = fromAccountId;
	}

}
