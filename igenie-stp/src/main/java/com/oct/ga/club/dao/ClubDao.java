package com.oct.ga.club.dao;

import java.util.List;

import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ClubBaseInfo;
import com.oct.ga.comm.domain.club.ClubDetailInfo;
import com.oct.ga.comm.domain.club.ClubMasterInfo;

public interface ClubDao
{
	public List<ClubBaseInfo> queryNameList(String userId);

	public void add(ClubMasterInfo club, int timestamp);

	public void update(ClubMasterInfo club, int timestamp);

	public ClubDetailInfo queryDetail(String clubId);

	// ///////////////////////////////////////////////////////////

	public void addSubscriber(String clubId, String userId, short state, int timestamp);

	public void removeSubscriber(String clubId, String userId, int timestamp);

	public boolean isExistSubscriber(String clubId, String userId);

	public boolean isSubscriber(String clubId, String userId);

	public int querySubscriberNum(String clubId);

	public void updateSubscriberNum(String clubId, int num, int timestamp);

	public List<AccountBasic> querySubscribers(String clubId);

	public List<String> querySubscriberIds(String clubId);

	public void updateSubscriberState(String clubId, String userId, short state, int timestamp);

	// ///////////////////////////////////////////////////////////

}
