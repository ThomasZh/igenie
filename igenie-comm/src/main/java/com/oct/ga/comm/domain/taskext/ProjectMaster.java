package com.oct.ga.comm.domain.taskext;

import com.google.gson.Gson;

public class ProjectMaster
		extends TaskBase
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1900765144428368095L;
	private int childNum;
	private String leaderId;
	private String leaderName;
	private String leaderAvatarUrl;

	@Override
	public ProjectMaster decode(String json)
	{
		Gson gson = new Gson();
		ProjectMaster info = gson.fromJson(json, ProjectMaster.class);
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

	public int getChildNum()
	{
		return childNum;
	}

	public void setChildNum(int childNum)
	{
		this.childNum = childNum;
	}

	public String getLeaderId()
	{
		return leaderId;
	}

	public void setLeaderId(String leaderId)
	{
		this.leaderId = leaderId;
	}

	public String getLeaderName()
	{
		return leaderName;
	}

	public void setLeaderName(String leaderName)
	{
		this.leaderName = leaderName;
	}

	public String getLeaderAvatarUrl()
	{
		return leaderAvatarUrl;
	}

	public void setLeaderAvatarUrl(String leaderAvatarUrl)
	{
		this.leaderAvatarUrl = leaderAvatarUrl;
	}

}
