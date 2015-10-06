package com.oct.ga.activity.dao;

import java.util.List;

import com.oct.ga.activity.domain.RecommendedAct;

public interface RecommendedActDao {
	void create(RecommendedAct recommendedAct);

	boolean delete(String accountId, String activityId);

	boolean update(String accountId, String activityId, long lastUpdateTime);

	List<RecommendedAct> find(String accountId, long beginTime, long lastUpdateTime, boolean prev, int pageSize);
}
