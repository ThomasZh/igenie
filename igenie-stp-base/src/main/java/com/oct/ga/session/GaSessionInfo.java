package com.oct.ga.session;

import java.io.Serializable;

public class GaSessionInfo
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1308077211464312407L;
	private String gateToken;
	/**
	 * sessionId
	 */
	private String sessionToken;
	private String stpId;
	/**
	 * stpSessionId
	 */
	private long ioSessionId;
	private String accountId;
	private String accountName;
	private String apnsToken;
	private String osVersion;
	private String clientVersion;

	public String getGateToken()
	{
		return gateToken;
	}

	public void setGateToken(String gateToken)
	{
		this.gateToken = gateToken;
	}

	public String getSessionToken()
	{
		return sessionToken;
	}

	public void setSessionToken(String sessionToken)
	{
		this.sessionToken = sessionToken;
	}

	public String getStpId()
	{
		return stpId;
	}

	public void setStpId(String stpId)
	{
		this.stpId = stpId;
	}

	public long getIoSessionId()
	{
		return ioSessionId;
	}

	public void setIoSessionId(long ioSessionId)
	{
		this.ioSessionId = ioSessionId;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public String getApnsToken()
	{
		return apnsToken;
	}

	public void setApnsToken(String apnsToken)
	{
		this.apnsToken = apnsToken;
	}

	public String getOsVersion()
	{
		return osVersion;
	}

	public void setOsVersion(String osVersion)
	{
		this.osVersion = osVersion;
	}

	public String getClientVersion()
	{
		return clientVersion;
	}

	public void setClientVersion(String clientVersion)
	{
		this.clientVersion = clientVersion;
	}
}
