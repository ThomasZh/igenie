package com.oct.ga.moment.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.moment.GaMomentLogObject;
import com.oct.ga.moment.dao.GaMomentLogDao;
import com.oct.ga.stp.utility.PaginationHelper;

public class MomentLogDaoImpl
		extends JdbcDaoSupport
		implements GaMomentLogDao
{
	@Override
	public void add(final String momentId, final String accountId, final short action, final String txt,
			final String toAccountId, final int timestamp)
	{
		String sql = "INSERT INTO ga_moment_log (moment_id,account_id,action,txt,to_account_id,create_time,last_update_time) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_moment_log (moment_id,account_id,action,txt,to_account_id,create_time,last_update_time) VALUES ("
				+ momentId
				+ ","
				+ accountId
				+ ","
				+ action
				+ ","
				+ txt
				+ ","
				+ toAccountId
				+ ","
				+ timestamp
				+ ","
				+ timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;
				ps.setString(n++, momentId);
				ps.setString(n++, accountId);
				ps.setShort(n++, action);
				ps.setString(n++, txt);
				ps.setString(n++, toAccountId);
				ps.setInt(n++, timestamp);
				ps.setInt(n++, timestamp);
			}
		});
	}

	@Override
	public Page<GaMomentLogObject> queryPagination(String toAccountId, int pageNum, int pageSize)
	{
		PaginationHelper<GaMomentLogObject> ph = new PaginationHelper<GaMomentLogObject>();
		String countSql = "SELECT count(moment_id) FROM ga_moment_log WHERE to_account_id=?";
		String sql = "SELECT moment_id,account_id,action,txt,sync_state,create_time FROM ga_moment_log WHERE to_account_id=? ORDER BY create_time DESC";
		logger.debug("SELECT moment_id,account_id,action,txt,sync_state,create_time FROM ga_moment_log WHERE to_account_id="
				+ toAccountId + " ORDER BY create_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { toAccountId }, pageNum, pageSize,
				new ParameterizedRowMapper<GaMomentLogObject>()
				{
					public GaMomentLogObject mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GaMomentLogObject data = new GaMomentLogObject();
						int n = 1;

						data.setMomentId(rs.getString(n++));
						data.setFromAccountId(rs.getString(n++));
						data.setAction(rs.getShort(n++));
						data.setTxt(rs.getString(n++));
						data.setSyncState(rs.getShort(n++));
						data.setTimestamp(rs.getInt(n++));

						return data;
					}
				});
	}

	@Override
	public int modifySyncState(final String toAccountId, final short syncState)
	{
		String sql = "UPDATE ga_moment_log SET sync_state=? WHERE to_account_id=?";
		logger.debug("UPDATE ga_moment_log SET sync_state=" + syncState + " WHERE to_account_id=" + toAccountId);

		int count = this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, syncState);
				ps.setString(2, toAccountId);
			}
		});

		return count;
	}

	private static final Logger logger = LoggerFactory.getLogger(MomentLogDaoImpl.class);

}
