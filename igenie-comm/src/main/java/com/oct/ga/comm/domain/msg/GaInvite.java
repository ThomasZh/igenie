package com.oct.ga.comm.domain.msg;

public class GaInvite
		extends MsgBasic
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5436390190562608020L;
	private String inviteId;
	private short inviteType;
	private String toUserSemiId;
	private int expiry;
	private int timestamp;

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

	public String getToUserSemiId()
	{
		return toUserSemiId;
	}

	public void setToUserSemiId(String toUserSemiId)
	{
		this.toUserSemiId = toUserSemiId;
	}

	public int getExpiry()
	{
		return expiry;
	}

	public void setExpiry(int expiry)
	{
		this.expiry = expiry;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int lastUpdateTime)
	{
		this.timestamp = lastUpdateTime;
	}

}
