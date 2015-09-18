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
import com.oct.ga.comm.domain.moment.GaMomentCommentObject;
import com.oct.ga.moment.dao.GaMomentCommentDao;
import com.oct.ga.stp.utility.PaginationHelper;

public class MomentCommentDaoImpl
		extends JdbcDaoSupport
		implements GaMomentCommentDao
{
	@Override
	public void add(final String momentId, final String accountId, final String txt, final int timestamp)
	{
		String sql = "INSERT INTO ga_moment_comment (moment_id,account_id,txt,create_time,last_update_time) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO ga_moment_comment (moment_id,account_id,txt,create_time,last_update_time) VALUES ("
				+ momentId + "," + accountId + "," + txt + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;
				ps.setString(n++, momentId);
				ps.setString(n++, accountId);
				ps.setString(n++, txt);
				ps.setInt(n++, timestamp);
				ps.setInt(n++, timestamp);
			}
		});
	}

	@Override
	public Page<GaMomentCommentObject> queryPagination(final String momentId, short pageNum, short pageSize)
	{
		PaginationHelper<GaMomentCommentObject> ph = new PaginationHelper<GaMomentCommentObject>();
		String countSql = "SELECT count(moment_id) FROM ga_moment_comment WHERE moment_id=?";
		String sql = "SELECT account_id,txt,create_time FROM ga_moment_comment WHERE moment_id=? ORDER BY create_time DESC";
		logger.debug("SELECT account_id,txt,create_time FROM ga_moment_comment WHERE moment_id=" + momentId
				+ " ORDER BY create_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { momentId }, pageNum, pageSize,
				new ParameterizedRowMapper<GaMomentCommentObject>()
				{
					public GaMomentCommentObject mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GaMomentCommentObject data = new GaMomentCommentObject();
						int n = 1;

						data.setMomentId(momentId);
						data.setFromAccountId(rs.getString(n++));
						data.setTxt(rs.getString(n++));
						data.setTimestamp(rs.getInt(n++));

						return data;
					}
				});
	}

	@Override
	public int countNum(String momentId)
	{
		String sql = "SELECT count(moment_id) FROM ga_moment_comment WHERE moment_id=?";
		logger.debug("SELECT count(moment_id) FROM ga_moment_comment WHERE moment_id=" + momentId);

		Object[] params = new Object[] { momentId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count;
	}

	private static final Logger logger = LoggerFactory.getLogger(MomentCommentDaoImpl.class);

}
