package com.oct.ga.talent.dao;

public interface GaTalentInfoDao
{
	public void add(String accountId, int timestamp);

	public boolean isExist(String accountId);
}
