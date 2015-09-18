package com.oct.ga.comm.domain.club;

import com.google.gson.Gson;

public class ClubMasterInfo
		extends ClubBaseInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1151817222977240434L;
	private String desc;
	private String[] sharingUserIds;
	private String creatorId;
	private int memberNum;

	@Override
	public ClubMasterInfo decode(String json)
	{
		Gson gson = new Gson();
		ClubMasterInfo info = gson.fromJson(json, ClubMasterInfo.class);
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

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String[] getSharingUserIds()
	{
		return sharingUserIds;
	}

	public void setSharingUserIds(String[] sharingUserIds)
	{
		this.sharingUserIds = sharingUserIds;
	}

	public int getMemberNum()
	{
		return memberNum;
	}

	public void setMemberNum(int num)
	{
		this.memberNum = num;
	}

	public String getCreatorId()
	{
		return creatorId;
	}

	public void setCreatorId(String creatorId)
	{
		this.creatorId = creatorId;
	}

}
