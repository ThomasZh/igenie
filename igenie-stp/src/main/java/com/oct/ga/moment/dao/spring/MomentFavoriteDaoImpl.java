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
import com.oct.ga.comm.domain.moment.GaMomentFavoriteObject;
import com.oct.ga.moment.dao.GaMomentFavoriteDao;
import com.oct.ga.stp.utility.PaginationHelper;

public class MomentFavoriteDaoImpl
		extends JdbcDaoSupport
		implements GaMomentFavoriteDao
{
	@Override
	public void add(final String momentId, final String accountId, final int timestamp)
	{
		String sql = "INSERT INTO ga_moment_favorite (moment_id,account_id,create_time,last_update_time) VALUES (?,?,?,?)";
		logger.debug("INSERT INTO ga_moment_favorite (moment_id,account_id,create_time,last_update_time) VALUES ("
				+ momentId + "," + accountId + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;
				ps.setString(n++, momentId);
				ps.setString(n++, accountId);
				ps.setInt(n++, timestamp);
				ps.setInt(n++, timestamp);
			}
		});
	}

	@Override
	public boolean isExist(String momentId, String accountId)
	{
		String sql = "SELECT count(account_id) FROM ga_moment_favorite WHERE moment_id=? AND account_id=?";
		logger.debug("SELECT count(account_id) FROM ga_moment_favorite WHERE moment_id=" + momentId
				+ " AND account_id=" + accountId);

		Object[] params = new Object[] { momentId, accountId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public Page<GaMomentFavoriteObject> queryPagination(final String momentId, short pageNum, short pageSize)
	{
		PaginationHelper<GaMomentFavoriteObject> ph = new PaginationHelper<GaMomentFavoriteObject>();
		String countSql = "SELECT count(moment_id) FROM ga_moment_favorite WHERE moment_id=?";
		String sql = "SELECT account_id,create_time FROM ga_moment_favorite WHERE moment_id=? ORDER BY create_time DESC";
		logger.debug("SELECT account_id,create_time FROM ga_moment_favorite WHERE moment_id=" + momentId
				+ " ORDER BY create_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { momentId }, pageNum, pageSize,
				new ParameterizedRowMapper<GaMomentFavoriteObject>()
				{
					public GaMomentFavoriteObject mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GaMomentFavoriteObject data = new GaMomentFavoriteObject();
						int n = 1;

						data.setMomentId(momentId);
						data.setFromAccountId(rs.getString(n++));
						data.setTimestamp(rs.getInt(n++));

						return data;
					}
				});
	}

	@Override
	public int countNum(String momentId)
	{
		String sql = "SELECT count(moment_id) FROM ga_moment_favorite WHERE moment_id=?";
		logger.debug("SELECT count(moment_id) FROM ga_moment_favorite WHERE moment_id=" + momentId);

		Object[] params = new Object[] { momentId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count;
	}

	private static final Logger logger = LoggerFactory.getLogger(MomentFavoriteDaoImpl.class);

}
