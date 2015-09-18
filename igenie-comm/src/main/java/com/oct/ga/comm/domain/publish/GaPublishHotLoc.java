package com.oct.ga.comm.domain.publish;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaPublishHotLoc
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1338098435938772373L;
	private String locDesc;
	private String locX;
	private String locY;
	private String locMask;
	private String url;
	private short weight;

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

	public short getWeight()
	{
		return weight;
	}

	public void setWeight(short weight)
	{
		this.weight = weight;
	}

	public String getLocMask()
	{
		return locMask;
	}

	public void setLocMask(String locMask)
	{
		this.locMask = locMask;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

}
