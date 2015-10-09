package com.oct.ga.activity.service;

import java.util.List;

import com.oct.ga.activity.domain.Itinerary;

public interface ItineraryService {

	List<Itinerary> findByActivityId(String activityId, int idx, int pageSize);

	void overwriteByActivityId(String activityId, List<Itinerary> itineraries);
}
