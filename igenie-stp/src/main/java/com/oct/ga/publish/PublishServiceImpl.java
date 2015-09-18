package com.oct.ga.publish;

import java.util.List;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.publish.GaPublishHotLoc;
import com.oct.ga.comm.domain.publish.GaPublishLoc;
import com.oct.ga.comm.domain.publish.IdAndTimestamp;
import com.oct.ga.publish.dao.GaPublishLocDao;
import com.oct.ga.service.GaPublishService;

public class PublishServiceImpl
		implements GaPublishService
{
	@Override
	public void modifyPublishLoc(String activityId, List<GaPublishLoc> locations, int timestamp)
	{
		publishLocDao.remove(activityId);
		for (GaPublishLoc location : locations) {
			publishLocDao.add(activityId, location, timestamp);
		}
	}

	@Override
	public List<GaPublishLoc> queryPublishLocs(String activityId)
	{
		return publishLocDao.selectPublish(activityId);
	}

	@Override
	public List<IdAndTimestamp> queryPushlishChannelIds(String locMask, int pageNum, int pageSize)
	{
		Page<IdAndTimestamp> page = publishLocDao.selectChannelIds(locMask, pageNum, pageSize);
		return page.getPageItems();
	}

	@Override
	public List<GaPublishHotLoc> queryHotLocs(int pageNum, int pageSize)
	{
		Page<GaPublishHotLoc> page = publishLocDao.selectHot(pageNum, pageSize);
		return page.getPageItems();
	}

	@Override
	public List<String> queryAllActivityIds()
	{
		return publishLocDao.selectAllChannelIds();
	}

	@Override
	public void remove(String activityId)
	{
		publishLocDao.remove(activityId);
	}
	
	// /////////////////////////////////////////////////////////////////

	private GaPublishLocDao publishLocDao;

	public GaPublishLocDao getPublishLocDao()
	{
		return publishLocDao;
	}

	public void setPublishLocDao(GaPublishLocDao publishLocDao)
	{
		this.publishLocDao = publishLocDao;
	}



}
