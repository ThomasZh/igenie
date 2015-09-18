package com.oct.ga.comm.domain.club;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class ActivityNameListInfo
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1971920333840740905L;
	private String id;
	private String name;
	private int memberNum;

	@Override
	public ActivityNameListInfo decode(String json)
	{
		Gson gson = new Gson();
		ActivityNameListInfo info = gson.fromJson(json, ActivityNameListInfo.class);
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

	public int getMemberNum()
	{
		return memberNum;
	}

	public void setMemberNum(int memberNum)
	{
		this.memberNum = memberNum;
	}

}
