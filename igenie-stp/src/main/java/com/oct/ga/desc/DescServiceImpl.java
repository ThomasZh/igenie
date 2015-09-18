package com.oct.ga.desc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.desc.dao.GaDescDao;
import com.oct.ga.service.GaDescService;

public class DescServiceImpl
		implements GaDescService
{
	@Override
	public void modify(String activityId, short seq, GaDescChapter chapter, int timestamp)
	{
		Gson gson = new Gson();
		String json = gson.toJson(chapter);
		logger.debug("json: " + json);

		if (descDao.isExist(activityId, seq)) {
			descDao.update(activityId, seq, chapter.getTitle(), json, timestamp);
		} else {
			descDao.insert(activityId, seq, chapter.getTitle(), json, timestamp);
		}
	}

	@Override
	public void remove(String activityId, short seq)
	{
		descDao.delete(activityId, seq);
	}

	@Override
	public void removeAll(String activityId)
	{
		descDao.deleteAll(activityId);
	}

	@Override
	public List<GaDescChapter> query(String activityId)
	{
		return descDao.select(activityId);
	}

	// ///////////////////////////////////////////////////

	private GaDescDao descDao;

	public GaDescDao getDescDao()
	{
		return descDao;
	}

	public void setDescDao(GaDescDao descDao)
	{
		this.descDao = descDao;
	}

	private final static Logger logger = LoggerFactory.getLogger(DescServiceImpl.class);

}
