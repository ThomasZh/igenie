package com.oct.ga.syncVer;

import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.syncVer.dao.GaSyncVerDao;

public class SyncVerServiceImpl
		implements GaSyncVerService
{
	@Override
	public int queryMax(String oid, short syncType)
	{
		return syncVerDao.queryLastVer(oid, syncType);
	}

	@Override
	public int queryUpdateTime(String oid, short syncType, int ver)
	{
		return syncVerDao.queryUpdateTime(oid, syncType, ver);
	}

	@Override
	public int increase(String oid, short syncType, int timestamp, String userId, short action)
	{
		int lastVer = syncVerDao.queryLastVer(oid, syncType);

		syncVerDao.add(oid, syncType, ++lastVer, timestamp, userId, action);

		return lastVer;
	}

	// /////////////////////////////////////////////////////

	private GaSyncVerDao syncVerDao;

	public GaSyncVerDao getSyncVerDao()
	{
		return syncVerDao;
	}

	public void setSyncVerDao(GaSyncVerDao syncVerDao)
	{
		this.syncVerDao = syncVerDao;
	}

}
