package com.oct.ga.comm.domain.msg;

public class MsgSyncState
{
	private String msgId;
	private short syncState;

	public String getMsgId()
	{
		return msgId;
	}

	public void setMsgId(String msgId)
	{
		this.msgId = msgId;
	}

	public short getSyncState()
	{
		return syncState;
	}

	public void setSyncState(short syncState)
	{
		this.syncState = syncState;
	}

}
