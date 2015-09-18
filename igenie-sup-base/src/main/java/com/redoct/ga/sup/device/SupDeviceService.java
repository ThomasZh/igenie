package com.redoct.ga.sup.device;

import com.oct.ga.comm.SupSocketException;
import com.redoct.ga.sup.device.domain.DeviceBasicInfo;

public interface SupDeviceService
{
	public DeviceBasicInfo query(String deviceId)
			throws SupSocketException;

	public void modifyClientVersion(String deviceId, String clientVersion, String appId, String vendorId, int timestamp)
			throws SupSocketException;

	public void modifyOsVersion(String deviceId, String osVersion, String notifyToken, int timestamp)
			throws SupSocketException;
}
