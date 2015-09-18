package com.oct.ga.comm.domain.account;

public class AccountDetail
		extends AccountMaster
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5876976436044298201L;
	private short state;

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

}
