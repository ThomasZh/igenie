package com.oct.ga.service;

import java.util.List;

import com.oct.ga.invite.domain.GaFeedbackBaseInfo;
import com.oct.ga.invite.domain.GaFeedbackMasterInfo;
import com.oct.ga.invite.domain.GaInviteBaseInfo;
import com.oct.ga.invite.domain.GaInviteMasterInfo;

public interface GaInviteService
{
	// ///////////////////////////////////////////////////////////////
	// invite original

	/**
	 * create an original invite(type,fromAccountId,channelId,expire)
	 * 
	 * @return inviteId
	 */
	public String create(GaInviteBaseInfo invite, int timestamp);

	/**
	 * query invite master
	 * info(id,type,fromAccountId,channelId,expire,lastUpdateTime) by inviteId
	 */
	public GaInviteMasterInfo queryMaster(String inviteId);

	/**
	 * modify an invite expire time
	 */
	public void modifyExpiryTime(String inviteId, int expiry, int timestamp);

	// ///////////////////////////////////////////////////////////////
	// invite subscribe

	/**
	 * ga system make account subscribe this invite
	 * 
	 * @param inviteType
	 *            =followMe(147);=joinActivity(141)
	 */
	public void subscribeInvite(String inviteId, String toAccountId, int timestamp);

	/**
	 * ga system make user(loginType,loginName) subscribe this invite
	 * 
	 * @param inviteType
	 *            =register(131,143);
	 */
	public void subscribeInvite(String inviteId, short loginType, String loginName, int timestamp);

	public boolean isExistSubscribeInvite(String inviteId, String toAccountId);

	/**
	 * @return loginName[]
	 */
	public List<String> queryExternalSubscribeIds(String inviteId);

	/**
	 * query the invite from account to other account
	 * 
	 * @param inviteType
	 *            =followMe(147);=joinActivity(141)
	 */
	public GaInviteMasterInfo queryMaster(short inviteType, String fromAccountId, String toAccountId);

	/**
	 * query the invite from account to other user(loginName)
	 * 
	 * @param inviteType
	 *            =register(131,143)
	 */
	public GaInviteMasterInfo queryMaster(short inviteType, String fromAccountId, short toLoginType, String toLoginName);

	/**
	 * change the state of invite to not received
	 */
	public void modifySyncStateToNotReceived(String inviteId, String toUserId, int timestamp);

	/**
	 * change the state of invite to not received
	 */
	public void modifySyncStateToNotReceived(String inviteId, short loginType, String loginName, int timestamp);

	/**
	 * account confirm already received this invite
	 */
	public void confirmReceivedInvite(String inviteId, String toAccountId, int timestamp);

	/**
	 * user(loginType,loginName) confirm already received this invite
	 */
	public void confirmReceivedInvite(String inviteId, short loginType, String loginName, int timestamp);

	public void confirmReadInvite(String inviteId, String toAccountId, int timestamp);

	public void confirmReadInvite(String inviteId, short loginType, String loginName, int timestamp);

	/**
	 * get toAccountId's invites has not received
	 */
	public List<GaInviteMasterInfo> queryNotReceivedInvite(String toAccountId);

	/**
	 * get loginName's invites has not received
	 * 
	 * @param loginType
	 *            =email,phone
	 */
	public List<GaInviteMasterInfo> queryNotReceivedInvite(short loginType, String loginName);

	// ///////////////////////////////////////////////////////////////
	// feedback

	/**
	 * feedback(accept/reject) the invite.
	 * 
	 * @param feedback
	 *            (inviteId,feedbackAccountId,inviteAccountId,feedbackState)
	 */
	public void feedback(GaFeedbackBaseInfo feedback, int timestamp);

	/**
	 * account(send the invite) confirm already received this feedback
	 */
	public void confirmReceivedFeedback(String inviteId, String feedbackAccountId, String inviteAccountId, int timestamp);

	// public void confirmReadFeedback(String inviteId, String toAccountId, int
	// timestamp);

	/**
	 * get account's invite feedbacks(from others) has not received
	 */
	public List<GaFeedbackMasterInfo> queryNotReceivedFeedback(String inviteAccountId);

}
