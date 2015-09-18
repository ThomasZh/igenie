package com.oct.ga.comm.domain.task;

import com.google.gson.Gson;

public class TaskDetailInfo
		extends TaskMasterInfo
{
	private static final long serialVersionUID = -1276136981693039127L;

	private String extAttribute;
	private String templateId;
	private int templateVersion;
	private String createAccountId;
	private short childNum;
	private short completePer;
	private short depth;
	private short channelType;
	private int permission = 508; // default:774

	@Override
	public TaskDetailInfo decode(String json)
	{
		Gson gson = new Gson();
		TaskDetailInfo info = gson.fromJson(json, TaskDetailInfo.class);
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

	public String getExtAttribute()
	{
		return extAttribute;
	}

	public void setExtAttribute(String extAttribute)
	{
		this.extAttribute = extAttribute;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getCreateAccountId()
	{
		return createAccountId;
	}

	public void setCreateAccountId(String createAccountID)
	{
		this.createAccountId = createAccountID;
	}

	public short getChildNum()
	{
		return childNum;
	}

	public void setChildNum(short childNum)
	{
		this.childNum = childNum;
	}

	public short getCompletePer()
	{
		return completePer;
	}

	public void setCompletePer(short completePer)
	{
		this.completePer = completePer;
	}

	private int startTime;

	public int getStartTime()
	{
		return startTime;
	}

	public void setStartTime(int startTime)
	{
		this.startTime = startTime;
	}

	private int endTime;

	public int getEndTime()
	{
		return endTime;
	}

	public void setEndTime(int endTime)
	{
		this.endTime = endTime;
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

	public short getDepth()
	{
		return depth;
	}

	public void setDepth(short depth)
	{
		this.depth = depth;
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
