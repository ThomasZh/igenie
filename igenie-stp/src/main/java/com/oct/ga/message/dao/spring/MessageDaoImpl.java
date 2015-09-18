// End of domain class for OriginalMsg
// copy to com.oct.ga.dao -> GaMessageDao.java
// Begin of Dao class for OriginalMsg
package com.oct.ga.message.dao.spring;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.BadgeNumberJsonBean;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.MessageOriginalMulticast;
import com.oct.ga.comm.domain.msg.MsgExtend;
import com.oct.ga.comm.domain.msg.MsgOriginal;
import com.oct.ga.comm.domain.msg.MsgSyncState;
import com.oct.ga.message.dao.GaMessageDao;
import com.oct.ga.stp.utility.PaginationHelper;

/**
 * (OriginalMsg) Data Access Object.
 * 
 * @author Thomas.Zhang
 */
public class MessageDaoImpl
		extends JdbcDaoSupport
		implements GaMessageDao
{
	@Override
	public void addOriginal(final MsgOriginal data, final int timestamp)
	{
		String sql = "INSERT INTO ga_message_original (msg_id,content_type,from_account_id,channel_type,channel_id,chat_id,timestamp,msg_txt,attach_url) VALUES (?,?,?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_message_original (msg_id,content_type,from_account_id,channel_type,channel_id,chat_id,timestamp,msg_txt,attach_url) VALUES ("
				+ data.getMsgId()
				+ ","
				+ data.getContentType()
				+ ","
				+ data.getFromAccountId()
				+ ","
				+ data.getChannelType()
				+ ","
				+ data.getChannelId()
				+ ","
				+ data.getChatId()
				+ ","
				+ timestamp
				+ ",?,"
				+ data.getAttachUrl() + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, data.getMsgId());
				ps.setShort(i++, data.getContentType());
				ps.setString(i++, data.getFromAccountId());
				ps.setShort(i++, data.getChannelType());
				ps.setString(i++, data.getChannelId());
				ps.setString(i++, data.getChatId());
				ps.setInt(i++, timestamp);

				Blob blob = null;
				if (data.getContent() != null && data.getContent().length() > 0) {
					logger.debug(data.getContent());
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					blob = new SerialBlob(data.getContent().getBytes());
				}
				ps.setBlob(i++, blob);// mysql

				ps.setString(i++, data.getAttachUrl());
			}
		});
	}

	@Override
	public void addExtend(final String msgId, final String accountId, final String chatId, final short state,
			final int timestamp)
	{
		String sql = "INSERT INTO ga_message_extend (msg_id,to_account_id,chat_id,sync_state,timestamp) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO ga_message_extend (msg_id,to_account_id,chat_id,sync_state,timestamp) VALUES ("
				+ msgId + "," + accountId + "," + chatId + "," + state + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, msgId);
				ps.setString(i++, accountId);
				ps.setString(i++, chatId);
				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public MsgExtend query(final String msgId)
	{
		final MsgExtend data = new MsgExtend();

		String sql = "SELECT o.content_type,o.from_account_id,o.channel_type,o.channel_id,o.chat_id,o.timestamp,o.msg_txt,o.attach_url,e.sync_state "
				+ " FROM ga_message_original o,ga_message_extend e WHERE o.msg_id=e.msg_id AND o.msg_id=?";
		logger.debug("SELECT o.content_type,o.from_account_id,o.channel_type,o.channel_id,o.chat_id,o.timestamp,o.msg_txt,o.attach_url,e.sync_state "
				+ " FROM ga_message_original o,ga_message_extend e WHERE o.msg_id=e.msg_id AND o.msg_id=" + msgId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, msgId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int i = 1;

				data.setMsgId(msgId);
				data.setContentType(rs.getShort(i++));
				data.setFromAccountId(rs.getString(i++));
				data.setChannelType(rs.getShort(i++));
				data.setChannelId(rs.getString(i++));
				data.setChatId(rs.getString(i++));
				data.setTimestamp(rs.getInt(i++));

				Blob blob = rs.getBlob(i++);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setContent(new String(bytes));
				}

				data.setAttachUrl(rs.getString(i++));
				data.setSyncState(rs.getShort(i++));
			}
		});

		return data;
	}

	@Override
	public Page<MsgSyncState> queryPagination(String chatId, String toAccountId, int lastTryTime, int pageNum,
			int pageSize)
	{
		PaginationHelper<MsgSyncState> ph = new PaginationHelper<MsgSyncState>();
		String countSql = "SELECT count(msg_id) FROM ga_message_extend WHERE chat_id=? AND to_account_id=? AND timestamp>? ORDER BY timestamp DESC";
		String sql = "SELECT msg_id,sync_state "
				+ " FROM ga_message_extend WHERE chat_id=? AND to_account_id=? AND timestamp>? ORDER BY timestamp DESC";
		logger.debug("SELECT msg_id,sync_state FROM ga_message_extend WHERE chat_id=" + chatId + " AND to_account_id="
				+ toAccountId + " AND timestamp>" + lastTryTime + " ORDER BY timestamp DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { chatId, toAccountId, lastTryTime },
				pageNum, pageSize, new ParameterizedRowMapper<MsgSyncState>()
				{
					public MsgSyncState mapRow(ResultSet rs, int i)
							throws SQLException
					{
						MsgSyncState data = new MsgSyncState();
						int n = 1;

						data.setMsgId(rs.getString(n++));
						data.setSyncState(rs.getShort(n++));

						return data;
					}
				});
	}

	@Override
	public Page<MsgExtend> queryPagination(final String chatId, final String toAccountId, final int pageNum,
			final int pageSize)
	{
		PaginationHelper<MsgExtend> ph = new PaginationHelper<MsgExtend>();
		String countSql = "SELECT count(o.msg_id) FROM ga_message_original o, ga_message_extend e WHERE o.msg_id=e.msg_id AND o.chat_id=? AND e.to_account_id=? ORDER BY e.timestamp DESC";
		String sql = "SELECT o.msg_id,o.content_type,o.from_account_id,o.channel_type,o.channel_id,o.timestamp,o.msg_txt,o.attach_url,e.sync_state "
				+ " FROM ga_message_original o, ga_message_extend e WHERE o.msg_id=e.msg_id AND o.chat_id=? AND e.to_account_id=? ORDER BY e.timestamp DESC";
		logger.debug("SELECT count(o.msg_id) FROM ga_message_original o, ga_message_extend e WHERE o.msg_id=e.msg_id AND o.chat_id="
				+ chatId + " AND e.to_account_id=" + toAccountId + " ORDER BY e.timestamp DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { chatId, toAccountId }, pageNum,
				pageSize, new ParameterizedRowMapper<MsgExtend>()
				{
					public MsgExtend mapRow(ResultSet rs, int i)
							throws SQLException
					{
						MsgExtend data = new MsgExtend();
						int n = 1;

						data.setMsgId(rs.getString(n++));
						data.setContentType(rs.getShort(n++));
						data.setFromAccountId(rs.getString(n++));
						data.setChannelType(rs.getShort(n++));
						data.setChannelId(rs.getString(n++));
						data.setTimestamp(rs.getInt(n++));

						Blob blob = rs.getBlob(n++);
						if (blob != null && blob.length() > 0) {
							byte[] bytes = blob.getBytes(1, (int) blob.length());
							// byte[] decoded = Base64.decodeBase64(bytes);
							data.setContent(new String(bytes));
						}

						data.setAttachUrl(rs.getString(n++));
						data.setSyncState(rs.getShort(n++));

						return data;
					}
				});
	}

	// ////////////////////////////////////////////////////////////

	@Override
	public void addOriginal(final MessageOriginalMulticast data)
	{
		String sql = "INSERT INTO cscart_ga_message_original (MessageID,MessageType,FromAccountID,FromAccountName,ToType,ToId,Timestamp,MessageTxt,AttachId) VALUES (?,?,?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_ga_message_original (MessageID,MessageType,FromAccountID,FromAccountName,ToType,ToId,Timestamp,MessageTxt,AttachId) VALUES ("
				+ data.get_id()
				+ ","
				+ data.getContentType()
				+ ","
				+ data.getFromAccountId()
				+ ","
				+ data.getFromAccountName()
				+ ","
				+ data.getChannelType()
				+ ","
				+ data.getChannelId()
				+ ","
				+ data.getTimestamp() + ",?," + data.getAttachUrl() + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, data.get_id());
				ps.setShort(2, data.getContentType());
				ps.setString(3, data.getFromAccountId());
				ps.setString(4, data.getFromAccountName());
				ps.setShort(5, data.getChannelType());
				ps.setString(6, data.getChannelId());
				ps.setInt(7, data.getTimestamp());

				Blob blob = null;
				if (data.getContent() != null && data.getContent().length() > 0) {
					logger.debug(data.getContent());
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					blob = new SerialBlob(data.getContent().getBytes());
				}
				ps.setBlob(8, blob);// mysql

				ps.setString(9, data.getAttachUrl());
			}
		});
	}

	@Override
	public void addExtend(final String msgId, final String userId, final int timestamp, final short state)
	{
		String sql = "INSERT INTO ga_message_extend (msg_id,to_account_id,timestamp,sync_state) VALUES (?,?,?,?)";
		logger.debug("INSERT INTO ga_message_extend (msg_id,to_account_id,timestamp,sync_state) VALUES (" + msgId + ","
				+ userId + "," + timestamp + "," + state + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, msgId);
				ps.setString(2, userId);
				ps.setInt(3, timestamp);
				ps.setShort(4, state);
			}
		});
	}

	// @Override
	// public void addExtend(final String msgId, final String userId, final int
	// timestamp, final short state)
	// {
	// String sql =
	// "INSERT INTO cscart_ga_message_extend (MessageID,ToAccountID,Timestamp,SyncState) VALUES (?,?,?,?)";
	// logger.debug("INSERT INTO cscart_ga_message_extend (MessageID,ToAccountID,Timestamp,SyncState) VALUES ("
	// + msgId + "," + userId + "," + timestamp + "," + state + ")");
	//
	// this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
	// {
	// public void setValues(PreparedStatement ps)
	// throws SQLException
	// {
	// ps.setString(1, msgId);
	// ps.setString(2, userId);
	// ps.setInt(3, timestamp);
	// ps.setShort(4, state);
	// }
	// });
	// }

	@Override
	public void updateExtendState(final String messageId, final String userId, final int timestamp,
			final short syncState)
	{
		String sql = "UPDATE cscart_ga_message_extend SET SyncState=?,Timestamp=? WHERE MessageID=? AND ToAccountId=?";
		logger.debug("UPDATE cscart_ga_message_extend SET SyncState=" + syncState + ",Timestamp=" + timestamp
				+ " WHERE MessageID=" + messageId + " AND e.ToAccountId=" + userId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, syncState);
				ps.setInt(2, timestamp);
				ps.setString(3, messageId);
				ps.setString(4, userId);
			}
		});
	}

	@Override
	public int batchUpdateExtendState(final String chatId, final String accountId, final short state,
			final int timestamp)
	{
		String sql = "UPDATE ga_message_extend SET sync_state=? WHERE to_account_id=? AND timestamp<? AND chat_id=? AND sync_state=?";
		logger.debug("UPDATE ga_message_extend SET sync_state=" + state + " WHERE to_account_id=" + accountId
				+ " AND e.timestamp<" + timestamp + " AND chat_id=" + chatId + " AND sync_state="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		int rows = this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setShort(i++, state);
				ps.setString(i++, accountId);
				ps.setInt(i++, timestamp);
				ps.setString(i++, chatId);
				ps.setShort(i++, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
			}
		});

		return rows;
	}

	@Override
	public int batchUpdateAllExtendState(final short state, final int timestamp)
	{
		String sql = "UPDATE ga_message_extend SET sync_state=? WHERE timestamp<? AND sync_state=?";
		logger.debug("UPDATE ga_message_extend SET sync_state=" + state + " timestamp<? AND sync_state="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		int rows = this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
				ps.setShort(i++, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
			}
		});

		return rows;
	}

	@Override
	public void removeOriginal(final String messageId)
	{
		String sql = "DELETE FROM cscart_ga_message_original WHERE MessageID=?";
		logger.debug("DELETE FROM cscart_ga_message_original WHERE MessageID=" + messageId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, messageId);
			}
		});
	}

	@Override
	public void removeExtend(final String messageId)
	{
		String sql = "DELETE FROM cscart_ga_message_extend WHERE MessageID=?";
		logger.debug("DELETE FROM cscart_ga_message_extend WHERE MessageID=" + messageId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, messageId);
			}
		});
	}

	@Override
	public Page<MessageInlinecast> queryPagination(final String channelId, final short channelType,
			final String toAccountId, final int lastTryTime, final int currentTimestamp, final int pageNum,
			final int pageSize)
	{
		PaginationHelper<MessageInlinecast> ph = new PaginationHelper<MessageInlinecast>();
		String countSql = "SELECT count(a.MessageID) FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.ToId=? AND a.ToType=? AND b.ToAccountId=? AND a.Timestamp>? ORDER BY a.Timestamp DESC";
		String sql = "SELECT a.MessageID,a.MessageType,a.FromAccountID,a.FromAccountName,a.Timestamp,a.MessageTxt,b.SyncState,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.ToId=? AND a.ToType=? AND b.ToAccountId=? AND a.Timestamp>? ORDER BY a.Timestamp DESC";
		logger.debug("SELECT count(a.MessageID) FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.ToId="
				+ channelId
				+ " AND a.ToType="
				+ channelType
				+ " AND b.ToAccountId="
				+ toAccountId
				+ " AND a.Timestamp>" + lastTryTime + " ORDER BY a.Timestamp DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { channelId, channelType, toAccountId,
				lastTryTime }, pageNum, pageSize, new ParameterizedRowMapper<MessageInlinecast>()
		{
			public MessageInlinecast mapRow(ResultSet rs, int i)
					throws SQLException
			{
				MessageInlinecast data = new MessageInlinecast();
				int n = 1;

				data.set_id(rs.getString(n++));
				data.setContentType(rs.getShort(n++));
				data.setFromAccountId(rs.getString(n++));
				data.setFromAccountName(rs.getString(n++));
				data.setChannelType(channelType);
				data.setChannelId(channelId);
				data.setTimestamp(rs.getInt(n++));

				Blob blob = rs.getBlob(n++);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setContent(new String(bytes));
				}

				data.setSyncState(rs.getShort(n++));
				data.setAttachUrl(rs.getString(n++));
				data.setCurrentTimestamp(currentTimestamp);

				return data;
			}
		});
	}

	@Override
	public Page<MessageInlinecast> queryPagination(final String channelId, final short channelType,
			final String fromAccountId, final String toAccountId, final int lastTryTime, final int currentTimestamp,
			final int pageNum, final int pageSize)
	{
		PaginationHelper<MessageInlinecast> ph = new PaginationHelper<MessageInlinecast>();
		String countSql = "SELECT count(a.MessageID) FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.ToId=? AND a.ToType=? AND a.FromAccountId=? AND b.ToAccountId=? AND a.Timestamp>? ORDER BY a.Timestamp DESC";
		String sql = "SELECT a.MessageID,a.MessageType,a.FromAccountName,a.Timestamp,a.MessageTxt,b.SyncState,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.ToId=? AND a.ToType=? AND a.FromAccountId=? AND b.ToAccountId=? AND a.Timestamp>? ORDER BY a.Timestamp DESC";
		logger.debug("SELECT count(a.MessageID) FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.ToId="
				+ channelId
				+ " AND a.ToType="
				+ channelType
				+ " AND a.FromAccountId="
				+ fromAccountId
				+ " AND b.ToAccountId="
				+ toAccountId
				+ " AND a.Timestamp>"
				+ lastTryTime
				+ " ORDER BY a.Timestamp DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { channelId, channelType,
				fromAccountId, toAccountId, lastTryTime }, pageNum, pageSize,
				new ParameterizedRowMapper<MessageInlinecast>()
				{
					public MessageInlinecast mapRow(ResultSet rs, int i)
							throws SQLException
					{
						MessageInlinecast data = new MessageInlinecast();
						int n = 1;

						data.set_id(rs.getString(n++));
						data.setContentType(rs.getShort(n++));
						data.setFromAccountId(fromAccountId);
						data.setFromAccountName(rs.getString(n++));
						data.setChannelType(channelType);
						data.setChannelId(channelId);
						data.setTimestamp(rs.getInt(n++));

						Blob blob = rs.getBlob(n++);
						if (blob != null && blob.length() > 0) {
							byte[] bytes = blob.getBytes(1, (int) blob.length());
							// byte[] decoded = Base64.decodeBase64(bytes);
							data.setContent(new String(bytes));
						}

						data.setSyncState(rs.getShort(n++));
						data.setAttachUrl(rs.getString(n++));
						data.setCurrentTimestamp(currentTimestamp);

						return data;
					}
				});
	}

	@Override
	public Page<MessageInlinecast> queryPagination(final String toAccountId, final int lastTryTime,
			final int currentTimestamp, final int pageNum, final int pageSize)
	{
		PaginationHelper<MessageInlinecast> ph = new PaginationHelper<MessageInlinecast>();
		String countSql = "SELECT count(a.MessageID) FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND b.ToAccountId=? AND a.Timestamp>? ORDER BY a.Timestamp DESC";
		String sql = "SELECT a.MessageID,a.MessageType,a.FromAccountID,a.FromAccountName,a.ToType,a.ToId,a.Timestamp,a.MessageTxt,b.SyncState,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND b.ToAccountId=? AND a.Timestamp>? ORDER BY a.Timestamp DESC";
		logger.debug("SELECT count(a.MessageID) FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND b.ToAccountId="
				+ toAccountId + " AND a.Timestamp>" + lastTryTime + " ORDER BY a.Timestamp DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { toAccountId, lastTryTime }, pageNum,
				pageSize, new ParameterizedRowMapper<MessageInlinecast>()
				{
					public MessageInlinecast mapRow(ResultSet rs, int i)
							throws SQLException
					{
						MessageInlinecast data = new MessageInlinecast();
						data.set_id(rs.getString(1));
						data.setContentType(rs.getShort(2));
						data.setFromAccountId(rs.getString(3));
						data.setFromAccountName(rs.getString(4));
						data.setChannelType(rs.getShort(5));
						data.setChannelId(rs.getString(6));
						data.setTimestamp(rs.getInt(7));

						Blob blob = rs.getBlob(8);
						if (blob != null && blob.length() > 0) {
							byte[] bytes = blob.getBytes(1, (int) blob.length());
							// byte[] decoded = Base64.decodeBase64(bytes);
							data.setContent(new String(bytes));
						}

						data.setSyncState(rs.getShort(9));
						data.setAttachUrl(rs.getString(10));
						data.setCurrentTimestamp(currentTimestamp);

						return data;
					}
				});
	}

	public List<MessageInlinecast> queryByState(final String toAccountId, final int timestamp, final short state)
	{
		// a json array is part of the json-lib API
		final List<MessageInlinecast> array = new ArrayList<MessageInlinecast>();

		String sql = "SELECT a.MessageID,a.MessageType,a.FromAccountID,a.FromAccountName,a.ToType,a.ToId,b.Timestamp,a.MessageTxt,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND b.ToAccountId=? AND b.Timestamp>? AND b.SyncState=?";
		logger.debug("SELECT a.MessageID,a.MessageType,a.FromAccountID,a.FromAccountName,a.ToType,a.ToId,b.Timestamp,a.MessageTxt,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND b.ToAccountId="
				+ toAccountId + " AND b.Timestamp>" + timestamp + " AND b.SyncState=" + state);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, toAccountId);
				ps.setInt(2, timestamp);
				ps.setShort(3, state);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				MessageInlinecast data = new MessageInlinecast();

				data.set_id(rs.getString(1));
				data.setContentType(rs.getShort(2));
				data.setFromAccountId(rs.getString(3));
				data.setFromAccountName(rs.getString(4));
				data.setChannelType(rs.getShort(5));
				data.setChannelId(rs.getString(6));
				data.setTimestamp(rs.getInt(7));

				Blob blob = rs.getBlob(8);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setContent(new String(bytes));
				}

				data.setAttachUrl(rs.getString(9));
				data.setSyncState(state);

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public List<BadgeNumberJsonBean> queryUnreadNumberByChat(final String toAccountId, final int timestamp)
	{
		// a json array is part of the json-lib API
		final List<BadgeNumberJsonBean> array = new ArrayList<BadgeNumberJsonBean>();

		String sql = "SELECT o.ToId, count(o.MessageID) FROM cscart_ga_message_original o,cscart_ga_message_extend e WHERE o.MessageID=e.MessageID AND e.ToAccountId=? AND e.SyncState=? AND e.Timestamp>? GROUP BY o.ToID";
		logger.debug("SELECT o.ToId, count(o.MessageID) FROM cscart_ga_message_original o,cscart_ga_message_extend e WHERE o.MessageID=e.MessageID AND e.ToAccountId="
				+ toAccountId
				+ " AND e.SyncState="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED
				+ " AND e.Timestamp>"
				+ timestamp + " GROUP BY o.ToID");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, toAccountId);
				ps.setShort(2, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
				ps.setInt(3, timestamp);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				BadgeNumberJsonBean data = new BadgeNumberJsonBean();
				data.setId(rs.getString(1));
				data.setNumber(rs.getInt(2));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public MessageInlinecast queryLastOneByChat(final String chatId, final String toAccountId, final int timestamp)
	{
		final MessageInlinecast data = new MessageInlinecast();

		String sql = "SELECT a.MessageID,a.MessageType,a.FromAccountID,a.FromAccountName,a.ToType,a.Timestamp,a.MessageTxt,b.SyncState,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.ToId=? AND b.ToAccountId=? AND a.Timestamp>=? ORDER BY a.Timestamp DESC LIMIT 1";
		logger.debug("SELECT a.MessageID,a.MessageType,a.FromAccountID,a.FromAccountName,a.ToType,a.Timestamp,a.MessageTxt,b.SyncState,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.ToId="
				+ chatId
				+ " AND b.ToAccountId="
				+ toAccountId
				+ " AND a.Timestamp>="
				+ timestamp
				+ " ORDER BY a.Timestamp DESC LIMIT 1");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, chatId);
				ps.setString(2, toAccountId);
				ps.setInt(3, timestamp);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.set_id(rs.getString(1));
				data.setContentType(rs.getShort(2));
				data.setFromAccountId(rs.getString(3));
				data.setFromAccountName(rs.getString(4));
				data.setChannelType(rs.getShort(5));
				data.setChannelId(chatId);
				data.setTimestamp(rs.getInt(6));

				Blob blob = rs.getBlob(7);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setContent(new String(bytes));
				}

				data.setSyncState(rs.getShort(8));
				data.setAttachUrl(rs.getString(9));
			}
		});

		return data;
	}

	@Override
	public MessageInlinecast queryLastUnreadByChat(final String chatId, final String toAccountId, final int timestamp)
	{
		final MessageInlinecast data = new MessageInlinecast();

		String sql = "SELECT a.MessageID,a.MessageType,a.FromAccountID,a.FromAccountName,a.ToType,a.Timestamp,a.MessageTxt,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.ToId=? AND b.ToAccountId=? AND b.SyncState=? AND a.Timestamp>? ORDER BY a.Timestamp ASC";
		logger.debug("SELECT a.MessageID,a.MessageType,a.FromAccountID,a.FromAccountName,a.ToType,a.Timestamp,a.MessageTxt,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.ToId="
				+ chatId
				+ " AND b.ToAccountId="
				+ toAccountId
				+ " AND b.SyncState="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED + " AND a.Timestamp>" + timestamp + " ORDER BY a.Timestamp ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, chatId);
				ps.setString(2, toAccountId);
				ps.setShort(3, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
				ps.setInt(4, timestamp);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.set_id(rs.getString(1));
				data.setContentType(rs.getShort(2));
				data.setFromAccountId(rs.getString(3));
				data.setFromAccountName(rs.getString(4));
				data.setChannelType(rs.getShort(5));
				data.setChannelId(chatId);
				data.setTimestamp(rs.getInt(6));

				Blob blob = rs.getBlob(7);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setContent(new String(bytes));
				}

				data.setSyncState(GlobalArgs.SYNC_STATE_NOT_RECEIVED);
				data.setAttachUrl(rs.getString(8));
			}
		});

		return data;
	}

	// @Override
	// public MessageInlinecast query(final String msgId)
	// {
	// final MessageInlinecast data = new MessageInlinecast();
	//
	// String sql =
	// "SELECT a.MessageID,a.MessageType,a.FromAccountID,a.FromAccountName,a.ToId,a.ToType,a.Timestamp,a.MessageTxt,b.SyncState,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.MessageId=?";
	// logger.debug("SELECT a.MessageID,a.MessageType,a.FromAccountID,a.FromAccountName,a.ToId,a.ToType,a.Timestamp,a.MessageTxt,b.SyncState,a.AttachId FROM cscart_ga_message_original a, cscart_ga_message_extend b WHERE a.MessageId=b.MessageId AND a.MessageId="
	// + msgId);
	//
	// this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
	// {
	// public void setValues(PreparedStatement ps)
	// throws SQLException
	// {
	// ps.setString(1, msgId);
	// }
	// }, new RowCallbackHandler()
	// {
	// public void processRow(ResultSet rs)
	// throws SQLException
	// {
	// int i = 1;
	//
	// data.set_id(rs.getString(i++));
	// data.setContentType(rs.getShort(i++));
	// data.setFromAccountId(rs.getString(i++));
	// data.setFromAccountName(rs.getString(i++));
	// data.setToChannelId(rs.getString(i++));
	// data.setToChannelType(rs.getShort(i++));
	// data.setTimestamp(rs.getInt(i++));
	//
	// if (PropArgs.jdbcDriver.equals("org.sqlite.JDBC")) {
	// byte[] bytes = rs.getBytes(i++);
	// data.setContent(new String(bytes));
	// } else { // mysql
	// Blob blob = rs.getBlob(i++);
	// if (blob != null && blob.length() > 0) {
	// byte[] bytes = blob.getBytes(1, (int) blob.length());
	// // byte[] decoded = Base64.decodeBase64(bytes);
	// data.setContent(new String(bytes));
	// }
	// }
	//
	// data.setSyncState(rs.getShort(i++));
	// data.setAttachUrl(rs.getString(i++));
	// }
	// });
	//
	// return data;
	// }

	private static final Logger logger = LoggerFactory.getLogger(MessageDaoImpl.class);

}
// End of Dao class for OriginalMsg