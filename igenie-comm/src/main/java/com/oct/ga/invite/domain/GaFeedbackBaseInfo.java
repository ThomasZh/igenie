package com.oct.ga.invite.domain;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaFeedbackBaseInfo
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8523887275644492677L;
	private String inviteId;
	private String inviteAccountId;
	private String feedbackAccountId;
	private short feedbackState;

	public String getInviteId()
	{
		return inviteId;
	}

	public void setInviteId(String inviteId)
	{
		this.inviteId = inviteId;
	}

	public String getInviteAccountId()
	{
		return inviteAccountId;
	}

	public void setInviteAccountId(String inviteAccountId)
	{
		this.inviteAccountId = inviteAccountId;
	}

	public String getFeedbackAccountId()
	{
		return feedbackAccountId;
	}

	public void setFeedbackAccountId(String feedbackAccountId)
	{
		this.feedbackAccountId = feedbackAccountId;
	}

	public short getFeedbackState()
	{
		return feedbackState;
	}

	public void setFeedbackState(short feedbackState)
	{
		this.feedbackState = feedbackState;
	}

}
