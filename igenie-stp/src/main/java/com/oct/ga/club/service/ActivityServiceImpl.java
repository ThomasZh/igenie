package com.oct.ga.club.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.club.dao.ActivityDao;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.StringUtil;
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
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaClubService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.ApplicationContextUtil;
import com.oct.ga.stp.GlobalConfigurationVariables;

public class ActivityServiceImpl
		implements GaActivityService
{
	// ///////////////////////////////////////////////////////////

	@Override
	public Page<ActivitySubscribeInfo> queryFuturePagination(int endTime, int pageNum, int pageSize)
	{
		return activityDao.queryFuturePagination(endTime, pageNum, pageSize);
	}

	@Override
	public List<ActivitySubscribeInfo> querySubscribePagination(String userId, int endTime, int pageNum, int pageSize)
	{
		Page<ActivitySubscribeInfo> avtivities = activityDao.querySubscribePagination(userId, endTime, pageNum,
				pageSize);
		List<ActivitySubscribeInfo> array = avtivities.getPageItems();

		return array;
	}

	@Override
	public List<ActivitySubscribeInfo> querySubscribePagination(String userId, int startTime, int endTime, int pageNum,
			int pageSize)
	{
		Page<ActivitySubscribeInfo> avtivities = activityDao.querySubscribePagination(userId, startTime, endTime,
				pageNum, pageSize);
		List<ActivitySubscribeInfo> array = avtivities.getPageItems();

		return array;
	}

	@Override
	public List<ActivitySubscribeInfo> queryFutureFilterByLocPagination(String locMask, int endTime, int pageNum,
			int pageSize)
	{
		Page<ActivitySubscribeInfo> avtivities = activityDao.queryFutureFilterByLocPagination(locMask, endTime,
				pageNum, pageSize);
		List<ActivitySubscribeInfo> array = avtivities.getPageItems();

		return array;
	}

	@Override
	public List<ActivitySubscribeDetailInfo> querySubscribeOrderByCreateTimePagination(String userId, int endTime,
			int pageNum, int pageSize)
	{
		Page<ActivitySubscribeDetailInfo> avtivities = activityDao.querySubscribeOrderByCreateTimePagination(userId,
				endTime, pageNum, pageSize);
		List<ActivitySubscribeDetailInfo> array = avtivities.getPageItems();

		return array;
	}

	@Override
	public List<ActivitySubscribeInfo> queryHistoryOrderByLastUpdateTimePagination(String userId, int endTime,
			int pageNum, int pageSize)
	{
		Page<ActivitySubscribeInfo> avtivities = activityDao.queryHistoryOrderByLastUpdateTimePagination(userId,
				endTime, pageNum, pageSize);
		List<ActivitySubscribeInfo> array = avtivities.getPageItems();

		return array;
	}

	@Override
	public List<ActivityMasterInfo> querySubscribe(String userId, int startTime, int endTime)
	{
		return activityDao.querySubscribe(userId, startTime, endTime);
	}

	@Override
	public List<AccountBasic> querySubscribers(String activityId)
	{
		return activityDao.querySubscribers(activityId);
	}

	@Override
	public void addSubscribe(String clubId, String activityId, String userId, short syncState, int timestamp)
	{
		activityDao.addSubscribe(clubId, activityId, userId, syncState, timestamp);
	}

	@Override
	public void updateSubscribe(String clubId, String activityId, String userId, short syncState, int timestamp)
	{
		activityDao.updateSubscribeState(clubId, activityId, userId, syncState, timestamp);
	}

	@Override
	public ActivityDetailInfo query(String activityId, String userId)
	{
		ActivityDetailInfo detail = activityDao.queryDetailInfo(activityId);
		short applyFormType = activityDao.queryApplyFormType(activityId);
		short approveType = activityDao.queryApproveType(activityId);
		detail.setApproveType(approveType);
		detail.setApplyFormType(applyFormType);

		List<ActivityRecommend> recommends = activityDao.queryRecommends(activityId, userId);
		detail.setRecommends(recommends);

		return detail;
	}

	@Override
	public ActivitySubscribeInfo queryActivitySubscribeInfo(String activityId)
	{
		ActivityDetailInfo detail = activityDao.queryDetailInfo(activityId);

		ActivitySubscribeInfo activity = new ActivitySubscribeInfo();
		activity.setId(activityId);
		activity.setStartTime(detail.getStartTime());
		activity.setState(detail.getState());
		activity.setPublishType(detail.getPublishType());
		activity.setLocDesc(detail.getLocDesc());
		activity.setLocX(detail.getLocX());
		activity.setLocY(detail.getLocY());

		return activity;
	}

	@Override
	public Page<ActivityMasterInfo> queryHistoryPagination(String clubId, int pageNum, int pageSize)
	{
		return activityDao.queryHistoryPagination(clubId, pageNum, pageSize);
	}

	@Override
	public Page<ActivityExtendInfo> queryImagesPagination(String clubId, int pageNum, int pageSize)
	{
		return activityDao.queryImagesPagination(clubId, pageNum, pageSize);
	}

	@Override
	public List<ActivityNameListInfo> queryMyList(String userId)
	{
		return activityDao.queryMyList(userId);
	}

	// ///////////////////////////////////////////////////////////

	@Override
	public String create(ActivityCreateInfo activity, String myAccountId, int timestamp)
	{
		String id = UUID.randomUUID().toString();
		String locMask = StringUtil.locMask(activity.getLocX(), activity.getLocY());
		activityDao.add(id, activity, locMask, myAccountId, timestamp);

		if (activity.getApproveType() == GlobalArgs.TRUE) {
			activityDao.updateApproveType(id, GlobalArgs.TRUE, timestamp);
		}

		if (activity.getApplyFormType() == GlobalArgs.TRUE) {
			activityDao.updateApplyFormType(id, GlobalArgs.TRUE, timestamp);
		}

		return id;
	}

	@Override
	public void update(ActivityUpdateInfo activity, String myAccountId, int timestamp)
	{
		String locMask = StringUtil.locMask(activity.getLocX(), activity.getLocY());
		activityDao.update(activity, locMask, timestamp);
		activityDao.updateApproveType(activity.getId(), activity.getApproveType(), timestamp);
	}

	@Override
	public void cancel(String activityId, String myAccountId, int timestamp) throws SupSocketException
	{
		ActivityDetailInfo activity = activityDao.queryDetailInfo(activityId);

		// Logic: send notify to club member
		List<GroupMemberDetailInfo> members = groupService.queryMembers(activity.getPid());
		for (GroupMemberDetailInfo user : members) {
			String userId = user.getAccountId();
			logger.debug("member user id:" + userId);
			if (this.isExistSubscribe(activity.getId(), userId)) {
				if (userId.equals(myAccountId)) {
					this.updateSubscribe(activity.getPid(), activity.getId(), userId, GlobalArgs.SYNC_STATE_READ,
							timestamp);
				} else {
					this.updateSubscribe(activity.getPid(), activity.getId(), userId,
							GlobalArgs.SYNC_STATE_NOT_RECEIVED, timestamp);
				}
			} else {
				if (userId.equals(myAccountId)) {
					this.addSubscribe(activity.getPid(), activity.getId(), userId, GlobalArgs.SYNC_STATE_READ,
							timestamp);
				} else {
					this.addSubscribe(activity.getPid(), activity.getId(), userId, GlobalArgs.SYNC_STATE_NOT_RECEIVED,
							timestamp);
				}
			}
		}
	}

	@Override
	public boolean isExistSubscribe(String activityId, String userId)
	{
		return activityDao.isExistSubscribe(activityId, userId);
	}

	@Override
	public void kickoutSubscriber(String activityId, String userId, int timestamp)
	{
		activityDao.deleteSubscriber(activityId, userId);
	}

	@Override
	public void join(String activityId, String userId, int timestamp)
	{
		ActivityDetailInfo activity = activityDao.queryDetailInfo(activityId);
		String clubId = activity.getPid();
		// not join club, now join
		if (!clubService.isSubscriber(clubId, userId)) {
			clubService.join(clubId, userId, GlobalArgs.INVITE_STATE_ACCPET, timestamp);
		}

		int num = clubService.querySubscriberNum(clubId);
		clubService.updateSubscriberNum(clubId, num, timestamp);
	}

	// ///////////////////////////////////////////////////////////

	@Override
	public void create(ActivityRecommend recommend)
	{
		if (recommend.getToUserIds() != null) {
			ActivityDetailInfo activity = activityDao.queryDetailInfo(recommend.getActivityId());

			for (String userId : recommend.getToUserIds()) {
				logger.debug("user_id: " + userId);
				recommend.setToUserId(userId);

				if (activityDao.isExistRecommend(recommend.getActivityId(), recommend.getFromUserId(),
						recommend.getToUserId())) {
					activityDao.update(recommend);
				} else {
					activityDao.add(recommend);
				}

				if (!activityDao.isExistSubscribe(recommend.getActivityId(), recommend.getToUserId()))
					this.addSubscribe(activity.getPid(), activity.getId(), userId, GlobalArgs.SYNC_STATE_NOT_RECEIVED,
							recommend.getTimestamp());
			}
		}
	}

	@Override
	public int queryRecommendNum(String activityId, String toUserId)
	{
		return activityDao.queryRecommendNum(activityId, toUserId);
	}

	@Override
	public int countTotalJoinNum(String clubId)
	{
		return activityDao.countTotalJoinNum(clubId);
	}

	@Override
	public List<ActivitySubscribeInfo> queryHistoryPagination(String accountId, int timestamp, int pageNum, int pageSize)
	{
		Page<ActivitySubscribeInfo> avtivities = activityDao.queryHistoryPagination(accountId, timestamp, pageNum,
				pageSize);
		List<ActivitySubscribeInfo> array = avtivities.getPageItems();
		return array;
	}

	@Override
	public List<ActivitySubscribeInfo> queryFuturePagination(String accountId, int timestamp, int pageNum, int pageSize)
	{
		Page<ActivitySubscribeInfo> avtivities = activityDao.queryFuturePagination(accountId, timestamp, pageNum,
				pageSize);
		List<ActivitySubscribeInfo> array = avtivities.getPageItems();
		return array;
	}

	@Override
	public List<ActivitySubscribeInfo> queryCreateHistoryPagination(String accountId, int timestamp, int pageNum,
			int pageSize)
	{
		Page<ActivitySubscribeInfo> avtivities = activityDao.queryCreateHistoryPagination(accountId, timestamp,
				pageNum, pageSize);
		List<ActivitySubscribeInfo> array = avtivities.getPageItems();
		return array;
	}

	@Override
	public List<ActivitySubscribeInfo> queryCreateFuturePagination(String accountId, int timestamp, int pageNum,
			int pageSize)
	{
		Page<ActivitySubscribeInfo> avtivities = activityDao.queryCreateFuturePagination(accountId, timestamp, pageNum,
				pageSize);
		List<ActivitySubscribeInfo> array = avtivities.getPageItems();
		return array;
	}

	// ///////////////////////////////////////////////////////////

	@Override
	public short queryApproveType(String activityId)
	{
		return activityDao.queryApproveType(activityId);
	}

	@Override
	public short queryApplyFormType(String activityId)
	{
		return activityDao.queryApplyFormType(activityId);
	}

	// ///////////////////////////////////////////////////////////

	private ActivityDao activityDao;
	private GaClubService clubService;
	private GaGroupService groupService;

	public ActivityDao getActivityDao()
	{
		return activityDao;
	}

	public void setActivityDao(ActivityDao activityDao)
	{
		this.activityDao = activityDao;
	}

	public GaClubService getClubService()
	{
		return clubService;
	}

	public void setClubService(GaClubService clubService)
	{
		this.clubService = clubService;
	}

	public GaGroupService getGroupService()
	{
		return groupService;
	}

	public void setGroupService(GaGroupService groupService)
	{
		this.groupService = groupService;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

	// ///////////////////////////////////////////////////////////

	@Override
	public String createExerciseTask1(ApplicationContext context, String accountId, int timestamp)
	{
		GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
		GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
		GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

		GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext().getBean(
				"globalConfigurationVariables");
		
		ActivityCreateInfo activity = new ActivityCreateInfo();
//		activity.setName("Your first task for exercise");
		activity.setDesc("Please input your nickname & upload avatar image.");
//		activity.setName("第一个任务：修改头像和昵称");
		activity.setName(gcv.getDefaultProjectName1st());
		activity.setStartTime(timestamp);
		activity.setEndTime(timestamp + 3600); // 1 hour
		activity.setPublishType(GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_1);
		String activityId = activityService.create(activity, accountId, timestamp);

		groupService
				.createGroup(activityId, activity.getName(), GlobalArgs.CHANNEL_TYPE_ACTIVITY, timestamp, accountId);
		syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, timestamp, accountId,
				GlobalArgs.TASK_ACTION_ADD);

		groupService.joinAsLeader(activityId, accountId, timestamp);
		syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, timestamp, accountId,
				GlobalArgs.TASK_ACTION_ADD);

		// Logic: add myself to subscribe
		activityService.addSubscribe(activityId, activityId, accountId, GlobalArgs.SYNC_STATE_READ, timestamp);

		GaTaskLog log = new GaTaskLog();
		log.setLogId(UUID.randomUUID().toString());
		log.setChannelId(activityId);
		log.setFromAccountId(accountId);
		log.setActionTag(GlobalArgs.TASK_ACTION_ADD);
		log.setToActionId(activityId);
		taskService.addLog(log, timestamp);

		taskService.addLogExtend(log.getLogId(), accountId, activityId, GlobalArgs.TASK_ACTION_ADD,
				GlobalArgs.SYNC_STATE_READ, timestamp);

		return activityId;
	}

	@Override
	public String createExerciseTask2(ApplicationContext context, String accountId, int timestamp)
	{
		GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
		GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
		GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

		GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext().getBean(
				"globalConfigurationVariables");
		
		ActivityCreateInfo activity = new ActivityCreateInfo();
//		activity.setName("Your second task for exercise");
//		activity.setName("第二个任务：邀请一位朋友加入A计划");
		activity.setName(gcv.getDefaultProjectName2nd());
		activity.setDesc("Please invite a friend of your to join & follow you.");
		activity.setStartTime(timestamp);
		activity.setEndTime(timestamp + 3600); // 1 hour
		activity.setPublishType(GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_2);
		String activityId = activityService.create(activity, accountId, timestamp);

		groupService
				.createGroup(activityId, activity.getName(), GlobalArgs.CHANNEL_TYPE_ACTIVITY, timestamp, accountId);
		syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, timestamp, accountId,
				GlobalArgs.TASK_ACTION_ADD);

		groupService.joinAsLeader(activityId, accountId, timestamp);
		syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, timestamp, accountId,
				GlobalArgs.TASK_ACTION_ADD);

		// Logic: add myself to subscribe
		activityService.addSubscribe(activityId, activityId, accountId, GlobalArgs.SYNC_STATE_READ, timestamp);

		GaTaskLog log = new GaTaskLog();
		log.setLogId(UUID.randomUUID().toString());
		log.setChannelId(activityId);
		log.setFromAccountId(accountId);
		log.setActionTag(GlobalArgs.TASK_ACTION_ADD);
		log.setToActionId(activityId);
		taskService.addLog(log, timestamp);

		taskService.addLogExtend(log.getLogId(), accountId, activityId, GlobalArgs.TASK_ACTION_ADD,
				GlobalArgs.SYNC_STATE_READ, timestamp);

		return activityId;
	}

	@Override
	public String createExerciseTask3(ApplicationContext context, String accountId, int timestamp)
	{
		GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
		GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
		GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

		GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext().getBean(
				"globalConfigurationVariables");
		
		ActivityCreateInfo activity = new ActivityCreateInfo();
//		activity.setName("Your third task for exercise");
//		activity.setName("第三个任务：创建一个活动");
		activity.setName(gcv.getDefaultProjectName3rd());
		activity.setDesc("Please create a project by yourself.");
		activity.setStartTime(timestamp);
		activity.setEndTime(timestamp + 3600); // 1 hour
		activity.setPublishType(GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_3);
		String activityId = activityService.create(activity, accountId, timestamp);

		groupService
				.createGroup(activityId, activity.getName(), GlobalArgs.CHANNEL_TYPE_ACTIVITY, timestamp, accountId);
		syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, timestamp, accountId,
				GlobalArgs.TASK_ACTION_ADD);

		groupService.joinAsLeader(activityId, accountId, timestamp);
		syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, timestamp, accountId,
				GlobalArgs.TASK_ACTION_ADD);

		// Logic: add myself to subscribe
		activityService.addSubscribe(activityId, activityId, accountId, GlobalArgs.SYNC_STATE_READ, timestamp);

		GaTaskLog log = new GaTaskLog();
		log.setLogId(UUID.randomUUID().toString());
		log.setChannelId(activityId);
		log.setFromAccountId(accountId);
		log.setActionTag(GlobalArgs.TASK_ACTION_ADD);
		log.setToActionId(activityId);
		taskService.addLog(log, timestamp);

		taskService.addLogExtend(log.getLogId(), accountId, activityId, GlobalArgs.TASK_ACTION_ADD,
				GlobalArgs.SYNC_STATE_READ, timestamp);

		return activityId;
	}

}
