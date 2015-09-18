package com.oct.ga.task.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.stp.utility.PaginationHelper;
import com.oct.ga.task.dao.GaTaskLogDao;

public class TaskLogDaoImpl
		extends JdbcDaoSupport
		implements GaTaskLogDao
{
	@Override
	public void insert(final GaTaskLog data, final int timestamp)
	{
		String sql = "INSERT INTO ga_task_log (log_id,channel_id,from_account_id,to_action_tag,action_id,create_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_task_log (log_id,channel_id,from_account_id,to_action_tag,action_id,create_time) VALUES ("
				+ data.getLogId()
				+ ","
				+ data.getChannelId()
				+ ","
				+ data.getFromAccountId()
				+ ","
				+ data.getActionTag() + "," + data.getToActionId() + "," + timestamp);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, data.getLogId());
				ps.setString(2, data.getChannelId());// taskId
				ps.setString(3, data.getFromAccountId());
				ps.setShort(4, data.getActionTag());
				ps.setString(5, data.getToActionId());
				ps.setInt(6, timestamp);
			}
		});
	}

	@Override
	public void insert(final String logId, final String toAccountId, final String channelId, final short actionTag,
			final short syncState, final int timestamp)
	{
		String sql = "INSERT INTO ga_task_log_extend (log_id,to_account_id,channel_id,to_action_tag,sync_state,create_time,last_update_time) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_task_log_extend (log_id,to_account_id,channel_id,to_action_tag,sync_state,create_time,last_update_time) VALUES ("
				+ logId
				+ ","
				+ toAccountId
				+ ","
				+ channelId
				+ ","
				+ actionTag
				+ ","
				+ syncState
				+ ","
				+ timestamp
				+ "," + timestamp);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, logId);
				ps.setString(2, toAccountId);
				ps.setString(3, channelId); // taskId
				ps.setShort(4, actionTag);
				ps.setShort(5, syncState);
				ps.setInt(6, timestamp);
				ps.setInt(7, timestamp);
			}
		});
	}

	@Override
	public Page<String> queryLogPagination(String toAccountId, short actionTag, int pageNum, int pageSize)
	{
		PaginationHelper<String> ph = new PaginationHelper<String>();
		String countSql = "SELECT count(log_id) FROM ga_task_log_extend WHERE to_account_id=? AND to_action_tag=?";
		String sql = "SELECT log_id FROM ga_task_log_extend WHERE to_account_id=? AND to_action_tag=? ORDER BY create_time DESC";
		logger.debug("SELECT log_id FROM ga_task_log_extend WHERE to_account_id=" + toAccountId + " AND to_action_tag="
				+ actionTag + " ORDER BY create_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { toAccountId, actionTag }, pageNum,
				pageSize, new ParameterizedRowMapper<String>()
				{
					public String mapRow(ResultSet rs, int i)
							throws SQLException
					{
						String data = rs.getString(1);
						return data;
					}
				});
	}

	@Override
	public Page<String> queryLogPagination(String toAccountId, int pageNum, int pageSize)
	{
		PaginationHelper<String> ph = new PaginationHelper<String>();
		String countSql = "SELECT count(log_id) FROM ga_task_log_extend WHERE to_account_id=?";
		String sql = "SELECT log_id FROM ga_task_log_extend WHERE to_account_id=? ORDER BY create_time DESC";
		logger.debug("SELECT log_id FROM ga_task_log_extend WHERE to_account_id=" + toAccountId
				+ " ORDER BY create_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { toAccountId }, pageNum, pageSize,
				new ParameterizedRowMapper<String>()
				{
					public String mapRow(ResultSet rs, int i)
							throws SQLException
					{
						String data = rs.getString(1);
						return data;
					}
				});
	}

	@Override
	public GaTaskLog query(final String logId)
	{
		final GaTaskLog data = new GaTaskLog();

		String sql = "SELECT channel_id,from_account_id,to_action_tag,action_id,create_time FROM ga_task_log WHERE log_id=?";
		logger.debug("SELECT channel_id,from_account_id,to_action_tag,action_id,create_time FROM ga_task_log WHERE log_id="
				+ logId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, logId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setLogId(logId);
				data.setChannelId(rs.getString(1));
				data.setFromAccountId(rs.getString(2));
				data.setActionTag(rs.getShort(3));
				data.setToActionId(rs.getString(4));
				data.setTimestamp(rs.getInt(5));
			}
		});

		return data;
	}

	private static final Logger logger = LoggerFactory.getLogger(TaskLogDaoImpl.class);

}
