package com.oct.ga.activity.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oct.ga.activity.dao.ItineraryDao;
import com.oct.ga.activity.domain.Itinerary;
import com.oct.ga.activity.service.ItineraryService;

@Transactional
@Service
public class ItineraryServiceImpl implements ItineraryService {

	@Autowired
	private ItineraryDao itineraryDao;

	@Transactional(readOnly = true)
	@Override
	public List<Itinerary> findByActivityId(String activityId, int idx, int pageSize) {
		return itineraryDao.findByActivityId(activityId, idx, pageSize);
	}

	@Override
	public void overwriteByActivityId(String activityId, List<Itinerary> itineraries) {
		itineraryDao.deleteByActivityId(activityId);
		if (itineraries.size() > 0) {
			int idx = 1;
			for (Itinerary itinerary : itineraries) {
				itinerary.setActivityId(activityId);
				itinerary.setIdx(idx++);
				itineraryDao.create(itinerary);
			}
		}
	}

}
