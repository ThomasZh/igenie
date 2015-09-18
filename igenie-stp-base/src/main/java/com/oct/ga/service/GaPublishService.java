package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.publish.GaPublishHotLoc;
import com.oct.ga.comm.domain.publish.GaPublishLoc;
import com.oct.ga.comm.domain.publish.IdAndTimestamp;

public interface GaPublishService
{
	public void modifyPublishLoc(String activityId, List<GaPublishLoc> locations, int timestamp);

	public List<GaPublishLoc> queryPublishLocs(String activityId);

	public List<IdAndTimestamp> queryPushlishChannelIds(String locMask, int pageNum, int pageSize);

	public List<GaPublishHotLoc> queryHotLocs(int pageNum, int pageSize);

	public List<String> queryAllActivityIds();

	public void remove(String activityId);
}
