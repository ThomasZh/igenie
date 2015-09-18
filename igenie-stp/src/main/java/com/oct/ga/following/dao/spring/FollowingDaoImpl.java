package com.oct.ga.following.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.account.AccountDetail;
import com.oct.ga.following.dao.FollowingDao;

public class FollowingDaoImpl
		extends JdbcDaoSupport
		implements FollowingDao
{
	/**
	 * my follow
	 */
	@Override
	public List<String> queryFollowing(final String myUserId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT following_user_id FROM cscart_ga_user_following WHERE user_id=? AND state="
				+ GlobalArgs.USER_FOLLOWING;
		logger.debug("SELECT following_user_id FROM cscart_ga_user_following WHERE user_id=" + myUserId + " AND state="
				+ GlobalArgs.USER_FOLLOWING);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, myUserId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String userId = rs.getString(1);
				array.add(userId);
			}
		});

		return array;
	}

	@Override
	public List<String> queryFollowingLastUpdateIds(final String accountId, final int lastTryTime)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT following_user_id FROM cscart_ga_user_following WHERE user_id=? AND last_update_time>? AND state=?";
		logger.debug("SELECT following_user_id FROM cscart_ga_user_following WHERE user_id=" + accountId
				+ " AND last_update_time>" + lastTryTime + " AND state=" + GlobalArgs.USER_FOLLOWING);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, accountId);
				ps.setInt(2, lastTryTime);
				ps.setShort(3, GlobalArgs.USER_FOLLOWING);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String data = rs.getString(1);

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public List<AccountDetail> queryFollowingLastUpdate(final String myUserId, final int lastTryTime)
	{
		final List<AccountDetail> array = new ArrayList<AccountDetail>();

		String sql = "SELECT following_user_id,state FROM cscart_ga_user_following WHERE user_id=? AND last_update_time>?";
		logger.debug("SELECT following_user_id,state FROM cscart_ga_user_following WHERE user_id=" + myUserId
				+ " AND last_update_time>" + lastTryTime);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, myUserId);
				ps.setInt(2, lastTryTime);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				AccountDetail data = new AccountDetail();

				data.setAccountId(rs.getString(1));
				data.setState(rs.getShort(2));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public List<String> queryBlackList(final String myUserId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT following_user_id FROM cscart_ga_user_following WHERE user_id=? AND state="
				+ GlobalArgs.USER_UNFOLLOW;
		logger.debug("SELECT following_user_id FROM cscart_ga_user_following WHERE user_id=" + myUserId + " AND state="
				+ GlobalArgs.USER_UNFOLLOW);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, myUserId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String userId = rs.getString(1);
				array.add(userId);
			}
		});

		return array;
	}

	/**
	 * follow me
	 */
	@Override
	public List<String> queryFollowed(final String myUserId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT user_id FROM cscart_ga_user_following WHERE following_user_id=? AND state="
				+ GlobalArgs.USER_FOLLOWING;
		logger.debug("SELECT user_id FROM cscart_ga_user_following WHERE following_user_id=" + myUserId + " AND state="
				+ GlobalArgs.USER_FOLLOWING);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, myUserId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String userId = rs.getString(1);
				array.add(userId);
			}
		});

		return array;
	}

	@Override
	public void add(final String myUserId, final String friendUserId, final int timestamp)
	{
		String sql = "INSERT INTO cscart_ga_user_following (user_id,following_user_id,create_time,last_update_time) VALUES (?,?,?,?)";
		logger.debug("INSERT INTO cscart_ga_user_following (user_id,following_user_id,create_time,last_update_time) VALUES ("
				+ myUserId + "," + friendUserId + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, myUserId);
				ps.setString(2, friendUserId);
				ps.setInt(3, timestamp);
				ps.setInt(4, timestamp);
			}
		});
	}

	@Override
	public void remove(final String myUserId, final String friendUserId)
	{
		String sql = "DELETE FROM cscart_ga_user_following WHERE user_id=? AND following_user_id=?";
		logger.debug("DELETE FROM cscart_ga_user_following WHERE user_id=" + myUserId + " AND following_user_id="
				+ friendUserId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, myUserId);
				ps.setString(2, friendUserId);
			}
		});
	}

	@Override
	public void updateState(final String myUserId, final String friendUserId, final short state, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_user_following SET state=?,last_update_time=? WHERE user_id=? AND following_user_id=?";
		logger.debug("UPDATE cscart_ga_user_following SET state=" + state + ",last_update_time=" + timestamp
				+ " WHERE user_id=" + myUserId + " AND following_user_id=" + friendUserId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, state);
				ps.setInt(2, timestamp);
				ps.setString(3, myUserId);
				ps.setString(4, friendUserId);
			}
		});
	}

	@Override
	public void updateMyLastUpdateTimeInFollowed(final String userId, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_user_following SET last_update_time=? WHERE following_user_id=?";
		logger.debug("UPDATE cscart_ga_user_following SET last_update_time=" + timestamp + " WHERE following_user_id="
				+ timestamp);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, timestamp);
				ps.setString(2, userId);
			}
		});
	}

	@Override
	public boolean isExist(String myUserId, String friendUserId)
	{
		String sql = "SELECT count(user_id) FROM cscart_ga_user_following WHERE user_id=? AND following_user_id=?";
		logger.debug("SELECT count(user_id) FROM cscart_ga_user_following WHERE user_id=" + myUserId
				+ " AND following_user_id=?" + friendUserId);

		Object[] params = new Object[] { myUserId, friendUserId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public boolean isFollowing(String myUserId, String friendUserId)
	{
		String sql = "SELECT count(user_id) FROM cscart_ga_user_following WHERE user_id=? AND following_user_id=? AND state="
				+ GlobalArgs.USER_FOLLOWING;
		logger.debug("SELECT count(user_id) FROM cscart_ga_user_following WHERE user_id=" + myUserId
				+ " AND following_user_id=" + friendUserId + " AND state=" + GlobalArgs.USER_FOLLOWING);

		Object[] params = new Object[] { myUserId, friendUserId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public boolean isUnfollow(String myUserId, String friendUserId)
	{
		String sql = "SELECT count(user_id) FROM cscart_ga_user_following WHERE user_id=? AND following_user_id=? AND state="
				+ GlobalArgs.USER_UNFOLLOW;
		logger.debug("SELECT count(user_id) FROM cscart_ga_user_following WHERE user_id=" + myUserId
				+ " AND following_user_id=" + friendUserId + " AND state=" + GlobalArgs.USER_UNFOLLOW);

		Object[] params = new Object[] { myUserId, friendUserId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

}
