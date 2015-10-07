package com.oct.ga.activity.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oct.ga.activity.dao.ActivityDao;
import com.oct.ga.activity.dao.ActivityMemberDao;
import com.oct.ga.activity.dao.FriendActivityDao;
import com.oct.ga.activity.dao.RecommendedActDao;
import com.oct.ga.activity.domain.Activity;
import com.oct.ga.activity.domain.ActivityMember;
import com.oct.ga.activity.domain.FriendActivity;
import com.oct.ga.activity.domain.RecommendedAct;
import com.oct.ga.activity.service.ActivityService;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityMemberDao activityMemberDao;
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private FriendActivityDao friendActivityDao;
	@Autowired
	private RecommendedActDao recommendedActDao;

	@Transactional(readOnly = true)
	@Override
	public List<Activity> findJoinedInActivities(String accountId, long beginTime, boolean prev, int pageSize) {
		List<Activity> activities = new ArrayList<>();
		List<ActivityMember> activityMembers = activityMemberDao.findByAccountId(accountId, beginTime, prev, pageSize);
		for (ActivityMember activityMember : activityMembers) {
			Activity activity = activityDao.read(activityMember.getActivityId());
			if (activityMember.isLeader()) {
				activity.setLeader(activityMember);
			} else {
				ActivityMember leader = activityMemberDao.getLeader(activity.getId());
				activity.setLeader(leader);
			}
			activities.add(activity);
		}
		return activities;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Activity> findFriendActivities(String accountId, long beginTime, boolean prev, int pageSize) {
		List<Activity> activities = new ArrayList<>();
		List<FriendActivity> friendActivities = friendActivityDao.findByAccountId(accountId, beginTime, prev, pageSize);
		for (FriendActivity friendActivity : friendActivities) {
			Activity activity = activityDao.read(friendActivity.getActivityId());
			ActivityMember leader = activityMemberDao.getLeader(activity.getId());
			activity.setLeader(leader);
			activities.add(activity);
		}
		return activities;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Activity> findRecommendedActivities(String accountId, long beginTime, boolean prev, int pageSize) {
		List<Activity> activities = new ArrayList<>();
		List<RecommendedAct> recommendedActs = recommendedActDao.find(accountId, beginTime, prev, pageSize);
		for (RecommendedAct recommendedAct : recommendedActs) {
			Activity activity = activityDao.read(recommendedAct.getActivityId());
			ActivityMember leader = activityMemberDao.getLeader(activity.getId());
			activity.setLeader(leader);
			activities.add(activity);
		}
		return activities;
	}

	@Transactional(readOnly = true)
	@Override
	public Activity read(String id) {
		Activity activity = activityDao.read(id);
		if (activity != null) {
			ActivityMember leader = activityMemberDao.getLeader(id);
			activity.setLeader(leader);
		}
		return activity;
	}

	@Override
	public void create(Activity activity) {
		activityDao.create(activity);
	}

	@Override
	public void update(Activity activity) {
		activityDao.update(activity);
	}

}
