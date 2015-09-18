package com.redoct.ga.sup.account.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.redoct.ga.sup.account.dao.SupEkeyDao;
import com.redoct.ga.sup.account.domain.LostPwdEkey;
import com.redoct.ga.sup.account.domain.VerificationCode;

public class EkeyDaoImpl
		extends JdbcDaoSupport
		implements SupEkeyDao
{
	@Override
	public void addEkey(final String ekey, final String accountId, final short loginType, final String loginName,
			final int ttl)
	{
		String sql = "INSERT INTO ga_ekey_lost_pwd (ekey,account_id,login_type,login_name,ttl) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO ga_ekey_lost_pwd (ekey,account_id,login_type,login_name,ttl) VALUES (" + ekey + ","
				+ accountId + "," + loginType + "," + loginName + "," + ttl + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			@Override
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, ekey);
				ps.setString(i++, accountId);
				ps.setShort(i++, loginType);
				ps.setString(i++, loginName);
				ps.setInt(i++, ttl);
			}
		});
	}

	@Override
	public LostPwdEkey query(final String ekey)
	{
		final LostPwdEkey data = new LostPwdEkey();

		String sql = "SELECT account_id,login_type,login_name,ttl FROM ga_ekey_lost_pwd WHERE ekey=?";
		logger.debug("SELECT account_id,login_type,login_name,ttl FROM ga_ekey_lost_pwd WHERE ekey=" + ekey);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, ekey);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int i = 1;
				data.setAccountId(rs.getString(i++));
				data.setLoginType(rs.getShort(i++));
				data.setLoginName(rs.getString(i++));
				data.setTtl(rs.getInt(i++));
			}
		});

		return data;
	}

	@Override
	public void remove(final String ekey)
	{
		String sql = "DELETE FROM ga_ekey_lost_pwd WHERE ekey=?";
		logger.debug("DELETE FROM ga_ekey_lost_pwd WHERE ekey=" + ekey);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, ekey);
			}
		});
	}

	@Override
	public void addVerificatonCode(final short type, final String deviceId, final String phone, final String ekey,
			final int ttl, final int timestamp)
	{
		String sql = "INSERT INTO ga_verification_code (verification_type,device_id,phone,ekey,ttl,create_time,last_update_time) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_verification_code (verification_type,device_id,phone,ekey,ttl,create_time,last_update_time) VALUES ("
				+ type
				+ ","
				+ deviceId
				+ ","
				+ phone
				+ ","
				+ ekey
				+ ","
				+ ttl
				+ ","
				+ timestamp
				+ ","
				+ timestamp
				+ ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			@Override
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setShort(i++, type);
				ps.setString(i++, deviceId);
				ps.setString(i++, phone);
				ps.setString(i++, ekey);
				ps.setInt(i++, ttl);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void modifyVerificatonCode(final short type, final String deviceId, final String phone, final String ekey,
			final int ttl, final int timestamp, final int count)
	{
		String sql = "UPDATE ga_verification_code SET phone=?,ekey=?,ttl=?,last_update_time=?,count=? WHERE verification_type=? AND device_id=?";
		logger.debug("UPDATE ga_verification_code SET phone=" + phone + ",ekey=" + ekey + ",ttl=" + ttl
				+ ",last_update_time=" + timestamp + ",count=" + count + " WHERE verification_type=" + type
				+ " AND device_id=" + deviceId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, phone);
				ps.setString(i++, ekey);
				ps.setInt(i++, ttl);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, count);
				ps.setShort(i++, type);
				ps.setString(i++, deviceId);
			}
		});
	}

	@Override
	public boolean isExist(short type, String deviceId)
	{
		String sql = "SELECT count(device_id) FROM ga_verification_code WHERE verification_type=? AND device_id=?";
		logger.debug("SELECT count(device_id) FROM ga_verification_code WHERE verification_type=" + type
				+ " AND device_id=" + deviceId);

		Object[] params = new Object[] { type, deviceId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public VerificationCode query(final short type, final String deviceId)
	{
		final VerificationCode data = new VerificationCode();

		String sql = "SELECT verification_type,device_id,phone,ekey,ttl,count FROM ga_verification_code WHERE verification_type=? AND device_id=?";
		logger.debug("SELECT verification_type,device_id,phone,ekey,ttl,count FROM ga_verification_code WHERE verification_type="
				+ type + " AND device_id=" + deviceId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, type);
				ps.setString(2, deviceId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int i = 1;

				data.setType(rs.getShort(i++));
				data.setDeviceId(rs.getString(i++));
				data.setPhone(rs.getString(i++));
				data.setEkey(rs.getString(i++));
				data.setTtl(rs.getInt(i++));
				data.setCount(rs.getInt(i++));
			}
		});

		return data;
	}

	@Override
	public void remove(final short type, final String deviceId)
	{
		String sql = "DELETE FROM ga_verification_code WHERE verification_type=? AND device_id=?";
		logger.debug("DELETE FROM ga_verification_code WHERE verification_type=" + type + " AND device_id=" + deviceId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, type);
				ps.setString(2, deviceId);
			}
		});
	}

	private final static Logger logger = LoggerFactory.getLogger(EkeyDaoImpl.class);

}
