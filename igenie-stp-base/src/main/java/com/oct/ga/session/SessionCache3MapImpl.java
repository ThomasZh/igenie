package com.oct.ga.session;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;

/**
 * accountId:deviceList deivceId:gaSession accountId+deviceId:online
 * 
 * @author thomas
 * 
 */
public class SessionCache3MapImpl
{

	// ##############################################################
	// Map1:GaAccountDeviceState:
	// (key)accountId+deviceId:(value)online+lastTryTime
	// //////////////////////////////////////////////////////////////

	protected void setOnline(String accountId, String deivceId)
	{
		GaAccountDeviceState state = getState(accountId, deivceId);
		state.setOnline(true);
		putState(accountId, deivceId, state);
	}

	protected void setOffline(String accountId, String deivceId)
	{
		GaAccountDeviceState state = getState(accountId, deivceId);
		state.setOnline(false);
		putState(accountId, deivceId, state);
	}

	protected void setLastTryTime(String accountId, String deivceId, int timestamp)
	{
		GaAccountDeviceState state = getState(accountId, deivceId);
		state.setLastTryTime(timestamp);
		putState(accountId, deivceId, state);
	}

	// //////////////////////////////////////////////////////////////

	public void putState(String accountId, String deivceId, GaAccountDeviceState state)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		cache.put(accountId + deivceId, state);
	}

	public GaAccountDeviceState getState(String accountId, String deivceId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		return (GaAccountDeviceState) cache.get(accountId + deivceId);
	}

	protected void removeState(String accountId, String deivceId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		cache.remove(accountId + deivceId);
	}

	// //////////////////////////////////////////////////////////////
	// End Map1:GaAccountDeviceState
	// ##############################################################

	// ##############################################################
	// Map2:GaSessionInfo:
	// (key)deviceId:(value)sessionInfo
	// //////////////////////////////////////////////////////////////

	// @Override
	public void putSession(String deviceId, GaSessionInfo session)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		cache.put(deviceId, session);
	}

	// @Override
	public GaSessionInfo getSession(String deviceId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		return (GaSessionInfo) cache.get(deviceId);
	}

	// //////////////////////////////////////////////////////////////
	// End Map2:GaSessionInfo
	// ##############################################################

	// ##############################################################
	// Map3:DeviceList:
	// (key)accountId:(value)deviceSet
	// //////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	// @Override
	public void putDevice(String accountId, String deviceId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		Set<String> deviceSet = (Set<String>) cache.get(accountId);
		if (deviceSet == null) {
			deviceSet = Collections.synchronizedSet(new HashSet<String>());
		}
		deviceSet.add(deviceId);
		cache.put(accountId, deviceSet);
	}

	@SuppressWarnings("unchecked")
	// @Override
	public Set<String> getDeviceList(String accountId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		return (Set<String>) cache.get(accountId);
	}

	@SuppressWarnings("unchecked")
	// @Override
	public void removeDevice(String accountId, String deviceId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		Set<String> deviceSet = (Set<String>) cache.get(accountId);
		if (deviceSet != null) {
			deviceSet.remove(deviceId);
			cache.put(accountId, deviceSet);
		}
		if (deviceSet.size() == 0) {
			cache.remove(accountId);
		} else {
			cache.put(accountId, deviceSet);
		}
	}

	// //////////////////////////////////////////////////////////////
	// End Map3:DeviceList
	// ##############################################################

	// ##############################################################
	// Map4:StpServerInfo:
	// (key)stpId:(value)stp
	// //////////////////////////////////////////////////////////////

	public void putStp(String stpId, StpServerInfoJsonBean stp)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		cache.put(stpId, stp);
	}

	public StpServerInfoJsonBean getStp(String stpId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		return (StpServerInfoJsonBean) cache.get(stpId);
	}

	// //////////////////////////////////////////////////////////////
	// End Map4:StpServerInfo
	// ##############################################################

	// @Override
	public void init()
	{
		manager = CacheUtil.getCacheManager(IMemcachedCache.class, MemcachedCacheManager.class.getName());
		manager.setConfigFile("memcached.xml");
		manager.setResponseStatInterval(5000); // 5 sec
		manager.start();
	}

	static ICacheManager<IMemcachedCache> manager;
}
