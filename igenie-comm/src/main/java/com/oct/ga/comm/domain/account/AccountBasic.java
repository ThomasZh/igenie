package com.oct.ga.comm.domain.account;

import java.io.Serializable;

public class AccountBasic
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4031908223758752017L;

	private String accountId;
	private String nickname;
	private String avatarUrl;
	private String desc;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getAvatarUrl()
	{
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl)
	{
		this.avatarUrl = avatarUrl;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

}
