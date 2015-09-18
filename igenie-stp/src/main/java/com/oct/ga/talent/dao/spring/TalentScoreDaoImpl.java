package com.oct.ga.talent.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.talent.TalentInfo;
import com.oct.ga.comm.domain.talent.TalentScore;
import com.oct.ga.stp.utility.PaginationHelper;
import com.oct.ga.talent.dao.GaTalentScoreDao;

public class TalentScoreDaoImpl
		extends JdbcDaoSupport
		implements GaTalentScoreDao
{
	@Override
	public void add(final String accountId, final int timestamp)
	{
		String sql = "INSERT INTO ga_talent_score (account_id,create_time,last_update_time) VALUES (?,?,?)";
		logger.debug("INSERT INTO ga_talent_score (account_id,create_time,last_update_time) VALUES (" + accountId + ","
				+ timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;
				ps.setString(n++, accountId);
				ps.setInt(n++, timestamp);
				ps.setInt(n++, timestamp);
			}
		});
	}

	@Override
	public boolean isExist(String accountId)
	{
		String sql = "SELECT count(account_id) FROM ga_talent_score WHERE account_id=?";
		logger.debug("SELECT count(account_id) FROM ga_talent_score WHERE account_id=" + accountId);

		Object[] params = new Object[] { accountId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public int queryVotedNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT voted_num FROM ga_talent_score WHERE account_id=?";
			logger.debug("SELECT voted_num FROM ga_talent_score WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT voted_num FROM ga_talent_score WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return count;
	}

	@Override
	public void modifyVotedNum(final String accountId, final int num, final int timestamp)
	{
		String sql = "UPDATE ga_talent_score SET voted_num=?,last_update_time=? WHERE account_id=?";
		logger.debug("UPDATE ga_talent_score SET voted_num=" + num + ",last_update_time=" + timestamp
				+ " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setInt(2, timestamp);
				ps.setString(3, accountId);
			}
		});
	}

	@Override
	public int queryMaxPosition()
	{
		String sql = "SELECT MAX(position) FROM ga_talent_score";
		logger.debug("SELECT MAX(position) FROM ga_talent_score");

		int count = this.getJdbcTemplate().queryForInt(sql);
		return count;
	}

	@Override
	public int queryPosition(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT position FROM ga_talent_score WHERE account_id=?";
			logger.debug("SELECT position FROM ga_talent_score WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT position FROM ga_talent_score WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return count;
	}

	@Override
	public void modifyPosition(final String accountId, final int num, final int timestamp)
	{
		String sql = "UPDATE ga_talent_score SET position=?,last_update_time=? WHERE account_id=?";
		logger.debug("UPDATE ga_talent_score SET position=" + num + ",last_update_time=" + timestamp
				+ " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setInt(2, timestamp);
				ps.setString(3, accountId);
			}
		});
	}

	@Override
	public List<TalentScore> queryTalentlistOrderByPosition(final int votedNum)
	{
		final List<TalentScore> array = new ArrayList<TalentScore>();

		String sql = "SELECT account_id,position FROM ga_talent_score WHERE voted_num=? ORDER BY position ASC";
		logger.debug("SELECT account_id,position FROM ga_talent_score WHERE voted_num=" + votedNum
				+ " ORDER BY position ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, votedNum);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				TalentScore data = new TalentScore();

				data.setAccountId(rs.getString(1));
				data.setPosition(rs.getInt(2));
				data.setVotedNum(votedNum);

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public TalentScore select(final String accountId)
	{
		final TalentScore data = new TalentScore();

		String sql = "SELECT account_id,position,voted_num FROM ga_talent_score WHERE account_id=?";
		logger.debug("SELECT account_id,position,voted_num FROM ga_talent_score WHERE account_id=" + accountId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, accountId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setAccountId(rs.getString(1));
				data.setPosition(rs.getInt(2));
				data.setVotedNum(rs.getInt(3));
			}
		});

		return data;
	}

	@Override
	public Page<TalentInfo> select(int pageNum, int pageSize)
	{
		PaginationHelper<TalentInfo> ph = new PaginationHelper<TalentInfo>();

		String countSql = "SELECT count(account_id) FROM ga_talent_score";
		String sql = "SELECT account_id,position,voted_num FROM ga_talent_score" + " ORDER BY position ASC";
		logger.debug("SELECT account_id,position,voted_num FROM ga_talent_score" + " ORDER BY position ASC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] {}, pageNum, pageSize,
				new ParameterizedRowMapper<TalentInfo>()
				{
					public TalentInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						TalentInfo data = new TalentInfo();

						data.setAccountId(rs.getString(1));
						data.setPosition(rs.getInt(2));
						data.setVotedNum(rs.getInt(3));

						return data;
					}
				});
	}

	@Override
	public int queryFollowingNum(String accountId)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryFollowedNum(String accountId)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryVoteNum(String accountId)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void modifyFollowingNum(String accountId, int count, int timestamp)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyFollowedNum(String accountId, int count, int timestamp)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyVoteNum(String accountId, int count, int timestamp)
	{
		// TODO Auto-generated method stub

	}

	private final static Logger logger = LoggerFactory.getLogger(TalentScoreDaoImpl.class);

}
