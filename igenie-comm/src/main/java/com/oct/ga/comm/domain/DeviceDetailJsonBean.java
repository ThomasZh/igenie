package com.oct.ga.comm.domain;

public class DeviceDetailJsonBean
		extends Device
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6617521051364886388L;
	private int createTime;
	private int lastUpdateTime;
	private short state;

	public int getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(int createTime)
	{
		this.createTime = createTime;
	}

	public int getLastUpdateTime()
	{
		return lastUpdateTime;
	}

	public void setLastUpdateTime(int lastUpdateTime)
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}
}
