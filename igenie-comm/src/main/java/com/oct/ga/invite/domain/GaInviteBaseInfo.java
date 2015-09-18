package com.oct.ga.invite.domain;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaInviteBaseInfo
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 798611818345750782L;
	private String inviteId;
	private short inviteType;
	private String channelId;
	private String fromAccountId;
	private int expiry;

	public String getInviteId()
	{
		return inviteId;
	}

	public void setInviteId(String inviteId)
	{
		this.inviteId = inviteId;
	}

	public short getInviteType()
	{
		return inviteType;
	}

	public void setInviteType(short inviteType)
	{
		this.inviteType = inviteType;
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

	public int getExpiry()
	{
		return expiry;
	}

	public void setExpiry(int expiryTime)
	{
		this.expiry = expiryTime;
	}

}
