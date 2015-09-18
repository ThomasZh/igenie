package com.redoct.ga.sup.device.domain;

import java.io.Serializable;

public class DeviceBasicInfo
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7072206100393156725L;
	private String deviceId;
	private String clientVersion;
	private String appId;
	private String vendorId;
	private String osVersion;
	/**
	 * apnsToken or jpush
	 */
	private String notifyToken;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getClientVersion()
	{
		return clientVersion;
	}

	public void setClientVersion(String clientVersion)
	{
		this.clientVersion = clientVersion;
	}

	public String getOsVersion()
	{
		return osVersion;
	}

	public void setOsVersion(String osVersion)
	{
		this.osVersion = osVersion;
	}

	public String getNotifyToken()
	{
		return notifyToken;
	}

	public void setNotifyToken(String notifyToken)
	{
		this.notifyToken = notifyToken;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

}
