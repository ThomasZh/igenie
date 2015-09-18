package com.oct.ga.comm.domain.taskpro;

import com.google.gson.Gson;

public class TaskProExtInfo
		extends TaskProDetailInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7990191141272088475L;
	private String templateId;
	private int templateVersion;
	private String createAccountId;
	private int permission = 508; // default:774
	private short channelType;

	@Override
	public TaskProExtInfo decode(String json)
	{
		Gson gson = new Gson();
		TaskProExtInfo info = gson.fromJson(json, TaskProExtInfo.class);
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

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public int getPermission()
	{
		return permission;
	}

	public void setPermission(int permission)
	{
		this.permission = permission;
	}

	public int getTemplateVersion()
	{
		return templateVersion;
	}

	public void setTemplateVersion(int templateVersion)
	{
		this.templateVersion = templateVersion;
	}

	public String getCreateAccountId()
	{
		return createAccountId;
	}

	public void setCreateAccountId(String createAccountId)
	{
		this.createAccountId = createAccountId;
	}

	public short getChannelType()
	{
		return channelType;
	}

	public void setChannelType(short channelType)
	{
		this.channelType = channelType;
	}
}
