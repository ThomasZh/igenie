package com.oct.ga.message.dao.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.oct.ga.comm.domain.BadgeNumberJsonBean;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.MessageOriginalMulticast;
import com.oct.ga.comm.domain.msg.MsgExtend;
import com.oct.ga.comm.domain.msg.MsgOriginal;
import com.oct.ga.comm.domain.msg.MsgSyncState;
import com.oct.ga.message.dao.GaMessageDao;

public class MessageDaoImpl
		extends SimpleMongoTemplate
		implements GaMessageDao
{

	@Override
	public List<BadgeNumberJsonBean> queryUnreadNumberByChat(String toAccountId, int timestamp)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageInlinecast queryLastOneByChat(String chatId, String toAccountId, int timestamp)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageInlinecast queryLastUnreadByChat(String chatId, String toAccountId, int timestamp)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addOriginal(MessageOriginalMulticast data)
	{
		try {
			// if collection doesn't exists, mongodb will create it for you
			DBCollection coll = this.getMongoDbFactory().getDb().getCollection("MessageOriginal");

			BasicDBObject doc = new BasicDBObject("_id", data.get_id()).append("contentType", data.getContentType())
					.append("fromAccountId", data.getFromAccountId())
					.append("fromAccountName", data.getFromAccountName())
					.append("channelType", data.getChannelType()).append("channelId", data.getChannelId())
					.append("timestamp", data.getTimestamp()).append("content", data.getContent());

			this.getMongoDbFactory().getDb().requestStart();
			coll.insert(doc);
		} finally {
			this.getMongoDbFactory().getDb().requestDone();
		}
	}

	@Override
	public void addExtend(final String msgId, final String userId, final int timestamp, final short state)
	{
		try {
			// if collection doesn't exists, mongodb will create it for you
			DBCollection coll = this.getMongoDbFactory().getDb().getCollection("MessageExtend");

			BasicDBObject doc = new BasicDBObject("_id", msgId).append("toAccountId", userId)
					.append("timestamp", timestamp).append("syncState", state);

			this.getMongoDbFactory().getDb().requestStart();
			coll.insert(doc);
		} finally {
			this.getMongoDbFactory().getDb().requestDone();
		}
	}

	@Override
	public int batchUpdateExtendState(String groupId, String userId, short state, int timestamp)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeExtend(String messageId)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void removeOriginal(String messageId)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<MessageInlinecast> queryByState(String toAccountId, int timestamp, short state)
	{
		final List<MessageInlinecast> array = new ArrayList<MessageInlinecast>();

		DBCursor cursor = null;
		try {
			// if collection doesn't exists, mongodb will create it for you
			DBCollection coll = this.getMongoDbFactory().getDb().getCollection("MessageOriginal");

			// toId=? AND timestamp>?
			BasicDBObject queryCondition = new BasicDBObject();
			queryCondition.put("toAccountId", toAccountId);
			queryCondition.put("syncState", state);
			queryCondition.put("timestamp", new BasicDBObject("$gt", timestamp));

			cursor = coll.find(queryCondition);
			while (cursor.hasNext()) {
				// do something with cursor.next();
				String json = cursor.next().toString();

				Map<String, Object> classMap = new HashMap<String, Object>();
				classMap.put("MessageInlinecast", MessageInlinecast.class);
				MessageInlinecast msg = (MessageInlinecast) JSONObject.toBean(JSONObject.fromObject(json),
						MessageInlinecast.class, classMap);

				array.add(msg);
			}
		} finally {
			if (cursor != null)
				cursor.close();
			this.getMongoDbFactory().getDb().requestDone();
		}

		return array;
	}

	@Override
	public Page<MessageInlinecast> queryPagination(String toAccountId, int lastTryTime, int currentTimestamp,
			int pageNum, int pageSize)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public  MsgExtend query(String id)
	{
		MsgExtend msg = null;

		try {
			// if collection doesn't exists, mongodb will create it for you
			DBCollection coll = this.getMongoDbFactory().getDb().getCollection("MsgExtend");

			// toId=? AND timestamp>?
			BasicDBObject queryCondition = new BasicDBObject();
			queryCondition.put("_id", new ObjectId(id));
			DBObject dbObj = coll.findOne(queryCondition);

			// translate dbObj to java bean
			String json = dbObj.toString();

			Map<String, Object> classMap = new HashMap<String, Object>();
			classMap.put("MsgExtend", MsgExtend.class);
			msg = (MsgExtend) JSONObject.toBean(JSONObject.fromObject(json), MsgExtend.class, classMap);
		} finally {
			this.getMongoDbFactory().getDb().requestDone();
		}

		return msg;
	}

	@Override
	public void updateExtendState(String msgId, String userId, int timestamp, short state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Page<MessageInlinecast> queryPagination(String channelId, short channelType, String toAccountId,
			int lastTryTime, int currentTimestamp, int pageNum, int pageSize)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<MessageInlinecast> queryPagination(String channelId, short channelType, String fromAccountId,
			String toAccountId, int lastTryTime, int currentTimestamp, int pageNum, int pageSize)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addOriginal(MsgOriginal msg, int timestamp)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Page<MsgExtend> queryPagination(String chatId, String toAccountId, int pageNum, int pageSize)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addExtend(String msgId, String accountId, String chatId, short state, int timestamp)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<MsgSyncState> queryPagination(String chatId, String toAccountId, int lastTryTime, int pageNum, int pageSize)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int batchUpdateAllExtendState(short state, int timestamp)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
