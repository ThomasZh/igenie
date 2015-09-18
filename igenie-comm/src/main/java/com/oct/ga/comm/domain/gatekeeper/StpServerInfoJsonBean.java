package com.oct.ga.comm.domain.gatekeeper;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class StpServerInfoJsonBean
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3405084704697802754L;

	private String stpId;
	private String serverIp;
	private int port;
	private long lastTryTimestamp;
	private boolean active;
	private String maxVersion;
	private String minVersion;

	@Override
	public StpServerInfoJsonBean decode(String json)
	{
		Gson gson = new Gson();
		StpServerInfoJsonBean info = gson.fromJson(json, StpServerInfoJsonBean.class);
		return info;
	}

	@Override
	public String encode()
	{
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}

	@Override
	public String toString()
	{
		return this.encode();
	}

	public String getServerIp()
	{
		return serverIp;
	}

	public void setServerIp(String serverIp)
	{
		this.serverIp = serverIp;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public String getStpId()
	{
		return stpId;
	}

	public void setStpId(String serverId)
	{
		this.stpId = serverId;
	}

	public long getLastTryTimestamp()
	{
		return lastTryTimestamp;
	}

	public void setLastTryTimestamp(long lastTryTimestamp)
	{
		this.lastTryTimestamp = lastTryTimestamp;
	}

	public String getMaxVersion()
	{
		return maxVersion;
	}

	public void setMaxVersion(String maxVersion)
	{
		this.maxVersion = maxVersion;
	}

	public String getMinVersion()
	{
		return minVersion;
	}

	public void setMinVersion(String minVersion)
	{
		this.minVersion = minVersion;
	}

}
