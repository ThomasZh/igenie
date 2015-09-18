package com.oct.ga.invite;

import java.util.List;
import java.util.UUID;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.invite.dao.GaInviteDao;
import com.oct.ga.invite.dao.GaInviteFeedbackDao;
import com.oct.ga.invite.domain.GaFeedbackBaseInfo;
import com.oct.ga.invite.domain.GaFeedbackMasterInfo;
import com.oct.ga.invite.domain.GaInviteBaseInfo;
import com.oct.ga.invite.domain.GaInviteMasterInfo;
import com.oct.ga.service.GaInviteService;

public class InviteServiceImpl
		implements GaInviteService
{
	// ///////////////////////////////////////////////////////////////
	// invite original

	@Override
	public String create(GaInviteBaseInfo invite, int timestamp)
	{
		String inviteId = UUID.randomUUID().toString();
		inviteDao.addOriginal(inviteId, invite.getInviteType(), invite.getFromAccountId(), invite.getChannelId(),
				invite.getExpiry(), timestamp);
		return inviteId;
	}

	@Override
	public GaInviteMasterInfo queryMaster(String inviteId)
	{
		return inviteDao.queryMaster(inviteId);
	}

	@Override
	public void modifyExpiryTime(String inviteId, int expiry, int timestamp)
	{
		inviteDao.updateExpiryTime(inviteId, expiry, timestamp);
	}

	// ///////////////////////////////////////////////////////////////
	// invite subscribe

	@Override
	public void subscribeInvite(String inviteId, String toAccountId, int timestamp)
	{
		inviteDao.addSysSubscribe(inviteId, toAccountId, timestamp);
	}

	@Override
	public void subscribeInvite(String inviteId, short loginType, String loginName, int timestamp)
	{
		inviteDao.addExternalSubscribe(inviteId, loginType, loginName, timestamp);
	}

	@Override
	public boolean isExistSubscribeInvite(String inviteId, String toAccountId)
	{
		return inviteDao.isExistSysSubscribe(inviteId, toAccountId);
	}

	@Override
	public GaInviteMasterInfo queryMaster(short inviteType, String fromAccountId, String toAccountId)
	{
		return inviteDao.querySysSubscribe(inviteType, fromAccountId, toAccountId);
	}

	@Override
	public GaInviteMasterInfo queryMaster(short inviteType, String fromAccountId, short loginType, String toLoginName)
	{
		return inviteDao.queryExternalSubscribe(inviteType, fromAccountId, loginType, toLoginName);
	}

	@Override
	public List<String> queryExternalSubscribeIds(String inviteId)
	{
		return inviteDao.queryExternalSubscribeIds(inviteId);
	}

	@Override
	public void modifySyncStateToNotReceived(String inviteId, String toAccountId, int timestamp)
	{
		inviteDao.updateSysSubscribeState(inviteId, toAccountId, GlobalArgs.SYNC_STATE_NOT_RECEIVED, timestamp);
	}

	@Override
	public void modifySyncStateToNotReceived(String inviteId, short loginType, String loginName, int timestamp)
	{
		inviteDao.updateExternalSubscribeState(inviteId, loginType, loginName, GlobalArgs.SYNC_STATE_NOT_RECEIVED,
				timestamp);
	}

	@Override
	public void confirmReceivedInvite(String inviteId, String toAccountId, int timestamp)
	{
		inviteDao.updateSysSubscribeState(inviteId, toAccountId, GlobalArgs.SYNC_STATE_RECEIVED, timestamp);
	}

	@Override
	public void confirmReceivedInvite(String inviteId, short loginType, String loginName, int timestamp)
	{
		inviteDao.updateExternalSubscribeState(inviteId, loginType, loginName, GlobalArgs.SYNC_STATE_RECEIVED,
				timestamp);
	}

	@Override
	public void confirmReadInvite(String inviteId, String toAccountId, int timestamp)
	{
		inviteDao.updateSysSubscribeState(inviteId, toAccountId, GlobalArgs.SYNC_STATE_READ, timestamp);
	}

	@Override
	public void confirmReadInvite(String inviteId, short loginType, String loginName, int timestamp)
	{
		inviteDao.updateExternalSubscribeState(inviteId, loginType, loginName, GlobalArgs.SYNC_STATE_READ, timestamp);
	}

	@Override
	public List<GaInviteMasterInfo> queryNotReceivedInvite(String toAccountId)
	{
		return inviteDao.queryNotReceivedInvite(toAccountId);
	}

	@Override
	public List<GaInviteMasterInfo> queryNotReceivedInvite(short loginType, String loginName)
	{
		return inviteDao.queryNotReceivedInvite(loginType, loginName);
	}

	// ///////////////////////////////////////////////////////////////
	// feedback

	@Override
	public void feedback(GaFeedbackBaseInfo feedback, int timestamp)
	{
		if (feedbackDao.isExist(feedback.getInviteId(), feedback.getFeedbackAccountId(), feedback.getInviteAccountId())) {
		} else {
			feedbackDao.add(feedback.getInviteId(), feedback.getFeedbackAccountId(), feedback.getInviteAccountId(),
					feedback.getFeedbackState(), timestamp);
		}
	}

	@Override
	public void confirmReceivedFeedback(String inviteId, String feedbackAccountId, String inviteAccountId, int timestamp)
	{
		// feedbackDao.updateSyncState(inviteId, feedbackAccountId,
		// inviteAccountId, GlobalArgs.SYNC_STATE_RECEIVED,
		// timestamp);
		// 临时解决方案,client未传回feedbackAccountId,整体update
		feedbackDao.updateSyncState(inviteId, inviteAccountId, GlobalArgs.SYNC_STATE_RECEIVED, timestamp);
	}

	@Override
	public List<GaFeedbackMasterInfo> queryNotReceivedFeedback(String toAccountId)
	{
		return feedbackDao.queryNotReceived(toAccountId);
	}

	// ///////////////////////////////////////////////////////////////////

	private GaInviteDao inviteDao;
	private GaInviteFeedbackDao feedbackDao;

	public GaInviteDao getInviteDao()
	{
		return inviteDao;
	}

	public void setInviteDao(GaInviteDao inviteDao)
	{
		this.inviteDao = inviteDao;
	}

	public GaInviteFeedbackDao getFeedbackDao()
	{
		return feedbackDao;
	}

	public void setFeedbackDao(GaInviteFeedbackDao feedbackDao)
	{
		this.feedbackDao = feedbackDao;
	}

}
