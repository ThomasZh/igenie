package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.account.AccountDetail;

public interface GaFollowingService
{
	public void follow(String myUserId, String friendUserId, int timestamp);

	public void unfollow(String myUserId, String friendUserId, int timestamp);

	/**
	 * my follow
	 */
	public List<String> queryFollowing(String userId);

	public List<String> queryFollowingLastUpdateIds(String accountId, int lastTryTime);

	public List<AccountDetail> queryFollowingLastUpdate(String userId, int lastTryTime);

	/**
	 * follow me
	 */
	public List<String> queryFollowed(String userId);

	/**
	 * my unfollow list
	 */
	public List<String> queryBlackList(String userId);

	/**
	 * include following & unfollow
	 */
	public boolean isExist(String myUserId, String friendUserId);

	public void updateMyLastUpdateTimeInFollowed(String userId, int timestamp);

}
