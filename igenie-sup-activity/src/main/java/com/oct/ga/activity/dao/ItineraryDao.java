package com.oct.ga.activity.dao;

import java.util.List;

import com.oct.ga.activity.domain.Itinerary;

public interface ItineraryDao {

	List<Itinerary> findByActivityId(String activityId, int idx, int pageSize);

	void create(Itinerary itinerary);

	int deleteByActivityId(String activityId);
}
