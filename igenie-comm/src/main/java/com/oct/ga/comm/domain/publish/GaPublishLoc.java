package com.oct.ga.comm.domain.publish;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaPublishLoc
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5340625595290033894L;
	private short seq;
	private String locDesc;
	private String locX;
	private String locY;
	private String locMask;

	public short getSeq()
	{
		return seq;
	}

	public void setSeq(short seq)
	{
		this.seq = seq;
	}

	public String getLocDesc()
	{
		return locDesc;
	}

	public void setLocDesc(String locDesc)
	{
		this.locDesc = locDesc;
	}

	public String getLocX()
	{
		return locX;
	}

	public void setLocX(String locX)
	{
		this.locX = locX;
	}

	public String getLocY()
	{
		return locY;
	}

	public void setLocY(String locY)
	{
		this.locY = locY;
	}

	public String getLocMask()
	{
		return locMask;
	}

	public void setLocMask(String locMask)
	{
		this.locMask = locMask;
	}

}
