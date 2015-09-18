package com.redoct.ga.sup.account;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;
import com.oct.ga.comm.domain.account.AccountBasic;

public class AccountCacheManager
{
	// ##############################################################
	// Map1:AccountBasic:
	// (key)accountId:(value)account=[accountId,nickname,avatatUrl,desc]
	// //////////////////////////////////////////////////////////////

	// @Override
	public void putAccount(String accountId, AccountBasic account)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null) {
			cache.remove(accountId);
			cache.put(accountId, account);
		}
	}

	// @Override
	public AccountBasic getAccount(String accountId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null)
			return (AccountBasic) cache.get(accountId);
		else
			return null;
	}
	
	public void removeAccount(String accountId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null) {
			cache.remove(accountId);
		}
	}

	// //////////////////////////////////////////////////////////////
	// End Map1:AccountBasic
	// ##############################################################

	// ##############################################################
	// Map2:LoginInfo:
	// (key)loginType+loginName:(value)accountId
	// //////////////////////////////////////////////////////////////

	// @Override
	public void putLogin(short loginType, String loginName, String accountId)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null) {
			String key = "" + loginType + loginName;
			cache.remove(key);
			cache.put(key, accountId);
		}
	}

	public void removeLogin(short loginType, String loginName)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null) {
			String key = "" + loginType + loginName;
			cache.remove(key);
		}
	}
	
	// @Override
	public String getAccountId(short loginType, String loginName)
	{
		IMemcachedCache cache = manager.getCache("mclient0");
		if (cache != null)
			return (String) cache.get("" + loginType + loginName);
		else
			return null;
	}

	// //////////////////////////////////////////////////////////////
	// End Map2:LoginInfo
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
