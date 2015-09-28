package com.redoct.ga.sup.device.dao;

import com.redoct.ga.sup.device.domain.DeviceBasicInfo;

public interface SupDeviceDao
{
	public DeviceBasicInfo select(String deviceId);

	public boolean isExist(String deviceId);

	public void insert(String deviceId, String clientVersion, String appId, String vendorId, int timestamp);

	public void update(String deviceId, String clientVersion, String appId, String vendorId, int timestamp);

	public void update(String deviceId, String osVersion, String notifyToken, int timestamp);
}
