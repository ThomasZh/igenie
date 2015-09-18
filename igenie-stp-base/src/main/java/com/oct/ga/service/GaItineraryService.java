package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.club.ItineraryInfo;

public interface GaItineraryService {

	List<ItineraryInfo> find(String activityId);

	void overWrite(List<ItineraryInfo> itineraryInfos);

	int delete(String activityId);

}
