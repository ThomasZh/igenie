package com.oct.ga.activity.dao;

import java.util.List;

import com.oct.ga.activity.domain.ActivityDesc;

public interface ActivityDescDao {
	void create(ActivityDesc desc);

	int deleteByActivityId(String activityId);

	List<ActivityDesc> findByActivityId(String activityId);
}
