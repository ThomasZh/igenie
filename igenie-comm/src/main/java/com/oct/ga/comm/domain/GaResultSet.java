package com.oct.ga.comm.domain;

public class GaResultSet
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3444805454681500244L;

	private short action;
	private String id;
	private short errorCode;

	public short getAction()
	{
		return action;
	}

	public void setAction(short action)
	{
		this.action = action;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public short getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(short errorCode)
	{
		this.errorCode = errorCode;
	}

}
