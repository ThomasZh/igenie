package com.oct.ga.comm.domain;

import java.io.Serializable;

public class Device
		implements Serializable
{
	public Device()
	{
	}

	public Device(String deviceId, String apnsToken, String osVersion)
	{
		this.setDeviceId(deviceId);
		this.setApnsToken(apnsToken);
		this.setOsVersion(osVersion);
	}

	private String deviceId;
	private String apnsToken;
	private String osVersion;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
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

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7710689458134049234L;

}
