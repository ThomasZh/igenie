package com.oct.ga.club.service;

import java.util.List;
import java.util.UUID;

import com.oct.ga.club.dao.ActivityDao;
import com.oct.ga.club.dao.ClubDao;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ActivityNameListInfo;
import com.oct.ga.comm.domain.club.ClubBaseInfo;
import com.oct.ga.comm.domain.club.ClubDetailInfo;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.oct.ga.service.GaClubService;
import com.redoct.ga.sup.account.SupAccountService;

public class ClubServiceImpl
		implements GaClubService
{
	@Override
	public List<ClubBaseInfo> queryNameListByUserId(String userId)
	{
		return clubDao.queryNameList(userId);
	}

	@Override
	public String create(ClubMasterInfo club, int timestamp)
	{
		club.setId(UUID.randomUUID().toString());
		clubDao.add(club, timestamp);

		return club.getId();
	}

	@Override
	public void update(ClubMasterInfo club, int timestamp)
	{
		clubDao.update(club, timestamp);
	}

	/**
	 * Join a club
	 * 
	 * @param clubId
	 * @param accountId
	 */
	@Override
	public void join(String clubId, String userId, short state, int timestamp)
	{
		if (clubDao.isSubscriber(clubId, userId)) {
			clubDao.updateSubscriberState(clubId, userId, state, timestamp);
		} else {
			clubDao.addSubscriber(clubId, userId, state, timestamp);
		}
	}

	@Override
	public ClubDetailInfo queryDetail(String clubId)
	{
		return clubDao.queryDetail(clubId);
	}

	// ///////////////////////////////////////////////////////////

	@Override
	public void addSubscriber(String clubId, String userId, short state, int timestamp)
	{
		if (clubDao.isExistSubscriber(clubId, userId)) {
			if (!clubDao.isSubscriber(clubId, userId))
				clubDao.updateSubscriberState(clubId, userId, GlobalArgs.INVITE_STATE_APPLY, timestamp);
		} else {
			clubDao.addSubscriber(clubId, userId, GlobalArgs.INVITE_STATE_APPLY, timestamp);
		}
	}

	/**
	 * include quit/kickout
	 * 
	 * @param clubId
	 * @param userId
	 * @return
	 */
	@Override
	public boolean isExistSubscriber(String clubId, String userId)
	{
		return clubDao.isExistSubscriber(clubId, userId);
	}

	/**
	 * include quit, not include kickout
	 * 
	 * @param clubId
	 * @param userId
	 * @return
	 */
	@Override
	public boolean isSubscriber(String clubId, String userId)
	{
		return clubDao.isSubscriber(clubId, userId);
	}

	@Override
	public int querySubscriberNum(String clubId)
	{
		return clubDao.querySubscriberNum(clubId);
	}

	@Override
	public void updateSubscriberNum(String clubId, int num, int timestamp)
	{
		clubDao.updateSubscriberNum(clubId, num, timestamp);
	}

	@Override
	public List<AccountBasic> querySubscribers(String clubId)
			throws SupSocketException
	{
		List<AccountBasic> subscribers = clubDao.querySubscribers(clubId);
		for (AccountBasic subscriber : subscribers) {
			AccountBasic account = accountService.queryAccount(subscriber.getAccountId());

			subscriber.setNickname(account.getNickname());
			subscriber.setAvatarUrl(account.getAvatarUrl());
		}
		return subscribers;
	}

	@Override
	public List<String> querySubscriberIds(String clubId)
	{
		return clubDao.querySubscriberIds(clubId);
	}

	@Override
	public void updateSubscribers(String clubId, String[] updateSubscribers, int timestamp)
	{
		int today = timestamp - timestamp % (86400);// whole days
		List<AccountBasic> oldSubscribers = clubDao.querySubscribers(clubId);

		// Logic: find new userId, add
		for (int i = 0; i < updateSubscribers.length; i++) {
			String updateSubscriberId = updateSubscribers[i];
			int count = 0;

			for (int j = 0; j < oldSubscribers.size(); j++) {
				AccountBasic oldSubscriber = oldSubscribers.get(j);

				if (updateSubscriberId.equals(oldSubscriber.getAccountId())) {
					count++;
					break;
				}
			}

			if (count == 0) { // not exist
				this.addSubscriber(clubId, updateSubscriberId, GlobalArgs.INVITE_STATE_APPLY, timestamp);

				// Logic: add exist activity(not completed) to subscribe list
				List<ActivityNameListInfo> activityNameList = activityDao.queryNotCompletedNameList(clubId, today);
				for (ActivityNameListInfo activityNameInfo : activityNameList) {
					String activityId = activityNameInfo.getId();

					if (!activityDao.isExistSubscribe(activityId, updateSubscriberId)) {
						activityDao.addSubscribe(clubId, activityId, updateSubscriberId,
								GlobalArgs.SYNC_STATE_NOT_RECEIVED, timestamp);
					}
				}
			}
		}

		ClubDetailInfo clubExist = this.queryDetail(clubId);

		// Logic: find lost userId, delete
		for (int i = 0; i < oldSubscribers.size(); i++) {
			AccountBasic oldSubscriber = oldSubscribers.get(i);
			int count = 0;

			for (int j = 0; j < updateSubscribers.length; j++) {
				String updateSubscriberId = updateSubscribers[j];

				if (updateSubscriberId.equals(oldSubscriber.getAccountId())) {
					count++;
					break;
				}
			}

			if (count == 0) { // not exist
				if (!oldSubscriber.getAccountId().equals(clubExist.getCreatorId())) {
					this.kickoutSubscriber(clubId, oldSubscriber.getAccountId(), timestamp);
				}
			}
		}

		int num = clubDao.querySubscriberNum(clubId);
		clubDao.updateSubscriberNum(clubId, num, timestamp);
	}

	@Override
	public void kickoutSubscriber(String clubId, String userId, int timestamp)
	{
		// clubDao.updateSubscriberState(clubId, userId,
		// GlobalArgs.INVITE_STATE_KICKOFF, timestamp);
		clubDao.removeSubscriber(clubId, userId, timestamp);
	}

	@Override
	public void quitSubscriber(String clubId, String userId, int timestamp)
	{
		clubDao.updateSubscriberState(clubId, userId, GlobalArgs.INVITE_STATE_QUIT, timestamp);
	}

	@Override
	public void updateSubscriberState(String clubId, String userId, short state, int timestamp)
	{
		clubDao.updateSubscriberState(clubId, userId, state, timestamp);
	}

	// ///////////////////////////////////////////////////////////

	private ClubDao clubDao;
	private ActivityDao activityDao;
	private SupAccountService accountService;

	public void setClubDao(ClubDao clubDao)
	{
		this.clubDao = clubDao;
	}

	public ActivityDao getActivityDao()
	{
		return activityDao;
	}

	public void setActivityDao(ActivityDao activityDao)
	{
		this.activityDao = activityDao;
	}

	public ClubDao getClubDao()
	{
		return clubDao;
	}

	public SupAccountService getAccountService()
	{
		return accountService;
	}

	public void setAccountService(SupAccountService accountService)
	{
		this.accountService = accountService;
	}

}
