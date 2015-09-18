package com.oct.ga.comm.domain.jqgrid;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class JqAccount
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5519843565281198112L;
	private String account_id;
	private String login_name;
	private short state;
	private String account_name;
	private String account_desc;
	private String avatar_url;
	private long create_time;

	@Override
	public JqAccount decode(String json)
	{
		Gson gson = new Gson();
		JqAccount info = gson.fromJson(json, JqAccount.class);
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

	public String getAccount_id()
	{
		return account_id;
	}

	public void setAccount_id(String account_id)
	{
		this.account_id = account_id;
	}

	public String getLogin_name()
	{
		return login_name;
	}

	public void setLogin_name(String login_name)
	{
		this.login_name = login_name;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

	public String getAccount_name()
	{
		return account_name;
	}

	public void setAccount_name(String account_name)
	{
		this.account_name = account_name;
	}

	public String getAccount_desc()
	{
		return account_desc;
	}

	public void setAccount_desc(String account_desc)
	{
		this.account_desc = account_desc;
	}

	public String getAvatar_url()
	{
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url)
	{
		this.avatar_url = avatar_url;
	}

	public long getCreate_time()
	{
		return create_time;
	}

	public void setCreate_time(long create_time)
	{
		this.create_time = create_time;
	}

}
