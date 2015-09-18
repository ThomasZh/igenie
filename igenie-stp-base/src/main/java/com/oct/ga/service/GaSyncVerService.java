package com.oct.ga.service;

public interface GaSyncVerService
{
	public int queryMax(String oid, short syncType);

	public int queryUpdateTime(String oid, short syncType, int ver);

	public int increase(String oid, short syncType, int timestamp, String userId, short action);
}
