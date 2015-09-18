package com.oct.ga.session;

import java.io.Serializable;

/**
 * key:accountId+deviceId
 * value:online+lastTryTime
 * 
 * @author thomas
 *
 */
public class GaAccountDeviceState
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6561501832276645196L;
	private boolean online;
	private int lastTryTime;

	public boolean isOnline()
	{
		return online;
	}

	public void setOnline(boolean online)
	{
		this.online = online;
	}

	public int getLastTryTime()
	{
		return lastTryTime;
	}

	public void setLastTryTime(int lastTryTime)
	{
		this.lastTryTime = lastTryTime;
	}
}
