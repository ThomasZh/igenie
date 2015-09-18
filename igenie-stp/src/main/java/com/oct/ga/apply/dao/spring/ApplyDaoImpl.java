package com.oct.ga.apply.dao.spring;

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

import com.oct.ga.apply.dao.GaApplyDao;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;

public class ApplyDaoImpl
		extends JdbcDaoSupport
		implements GaApplyDao
{
	@Override
	public List<GaApplyStateNotify> queryNotReceived(final String toAccountId)
	{
		final List<GaApplyStateNotify> array = new ArrayList<GaApplyStateNotify>();

		String sql = "SELECT msg_id,from_account_id,to_account_id,channel_id,action,txt,last_update_time "
				+ " FROM ga_apply_state " + " WHERE to_account_id=? AND sync_state=?";
		logger.debug("SELECT msg_id,from_account_id,to_account_id,channel_id,action,txt,last_update_time "
				+ " FROM ga_apply_state " + " WHERE to_account_id=" + toAccountId + " AND s.sync_state="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, toAccountId);
				ps.setShort(2, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				GaApplyStateNotify data = new GaApplyStateNotify();
				int i = 1;

				data.setMsgId(rs.getString(i++));
				data.setFromAccountId(rs.getString(i++));
				data.setToAccountId(rs.getString(i++));
				data.setChannelId(rs.getString(i++));
				data.setAction(rs.getShort(i++));
				data.setTxt(rs.getString(i++));
				data.setTimestamp(rs.getInt(i++));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public void add(final String msgId, final String fromAccountId, final String toAccountId, final String channelId,
			final short action, final String txt, final int timestamp)
	{
		String sql = "INSERT INTO ga_apply_state (msg_id,from_account_id,to_account_id,channel_id,action,txt,create_time,last_update_time) VALUES (?,?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_apply_state (msg_id,from_account_id,to_account_id,channel_id,action,txt,create_time,last_update_time) VALUES ("
				+ msgId
				+ ","
				+ fromAccountId
				+ ","
				+ toAccountId
				+ ","
				+ channelId
				+ ","
				+ action
				+ ","
				+ txt
				+ ","
				+ timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, msgId);
				ps.setString(i++, fromAccountId);
				ps.setString(i++, toAccountId);
				ps.setString(i++, channelId);
				ps.setShort(i++, action);
				ps.setString(i++, txt);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void updateSyncState(final String fromAccountId, final String toAccountId, final String channelId,
			final short state, final int timestamp)
	{
		String sql = "UPDATE ga_apply_state SET sync_state=?,last_update_time=? WHERE from_account_id=? AND to_account_id=? AND channel_id=?";
		logger.debug("UPDATE ga_apply_state SET sync_state=" + state + ",last_update_time=" + timestamp
				+ " WHERE from_account_id=" + fromAccountId + " AND to_account_id=" + toAccountId + " AND channel_id="
				+ channelId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
				ps.setString(i++, fromAccountId);
				ps.setString(i++, toAccountId);
				ps.setString(i++, channelId);
			}
		});
	}

	@Override
	public boolean isExist(String fromAccountId, String toAccountId, String channelId)
	{
		String sql = "SELECT count(*) FROM ga_apply_state WHERE from_account_id=? AND to_account_id=? AND channel_id=?";
		logger.debug("SELECT count(*) FROM ga_apply_state WHERE from_account_id=" + fromAccountId
				+ " AND to_account_id=" + toAccountId + " AND channel_id=" + channelId);

		Object[] params = new Object[] { fromAccountId, toAccountId, channelId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public String queryId(final String fromAccountId, final String toAccountId, final String channelId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT msg_id FROM ga_apply_state WHERE from_account_id=? AND to_account_id=? AND channel_id=?";
		logger.debug("SELECT msg_id FROM ga_apply_state WHERE from_account_id=" + fromAccountId + " AND to_account_id="
				+ toAccountId + " AND channel_id=" + channelId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, fromAccountId);
				ps.setString(2, toAccountId);
				ps.setString(3, channelId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String msgId = rs.getString(1);

				array.add(msgId);
			}
		});

		if (array.size() > 0)
			return array.get(0);
		else
			return "";
	}

	@Override
	public void update(final String fromAccountId, final String toAccountId, final String channelId,
			final short action, final String txt, final int timestamp)
	{
		String sql = "UPDATE ga_apply_state SET sync_state=?,action=?,txt=?,last_update_time=? WHERE from_account_id=? AND to_account_id=? AND channel_id=?";
		logger.debug("UPDATE ga_apply_state SET sync_state=" + GlobalArgs.SYNC_STATE_NOT_RECEIVED + ",action=" + action
				+ ",txt=" + txt + ",last_update_time=" + timestamp + " WHERE from_account_id=" + fromAccountId
				+ " AND to_account_id=" + toAccountId + " AND channel_id=" + channelId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setShort(i++, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
				ps.setShort(i++, action);
				ps.setString(i++, txt);
				ps.setInt(i++, timestamp);
				ps.setString(i++, fromAccountId);
				ps.setString(i++, toAccountId);
				ps.setString(i++, channelId);
			}
		});
	}

	@Override
	public short queryApplyNum(String accountId)
	{
		String sql = "SELECT count(*) FROM ga_apply_state WHERE to_account_id=? AND sync_state=?";
		logger.debug("SELECT count(*) FROM ga_apply_state WHERE to_account_id=" + accountId + " AND sync_state="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		Object[] params = new Object[] { accountId, GlobalArgs.SYNC_STATE_NOT_RECEIVED };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return (short) count;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplyDaoImpl.class);

}
