package com.oct.ga.appver;

import com.oct.ga.appver.dao.GaAppVersionDao;
import com.oct.ga.service.GaAppVersionService;

public class AppVersionServiceImpl
		implements GaAppVersionService
{
	@Override
	public short queryUpgradePriority(String clientVersion)
	{
		return appVersionDao.queryPriority(clientVersion);
	}

	// ////////////////////////////////////////////////////////

	private GaAppVersionDao appVersionDao;

	public GaAppVersionDao getAppVersionDao()
	{
		return appVersionDao;
	}

	public void setAppVersionDao(GaAppVersionDao appVersionDao)
	{
		this.appVersionDao = appVersionDao;
	}

}
