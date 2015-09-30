package com.redoct.ga.sup.device.service;

import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.device.dao.SupDeviceDao;
import com.redoct.ga.sup.device.domain.DeviceBasicInfo;

public class DeviceServiceImpl
		implements SupDeviceService
{
	@Override
	public DeviceBasicInfo query(String deviceId)
	{
		return deviceDao.select(deviceId);
	}

	@Override
	public void modifyClientVersion(String deviceId, String clientVersion, String appId, String vendorId, int timestamp)
	{
		if (deviceDao.isExist(deviceId)) {
			deviceDao.update(deviceId, clientVersion, appId, vendorId, timestamp);
		} else {
			deviceDao.insert(deviceId, clientVersion, appId, vendorId, timestamp);
		}
	}

	@Override
	public void modifyOsVersion(String deviceId, String osVersion, String notifyToken, int timestamp)
	{
		deviceDao.update(deviceId, osVersion, notifyToken, timestamp);
	}

	// //////////////////////////////////////////////////////////

	private SupDeviceDao deviceDao;

	public SupDeviceDao getDeviceDao()
	{
		return deviceDao;
	}

	public void setDeviceDao(SupDeviceDao deviceDao)
	{
		this.deviceDao = deviceDao;
	}

}
