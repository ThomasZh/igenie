package com.oct.ga.syncVer.dao;

public interface GaSyncVerDao
{
	public int queryLastVer(String oid, short syncType);

	public int queryUpdateTime(String oid, short syncType, int ver);

	public void add(String oid, short syncType, int ver, int timestamp, String userId, short action);

}
