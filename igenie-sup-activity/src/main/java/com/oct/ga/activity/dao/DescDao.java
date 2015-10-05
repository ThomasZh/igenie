package com.oct.ga.activity.dao;

import java.util.List;

import com.oct.ga.activity.domain.Desc;

public interface DescDao {
	void create(Desc desc);

	int deleteByActivityId(String activityId);

	List<Desc> findByActivityId(String activityId);
}
