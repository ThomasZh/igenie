package com.oct.ga.activity.dao;

import java.util.List;

import com.oct.ga.activity.domain.Recommendation;

public interface RecommendationDao {
	void create(Recommendation recommendation);

	boolean delete(String id);

	List<Recommendation> findByActivityId(String activityId);
}
