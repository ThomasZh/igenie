package com.oct.ga.comm.domain;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class FriendInvite
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2477453019016478046L;

	private String ekey;
	private String fromUserId;
	private String toInvitedRegisterId;
	private short channelType;
	private String channelId;

	public String getEkey()
	{
		return ekey;
	}

	public void setEkey(String ekey)
	{
		this.ekey = ekey;
	}

	public String getFromUserId()
	{
		return fromUserId;
	}

	public void setFromUserId(String fromUserId)
	{
		this.fromUserId = fromUserId;
	}

	public String getToInvitedRegisterId()
	{
		return toInvitedRegisterId;
	}

	public void setToInvitedRegisterId(String toRegisterId)
	{
		this.toInvitedRegisterId = toRegisterId;
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

}
