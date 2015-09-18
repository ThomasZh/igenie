package com.oct.ga.comm.domain.account;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class LoginInfo
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1519672483255350532L;
	private short loginType;
	private String loginName;

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
