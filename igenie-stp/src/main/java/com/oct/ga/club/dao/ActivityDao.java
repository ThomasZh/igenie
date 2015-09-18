package com.oct.ga.club.dao;

import java.util.List;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ActivityCreateInfo;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.oct.ga.comm.domain.club.ActivityExtendInfo;
import com.oct.ga.comm.domain.club.ActivityMasterInfo;
import com.oct.ga.comm.domain.club.ActivityNameListInfo;
import com.oct.ga.comm.domain.club.ActivityRecommend;
import com.oct.ga.comm.domain.club.ActivitySubscribeDetailInfo;
import com.oct.ga.comm.domain.club.ActivitySubscribeInfo;
import com.oct.ga.comm.domain.club.ActivityUpdateInfo;

public interface ActivityDao
{
	// ///////////////////////////////////////////////////////////

	public Page<ActivitySubscribeInfo> queryFuturePagination(int endTime, int pageNum, int pageSize);

	public Page<ActivitySubscribeInfo> querySubscribePagination(String userId, int endTime, int pageNum, int pageSize);

	public Page<ActivitySubscribeInfo> querySubscribePagination(String userId, int startTime, int endTime, int pageNum,
			int pageSize);

	public Page<ActivitySubscribeInfo> queryFutureFilterByLocPagination(String locMask, int endTime, int pageNum,
			int pageSize);

	public Page<ActivitySubscribeDetailInfo> querySubscribeOrderByCreateTimePagination(String userId, int endTime,
			int pageNum, int pageSize);

	public Page<ActivitySubscribeInfo> queryHistoryOrderByLastUpdateTimePagination(String userId, int endTime,
			int pageNum, int pageSize);

	public List<ActivityMasterInfo> querySubscribe(String userId, int startTime, int endTime);

	public List<AccountBasic> querySubscribers(String activityId);

	public void addSubscribe(String clubId, String activityId, String userId, short syncState, int timestamp);

	public void updateSubscribeState(String clubId, String activityId, String userId, short syncState, int timestamp);

	public ActivityDetailInfo queryDetailInfo(String activityId);

	public Page<ActivityMasterInfo> queryHistoryPagination(String clubId, int pageNum, int pageSize);

	public Page<ActivitySubscribeInfo> queryFuturePagination(String accountId, int timestamp, int pageNum, int pageSize);

	public Page<ActivitySubscribeInfo> queryHistoryPagination(String accountId, int timestamp, int pageNum, int pageSize);

	public Page<ActivitySubscribeInfo> queryCreateFuturePagination(String accountId, int timestamp, int pageNum,
			int pageSize);

	public Page<ActivitySubscribeInfo> queryCreateHistoryPagination(String accountId, int timestamp, int pageNum,
			int pageSize);

	public Page<ActivityExtendInfo> queryImagesPagination(String clubId, int pageNum, int pageSize);

	public List<ActivityNameListInfo> queryMyList(String userId);

	public List<ActivityNameListInfo> queryNotCompletedNameList(String clubId, int timestamp);

	// ///////////////////////////////////////////////////////////

	public void add(String id, ActivityCreateInfo activity, String locMask, String userId, int timestamp);

	public void updateApproveType(String activityId, short state, int timestamp);

	public void updateApplyFormType(String activityId, short state, int timestamp);

	public short queryApproveType(String activityId);

	public short queryApplyFormType(String activityId);

	public void update(ActivityUpdateInfo activity, String locMask, int timestamp);

	public boolean isExistSubscribe(String activityId, String userId);

	public void deleteSubscriber(String activityId, String userId);

	public List<ActivityDetailInfo> queryByUser(String userId);

	public void add(ActivityRecommend recommend);

	public void update(ActivityRecommend recommend);

	public boolean isExistRecommend(String activityId, String fromUserId, String toUserId);

	public int queryRecommendNum(String activityId, String toUserId);

	public List<ActivityRecommend> queryRecommends(String activityId, String toUserId);

	public int countTotalJoinNum(String clubId);

}
