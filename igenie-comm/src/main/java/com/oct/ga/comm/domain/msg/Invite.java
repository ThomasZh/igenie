package com.oct.ga.comm.domain.msg;

public class Invite
		extends MessageInlinecast
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4272723893486303731L;
	/**
	 * Invire ID.
	 */
	private String srcInviteId;
	/**
	 * 121: Apply,122: Accept,123: Reject
	 */
	private short inviteState;
	/**
	 * ����taskdesc
	 */
	private String attachDesc;

	public String getSrcInviteId()
	{
		return srcInviteId;
	}

	public void setSrcInviteId(String srcInviteId)
	{
		this.srcInviteId = srcInviteId;
	}

	public short getInviteState()
	{
		return inviteState;
	}

	public void setInviteState(short inviteState)
	{
		this.inviteState = inviteState;
	}

	public String getAttachDesc()
	{
		return attachDesc;
	}

	public void setAttachDesc(String attachDesc)
	{
		this.attachDesc = attachDesc;
	}
}
