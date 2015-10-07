package com.oct.ga.activity.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oct.ga.activity.dao.ActivityMemberDao;
import com.oct.ga.activity.domain.ActivityMember;
import com.oct.ga.activity.service.ActivityMemberService;

@Service
@Transactional
public class ActivityMemberServiceImpl implements ActivityMemberService {

	@Autowired
	private ActivityMemberDao activityMemberDao;

	@Transactional(readOnly = true)
	@Override
	public List<ActivityMember> findByActivityId(String activityId, long createTime, int pageSize) {
		return activityMemberDao.findByActivityId(activityId, createTime, pageSize);
	}

}
