package com.oct.ga.club.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.club.dao.ClubDao;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ClubBaseInfo;
import com.oct.ga.comm.domain.club.ClubDetailInfo;
import com.oct.ga.comm.domain.club.ClubMasterInfo;

public class ClubDaoImpl
		extends JdbcDaoSupport
		implements ClubDao
{
	@Override
	public void add(final ClubMasterInfo club, final int timestamp)
	{
		String sql = "INSERT INTO cscart_club (club_id,club_name,description,title_background_image,creator_id,create_time,last_update_time) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_club (club_id,club_name,description,title_background_image,creator_id,create_time,last_update_time) VALUES ("
				+ club.getId()
				+ ","
				+ club.getName()
				+ ","
				+ club.getDesc()
				+ ","
				+ club.getTitleBkImage()
				+ ","
				+ club.getCreatorId() + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, club.getId());
				ps.setString(2, club.getName());
				ps.setString(3, club.getDesc());
				ps.setString(4, club.getTitleBkImage());
				ps.setString(5, club.getCreatorId());
				ps.setInt(6, timestamp);
				ps.setInt(7, timestamp);
			}
		});
	}

	@Override
	public List<ClubBaseInfo> queryNameList(final String userId)
	{
		final List<ClubBaseInfo> array = new ArrayList<ClubBaseInfo>();

		String sql = "SELECT club_id,club_name,title_background_image,subscriber_num,create_time FROM cscart_club WHERE creator_id=? ORDER BY create_time DESC";
		logger.debug("SELECT club_id,club_name,title_background_image,subscriber_num,create_time FROM cscart_club WHERE creator_id="
				+ userId + " ORDER BY create_time DESC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, userId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				ClubBaseInfo club = new ClubBaseInfo();

				club.setId(rs.getString(1));
				club.setName(rs.getString(2));
				club.setTitleBkImage(rs.getString(3));
				club.setSubscriberNum(rs.getInt(4));

				array.add(club);
			}
		});

		return array;
	}

	@Override
	public void update(final ClubMasterInfo club, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_club SET club_name=?,description=?,title_background_image=?,last_update_time=? WHERE club_id=?";
		logger.debug("UPDATE cscart_club SET club_name=" + club.getName() + ",description=" + club.getDesc()
				+ ",title_background_image=" + club.getTitleBkImage() + ",last_update_time=" + timestamp
				+ " WHERE club_id=" + club.getId());

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, club.getName());
				ps.setString(2, club.getDesc());
				ps.setString(3, club.getTitleBkImage());
				ps.setInt(4, timestamp);
				ps.setString(5, club.getId());
			}
		});
	}

	// ///////////////////////////////////////////////////////////

	@Override
	public void addSubscriber(final String clubId, final String userId, final short state, final int timestamp)
	{
		String sql = "INSERT INTO cscart_club_subscriber (club_id,user_id,create_time,last_update_time,state) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_club_subscriber (club_id,user_id,create_time,last_update_time,state) VALUES ("
				+ clubId + "," + userId + "," + timestamp + "," + timestamp + "," + state + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, clubId);
				ps.setString(2, userId);
				ps.setInt(3, timestamp);
				ps.setInt(4, timestamp);
				ps.setShort(5, state);
			}
		});
	}

	@Override
	public void removeSubscriber(final String clubId, final String userId, int timestamp)
	{
		String sql = "DELETE FROM cscart_club_subscriber WHERE club_id=? AND user_id=?";
		logger.debug("DELETE FROM cscart_club_subscriber WHERE club_id=" + clubId + " AND user_id=" + userId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, clubId);
				ps.setString(2, userId);
			}
		});
	}

	@Override
	public boolean isExistSubscriber(final String clubId, final String userId)
	{
		String sql = "SELECT count(user_id) FROM cscart_club_subscriber WHERE club_id=? AND user_id=?";
		logger.debug("SELECT count(user_id) FROM cscart_club_subscriber WHERE club_id=" + clubId + " AND user_id="
				+ userId);

		Object[] params = new Object[] { clubId, userId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public boolean isSubscriber(final String clubId, final String userId)
	{
		String sql = "SELECT count(user_id) FROM cscart_club_subscriber WHERE club_id=? AND user_id=? AND state<?";
		logger.debug("SELECT count(user_id) FROM cscart_club_subscriber WHERE club_id=" + clubId + " AND user_id="
				+ userId + " AND state<" + GlobalArgs.INVITE_STATE_KICKOFF);

		Object[] params = new Object[] { clubId, userId, GlobalArgs.INVITE_STATE_KICKOFF };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public int querySubscriberNum(String clubId)
	{
		String sql = "SELECT count(user_id) FROM cscart_club_subscriber WHERE club_id=? AND state<?";
		logger.debug("SELECT count(user_id) FROM cscart_club_subscriber WHERE club_id=" + clubId + " AND state<"
				+ GlobalArgs.INVITE_STATE_KICKOFF);

		Object[] params = new Object[] { clubId, GlobalArgs.INVITE_STATE_KICKOFF };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count;
	}

	@Override
	public void updateSubscriberNum(final String clubId, final int num, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_club SET subscriber_num=?,last_update_time=? WHERE club_id=?";
		logger.debug("UPDATE cscart_club SET subscriber_num=" + num + ",last_update_time=" + timestamp
				+ " WHERE club_id=" + clubId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setInt(2, timestamp);
				ps.setString(3, clubId);
			}
		});
	}

	@Override
	public List<AccountBasic> querySubscribers(final String clubId)
	{
		final List<AccountBasic> array = new ArrayList<AccountBasic>();

		String sql = "SELECT a.user_id FROM cscart_club_subscriber a WHERE a.club_id=? AND a.state<? ORDER BY a.create_time ASC";
		logger.debug("SELECT a.user_id FROM cscart_club_subscriber a WHERE a.club_id=" + clubId + " AND a.state<"
				+ GlobalArgs.INVITE_STATE_KICKOFF + " ORDER BY a.create_time ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, clubId);
				ps.setShort(2, GlobalArgs.INVITE_STATE_KICKOFF);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				AccountBasic user = new AccountBasic();

				user.setAccountId(rs.getString(1));

				array.add(user);
			}
		});

		return array;
	}

	@Override
	public List<String> querySubscriberIds(final String clubId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT user_id FROM cscart_club_subscriber WHERE club_id=? AND state<? ORDER BY create_time ASC";
		logger.debug("SELECT user_id FROM cscart_club_subscriber WHERE club_id=" + clubId + " AND state<"
				+ GlobalArgs.INVITE_STATE_KICKOFF + " ORDER BY create_time ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, clubId);
				ps.setShort(2, GlobalArgs.INVITE_STATE_KICKOFF);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String id = rs.getString(1);

				array.add(id);
			}
		});

		return array;
	}

	@Override
	public void updateSubscriberState(final String clubId, final String userId, final short state, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_club_subscriber SET state=?,last_update_time=? WHERE club_id=? AND user_id=?";
		logger.debug("UPDATE cscart_club_subscriber SET state=" + state + ",last_update_time=" + timestamp
				+ " WHERE club_id=" + clubId + " AND user_id=" + userId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, state);
				ps.setInt(2, timestamp);
				ps.setString(3, clubId);
				ps.setString(4, userId);
			}
		});
	}

	// ///////////////////////////////////////////////////////////

	@Override
	public ClubDetailInfo queryDetail(final String clubId)
	{
		final ClubDetailInfo club = new ClubDetailInfo();

		String sql = "SELECT club_id,club_name,description,title_background_image,creator_id,member_num,subscriber_num FROM cscart_club WHERE club_id=?";
		logger.debug("SELECT club_id,club_name,description,title_background_image,creator_id,member_num,subscriber_num FROM cscart_club WHERE club_id="
				+ clubId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, clubId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				club.setId(rs.getString(1));
				club.setName(rs.getString(2));
				club.setDesc(rs.getString(3));
				club.setTitleBkImage(rs.getString(4));
				club.setCreatorId(rs.getString(5));
				club.setMemberNum(rs.getInt(6));
				club.setSubscriberNum(rs.getInt(7));
			}
		});

		return club;
	}

}
