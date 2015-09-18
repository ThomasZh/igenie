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

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.BadgeNumberJsonBean;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.stp.utility.PaginationHelper;
import com.oct.ga.task.dao.GaTaskActivityDao;

/**
 * (NotifyTaskLog) Data Access Object.
 * 
 * @author Thomas.Zhang
 */
public class TaskActivityDaoImpl
		extends JdbcDaoSupport
		implements GaTaskActivityDao
{
	@Override
	public Page<NotifyTaskLog> queryPagination(final String taskId, final String accountId, final int timestamp,
			final int pageNum, final int pageSize)
	{
		PaginationHelper<NotifyTaskLog> ph = new PaginationHelper<NotifyTaskLog>();
		String countSql = "SELECT count(o.ActivityId) FROM cscart_ga_task_activity o,cscart_ga_task_activity_extend e  WHERE o.ActivityId=e.ActivityId AND (o.TaskId=? OR o.TaskPid=?) AND e.ToAccountId=? AND o.Timestamp>?";
		String sql = "SELECT o.ActivityId,o.TaskId,o.TaskName,o.FromAccountId,o.FromAccountName,o.ToAccountId,o.ToAccountName,o.Timestamp,o.State,o.CommandTag,o.TaskPid,e.SyncState FROM cscart_ga_task_activity o,cscart_ga_task_activity_extend e  WHERE o.ActivityId=e.ActivityId AND (o.TaskId=? OR o.TaskPid=?) AND e.ToAccountId=? AND o.Timestamp>? ORDER BY o.Timestamp DESC";
		logger.debug("SELECT o.ActivityId,o.TaskId,o.TaskName,o.FromAccountId,o.FromAccountName,o.ToAccountId,o.ToAccountName,o.Timestamp,o.State,o.CommandTag,o.TaskPid,e.SyncState FROM cscart_ga_task_activity o,cscart_ga_task_activity_extend e  WHERE o.ActivityId=e.ActivityId AND (o.TaskId="
				+ taskId
				+ " OR o.TaskPid="
				+ taskId
				+ ") AND e.ToAccountId="
				+ accountId
				+ " AND o.Timestamp>"
				+ timestamp + " ORDER BY o.Timestamp DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql,
				new Object[] { taskId, taskId, accountId, timestamp }, pageNum, pageSize,
				new ParameterizedRowMapper<NotifyTaskLog>()
				{
					public NotifyTaskLog mapRow(ResultSet rs, int i)
							throws SQLException
					{
						NotifyTaskLog data = new NotifyTaskLog();
						data.set_id(rs.getString(1));
						data.setChannelId(rs.getString(2));// taskId
						data.setChannelName(rs.getString(3));// taskName
						data.setFromAccountId(rs.getString(4));
						data.setFromAccountName(rs.getString(5));
						data.setToAccountId(rs.getString(6));
						data.setToAccountName(rs.getString(7));
						data.setTimestamp(rs.getInt(8));
						data.setActivityState(rs.getShort(9));
						data.setCommandTag(rs.getShort(10));
						data.setTaskPid(rs.getString(11));
						data.setSyncState(rs.getShort(12));

						return data;
					}
				});
	}

	@Override
	public void add(final NotifyTaskLog data)
	{
		String sql = "INSERT INTO cscart_ga_task_activity (ActivityId,TaskId,TaskName,FromAccountId,FromAccountName,ToAccountId,ToAccountName,Timestamp,State,CommandTag,TaskPid) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_ga_task_activity (ActivityId,TaskId,TaskName,FromAccountId,FromAccountName,ToAccountId,ToAccountName,Timestamp,State,CommandTag,TaskPid) VALUES ("
				+ data.get_id()
				+ ","
				+ data.getChannelId()
				+ ","
				+ data.getChannelName()
				+ ","
				+ data.getFromAccountId()
				+ ","
				+ data.getFromAccountName()
				+ ","
				+ data.getToAccountId()
				+ ","
				+ data.getToAccountName()
				+ ","
				+ data.getTimestamp()
				+ ","
				+ data.getActivityState()
				+ ","
				+ data.getCommandTag() + "," + data.getTaskPid());

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, data.get_id());
				ps.setString(2, data.getChannelId());// taskId
				ps.setString(3, data.getChannelName());// taskName
				ps.setString(4, data.getFromAccountId());
				ps.setString(5, data.getFromAccountName());
				ps.setString(6, data.getToAccountId());
				ps.setString(7, data.getToAccountName());
				ps.setInt(8, data.getTimestamp());
				ps.setShort(9, data.getActivityState());
				ps.setShort(10, data.getCommandTag());
				ps.setString(11, data.getTaskPid());
			}
		});
	}

	@Override
	public void addExtend(final String activityId, final String userId, final short state, final int timestamp)
	{
		String sql = "INSERT INTO cscart_ga_task_activity_extend (ActivityId,ToAccountId,Timestamp,SyncState) VALUES (?,?,?,?)";
		logger.debug("INSERT INTO cscart_ga_task_activity_extend (ActivityId,ToAccountId,Timestamp,SyncState) VALUES ("
				+ activityId + "," + userId + "," + timestamp + "," + state + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, activityId);
				ps.setString(2, userId);
				ps.setInt(3, timestamp);
				ps.setShort(4, state);
			}
		});
	}

	@Override
	public Page<NotifyTaskLog> queryPaginationByAccount(String accountId, int timestamp, int pageNum, int pageSize)
	{
		PaginationHelper<NotifyTaskLog> ph = new PaginationHelper<NotifyTaskLog>();
		String countSql = "SELECT count(e.ActivityId) FROM cscart_ga_task_activity a,cscart_ga_task_activity_extend e WHERE e.ToAccountId=? AND e.Timestamp>? AND a.ActivityId=e.ActivityId";
		String sql = "SELECT e.ActivityId,a.TaskId,a.TaskName,a.FromAccountId,a.FromAccountName,a.ToAccountId,a.ToAccountName,e.ToAccountId,e.ToAccountName,e.Timestamp,a.State,a.CommandTag,a.TaskPid,e.SyncState FROM cscart_ga_task_activity a,cscart_ga_task_activity_extend e WHERE e.ToAccountId=? AND o.Timestamp>? AND a.ActivityId=e.ActivityId ORDER BY o.Timestamp DESC";
		logger.debug("SELECT e.ActivityId,a.TaskId,a.TaskName,a.FromAccountId,a.FromAccountName,a.ToAccountId,a.ToAccountName,e.ToAccountId,e.ToAccountName,e.Timestamp,a.State,a.CommandTag,a.TaskPid,e.SyncState FROM cscart_ga_task_activity a,cscart_ga_task_activity_extend e WHERE e.ToAccountId="
				+ accountId
				+ " AND o.Timestamp>"
				+ timestamp
				+ " AND a.ActivityId=e.ActivityId ORDER BY o.Timestamp DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { accountId, timestamp }, pageNum,
				pageSize, new ParameterizedRowMapper<NotifyTaskLog>()
				{
					public NotifyTaskLog mapRow(ResultSet rs, int i)
							throws SQLException
					{
						NotifyTaskLog data = new NotifyTaskLog();
						data.set_id(rs.getString(1));
						data.setChannelId(rs.getString(2));// taskId
						data.setChannelName(rs.getString(3));// taskPid
						data.setFromAccountId(rs.getString(4));
						data.setFromAccountName(rs.getString(5));
						data.setToAccountId(rs.getString(6));
						data.setToAccountName(rs.getString(7));
						data.setSendToAccountId(rs.getString(8));
						data.setSendToAccountName(rs.getString(9));
						data.setTimestamp(rs.getInt(10));
						data.setActivityState(rs.getShort(11));
						data.setCommandTag(rs.getShort(12));
						data.setTaskPid(rs.getString(13));
						data.setSyncState(rs.getShort(14));

						return data;
					}
				});
	}

	@Override
	public BadgeNumberJsonBean queryUnreadNumberByProject(final String projectId, final String accountId,
			final int lastTryTime)
	{
		// a json array is part of the json-lib API
		final BadgeNumberJsonBean data = new BadgeNumberJsonBean();

		String sql = "SELECT count(o.ActivityID) FROM cscart_ga_task_activity o,cscart_ga_task_activity_extend e WHERE o.ActivityID=e.ActivityID AND (o.TaskId=? OR o.TaskPid=?) AND e.ToAccountId=? AND e.SyncState=? AND o.Timestamp>?";
		logger.debug("SELECT count(o.ActivityID) FROM cscart_ga_task_activity o,cscart_ga_task_activity_extend e WHERE o.ActivityID=e.ActivityID AND (o.TaskId="
				+ projectId
				+ " OR o.TaskPid="
				+ projectId
				+ ") AND e.ToAccountId=? AND e.SyncState="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED + " AND o.Timestamp>" + lastTryTime);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, projectId);
				ps.setString(2, projectId);
				ps.setString(3, accountId);
				ps.setShort(4, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
				ps.setInt(5, lastTryTime);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setId(projectId);
				data.setNumber(rs.getInt(1));
			}
		});

		return data;
	}

	@Override
	public NotifyTaskLog queryLastOneByProject(final String projectId, final String toAccountId, final int timestamp)
	{
		final NotifyTaskLog data = new NotifyTaskLog();

		String sql = "SELECT o.ActivityId,o.TaskId,o.TaskName,o.FromAccountId,o.FromAccountName,e.ToAccountId,o.ToAccountName,o.Timestamp,o.State,o.CommandTag,o.TaskPid,e.SyncState "
				+ " FROM cscart_ga_task_activity o, cscart_ga_task_activity_extend e "
				+ " WHERE o.ActivityId=e.ActivityId AND (o.TaskId=? OR o.TaskPid=?) AND e.ToAccountId=? AND o.Timestamp>? "
				+ " ORDER BY o.Timestamp DESC LIMIT 1";
		logger.debug("SELECT o.ActivityId,o.TaskId,o.TaskName,o.FromAccountId,o.FromAccountName,e.ToAccountId,o.ToAccountName,o.Timestamp,o.State,o.CommandTag,o.TaskPid,e.SyncState "
				+ " FROM cscart_ga_task_activity o, cscart_ga_task_activity_extend e "
				+ " WHERE o.ActivityId=e.ActivityId AND (o.TaskId="
				+ projectId
				+ " OR o.TaskPid="
				+ projectId
				+ ") AND e.ToAccountId="
				+ toAccountId
				+ " AND o.Timestamp>"
				+ timestamp
				+ " ORDER BY o.Timestamp DESC LIMIT 1");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, projectId);
				ps.setString(2, projectId);
				ps.setString(3, toAccountId);
				ps.setInt(4, timestamp);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.set_id(rs.getString(1));
				data.setChannelId(rs.getString(2));// taskId
				data.setChannelName(rs.getString(3));// taskName
				data.setFromAccountId(rs.getString(4));
				data.setFromAccountName(rs.getString(5));
				data.setToAccountId(rs.getString(6));
				data.setToAccountName(rs.getString(7));
				data.setTimestamp(rs.getInt(8));
				data.setActivityState(rs.getShort(9));
				data.setCommandTag(rs.getShort(10));
				data.setTaskPid(rs.getString(11));
				data.setSyncState(rs.getShort(12));
			}
		});

		return data;
	}

	@Override
	public void updateExtendState(final String activityId, final String userId, final short syncState,
			final int timestamp)
	{
		String sql = "UPDATE cscart_ga_task_activity_extend SET SyncState=?,Timestamp=? WHERE ActivityID=? AND ToAccountId=?";
		logger.debug("UPDATE cscart_ga_task_activity_extend SET SyncState=" + syncState + ",Timestamp=" + timestamp
				+ " WHERE ActivityID=" + activityId + " AND ToAccountId=" + userId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, syncState);
				ps.setInt(2, timestamp);
				ps.setString(3, activityId);
				ps.setString(4, userId);
			}
		});
	}

	@Override
	public int batchUpdateToSyncState(final String taskId, final String userId, final short state, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_task_activity_extend e,cscart_ga_task_activity a "
				+ " SET e.SyncState=? "
				+ " WHERE a.ActivityID=e.ActivityID AND e.Timestamp<? AND  (a.TaskId=? OR a.TaskPid=?) AND e.ToAccountID=? AND e.SyncState="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED;
		logger.debug("UPDATE cscart_ga_task_activity_extend e,cscart_ga_task_activity a " + " SET e.SyncState=" + state
				+ " WHERE a.ActivityID=e.ActivityID AND e.Timestamp<" + timestamp + " AND  (a.TaskId=" + taskId
				+ " OR a.TaskPid=" + taskId + ") AND e.ToAccountID=? AND e.SyncState="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		int rows = this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, state);
				ps.setInt(2, timestamp);
				ps.setString(3, taskId);
				ps.setString(4, taskId);
				ps.setString(5, userId);
			}
		});
		
		return rows;
	}

	private static final Logger logger = LoggerFactory.getLogger(TaskActivityDaoImpl.class);
}