package com.oct.ga.gatekeeper;

public class GlobalConfigurationVariables
{
	private String gatekeeperVersion;
	private String gatekeeperId;
	private int gatekeeperPort;
	private String apnsCertificateDestination;
	private String apnsCertificatePath;
	private String apnsCertificateCode;
	private String stpServerListPath;
	private String supServerListPath;
	private int heartbitInterval;
	private int checkInterval;

	public String getApnsCertificateDestination()
	{
		return apnsCertificateDestination;
	}

	public void setApnsCertificateDestination(String apnsCertificateDestination)
	{
		this.apnsCertificateDestination = apnsCertificateDestination;
	}

	public String getApnsCertificatePath()
	{
		return apnsCertificatePath;
	}

	public void setApnsCertificatePath(String apnsCertificatePath)
	{
		this.apnsCertificatePath = apnsCertificatePath;
	}

	public String getApnsCertificateCode()
	{
		return apnsCertificateCode;
	}

	public void setApnsCertificateCode(String apnsCertificateCode)
	{
		this.apnsCertificateCode = apnsCertificateCode;
	}

	public String getStpServerListPath()
	{
		return stpServerListPath;
	}

	public void setStpServerListPath(String stpServerListPath)
	{
		this.stpServerListPath = stpServerListPath;
	}

	public String getSupServerListPath()
	{
		return supServerListPath;
	}

	public void setSupServerListPath(String supServerListPath)
	{
		this.supServerListPath = supServerListPath;
	}

	public String getGatekeeperVersion()
	{
		return gatekeeperVersion;
	}

	public void setGatekeeperVersion(String stpVersion)
	{
		this.gatekeeperVersion = stpVersion;
	}

	public int getGatekeeperPort()
	{
		return gatekeeperPort;
	}

	public void setGatekeeperPort(int stpPort)
	{
		this.gatekeeperPort = stpPort;
	}

	public String getGatekeeperId()
	{
		return gatekeeperId;
	}

	public void setGatekeeperId(String supId)
	{
		this.gatekeeperId = supId;
	}

	public int getHeartbitInterval()
	{
		return heartbitInterval;
	}

	public void setHeartbitInterval(int heartbitInterval)
	{
		this.heartbitInterval = heartbitInterval;
	}

	public int getCheckInterval()
	{
		return checkInterval;
	}

	public void setCheckInterval(int checkInterval)
	{
		this.checkInterval = checkInterval;
	}

}
