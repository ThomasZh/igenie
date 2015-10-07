package com.oct.ga.activity.service;

import java.util.List;

import com.oct.ga.activity.domain.ActivityMember;

public interface ActivityMemberService {
	List<ActivityMember> findByActivityId(String activityId, long createTime, int pageSize);
}
