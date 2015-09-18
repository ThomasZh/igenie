package com.oct.ga.comm.domain.msg;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class MsgLastCacheJsonBean
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3463890141502341601L;
	private String chatId;
	private String msgId;
	private short badgeNum;

	public String getChatId()
	{
		return chatId;
	}

	public void setChatId(String channelId)
	{
		this.chatId = channelId;
	}

	public String getMsgId()
	{
		return msgId;
	}

	public void setMsgId(String fromAccountId)
	{
		this.msgId = fromAccountId;
	}

	public short getBadgeNum()
	{
		return badgeNum;
	}

	public void setBadgeNum(short badgeNum)
	{
		this.badgeNum = badgeNum;
	}

}
