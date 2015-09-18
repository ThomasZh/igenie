package com.oct.ga.comm.domain.club;

import java.util.List;

import com.google.gson.Gson;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.task.TaskMasterInfo;

public class ActivityDetailInfo
		extends TaskMasterInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 465791974542155313L;
	private String clubName;
	private short memberRank = GlobalArgs.MEMBER_RANK_NONE;
	private short memberState = GlobalArgs.INVITE_STATE_QUIT;
	/**
	 * private:1211, public:1212
	 */
	private short publishType = GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PRIVATE;
	/**
	 * yes:1, no:0
	 */
	private short approveType = GlobalArgs.FALSE;
	/**
	 * yes:1, no:0
	 */
	private short applyFormType = GlobalArgs.FALSE;
	private List<AccountBasic> members;
	private List<ActivityRecommend> recommends;

	public ActivityDetailInfo decode(String json)
	{
		Gson gson = new Gson();
		ActivityDetailInfo info = gson.fromJson(json, ActivityDetailInfo.class);
		return info;
	}

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

	public List<ActivityRecommend> getRecommends()
	{
		return recommends;
	}

	public void setRecommends(List<ActivityRecommend> recommends)
	{
		this.recommends = recommends;
	}

	public List<AccountBasic> getMembers()
	{
		return members;
	}

	public void setMembers(List<AccountBasic> members)
	{
		this.members = members;
	}

	public short getMemberRank()
	{
		return memberRank;
	}

	public void setMemberRank(short memberRank)
	{
		this.memberRank = memberRank;
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

	public short getApproveType()
	{
		return approveType;
	}

	public void setApproveType(short approveType)
	{
		this.approveType = approveType;
	}

	public short getApplyFormType()
	{
		return applyFormType;
	}

	public void setApplyFormType(short applyFormType)
	{
		this.applyFormType = applyFormType;
	}

}
