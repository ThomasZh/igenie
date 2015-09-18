package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.BadgeNumberJsonBean;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.MessageOriginalMulticast;
import com.oct.ga.comm.domain.msg.MsgExtend;
import com.oct.ga.comm.domain.msg.MsgLastCacheJsonBean;

public interface GaMessageService
{
	public void updateState(String msgId, String userId, int timesamp, short state);

	public MessageInlinecast queryLastOneByChat(String chatId, String toAccountId, int timestamp);

	public MessageInlinecast queryLastUnreadByChat(String chatId, String toAccountId, int timestamp);

	public void addExtend(String msgId, String userId, int timestamp, short state);

	public void addOriginal(MessageOriginalMulticast msg);

	public Page<MessageInlinecast> queryPagination(String toAccountId, int lastTryTime, int currentTimestamp,
			int pageNum, int pageSize);

	public Page<MessageInlinecast> queryPagination(String channelId, short channelType, String toAccountId,
			int lastTryTime, int currentTimestamp, int pageNum, int pageSize);

	public Page<MessageInlinecast> queryPagination(String channelId, short channelType, String fromAccountId,
			String toAccountId, int lastTryTime, int currentTimestamp, int pageNum, int pageSize);

	// /////////////////////////////////////////////////////////////////////

	public void addOriginal(MessageOriginalMulticast msg, int timestamp);

	public void addExtend(String msgId, String accountId, String chatId, short state, int timestamp);

	public MsgExtend query(String msgId);

	public int batchUpdateMessageToReceviedState(String chatId, String accountId, int timestamp);

	public List<MsgExtend> queryPagination(String chatId, String toAccountId, int pageNum, int pageSize);

	public List<BadgeNumberJsonBean> queryBadgeNumber(String accountId, int timestamp);

	// /////////////////////////////////////////////////////////////////////

	public short countCacheBadgeNum(String chatId, String toAccountId);

	public void updateCacheBadgeNum(String chatId, String toAccountId, short badgeNum, int timestamp);

	public boolean isExistCache(String chatId, String toAccountId);

	public void addCache(String chatId, String toAccountId, String msgId, short badgeNum, int timestamp);

	public void updateCache(String chatId, String toAccountId, String msgId, short badgeNum, int timestamp);

	public List<MsgLastCacheJsonBean> queryLastCaches(String toAccountId, int lastTryTime);

	// /////////////////////////////////////////////////////////////////////

	public void batchUpdateAllMessageToReceviedState(int timestamp);
}
