package com.redoct.ga.sup.session.domain;

import java.io.Serializable;

public class GateSession
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 734836273678917391L;
	private String gateToken;
	private String deviceId; // key
	private String stpId;
	private String stpIp;
	private int stpPort;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getStpId()
	{
		return stpId;
	}

	public void setStpId(String stpId)
	{
		this.stpId = stpId;
	}

	public String getStpIp()
	{
		return stpIp;
	}

	public void setStpIp(String stpIp)
	{
		this.stpIp = stpIp;
	}

	public int getStpPort()
	{
		return stpPort;
	}

	public void setStpPort(int stpPort)
	{
		this.stpPort = stpPort;
	}

	public String getGateToken()
	{
		return gateToken;
	}

	public void setGateToken(String gateToken)
	{
		this.gateToken = gateToken;
	}

}
