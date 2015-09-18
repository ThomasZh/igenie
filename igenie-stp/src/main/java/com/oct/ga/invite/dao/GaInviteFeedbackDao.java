package com.oct.ga.invite.dao;

import java.util.List;

import com.oct.ga.invite.domain.GaFeedbackMasterInfo;

public interface GaInviteFeedbackDao
{
	// ///////////////////////////////////////////////////////////////
	// feedback

	/**
	 * add a feedback, default sync_state=not_received
	 */
	public void add(String inviteId, String feedbackAccountId, String inviteAccountId, short feedbackState,
			int timestamp);

	/**
	 * modify sync_state in feedback table
	 */
	public void updateSyncState(String inviteId, String feedbackAccountId, String inviteAccountId, short syncState,
			int timestamp);

	/**
	 * 临时解决方案
	 */
	public void updateSyncState(String inviteId, String inviteAccountId, short syncState, int timestamp);

	/**
	 * get account's subscribed feedback list not received.
	 */
	public List<GaFeedbackMasterInfo> queryNotReceived(String inviteAccountId);

	public boolean isExist(String inviteId, String feedbackAccountId, String inviteAccountId);
}
