package com.redoct.ga.sup.account;

public class GlobalConfigurationVariables
{
	private String supVersion;
	private String supId;
	private int supPort;

	public String getSupVersion()
	{
		return supVersion;
	}

	public void setSupVersion(String stpVersion)
	{
		this.supVersion = stpVersion;
	}

	public int getSupPort()
	{
		return supPort;
	}

	public void setSupPort(int stpPort)
	{
		this.supPort = stpPort;
	}

	public String getSupId()
	{
		return supId;
	}

	public void setSupId(String supId)
	{
		this.supId = supId;
	}

}
