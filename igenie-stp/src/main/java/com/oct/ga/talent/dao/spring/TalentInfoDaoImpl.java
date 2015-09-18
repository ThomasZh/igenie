package com.oct.ga.talent.dao.spring;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.talent.dao.GaTalentInfoDao;

public class TalentInfoDaoImpl
		extends JdbcDaoSupport
		implements GaTalentInfoDao
{
	@Override
	public void add(final String accountId, final int timestamp)
	{
		String sql = "INSERT INTO ga_talent_info (account_id,create_time,last_update_time) VALUES (?,?,?)";
		logger.debug("INSERT INTO ga_talent_info (account_id,create_time,last_update_time) VALUES (" + accountId + ","
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
		String sql = "SELECT count(account_id) FROM ga_talent_info WHERE account_id=?";
		logger.debug("SELECT count(account_id) FROM ga_talent_info WHERE account_id=" + accountId);

		Object[] params = new Object[] { accountId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	private final static Logger logger = LoggerFactory.getLogger(TalentInfoDaoImpl.class);
}
