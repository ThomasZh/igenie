package com.oct.ga.message.dao;

import java.util.List;

import com.oct.ga.comm.domain.msg.MsgLastCacheJsonBean;

public interface MessageCacheDao
{
	public boolean isExist(String chatId, String toAccountId);

	public short countCacheBadgeNum(String chatId, String toAccountId);

	public void add(String chatId, String toAccountId, String msgId, short badgeNum, int timestamp);

	public void update(String chatId, String toAccountId, String msgId, short badgeNum, int timestamp);

	public void update(String chatId, String toAccountId, short badgeNum, int timestamp);

	public List<MsgLastCacheJsonBean> query(String toAccountId, int lastTryTime);
}
