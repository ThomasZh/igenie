package com.oct.ga.comm.domain.group;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.account.AccountBasic;

public class GroupMemberMasterInfo
		extends AccountBasic
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1918372402528494573L;
	private short state;
	private short rank;

	//@Override
	public GroupMemberMasterInfo decode(String json)
	{
		Gson gson = new Gson();
		GroupMemberMasterInfo info = gson.fromJson(json, GroupMemberMasterInfo.class);
		return info;
	}

	//@Override
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

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

	public short getRank()
	{
		return rank;
	}

	public void setRank(short rank)
	{
		this.rank = rank;
	}

}
