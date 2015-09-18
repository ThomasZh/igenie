package com.oct.ga.message.dao;

import java.util.List;

import com.oct.ga.comm.domain.BadgeNumberJsonBean;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.MessageOriginalMulticast;
import com.oct.ga.comm.domain.msg.MsgExtend;
import com.oct.ga.comm.domain.msg.MsgOriginal;
import com.oct.ga.comm.domain.msg.MsgSyncState;

/**
 * (OriginalMsg) Data Access Interface.
 * 
 * @author Thomas.Zhang
 */
public interface GaMessageDao
{
	public List<BadgeNumberJsonBean> queryUnreadNumberByChat(String toAccountId, int timestamp);

	public MsgExtend query(String msgId);

	public MessageInlinecast queryLastOneByChat(String chatId, String toAccountId, int timestamp);

	public MessageInlinecast queryLastUnreadByChat(String chatId, String toAccountId, int timestamp);

	public void addOriginal(MessageOriginalMulticast msg);

	public void addOriginal(MsgOriginal msg, int timestamp);

	public void addExtend(String msgId, String accountId, String chatId, short state, int timestamp);

	public void addExtend(String msgId, String userId, int timestamp, short state);

	public void updateExtendState(String msgId, String userId, int timestamp, short state);

	public int batchUpdateExtendState(String groupId, String userId, short state, int timestamp);

	public int batchUpdateAllExtendState(short state, int timestamp);

	public void removeExtend(final String messageId);

	public void removeOriginal(final String messageId);

	public List<MessageInlinecast> queryByState(String toAccountId, int timestamp, short state);

	public Page<MsgSyncState> queryPagination(String chatId, String toAccountId, int lastTryTime, int pageNum,
			int pageSize);

	public Page<MsgExtend> queryPagination(String chatId, String toAccountId, int pageNum, int pageSize);

	public Page<MessageInlinecast> queryPagination(String channelId, short channelType, String toAccountId,
			int lastTryTime, int currentTimestamp, int pageNum, int pageSize);

	public Page<MessageInlinecast> queryPagination(String channelId, short channelType, String fromAccountId,
			String toAccountId, int lastTryTime, int currentTimestamp, int pageNum, int pageSize);

	public Page<MessageInlinecast> queryPagination(String toAccountId, int lastTryTime, int currentTimestamp,
			int pageNum, int pageSize);

}
