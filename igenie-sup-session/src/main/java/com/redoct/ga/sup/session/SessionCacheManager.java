package com.redoct.ga.sup.session;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;
import com.redoct.ga.sup.session.domain.GateSession;
import com.redoct.ga.sup.session.domain.StpSession;

public class SessionCacheManager
{
	// ##############################################################
	// Map1:GateToken:
	// (key)deviceId:(value)gateSession=[gateToken,stpId,stpIp,stpPort]
	// //////////////////////////////////////////////////////////////

	// @Override
	public void putGateSession(String deviceId, GateSession gateSession)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null) {
			cache.remove(deviceId);
			cache.put(deviceId, gateSession);
		}
	}

	// @Override
	public GateSession getGateSession(String deviceId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null)
			return (GateSession) cache.get(deviceId);
		else
			return null;
	}

	// //////////////////////////////////////////////////////////////
	// End Map1:GateToken
	// ##############################################################

	// ##############################################################
	// Map2:StpSession:
	// (key)sessionTicket:(value)stpSession=[accountId,deviceId,deviceOsVersion,gateToken,ioSessionId,notifyToken,active,expiryTime]
	// //////////////////////////////////////////////////////////////

	// @Override
	public void putStpSession(String sessionTicket, StpSession stpSession)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null) {
			cache.remove(sessionTicket);
			cache.put(sessionTicket, stpSession);
		}
	}

	// @Override
	public StpSession getStpSession(String sessionTicket)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null) {
			Object object = cache.get(sessionTicket);
			if (object != null)
				return (StpSession) object;
			else
				return null;
		} else
			return null;
	}

	public void removeStpSession(String sessionTicket)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null)
			cache.remove(sessionTicket);
	}

	// //////////////////////////////////////////////////////////////
	// End Map2:StpSession
	// ##############################################################

	// ##############################################################
	// Map3:SessionTicket:
	// (key)accountId:(value)sessionTicket
	// //////////////////////////////////////////////////////////////

	// @Override
	public void putSessionTicket(String accountId, String sessionTicket)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null) {
			cache.remove(accountId);
			cache.put(accountId, sessionTicket);
		}
	}

	// @Override
	public String getSessionTicket(String accountId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null)
			return (String) cache.get(accountId);
		else
			return null;
	}

	public void removeSessionTicket(String accountId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null)
			cache.remove(accountId);
	}

	// //////////////////////////////////////////////////////////////
	// End Map3:SessionTicket
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
