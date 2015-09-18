package com.oct.ga.comm.cmd;

public abstract class RespCommand
		extends StpCommand
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3832718838707059L;
	private short respState;

	public short getRespState()
	{
		return respState;
	}

	public void setRespState(short respState)
	{
		this.respState = respState;
	}

}
