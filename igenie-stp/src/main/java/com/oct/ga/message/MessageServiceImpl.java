package com.oct.ga.message;

import java.util.ArrayList;
import java.util.List;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.BadgeNumberJsonBean;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.MessageOriginalMulticast;
import com.oct.ga.comm.domain.msg.MsgExtend;
import com.oct.ga.comm.domain.msg.MsgLastCacheJsonBean;
import com.oct.ga.comm.domain.msg.MsgOriginal;
import com.oct.ga.comm.domain.msg.MsgSyncState;
import com.oct.ga.message.dao.GaMessageDao;
import com.oct.ga.message.dao.MessageCacheDao;
import com.oct.ga.service.GaMessageService;

public class MessageServiceImpl
		implements GaMessageService
{
	@Override
	public int batchUpdateMessageToReceviedState(String groupId, String accountId, int timestamp)
	{
		return messageDao.batchUpdateExtendState(groupId, accountId, GlobalArgs.SYNC_STATE_RECEIVED, timestamp);
	}

	@Override
	public void updateState(String msgId, String userId, int timestamp, short state)
	{
		messageDao.updateExtendState(msgId, userId, timestamp, state);
	}

	@Override
	public List<BadgeNumberJsonBean> queryBadgeNumber(String accountId, int timestamp)
	{
		return messageDao.queryUnreadNumberByChat(accountId, timestamp);
	}

	@Override
	public MsgExtend query(String msgId)
	{
		return messageDao.query(msgId);
	}

	@Override
	public MessageInlinecast queryLastOneByChat(String chatId, String toAccountId, int timestamp)
	{
		return messageDao.queryLastOneByChat(chatId, toAccountId, timestamp);
	}

	@Override
	public MessageInlinecast queryLastUnreadByChat(String chatId, String toAccountId, int timestamp)
	{
		return messageDao.queryLastUnreadByChat(chatId, toAccountId, timestamp);
	}

	@Override
	public void addExtend(String msgId, String userId, int timestamp, short state)
	{
		messageDao.addExtend(msgId, userId, timestamp, state);
	}

	@Override
	public void addOriginal(MessageOriginalMulticast msg)
	{
		messageDao.addOriginal(msg);
	}

	@Override
	public void addOriginal(MessageOriginalMulticast msg, int timestamp)
	{
		MsgOriginal message = new MsgOriginal();
		message.setMsgId(msg.get_id());
		message.setFromAccountId(msg.getFromAccountId());
		message.setChannelType(msg.getChannelType());
		message.setChannelId(msg.getChannelId());
		message.setChatId(msg.getChatId());
		message.setContentType(msg.getContentType());
		message.setContent(msg.getContent());
		message.setAttachUrl(msg.getAttachUrl());
		
		messageDao.addOriginal(message, timestamp);
	}

	@Override
	public void addExtend(String msgId, String accountId, String chatId, short state, int timestamp)
	{
		messageDao.addExtend(msgId, accountId, chatId, state, timestamp);
	}

	@Override
	public List<MsgExtend> queryPagination(String chatId, String toAccountId, int pageNum, int pageSize)
	{
		Page<MsgSyncState> messages = messageDao.queryPagination(chatId, toAccountId, 0, pageNum, pageSize);
		List<MsgSyncState> states = messages.getPageItems();

		List<MsgExtend> array = new ArrayList<MsgExtend>();
		for (MsgSyncState state : states) {
			MsgExtend msg = messageDao.query(state.getMsgId());
			msg.setSyncState(state.getSyncState());

			array.add(msg);
		}

		return array;
	}

	@Override
	public Page<MessageInlinecast> queryPagination(String toAccountId, int lastTryTime, int currentTimestamp,
			int pageNum, int pageSize)
	{
		return messageDao.queryPagination(toAccountId, lastTryTime, currentTimestamp, pageNum, pageSize);
	}

	@Override
	public Page<MessageInlinecast> queryPagination(String channelId, short channelType, String toAccountId,
			int lastTryTime, int currentTimestamp, int pageNum, int pageSize)
	{
		return messageDao.queryPagination(channelId, channelType, toAccountId, lastTryTime, currentTimestamp, pageNum,
				pageSize);
	}

	@Override
	public Page<MessageInlinecast> queryPagination(String channelId, short channelType, String fromAccountId,
			String toAccountId, int lastTryTime, int currentTimestamp, int pageNum, int pageSize)
	{
		return messageDao.queryPagination(channelId, channelType, fromAccountId, toAccountId, lastTryTime,
				currentTimestamp, pageNum, pageSize);
	}

	// /////////////////////////////////////////////////////////////////////

	@Override
	public boolean isExistCache(String channelId, String toAccountId)
	{
		return messageCacheDao.isExist(channelId, toAccountId);
	}

	@Override
	public short countCacheBadgeNum(String chatId, String toAccountId)
	{
		return messageCacheDao.countCacheBadgeNum(chatId, toAccountId);
	}

	@Override
	public void addCache(String chatId, String toAccountId, String msgId, short badgeNum, int timestamp)
	{
		messageCacheDao.add(chatId, toAccountId, msgId, badgeNum, timestamp);
	}

	@Override
	public void updateCache(String chatId, String toAccountId, String msgId, short badgeNum, int timestamp)
	{
		messageCacheDao.update(chatId, toAccountId, msgId, badgeNum, timestamp);
	}

	@Override
	public void updateCacheBadgeNum(String chatId, String toAccountId, short badgeNum, int timestamp)
	{
		messageCacheDao.update(chatId, toAccountId, badgeNum, timestamp);
	}

	@Override
	public List<MsgLastCacheJsonBean> queryLastCaches(String toAccountId, int lastTryTime)
	{
		return messageCacheDao.query(toAccountId, lastTryTime);
	}

	@Override
	public void batchUpdateAllMessageToReceviedState(int timestamp)
	{
		messageDao.batchUpdateAllExtendState(GlobalArgs.SYNC_STATE_RECEIVED, timestamp);
	}

	// /////////////////////////////////////////////////////////////////////

	private GaMessageDao messageDao;
	private MessageCacheDao messageCacheDao;

	public GaMessageDao getMessageDao()
	{
		return messageDao;
	}

	public void setMessageDao(GaMessageDao messageDao)
	{
		this.messageDao = messageDao;
	}

	public MessageCacheDao getMessageCacheDao()
	{
		return messageCacheDao;
	}

	public void setMessageCacheDao(MessageCacheDao messageCacheDao)
	{
		this.messageCacheDao = messageCacheDao;
	}

}
