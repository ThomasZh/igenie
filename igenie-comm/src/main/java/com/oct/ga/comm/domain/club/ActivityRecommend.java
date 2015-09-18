package com.oct.ga.comm.domain.club;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class ActivityRecommend
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1437663749274999396L;
	private String activityId;
	private String fromUserId;
	private String fromUserName;
	private String fromUserImageUrl;
	private String[] toUserIds;
	private String toUserId;
	private short syncState;
	private int timestamp;
	private String text;

	@Override
	public ActivityRecommend decode(String json)
	{
		Gson gson = new Gson();
		ActivityRecommend info = gson.fromJson(json, ActivityRecommend.class);
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

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	public String getFromUserId()
	{
		return fromUserId;
	}

	public void setFromUserId(String fromUserId)
	{
		this.fromUserId = fromUserId;
	}

	public String getFromUserName()
	{
		return fromUserName;
	}

	public void setFromUserName(String fromUserName)
	{
		this.fromUserName = fromUserName;
	}

	public String[] getToUserIds()
	{
		return toUserIds;
	}

	public void setToUserIds(String[] toUserIds)
	{
		this.toUserIds = toUserIds;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String txt)
	{
		this.text = txt;
	}

	public String getToUserId()
	{
		return toUserId;
	}

	public void setToUserId(String toUserId)
	{
		this.toUserId = toUserId;
	}

	public short getSyncState()
	{
		return syncState;
	}

	public void setSyncState(short syncState)
	{
		this.syncState = syncState;
	}

	public String getFromUserImageUrl()
	{
		return fromUserImageUrl;
	}

	public void setFromUserImageUrl(String fromUserImageUrl)
	{
		this.fromUserImageUrl = fromUserImageUrl;
	}
}
