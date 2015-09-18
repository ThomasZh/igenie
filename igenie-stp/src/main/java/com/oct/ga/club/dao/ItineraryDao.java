package com.oct.ga.club.dao;

import java.util.List;

import com.oct.ga.comm.domain.club.ItineraryInfo;

public interface ItineraryDao {
	List<ItineraryInfo> find(String activityId);

	void create(List<ItineraryInfo> itineraryInfos);

	int delete(String activityId);

}
