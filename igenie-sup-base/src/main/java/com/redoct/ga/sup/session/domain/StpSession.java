package com.redoct.ga.sup.session.domain;

import java.io.Serializable;

public class StpSession
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3538954045400640586L;
	// private String sessionTicket; // key
	private String accountId;
	private String deviceId;
	private String deviceOsVersion;
	private String gateToken;
	private long ioSessionId;// mina
	private String notifyToken;
	private boolean active;
	private int expiryTime;
	private short loginType;
	private String loginName;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getGateToken()
	{
		return gateToken;
	}

	public void setGateToken(String gateToken)
	{
		this.gateToken = gateToken;
	}

	public long getIoSessionId()
	{
		return ioSessionId;
	}

	public void setIoSessionId(long ioSessionId)
	{
		this.ioSessionId = ioSessionId;
	}

	public String getNotifyToken()
	{
		return notifyToken;
	}

	public void setNotifyToken(String notifyToken)
	{
		this.notifyToken = notifyToken;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public int getExpiryTime()
	{
		return expiryTime;
	}

	public void setExpiryTime(int expiryTime)
	{
		this.expiryTime = expiryTime;
	}

	public String getDeviceOsVersion()
	{
		return deviceOsVersion;
	}

	public void setDeviceOsVersion(String deviceOsVersion)
	{
		this.deviceOsVersion = deviceOsVersion;
	}

	public short getLoginType()
	{
		return loginType;
	}

	public void setLoginType(short loginType)
	{
		this.loginType = loginType;
	}

	public String getLoginName()
	{
		return loginName;
	}

	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

}
