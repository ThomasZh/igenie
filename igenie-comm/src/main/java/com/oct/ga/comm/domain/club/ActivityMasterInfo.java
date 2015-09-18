package com.oct.ga.comm.domain.club;

import com.google.gson.Gson;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.task.TaskBaseInfo;

public class ActivityMasterInfo
		extends TaskBaseInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2619843241654490824L;

	private String clubName;
	private int memberRank=GlobalArgs.MEMBER_RANK_NONE;
	private short memberState = GlobalArgs.INVITE_STATE_QUIT;
	private int recommendNum;
	/**
	 * private:1211, public:1212
	 */
	private short publishType = GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PRIVATE;

	public String encode()
	{
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}

	public ActivityMasterInfo decode(String json)
	{
		Gson gson = new Gson();
		ActivityMasterInfo info = gson.fromJson(json, ActivityMasterInfo.class);
		return info;
	}

	public String getClubName()
	{
		return clubName;
	}

	public void setClubName(String clubName)
	{
		this.clubName = clubName;
	}

	public int getMemberRank()
	{
		return memberRank;
	}

	public void setMemberRank(int isMember)
	{
		this.memberRank = isMember;
	}

	public int getRecommendNum()
	{
		return recommendNum;
	}

	public void setRecommendNum(int recommendNum)
	{
		this.recommendNum = recommendNum;
	}

	public short getPublishType()
	{
		return publishType;
	}

	public void setPublishType(short publishType)
	{
		this.publishType = publishType;
	}

	public short getMemberState()
	{
		return memberState;
	}

	public void setMemberState(short memberState)
	{
		this.memberState = memberState;
	}

}
