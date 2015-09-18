package com.oct.ga.publish.dao;

import java.util.List;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.publish.GaPublishHotLoc;
import com.oct.ga.comm.domain.publish.GaPublishLoc;
import com.oct.ga.comm.domain.publish.IdAndTimestamp;

public interface GaPublishLocDao
{
	public void remove(String activityId);

	public void add(String activityId, GaPublishLoc location, int timestamp);

	public List<GaPublishLoc> selectPublish(String activityId);

	public Page<IdAndTimestamp> selectChannelIds(String locMask, int pageNum, int pageSize);

	public List<String> selectAllChannelIds();

	public Page<GaPublishHotLoc> selectHot(int pageNum, int pageSize);
}
