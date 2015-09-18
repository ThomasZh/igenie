package com.oct.ga.comm.domain;

import java.io.Serializable;
import java.util.List;

import com.oct.ga.comm.domain.msg.MsgExtend;
import com.oct.ga.comm.domain.msg.MsgLastCacheJsonBean;

public class MessageBadgeNumberJsonBean
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2939406634896393492L;

	private List<MsgLastCacheJsonBean> chatNumber;
	private List<MsgExtend> messageList;

	public List<MsgLastCacheJsonBean> getChatNumber()
	{
		return chatNumber;
	}

	public void setChatNumber(List<MsgLastCacheJsonBean> chatNumber)
	{
		this.chatNumber = chatNumber;
	}

	public List<MsgExtend> getMessageList()
	{
		return messageList;
	}

	public void setMessageList(List<MsgExtend> messageList)
	{
		this.messageList = messageList;
	}

}
