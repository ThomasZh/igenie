package com.oct.ga.following.dao;

import java.util.List;

import com.oct.ga.comm.domain.account.AccountDetail;

public interface FollowingDao
{
	/**
	 * my follow
	 * 
	 * @param userId
	 * @return
	 */
	public List<String> queryFollowing(String userId);

	public List<String> queryFollowingLastUpdateIds(String accountId, int lastTryTime);

	public List<AccountDetail> queryFollowingLastUpdate(String userId, int lastTryTime);

	/**
	 * follow me
	 * 
	 * @param userId
	 * @return
	 */
	public List<String> queryFollowed(String userId);

	/**
	 * my unfollow
	 * 
	 * @param userId
	 * @return
	 */
	public List<String> queryBlackList(String userId);

	public void add(String myUserId, String friendUserId, int timestamp);

	public void updateState(String myUserId, String friendUserId, short state, int timestamp);

	public void remove(String myUserId, String friendUserId);

	public boolean isExist(String myUserId, String friendUserId);

	public boolean isFollowing(String myUserId, String friendUserId);

	public boolean isUnfollow(String myUserId, String friendUserId);

	public void updateMyLastUpdateTimeInFollowed(String userId, int timestamp);
}
