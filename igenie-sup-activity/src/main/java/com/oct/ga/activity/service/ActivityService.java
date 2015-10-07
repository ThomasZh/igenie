package com.oct.ga.activity.service;

import java.util.List;

import com.oct.ga.activity.domain.Activity;

public interface ActivityService {
	List<Activity> findJoinedInActivities(String accountId, long beginTime, boolean prev, int pageSize);

	List<Activity> findFriendActivities(String accountId, long beginTime, boolean prev, int pageSize);

	List<Activity> findRecommendedActivities(String accountId, long beginTime, boolean prev, int pageSize);

	Activity read(String id);

	void create(Activity activity);

	void update(Activity activity);
}
