package com.oct.ga.comm.domain.moment;

public class GaMomentLogObject
		extends GaMomentCommentObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4979190946781213562L;
	private short action;
	private short syncState;

	public short getAction()
	{
		return action;
	}

	public void setAction(short action)
	{
		this.action = action;
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
