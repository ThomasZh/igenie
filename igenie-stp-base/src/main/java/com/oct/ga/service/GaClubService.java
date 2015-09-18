package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ClubBaseInfo;
import com.oct.ga.comm.domain.club.ClubDetailInfo;
import com.oct.ga.comm.domain.club.ClubMasterInfo;

public interface GaClubService
{
	public List<ClubBaseInfo> queryNameListByUserId(String userId);

	public String create(ClubMasterInfo club, int timestamp);

	public void update(ClubMasterInfo club, int timestamp);

	/**
	 * Join a club
	 * 
	 * @param clubId
	 * @param accountId
	 */
	public void join(String clubId, String userId, short state, int timestamp);

	public ClubDetailInfo queryDetail(String clubId);

	// ///////////////////////////////////////////////////////////

	public void addSubscriber(String clubId, String userId, short state, int timestamp);

	/**
	 * include quit/kickout
	 * 
	 * @param clubId
	 * @param userId
	 * @return
	 */
	public boolean isExistSubscriber(String clubId, String userId);

	/**
	 * include quit, not include kickout
	 * 
	 * @param clubId
	 * @param userId
	 * @return
	 */
	public boolean isSubscriber(String clubId, String userId);

	public int querySubscriberNum(String clubId);

	public void updateSubscriberNum(String clubId, int num, int timestamp);

	public List<AccountBasic> querySubscribers(String clubId)
			throws SupSocketException;

	public List<String> querySubscriberIds(String clubId);

	/**
	 * 
	 * @param clubId
	 * @param updateSubscribers
	 *            : full member lists
	 * @param timestamp
	 */
	public void updateSubscribers(String clubId, String[] updateSubscribers, int timestamp);

	public void kickoutSubscriber(String clubId, String userId, int timestamp);

	public void quitSubscriber(String clubId, String userId, int timestamp);

	public void updateSubscriberState(String clubId, String userId, short state, int timestamp);

}
