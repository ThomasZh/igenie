package com.oct.ga.message.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.msg.MsgLastCacheJsonBean;
import com.oct.ga.message.dao.MessageCacheDao;

public class MessageCacheDaoImpl
		extends JdbcDaoSupport
		implements MessageCacheDao
{
	@Override
	public boolean isExist(String chatId, String toAccountId)
	{
		String sql = "SELECT count(msg_id) FROM ga_message_last_cache WHERE chat_id=? AND to_account_id=?";
		logger.debug("SELECT count(msg_id) FROM ga_message_last_cache WHERE WHERE chat_id=" + chatId
				+ " AND to_account_id=" + toAccountId);

		Object[] params = new Object[] { chatId, toAccountId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public short countCacheBadgeNum(String chatId, String toAccountId)
	{
		String sql = "SELECT count(msg_id) FROM ga_message_extend WHERE chat_id=? AND to_account_id=? AND sync_state=?";
		logger.debug("SELECT count(msg_id) FROM ga_message_extend WHERE chat_id=" + chatId + " AND to_account_id="
				+ toAccountId + " AND sync_state=" + GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		Object[] params = new Object[] { chatId, toAccountId, GlobalArgs.SYNC_STATE_NOT_RECEIVED };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return (short) count;
	}

	@Override
	public void add(final String chatId, final String toAccountId, final String msgId, final short badgeNum,
			final int timestamp)
	{
		String sql = "INSERT INTO ga_message_last_cache (chat_id,to_account_id,msg_id,badge_num,timestamp) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO ga_message_last_cache (chat_id,to_account_id,msg_id,badge_num,timestamp) VALUES ("
				+ chatId + "," + toAccountId + "," + msgId + "," + badgeNum + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, chatId);
				ps.setString(i++, toAccountId);
				ps.setString(i++, msgId);
				ps.setShort(i++, badgeNum);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void update(final String chatId, final String toAccountId, final String msgId, final short badgeNum,
			final int timestamp)
	{
		String sql = "UPDATE ga_message_last_cache SET msg_id=?,badge_num=?,timestamp=? WHERE chat_id=? AND to_account_id=?";
		logger.debug("UPDATE ga_message_last_cache SET msg_id=" + msgId + ",badge_num=" + badgeNum + ",timestamp="
				+ timestamp + " WHERE chat_id=" + chatId + " AND to_account_id=" + toAccountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, msgId);
				ps.setShort(i++, badgeNum);
				ps.setInt(i++, timestamp);
				ps.setString(i++, chatId);
				ps.setString(i++, toAccountId);
			}
		});
	}

	@Override
	public void update(final String chatId, final String toAccountId, final short badgeNum, final int timestamp)
	{
		String sql = "UPDATE ga_message_last_cache SET badge_num=?,timestamp=? WHERE chat_id=? AND to_account_id=?";
		logger.debug("UPDATE ga_message_last_cache SET badge_num=" + badgeNum + ",timestamp=" + timestamp
				+ " WHERE chat_id=" + chatId + " AND to_account_id=" + toAccountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setShort(i++, badgeNum);
				ps.setInt(i++, timestamp);
				ps.setString(i++, chatId);
				ps.setString(i++, toAccountId);
			}
		});
	}

	@Override
	public List<MsgLastCacheJsonBean> query(final String toAccountId, final int lastTryTime)
	{
		final List<MsgLastCacheJsonBean> array = new ArrayList<MsgLastCacheJsonBean>();

		String sql = "SELECT chat_id,msg_id,badge_num FROM ga_message_last_cache WHERE badge_num>0 AND to_account_id=? AND timestamp>?";
		logger.debug("SELECT chat_id,msg_id,badge_num FROM ga_message_last_cache WHERE badge_num>0 AND to_account_id="
				+ toAccountId + " AND timestamp>" + lastTryTime);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, toAccountId);
				ps.setInt(2, lastTryTime);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				MsgLastCacheJsonBean data = new MsgLastCacheJsonBean();

				int i = 1;
				data.setChatId(rs.getString(i++));
				data.setMsgId(rs.getString(i++));
				data.setBadgeNum(rs.getShort(i++));

				array.add(data);
			}
		});

		return array;
	}

	private static final Logger logger = LoggerFactory.getLogger(MessageCacheDaoImpl.class);

}
