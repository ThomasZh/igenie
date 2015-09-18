package com.oct.ga.comm.domain.msg;

import com.oct.ga.comm.GlobalArgs;

public class MessageExtendUnicast
		extends MessageOriginalMulticast
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5863172670010216511L;

	public MessageExtendUnicast()
	{
		this.setSyncState(GlobalArgs.SYNC_STATE_NOT_RECEIVED);
	}

	/**
	 * notRead:51, read:52
	 */
	private short syncState;
	/**
	 * accountId(UUID)
	 */
	private String toAccountId;
	private String toAccountName;

	public short getSyncState()
	{
		return syncState;
	}

	public void setSyncState(short syncState)
	{
		this.syncState = syncState;
	}

	public String getToAccountId()
	{
		return toAccountId;
	}

	public void setToAccountId(String toAccountId)
	{
		this.toAccountId = toAccountId;
	}

	public String getToAccountName()
	{
		return toAccountName;
	}

	public void setToAccountName(String toAccountName)
	{
		this.toAccountName = toAccountName;
	}

}
