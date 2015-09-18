package com.oct.ga.service;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.SupSocketException;
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

public interface GaActivityService
{
	// ///////////////////////////////////////////////////////////

	public List<ActivitySubscribeInfo> querySubscribePagination(String userId, int endTime, int pageNum, int pageSize);

	public Page<ActivitySubscribeInfo> queryFuturePagination(int endTime, int pageNum, int pageSize);

	public List<ActivitySubscribeInfo> queryFutureFilterByLocPagination(String locMask, int endTime, int pageNum,
			int pageSize);

	public List<ActivitySubscribeInfo> querySubscribePagination(String userId, int startTime, int endTime, int pageNum,
			int pageSize);

	public List<ActivitySubscribeDetailInfo> querySubscribeOrderByCreateTimePagination(String userId, int endTime,
			int pageNum, int pageSize);

	public List<ActivitySubscribeInfo> queryHistoryOrderByLastUpdateTimePagination(String userId, int endTime,
			int pageNum, int pageSize);

	public List<ActivityMasterInfo> querySubscribe(String userId, int startTime, int endTime);

	public List<AccountBasic> querySubscribers(String activityId);

	public void addSubscribe(String clubId, String activityId, String userId, short syncState, int timestamp);

	public void updateSubscribe(String clubId, String activityId, String userId, short syncState, int timestamp);

	public ActivityDetailInfo query(String activityId, String userId);

	public ActivitySubscribeInfo queryActivitySubscribeInfo(String activityId);

	public Page<ActivityMasterInfo> queryHistoryPagination(String clubId, int pageNum, int pageSize);

	public List<ActivitySubscribeInfo> queryHistoryPagination(String accountId, int timestamp, int pageNum, int pageSize);

	public List<ActivitySubscribeInfo> queryFuturePagination(String accountId, int timestamp, int pageNum, int pageSize);

	public List<ActivitySubscribeInfo> queryCreateHistoryPagination(String accountId, int timestamp, int pageNum,
			int pageSize);

	public List<ActivitySubscribeInfo> queryCreateFuturePagination(String accountId, int timestamp, int pageNum,
			int pageSize);

	public Page<ActivityExtendInfo> queryImagesPagination(String clubId, int pageNum, int pageSize);

	public List<ActivityNameListInfo> queryMyList(String userId);

	// ///////////////////////////////////////////////////////////

	public String create(ActivityCreateInfo activity, String myAccountId, int timestamp);

	public void update(ActivityUpdateInfo activity, String myAccountId, int timestamp);

	public void cancel(String activityId, String myAccountId, int timestamp) throws SupSocketException;

	public boolean isExistSubscribe(String activityId, String userId);

	public void join(String activityId, String userId, int timestamp);

	public void kickoutSubscriber(String activityId, String userId, int timestamp);

	// ///////////////////////////////////////////////////////////

	public void create(ActivityRecommend recommend);

	public int queryRecommendNum(String activityId, String toUserId);

	public int countTotalJoinNum(String clubId);

	// ///////////////////////////////////////////////////////////

	public short queryApproveType(String activityId);

	public short queryApplyFormType(String activityId);

	// ///////////////////////////////////////////////////////////

	// input your nickname & avatar photo
	public String createExerciseTask1(ApplicationContext context, String accountId, int timestamp);

	// invite a friend
	public String createExerciseTask2(ApplicationContext context, String accountId, int timestamp);

	// create a project
	public String createExerciseTask3(ApplicationContext context, String accountId, int timestamp);

}
