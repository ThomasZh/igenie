package com.oct.ga.activity.dao;

import java.util.List;

import com.oct.ga.activity.domain.ActivityMember;

public interface ActivityMemberDao {
	void create(ActivityMember activityMember);

	boolean delete(String activityId, String accountId);

	List<ActivityMember> findByAccountId(String accountId, long beginTime, boolean prev, int pageSize);

	List<ActivityMember> findByActivityId(String activityId, long createTime, int pageSize);

	int countActivityMembersByActivityId(String activityId);
}
