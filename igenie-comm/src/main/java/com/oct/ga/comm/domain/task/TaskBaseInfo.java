package com.oct.ga.comm.domain.task;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class TaskBaseInfo
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8525420949173158224L;

	private String id;
	private String name;
	private String pid;
	private int startTime;
	private short state;
	private short memberNum;
	private short memberAvailableNum;

	@Override
	public TaskBaseInfo decode(String json)
	{
		Gson gson = new Gson();
		TaskBaseInfo info = gson.fromJson(json, TaskBaseInfo.class);
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

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPid()
	{
		return pid;
	}

	public void setPid(String pid)
	{
		this.pid = pid;
	}

	public int getStartTime()
	{
		return startTime;
	}

	public void setStartTime(int startTime)
	{
		this.startTime = startTime;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

	public short getMemberNum()
	{
		return memberNum;
	}

	public void setMemberNum(short memberNum)
	{
		this.memberNum = memberNum;
	}

	public short getMemberAvailableNum()
	{
		return memberAvailableNum;
	}

	public void setMemberAvailableNum(short memberAvailableNum)
	{
		this.memberAvailableNum = memberAvailableNum;
	}

}
