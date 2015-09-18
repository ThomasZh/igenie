package com.oct.ga.talent.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.talent.TalentInfo;
import com.oct.ga.stp.utility.PaginationHelper;
import com.oct.ga.talent.dao.GaTalentVoteDao;

public class TalentVoteDaoImpl
		extends JdbcDaoSupport
		implements GaTalentVoteDao
{
	@Override
	public void add(final String accountId, final String voteAccountId, final int timestamp)
	{
		String sql = "INSERT INTO ga_talent_vote (account_id,vote_account_id,create_time,last_update_time) VALUES (?,?,?,?)";
		logger.debug("INSERT INTO ga_talent_vote (account_id,vote_account_id,create_time,last_update_time) VALUES ("
				+ accountId + "," + voteAccountId + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;
				ps.setString(n++, accountId);
				ps.setString(n++, voteAccountId);
				ps.setInt(n++, timestamp);
				ps.setInt(n++, timestamp);
			}
		});
	}

	@Override
	public boolean isExist(String accountId, String voteAccountId)
	{
		String sql = "SELECT count(account_id) FROM ga_talent_vote WHERE account_id=? AND vote_account_id=?";
		logger.debug("SELECT count(account_id) FROM ga_talent_vote WHERE account_id=" + accountId
				+ " AND vote_account_id=" + voteAccountId);

		Object[] params = new Object[] { accountId, voteAccountId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public Page<TalentInfo> selectVote(String accountId, int pageNum, int pageSize)
	{
		PaginationHelper<TalentInfo> ph = new PaginationHelper<TalentInfo>();

		String countSql = "SELECT count(vote_account_id) FROM ga_talent_vote WHERE account_id=?";
		String sql = "SELECT vote_account_id FROM ga_talent_vote" + " WHERE account_id=? ORDER BY last_update_time DESC";
		logger.debug("SELECT vote_account_id FROM ga_talent_vote" + " WHERE account_id=" + accountId
				+ " ORDER BY last_update_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { accountId }, pageNum, pageSize,
				new ParameterizedRowMapper<TalentInfo>()
				{
					public TalentInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						TalentInfo data = new TalentInfo();

						data.setAccountId(rs.getString(1));

						return data;
					}
				});
	}

	private final static Logger logger = LoggerFactory.getLogger(TalentVoteDaoImpl.class);

}
