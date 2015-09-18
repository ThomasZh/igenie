package com.oct.ga.comm.domain.club;

import java.util.List;

import com.google.gson.Gson;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.task.TaskBaseInfo;

public class ActivitySubscribeInfo
		extends TaskBaseInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1450934082887547272L;

	private String clubName;
	private int memberRank = GlobalArgs.MEMBER_RANK_NONE;
	private short memberState = GlobalArgs.INVITE_STATE_QUIT;
	private int recommendNum;
	private String leaderName;
	private String leaderAvatarUrl;
	private String locX;
	private String locY;
	private String locDesc;
	/**
	 * private:1211, public:1212
	 */
	private short publishType = GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PRIVATE;
	private List<String> images;

	@Override
	public ActivitySubscribeInfo decode(String json)
	{
		Gson gson = new Gson();
		ActivitySubscribeInfo info = gson.fromJson(json, ActivitySubscribeInfo.class);
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

	public void setMemberRank(int memberRank)
	{
		this.memberRank = memberRank;
	}

	public int getRecommendNum()
	{
		return recommendNum;
	}

	public void setRecommendNum(int recommendNum)
	{
		this.recommendNum = recommendNum;
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

	public String getLocDesc()
	{
		return locDesc;
	}

	public void setLocDesc(String locDesc)
	{
		this.locDesc = locDesc;
	}

	public List<String> getImages()
	{
		return images;
	}

	public void setImages(List<String> images)
	{
		this.images = images;
	}

	public short getPublishType()
	{
		return publishType;
	}

	public void setPublishType(short publishType)
	{
		this.publishType = publishType;
	}

	public String getLocX()
	{
		return locX;
	}

	public void setLocX(String locX)
	{
		this.locX = locX;
	}

	public String getLocY()
	{
		return locY;
	}

	public void setLocY(String locY)
	{
		this.locY = locY;
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
