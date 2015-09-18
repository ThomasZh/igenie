package com.oct.ga.comm.domain.taskext;

import com.google.gson.Gson;

public class ChildTaskMaster
		extends TaskBase
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5194288822130839583L;

	private int member;
	private short rank;
	private String desc;

	@Override
	public ChildTaskMaster decode(String json)
	{
		Gson gson = new Gson();
		ChildTaskMaster info = gson.fromJson(json, ChildTaskMaster.class);
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

	public int getMember()
	{
		return member;
	}

	public void setMember(int member)
	{
		this.member = member;
	}

	public short getRank()
	{
		return rank;
	}

	public void setRank(short rank)
	{
		this.rank = rank;
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
