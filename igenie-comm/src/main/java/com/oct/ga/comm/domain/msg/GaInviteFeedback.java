package com.oct.ga.comm.domain.msg;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaInviteFeedback
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2160104630233110253L;
	private String inviteId;
	private short inviteType;
	private String fromUserId;
	private String toUserSemiId;
	private String feedbackUserId;
	private String feedbackUserName;
	private String feedbackUserAvatarUrl;
	private short feedbackChannelType;
	private String feedbackChannelId;
	private String feedbackChannelName;
	private short feedbackState;
	private int timestamp;

	@Override
	public GaInviteFeedback decode(String json)
	{
		Gson gson = new Gson();
		GaInviteFeedback info = gson.fromJson(json, GaInviteFeedback.class);
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

	public String getInviteId()
	{
		return inviteId;
	}

	public void setInviteId(String inviteId)
	{
		this.inviteId = inviteId;
	}

	public String getFromUserId()
	{
		return fromUserId;
	}

	public void setFromUserId(String fromUserId)
	{
		this.fromUserId = fromUserId;
	}

	public String getToUserSemiId()
	{
		return toUserSemiId;
	}

	public void setToUserSemiId(String toUserSemiId)
	{
		this.toUserSemiId = toUserSemiId;
	}

	public String getFeedbackUserId()
	{
		return feedbackUserId;
	}

	public void setFeedbackUserId(String feedbackUserId)
	{
		this.feedbackUserId = feedbackUserId;
	}

	public short getFeedbackState()
	{
		return feedbackState;
	}

	public void setFeedbackState(short feedbackState)
	{
		this.feedbackState = feedbackState;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int lastUpdateTime)
	{
		this.timestamp = lastUpdateTime;
	}

	public String getFeedbackUserName()
	{
		return feedbackUserName;
	}

	public void setFeedbackUserName(String feedbackUserName)
	{
		this.feedbackUserName = feedbackUserName;
	}

	public String getFeedbackUserAvatarUrl()
	{
		return feedbackUserAvatarUrl;
	}

	public void setFeedbackUserAvatarUrl(String feedbackUserAvatarUrl)
	{
		this.feedbackUserAvatarUrl = feedbackUserAvatarUrl;
	}

	public short getFeedbackChannelType()
	{
		return feedbackChannelType;
	}

	public void setFeedbackChannelType(short feedbackChannelType)
	{
		this.feedbackChannelType = feedbackChannelType;
	}

	public String getFeedbackChannelId()
	{
		return feedbackChannelId;
	}

	public void setFeedbackChannelId(String feedbackChannelId)
	{
		this.feedbackChannelId = feedbackChannelId;
	}

	public String getFeedbackChannelName()
	{
		return feedbackChannelName;
	}

	public void setFeedbackChannelName(String feedbackChannelName)
	{
		this.feedbackChannelName = feedbackChannelName;
	}

	public short getInviteType()
	{
		return inviteType;
	}

	public void setInviteType(short inviteType)
	{
		this.inviteType = inviteType;
	}

}
