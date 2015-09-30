package com.redoct.ga.sup.device;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;
import com.redoct.ga.sup.device.domain.DeviceBasicInfo;

public class DeviceCacheManager
{
	// ##############################################################
	// Map1:DeviceBasicInfo:
	// (key)deviceId:(value)device=[deviceId,clientVersion,osVersion,notifyToken]
	// //////////////////////////////////////////////////////////////

	// @Override
	public void putDevice(String deviceId, DeviceBasicInfo device)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null) {
			cache.remove(deviceId);
			cache.put(deviceId, device);
		}
	}

	// @Override
	public DeviceBasicInfo getDevice(String deviceId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null)
			return (DeviceBasicInfo) cache.get(deviceId);
		else
			return null;
	}

	// //////////////////////////////////////////////////////////////
	// End Map1:AccountBasic
	// ##############################################################

	public void init()
	{
		manager = CacheUtil.getCacheManager(IMemcachedCache.class, MemcachedCacheManager.class.getName());
		manager.setConfigFile("memcached.xml");
		manager.setResponseStatInterval(5000); // 5 sec
		manager.start();
	}

	static ICacheManager<IMemcachedCache> manager;
}
