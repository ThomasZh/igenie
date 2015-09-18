package com.oct.ga.invite.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.invite.dao.GaInviteDao;
import com.oct.ga.invite.domain.GaInviteMasterInfo;

public class InviteDaoImpl
		extends JdbcDaoSupport
		implements GaInviteDao
{
	@Override
	public void addOriginal(final String inviteId, final short inviteType, final String fromAccountId,
			final String channelId, final int expiryTime, final int timestamp)
	{
		String sql = "INSERT INTO ga_invite_original (invite_id,invite_type,from_account_id,channel_id,expiry_time,create_time,last_update_time) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_invite_original (invite_id,invite_type,from_account_id,channel_id,expiry_time,create_time,last_update_time) VALUES ("
				+ inviteId
				+ ","
				+ inviteType
				+ ","
				+ fromAccountId
				+ ","
				+ channelId
				+ ","
				+ expiryTime
				+ ","
				+ timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, inviteId);
				ps.setShort(i++, inviteType);
				ps.setString(i++, fromAccountId);
				ps.setString(i++, channelId);
				ps.setInt(i++, expiryTime);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void updateExpiryTime(final String inviteId, final int expiryTime, final int timestamp)
	{
		String sql = "UPDATE ga_invite_original SET expiry_time=?,last_update_time=? WHERE invite_id=?";
		logger.debug("UPDATE ga_invite_original SET expiry_time=" + expiryTime + ",last_update_time=" + timestamp
				+ " WHERE invite_id=" + inviteId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setInt(i++, expiryTime);
				ps.setInt(i++, timestamp);
				ps.setString(i++, inviteId);
			}
		});
	}

	@Override
	public GaInviteMasterInfo queryMaster(final String inviteId)
	{
		final GaInviteMasterInfo data = new GaInviteMasterInfo();

		String sql = "SELECT invite_id,invite_type,from_account_id,channel_id,expiry_time,last_update_time FROM ga_invite_original WHERE invite_id=?";
		logger.debug("SELECT invite_id,invite_type,from_account_id,channel_id,expiry_time,last_update_time FROM ga_invite_original WHERE invite_id="
				+ inviteId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, inviteId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int i = 1;

				data.setInviteId(rs.getString(i++));
				data.setInviteType(rs.getShort(i++));
				data.setFromAccountId(rs.getString(i++));
				data.setChannelId(rs.getString(i++));
				data.setExpiry(rs.getInt(i++));
				data.setTimestamp(rs.getInt(i++));
			}
		});

		return data;
	}

	@Override
	public void addSysSubscribe(final String inviteId, final String toAccontId, final int timestamp)
	{
		String sql = "INSERT INTO ga_invite_subscribe_ga (invite_id,to_account_id,create_time,last_update_time) VALUES (?,?,?,?)";
		logger.debug("INSERT INTO ga_invite_subscribe_ga (invite_id,to_account_id,create_time,last_update_time) VALUES ("
				+ inviteId + "," + toAccontId + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, inviteId);
				ps.setString(i++, toAccontId);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public boolean isExistSysSubscribe(String inviteId, String toAccountId)
	{
		String sql = "SELECT count(invite_id) FROM ga_invite_subscribe_ga WHERE invite_id=? AND to_account_id=?";
		logger.debug("SELECT count(invite_id) FROM ga_invite_subscribe_ga WHERE invite_id=" + inviteId
				+ " AND to_account_id=" + toAccountId);

		Object[] params = new Object[] { inviteId, toAccountId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public void updateSysSubscribeState(final String inviteId, final String toAccontId, final short syncState,
			final int timestamp)
	{
		String sql = "UPDATE ga_invite_subscribe_ga SET sync_state=?,last_update_time=? WHERE invite_id=? AND to_account_id=?";
		logger.debug("UPDATE ga_invite_subscribe_ga SET sync_state=" + syncState + ",last_update_time=" + timestamp
				+ " WHERE invite_id=" + inviteId + " AND to_account_id=" + toAccontId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setInt(i++, syncState);
				ps.setInt(i++, timestamp);
				ps.setString(i++, inviteId);
				ps.setString(i++, toAccontId);
			}
		});
	}

	@Override
	public GaInviteMasterInfo querySysSubscribe(final short inviteType, final String fromAccountId,
			final String toAccontId)
	{
		final GaInviteMasterInfo data = new GaInviteMasterInfo();

		String sql = "SELECT g.invite_id,g.channel_id,g.expiry_time,g.last_update_time "
				+ " FROM ga_invite_original g, ga_invite_subscribe_ga s "
				+ " WHERE g.invite_id=s.invite_id AND g.invite_type=? AND g.from_account_id=? AND s.to_account_id=?";
		logger.debug("SELECT invite_id,channel_id,expiry_time,last_update_time "
				+ " FROM ga_invite_original g, ga_invite_subscribe_ga s "
				+ " WHERE g.invite_id=s.invite_id AND g.invite_type=" + inviteType + " AND g.from_account_id="
				+ fromAccountId + " AND s.to_account_id=" + toAccontId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, inviteType);
				ps.setString(2, fromAccountId);
				ps.setString(3, toAccontId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int i = 1;

				data.setInviteId(rs.getString(i++));
				data.setInviteType(inviteType);
				data.setFromAccountId(fromAccountId);
				data.setChannelId(rs.getString(i++));
				data.setExpiry(rs.getInt(i++));
				data.setTimestamp(rs.getInt(i++));
			}
		});

		return data;
	}

	@Override
	public List<GaInviteMasterInfo> queryNotReceivedInvite(final String toAccountId)
	{
		final List<GaInviteMasterInfo> array = new ArrayList<GaInviteMasterInfo>();

		String sql = "SELECT g.invite_id,g.invite_type,g.from_account_id,g.channel_id,g.expiry_time,g.last_update_time "
				+ " FROM ga_invite_original g, ga_invite_subscribe_ga s "
				+ " WHERE g.invite_id=s.invite_id AND s.to_account_id=? AND s.sync_state=?";
		logger.debug("SELECT invite_id,invite_type,from_account_id,channel_id,expiry_time,last_update_time "
				+ " FROM ga_invite_original g, ga_invite_subscribe_ga s "
				+ " WHERE g.invite_id=s.invite_id AND s.to_account_id=" + toAccountId + " AND s.sync_state="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, toAccountId);
				ps.setShort(2, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				GaInviteMasterInfo data = new GaInviteMasterInfo();
				int i = 1;

				data.setInviteId(rs.getString(i++));
				data.setInviteType(rs.getShort(i++));
				data.setFromAccountId(rs.getString(i++));
				data.setChannelId(rs.getString(i++));
				data.setExpiry(rs.getInt(i++));
				data.setTimestamp(rs.getInt(i++));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public void addExternalSubscribe(final String inviteId, final short toLoginType, final String toLoginName,
			final int timestamp)
	{
		String sql = "INSERT INTO ga_invite_subscribe_external (invite_id,to_login_type,to_login_name,create_time,last_update_time) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO ga_invite_subscribe_external (invite_id,to_login_type,to_login_name,create_time,last_update_time) VALUES ("
				+ inviteId + "," + toLoginType + "," + toLoginName + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, inviteId);
				ps.setShort(i++, toLoginType);
				ps.setString(i++, toLoginName);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void updateExternalSubscribeState(final String inviteId, final short toLoginType, final String toLoginName,
			final short syncState, final int timestamp)
	{
		String sql = "UPDATE ga_invite_subscribe_external SET sync_state=?,last_update_time=? WHERE invite_id=? AND to_login_type=? AND to_login_name=?";
		logger.debug("UPDATE ga_invite_subscribe_external SET sync_state=" + syncState + ",last_update_time="
				+ timestamp + " WHERE invite_id=" + inviteId + " AND to_login_type=" + toLoginType
				+ " AND to_login_name=" + toLoginName);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setInt(i++, syncState);
				ps.setInt(i++, timestamp);
				ps.setString(i++, inviteId);
				ps.setShort(i++, toLoginType);
				ps.setString(i++, toLoginName);
			}
		});
	}

	@Override
	public GaInviteMasterInfo queryExternalSubscribe(final short inviteType, final String fromAccountId,
			final short toLoginType, final String toLoginName)
	{
		final GaInviteMasterInfo data = new GaInviteMasterInfo();

		String sql = "SELECT g.invite_id,g.channel_id,g.expiry_time,g.last_update_time "
				+ " FROM ga_invite_original g, ga_invite_subscribe_external s "
				+ " WHERE g.invite_id=s.invite_id AND g.invite_type=? AND g.from_account_id=? AND s.to_login_type=? AND s.to_login_name=?";
		logger.debug("SELECT invite_id,channel_id,expiry_time,last_update_time "
				+ " FROM ga_invite_original g, ga_invite_subscribe_external s "
				+ " WHERE g.invite_id=s.invite_id AND g.invite_type=" + inviteType + " AND g.from_account_id="
				+ fromAccountId + " AND s.to_login_type=" + toLoginType + " AND s.to_login_name=" + toLoginName);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, inviteType);
				ps.setString(2, fromAccountId);
				ps.setShort(3, toLoginType);
				ps.setString(4, toLoginName);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int i = 1;

				data.setInviteId(rs.getString(i++));
				data.setInviteType(inviteType);
				data.setFromAccountId(fromAccountId);
				data.setChannelId(rs.getString(i++));
				data.setExpiry(rs.getInt(i++));
				data.setTimestamp(rs.getInt(i++));
			}
		});

		return data;
	}

	@Override
	public List<String> queryExternalSubscribeIds(final String inviteId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT s.to_login_type,s.to_login_name "
				+ " FROM ga_invite_original g, ga_invite_subscribe_external s "
				+ " WHERE g.invite_id=s.invite_id AND s.invite_id=? ";
		logger.debug("SELECT s.to_login_type,s.to_login_name "
				+ " FROM ga_invite_original g, ga_invite_subscribe_external s "
				+ " WHERE g.invite_id=s.invite_id AND s.invite_id=" + inviteId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, inviteId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int i = 1;

				short loginType = rs.getShort(i++);
				String loginName = rs.getString(i++);

				array.add(loginName);
			}
		});

		return array;
	}

	@Override
	public List<GaInviteMasterInfo> queryNotReceivedInvite(final short loginType, final String loginName)
	{
		final List<GaInviteMasterInfo> array = new ArrayList<GaInviteMasterInfo>();

		String sql = "SELECT g.invite_id,g.invite_type,g.from_account_id,g.channel_id,g.expiry_time,g.last_update_time "
				+ " FROM ga_invite_original g, ga_invite_subscribe_external s "
				+ " WHERE g.invite_id=s.invite_id AND s.to_login_type=? AND s.to_login_name=? AND s.sync_state=?";
		logger.debug("SELECT invite_id,invite_type,from_account_id,channel_id,expiry_time,last_update_time "
				+ " FROM ga_invite_original g, ga_invite_subscribe_external s "
				+ " WHERE g.invite_id=s.invite_id AND s.to_login_type=" + loginType + " AND s.to_login_name="
				+ loginName + " AND s.sync_state=" + GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, loginType);
				ps.setString(2, loginName);
				ps.setShort(3, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				GaInviteMasterInfo data = new GaInviteMasterInfo();
				int i = 1;

				data.setInviteId(rs.getString(i++));
				data.setInviteType(rs.getShort(i++));
				data.setFromAccountId(rs.getString(i++));
				data.setChannelId(rs.getString(i++));
				data.setExpiry(rs.getInt(i++));
				data.setTimestamp(rs.getInt(i++));

				array.add(data);
			}
		});

		return array;
	}

}
