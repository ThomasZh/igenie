package com.redoct.ga.sup.account.dao.spring;

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
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.domain.account.LoginInfo;
import com.redoct.ga.sup.account.dao.SupLoginDao;

public class LoginDaoImpl
		extends JdbcDaoSupport
		implements SupLoginDao
{
	@Override
	public boolean isExist(short loginType, String loginName)
	{
		String sql = "SELECT count(account_id) FROM ga_account_sso WHERE login_type=? AND login_name=?";
		logger.debug("SELECT count(account_id) FROM ga_account_sso WHERE login_type=" + loginType + " AND login_name="
				+ loginName);

		Object[] params = new Object[] { loginType, loginName };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public String queryAccountId(short loginType, String loginName)
	{
		try {
			String sql = "SELECT account_id FROM ga_account_sso WHERE login_type=? AND login_name=?";
			logger.debug("SELECT account_id FROM ga_account_sso WHERE login_type=" + loginType + " AND login_name="
					+ loginName);

			Object[] params = new Object[] { loginType, loginName };
			String salt = (String) this.getJdbcTemplate().queryForObject(sql, params, String.class);
			return salt;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	@Override
	public String queryLoginName(String accountId, short loginType)
	{
		try {
			String sql = "SELECT login_name FROM ga_account_sso WHERE login_type=? AND account_id=?";
			logger.debug("SELECT login_name FROM ga_account_sso WHERE login_type=" + loginType + " AND account_id="
					+ accountId);

			Object[] params = new Object[] { loginType, accountId };
			String loginName = (String) this.getJdbcTemplate().queryForObject(sql, params, String.class);
			return loginName;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	@Override
	public boolean isExist(short loginType, String loginName, String ecryptPwd)
	{
		String sql = "SELECT count(account_id) FROM ga_account_sso WHERE login_type=? AND login_name=? AND ecrypt_pwd=?";
		logger.debug("SELECT count(account_id) FROM ga_account_sso WHERE login_type=" + loginType + " AND login_name="
				+ loginName + " AND ecrypt_pwd=?");

		Object[] params = new Object[] { loginType, loginName, ecryptPwd };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public String querySalt(short loginType, String loginName)
	{
		try {
			String sql = "SELECT salt FROM ga_account_sso WHERE login_type=? AND login_name=?";
			logger.debug("SELECT salt FROM ga_account_sso WHERE login_type=" + loginType + " AND login_name="
					+ loginName);

			Object[] params = new Object[] { loginType, loginName };
			String salt = (String) this.getJdbcTemplate().queryForObject(sql, params, String.class);
			return salt;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	@Override
	public void updatePwd(final short loginType, final String loginName, final String ecryptPwd, final String salt,
			final int timestamp)
	{
		String sql = "UPDATE ga_account_sso SET ecrypt_pwd=?,salt=?,last_update_time=? WHERE login_type=? AND login_name=?";
		logger.debug("UPDATE ga_account_sso SET ecrypt_pwd=?,salt=?,last_update_time=" + timestamp
				+ " WHERE login_type=" + loginType + " AND login_name=" + loginName);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, ecryptPwd);
				ps.setString(i++, salt);
				ps.setInt(i++, timestamp);
				ps.setShort(i++, loginType);
				ps.setString(i++, loginName);
			}
		});
	}

	@Override
	public void updateState(final short loginType, final String loginName, final short state, final int timestamp)
	{
		String sql = "UPDATE ga_account_sso SET state=?,last_update_time=? WHERE login_type=? AND login_name=?";
		logger.debug("UPDATE ga_account_sso SET state=?,last_update_time=" + timestamp + " WHERE login_type="
				+ loginType + " AND login_name=" + loginName);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
				ps.setShort(i++, loginType);
				ps.setString(i++, loginName);
			}
		});
	}

	@Override
	public void add(final String accountId, final short loginType, final String loginName, final String ecryptPwd,
			final String salt, final int timestamp)
	{
		String sql = "INSERT INTO ga_account_sso (account_id,login_type,login_name,ecrypt_pwd,salt,create_time,last_update_time) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_account_sso (account_id,login_type,login_name,ecrypt_pwd,salt,create_time,last_update_time) VALUES ("
				+ accountId + "," + loginType + "," + loginName + ",?,?," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, accountId);
				ps.setShort(i++, loginType);
				ps.setString(i++, loginName);
				ps.setString(i++, ecryptPwd);
				ps.setString(i++, salt);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void add(final String accountId, final short loginType, final String loginName, final int timestamp)
	{
		String sql = "INSERT INTO ga_account_sso (account_id,login_type,login_name,create_time,last_update_time) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO ga_account_sso (account_id,login_type,login_name,create_time,last_update_time) VALUES ("
				+ accountId + "," + loginType + "," + loginName + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, accountId);
				ps.setShort(i++, loginType);
				ps.setString(i++, loginName);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void updateAccountId(final short loginType, final String loginName, final String accountId,
			final int timestamp)
	{
		String sql = "UPDATE ga_account_sso SET account_id=?,last_update_time=? WHERE login_type=? AND login_name=?";
		logger.debug("UPDATE ga_account_sso SET account_id=?,last_update_time=" + timestamp + " WHERE login_type="
				+ loginType + " AND login_name=" + loginName);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, accountId);
				ps.setInt(i++, timestamp);
				ps.setShort(i++, loginType);
				ps.setString(i++, loginName);
			}
		});
	}

	@Override
	public List<LoginInfo> selectLoginList(final String accountId)
	{
		final List<LoginInfo> array = new ArrayList<LoginInfo>();

		String sql = "SELECT login_type,login_name FROM ga_account_sso WHERE account_id=?";
		logger.debug("SELECT login_type,login_name FROM ga_account_sso WHERE account_id=" + accountId);

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

				LoginInfo data = new LoginInfo();
				data.setLoginType(rs.getShort(i++));
				data.setLoginName(rs.getString(i++));

				array.add(data);
			}
		});

		return array;
	}

	private final static Logger logger = LoggerFactory.getLogger(LoginDaoImpl.class);

}
