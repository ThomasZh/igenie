package com.redoct.ga.sup.account.domain;

import java.io.Serializable;

public class LostPwdEkey
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2382136771421884505L;
	private String accountId;
	private short loginType;
	private String loginName;
	private int ttl;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
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

	public int getTtl()
	{
		return ttl;
	}

	public void setTtl(int ttl)
	{
		this.ttl = ttl;
	}

}
