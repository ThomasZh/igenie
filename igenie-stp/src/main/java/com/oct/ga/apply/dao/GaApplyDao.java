package com.oct.ga.apply.dao;

import java.util.List;

import com.oct.ga.comm.domain.apply.GaApplyStateNotify;

public interface GaApplyDao
{
	public short queryApplyNum(String accountId);

	public List<GaApplyStateNotify> queryNotReceived(String accountId);

	public void add(String msgId, String fromAccountId, String toAccountId, String channelId, short action, String txt,
			int timestamp);

	public void updateSyncState(String fromAccountId, String toAccountId, String channelId, short state, int timestamp);

	public boolean isExist(String fromAccountId, String toAccountId, String channelId);
	
	public String queryId(String fromAccountId, String toAccountId, String channelId);

	public void update(String fromAccountId, String toAccountId, String channelId, short action, String txt,
			int timestamp);
}
