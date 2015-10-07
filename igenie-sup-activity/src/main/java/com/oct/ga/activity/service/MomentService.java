package com.oct.ga.activity.service;

import java.util.List;

import com.oct.ga.activity.domain.Moment;

public interface MomentService {
	List<Moment> findByActivityId(String activityId, long createTime, int pageSize);

	void create(Moment moment);

	void delete(String id);
}
