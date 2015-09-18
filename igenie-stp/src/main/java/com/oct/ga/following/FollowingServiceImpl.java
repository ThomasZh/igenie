package com.oct.ga.following;

import java.util.List;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.account.AccountDetail;
import com.oct.ga.following.dao.FollowingDao;
import com.oct.ga.service.GaFollowingService;

public class FollowingServiceImpl
		implements GaFollowingService
{
	@Override
	public void follow(String myUserId, String friendUserId, int timestamp)
	{
		if (followingDao.isExist(myUserId, friendUserId)) {
			followingDao.updateState(myUserId, friendUserId, GlobalArgs.USER_FOLLOWING, timestamp);
		} else {
			followingDao.add(myUserId, friendUserId, timestamp);
		}
	}

	@Override
	public void unfollow(String myUserId, String friendUserId, int timestamp)
	{
		followingDao.updateState(myUserId, friendUserId, GlobalArgs.USER_UNFOLLOW, timestamp);
	}

	/**
	 * follow me
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public List<String> queryFollowing(String userId)
	{
		return followingDao.queryFollowing(userId);
	}

	@Override
	public List<String> queryFollowingLastUpdateIds(String accountId, int lastTryTime)
	{
		return followingDao.queryFollowingLastUpdateIds(accountId, lastTryTime);
	}

	@Override
	public List<AccountDetail> queryFollowingLastUpdate(String userId, int lastTryTime)
	{
		return followingDao.queryFollowingLastUpdate(userId, lastTryTime);
	}

	/**
	 * my follow
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public List<String> queryFollowed(String userId)
	{
		return followingDao.queryFollowed(userId);
	}

	@Override
	public List<String> queryBlackList(String userId)
	{
		return followingDao.queryBlackList(userId);
	}

	@Override
	public boolean isExist(String myUserId, String friendUserId)
	{
		return followingDao.isExist(myUserId, friendUserId);
	}

	@Override
	public void updateMyLastUpdateTimeInFollowed(String userId, int timestamp)
	{
		followingDao.updateMyLastUpdateTimeInFollowed(userId, timestamp);
	}

	// ///////////////////////////////////////////////////////////

	private FollowingDao followingDao;

	public FollowingDao getFollowingDao()
	{
		return followingDao;
	}

	public void setFollowingDao(FollowingDao followerDao)
	{
		this.followingDao = followerDao;
	}

}
