package com.oct.ga.club.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oct.ga.club.dao.ItineraryDao;
import com.oct.ga.comm.domain.club.ItineraryInfo;
import com.oct.ga.service.GaItineraryService;

@Service
@Transactional
public class ItineraryServiceImpl implements GaItineraryService {

	@Autowired
	private ItineraryDao itineraryDao;

	@Transactional(readOnly = true)
	@Override
	public List<ItineraryInfo> find(String activityId) {
		return itineraryDao.find(activityId);
	}

	@Override
	public void overWrite(List<ItineraryInfo> itineraryInfos) {
		String activityId = itineraryInfos.get(0).getActivityId();
		itineraryDao.delete(activityId);
		itineraryDao.create(itineraryInfos);
	}

	@Override
	public int delete(String activityId) {
		return itineraryDao.delete(activityId);
	}

}
