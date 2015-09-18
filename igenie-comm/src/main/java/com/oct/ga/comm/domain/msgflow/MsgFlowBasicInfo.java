package com.oct.ga.comm.domain.msgflow;

import com.oct.ga.comm.domain.club.ActivitySubscribeInfo;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.domain.taskext.GaTaskLog;

public class MsgFlowBasicInfo
		extends GaTaskLog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8227690657821231022L;
	private String fromAccountName;
	private String fromAccountAvatarUrl;
	private String channelName;
	private String toActionAccountId;
	private String toActionAccountName;
	private String toActionAccountAvatarUrl;
	private ActivitySubscribeInfo activity;
	private GaMomentObject moment;

	public String getToActionAccountName()
	{
		return toActionAccountName;
	}

	public void setToActionAccountName(String toActionAccountName)
	{
		this.toActionAccountName = toActionAccountName;
	}

	public String getToActionAccountAvatarUrl()
	{
		return toActionAccountAvatarUrl;
	}

	public void setToActionAccountAvatarUrl(String toActionAccountAvatarUrl)
	{
		this.toActionAccountAvatarUrl = toActionAccountAvatarUrl;
	}

	public ActivitySubscribeInfo getActivity()
	{
		return activity;
	}

	public void setActivity(ActivitySubscribeInfo activity)
	{
		this.activity = activity;
	}

	public GaMomentObject getMoment()
	{
		return moment;
	}

	public void setMoment(GaMomentObject moment)
	{
		this.moment = moment;
	}

	public String getFromAccountName()
	{
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName)
	{
		this.fromAccountName = fromAccountName;
	}

	public String getFromAccountAvatarUrl()
	{
		return fromAccountAvatarUrl;
	}

	public void setFromAccountAvatarUrl(String fromAccountAvatarUrl)
	{
		this.fromAccountAvatarUrl = fromAccountAvatarUrl;
	}

	public String getChannelName()
	{
		return channelName;
	}

	public void setChannelName(String channelName)
	{
		this.channelName = channelName;
	}

	public String getToActionAccountId()
	{
		return toActionAccountId;
	}

	public void setToActionAccountId(String toActionAccountId)
	{
		this.toActionAccountId = toActionAccountId;
	}

}
