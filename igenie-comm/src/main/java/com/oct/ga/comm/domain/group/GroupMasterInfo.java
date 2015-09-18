package com.oct.ga.comm.domain.group;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GroupMasterInfo
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 630077415413326087L;
	private String id;
	private String name;
	private short channelType;
	private String creatorId;
	private int memberNum;
	private short state;

	@Override
	public GroupMasterInfo decode(String json)
	{
		Gson gson = new Gson();
		GroupMasterInfo info = gson.fromJson(json, GroupMasterInfo.class);
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

	public String getCreatorId()
	{
		return creatorId;
	}

	public void setCreatorId(String creatorId)
	{
		this.creatorId = creatorId;
	}

	public int getMemberNum()
	{
		return memberNum;
	}

	public void setMemberNum(int memberNum)
	{
		this.memberNum = memberNum;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
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
