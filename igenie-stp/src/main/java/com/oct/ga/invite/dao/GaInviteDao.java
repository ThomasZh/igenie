package com.oct.ga.invite.dao;

import java.util.List;

import com.oct.ga.invite.domain.GaInviteMasterInfo;

public interface GaInviteDao
{
	// ///////////////////////////////////////////////////////////////
	// original

	/**
	 * add an original invite
	 */
	public void addOriginal(String inviteId, short inviteType, String fromAccountId, String channelId, int expiryTime,
			int timestamp);

	/**
	 * modify expire time
	 */
	public void updateExpiryTime(String inviteId, int expiryTime, int timestamp);

	/**
	 * get invite(id,type,fromAccountId,channelId,expire,lastUpdateTime)
	 */
	public GaInviteMasterInfo queryMaster(String inviteId);

	// ///////////////////////////////////////////////////////////////
	// ga system account subscribe

	/**
	 * add subscribe into subscribe_sys table, default sync_state=not_received
	 */
	public void addSysSubscribe(String inviteId, String toAccontId, int timestamp);

	public boolean isExistSysSubscribe(String inviteId, String toAccountId);
	
	/**
	 * modify sync_state in subscribe_sys table
	 */
	public void updateSysSubscribeState(String inviteId, String toAccontId, short syncState, int timestamp);

	/**
	 * get invite that from an account invite and by another account subscribed
	 */
	public GaInviteMasterInfo querySysSubscribe(short inviteType, String fromAccountId, String toAccontId);

	/**
	 * get account's subscribed invite list not received.
	 */
	public List<GaInviteMasterInfo> queryNotReceivedInvite(String toAccountId);

	// ///////////////////////////////////////////////////////////////
	// external system user subscribe

	/**
	 * add subscribe into subscribe_external table, default
	 * sync_state=not_received
	 */
	public void addExternalSubscribe(String inviteId, short toLoginType, String toLoginName, int timestamp);

	/**
	 * modify sync_state in subscribe_external table
	 */
	public void updateExternalSubscribeState(String inviteId, short toLoginType, String toLoginName, short syncState,
			int timestamp);

	/**
	 * get invite that from an account invite and by another user(not register)
	 * subscribed
	 */
	public GaInviteMasterInfo queryExternalSubscribe(short inviteType, String fromAccountId, short toLoginType,
			String toLoginName);

	public List<String> queryExternalSubscribeIds(String inviteId);
	
	/**
	 * get user(loginName)'s subscribed invite list not received.
	 */
	public List<GaInviteMasterInfo> queryNotReceivedInvite(short loginType, String loginName);

}
