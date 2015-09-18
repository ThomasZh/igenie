package com.redoct.ga.sup.account.domain;

import java.io.Serializable;

public class VerificationCode
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2841485367303237137L;
	private short type;
	private String deviceId;
	private String phone;
	private String ekey;
	private int ttl;
	private int count;

	public short getType()
	{
		return type;
	}

	public void setType(short type)
	{
		this.type = type;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
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

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

}
