package com.oct.ga.comm.domain;

public class Ekey
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3287821370349812368L;

	private String userId;
	private String type;
	private String ekey;
	private int ttl;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getEkey()
	{
		return ekey;
	}

	public void setEkey(String ekey)
	{
		this.ekey = ekey;
	}

	public int getTtl()
	{
		return ttl;
	}

	public void setTtl(int ttl)
	{
		this.ttl = ttl;
	}

}
