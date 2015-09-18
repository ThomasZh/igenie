package com.oct.ga.comm.domain.group;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.account.AccountBasic;


public class GroupMemberDetailInfo
		extends AccountBasic
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8599071055763145031L;
	/**
	 * <li>MEMBER_RANK_NORMAL = 201; </li>
	 * <li>MEMBER_RANK_ELDER = 202; </li>
	 * <li>MEMBER_RANK_CO_LEADER = 203; </li>
	 * <li>MEMBER_RANK_LEADER = 204;</li>
	 */
	private short rank;
	/**
	 * <li>INVITE_STATE_QUIT = 120; </li>
	 * <li>INVITE_STATE_APPLY = 121; </li>
	 * <li>INVITE_STATE_ACCPET = 122; </li>
	 * <li>INVITE_STATE_REJECT = 123; </li>
	 * <li>INVITE_STATE_KICKOFF = 124;</li>
	 * <li>INVITE_STATE_DISPATCH = 125;</li>
	 */
	private short state;
	private String groupId;

	//@Override
	public GroupMemberDetailInfo decode(String json)
	{
		Gson gson = new Gson();
		GroupMemberDetailInfo info = gson.fromJson(json, GroupMemberDetailInfo.class);
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
	
	public short getRank()
	{
		return rank;
	}

	public void setRank(short rank)
	{
		this.rank = rank;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}
}
