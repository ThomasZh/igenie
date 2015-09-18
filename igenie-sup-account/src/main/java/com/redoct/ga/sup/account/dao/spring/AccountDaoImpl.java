package com.redoct.ga.sup.account.dao.spring;

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

import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.redoct.ga.sup.account.dao.SupAccountDao;

public class AccountDaoImpl
		extends JdbcDaoSupport
		implements SupAccountDao
{
	@Override
	public void add(final String accountId, final String nickname, final String avatarUrl, final String desc,
			final int timestamp)
	{
		String sql = "INSERT INTO ga_account_base (account_id,nickname,avatar_url,account_desc,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_account_base (account_id,nickname,avatar_url,account_desc,create_time,last_update_time) VALUES ("
				+ accountId + "," + nickname + "," + avatarUrl + "," + desc + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, accountId);
				ps.setString(i++, nickname);
				ps.setString(i++, avatarUrl);
				ps.setString(i++, desc);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public AccountBasic queryBasic(final String accountId)
	{
		final AccountBasic data = new AccountBasic();

		String sql = "SELECT account_id,nickname,avatar_url,account_desc FROM ga_account_base WHERE account_id=?";
		logger.debug("SELECT account_id,nickname,avatar_url,account_desc FROM ga_account_base WHERE account_id="
				+ accountId);

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
				int i = 1;

				data.setAccountId(rs.getString(i++));
				data.setNickname(rs.getString(i++));
				data.setAvatarUrl(rs.getString(i++));
				data.setDesc(rs.getString(i++));
			}
		});

		return data;
	}

	@Override
	public AccountMaster queryMaster(final String accountId)
	{
		final AccountMaster data = new AccountMaster();

		String sql = "SELECT account_id,nickname,avatar_url,account_desc,lang_code FROM ga_account_base WHERE account_id=?";
		logger.debug("SELECT account_id,nickname,avatar_url,account_desc,lang_code FROM ga_account_base WHERE account_id="
				+ accountId);

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
				int i = 1;

				data.setAccountId(rs.getString(i++));
				data.setNickname(rs.getString(i++));
				data.setAvatarUrl(rs.getString(i++));
				data.setDesc(rs.getString(i++));
				data.setLang(rs.getString(i++));
			}
		});

		return data;
	}

	@Override
	public void updateState(final String accountId, final short state, final int timestamp)
	{
		String sql = "UPDATE ga_account_base SET state=?,last_update_time=? WHERE account_id=?";
		logger.debug("UPDATE ga_account_base SET state=" + state + ",last_update_time=" + timestamp
				+ " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
				ps.setString(i++, accountId);
			}
		});
	}

	@Override
	public void updateNickname(final String accountId, final String nickname, final int timestamp)
	{
		String sql = "UPDATE ga_account_base SET nickname=?,last_update_time=? WHERE account_id=?";
		logger.debug("UPDATE ga_account_base SET nickname=" + nickname + ",last_update_time=" + timestamp
				+ " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, nickname);
				ps.setInt(i++, timestamp);
				ps.setString(i++, accountId);
			}
		});
	}

	@Override
	public void updateDesc(final String accountId, final String desc, final int timestamp)
	{
		String sql = "UPDATE ga_account_base SET account_desc=?,last_update_time=? WHERE account_id=?";
		logger.debug("UPDATE ga_account_base SET account_desc=" + desc + ",last_update_time=" + timestamp
				+ " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, desc);
				ps.setInt(i++, timestamp);
				ps.setString(i++, accountId);
			}
		});
	}

	@Override
	public void updateAvatarUrl(final String accountId, final String avatarUrl, final int timestamp)
	{
		String sql = "UPDATE ga_account_base SET avatar_url=?,last_update_time=? WHERE account_id=?";
		logger.debug("UPDATE ga_account_base SET avatar_url=" + avatarUrl + ",last_update_time=" + timestamp
				+ " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, avatarUrl);
				ps.setInt(i++, timestamp);
				ps.setString(i++, accountId);
			}
		});
	}

	@Override
	public List<AccountBasic> queryAllBasic()
	{
		final List<AccountBasic> array = new ArrayList<AccountBasic>();

		String sql = "SELECT account_id,nickname,avatar_url,account_desc FROM ga_account_base";
		logger.debug("SELECT account_id,nickname,avatar_url,account_desc FROM ga_account_base");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				AccountBasic data = new AccountBasic();
				int i = 1;

				data.setAccountId(rs.getString(i++));
				data.setNickname(rs.getString(i++));
				data.setAvatarUrl(rs.getString(i++));
				data.setDesc(rs.getString(i++));

				array.add(data);
			}
		});

		return array;
	}

	private final static Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

}
