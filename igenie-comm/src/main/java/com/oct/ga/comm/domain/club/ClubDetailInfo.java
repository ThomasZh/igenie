package com.oct.ga.comm.domain.club;

import com.google.gson.Gson;

public class ClubDetailInfo
		extends ClubMasterInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1994746484077468149L;
	private int activityNum;
	private int totalJoinNum;

	@Override
	public ClubDetailInfo decode(String json)
	{
		Gson gson = new Gson();
		ClubDetailInfo info = gson.fromJson(json, ClubDetailInfo.class);
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

	public int getActivityNum()
	{
		return activityNum;
	}

	public void setActivityNum(int activityNum)
	{
		this.activityNum = activityNum;
	}

	public int getTotalJoinNum()
	{
		return totalJoinNum;
	}

	public void setTotalJoinNum(int totalJoinNum)
	{
		this.totalJoinNum = totalJoinNum;
	}
}
