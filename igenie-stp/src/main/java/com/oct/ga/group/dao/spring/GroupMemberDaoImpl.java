package com.oct.ga.group.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.group.GroupMemberMasterInfo;
import com.oct.ga.group.dao.GaGroupMemberDao;

public class GroupMemberDaoImpl
		extends JdbcDaoSupport
		implements GaGroupMemberDao
{
	@Override
	public void add(final String groupId, final String userId, final short rank, final short state, final int timestamp)
	{
		String sql = "INSERT INTO cscart_ga_group_member (group_id,user_id,rank,state,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_ga_group_member (group_id,user_id,rank,state,create_time,last_update_time) VALUES ("
				+ groupId + "," + userId + "," + rank + "," + state + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
				ps.setString(2, userId);
				ps.setInt(3, rank);
				ps.setInt(4, state);
				ps.setInt(5, timestamp);
				ps.setInt(6, timestamp);
			}
		});
	}

	@Override
	public void remove(final String groupId, final String userId)
	{
		String sql = "DELETE FROM cscart_ga_group_member WHERE group_id=? AND user_id=?";
		logger.debug("DELETE FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND user_id=" + userId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
				ps.setString(2, userId);
			}
		});
	}

	@Override
	public boolean isExist(String groupId, String userId)
	{
		String sql = "SELECT count(user_id) FROM cscart_ga_group_member WHERE group_id=? AND user_id=?";
		logger.debug("SELECT count(user_id) FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND user_id="
				+ userId);

		Object[] params = new Object[] { groupId, userId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public boolean isMember(String groupId, String userId)
	{
		String sql = "SELECT count(user_id) FROM cscart_ga_group_member WHERE group_id=? AND user_id=? AND state=?";
		logger.debug("SELECT count(user_id) FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND user_id="
				+ userId + " AND state=" + GlobalArgs.INVITE_STATE_ACCPET);

		Object[] params = new Object[] { groupId, userId, GlobalArgs.INVITE_STATE_ACCPET };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public boolean isLeader(String groupId, String userId)
	{
		String sql = "SELECT count(user_id) FROM cscart_ga_group_member WHERE group_id=? AND user_id=? AND state=? AND rank=?";
		logger.debug("SELECT count(user_id) FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND user_id="
				+ userId + " AND state=" + GlobalArgs.INVITE_STATE_ACCPET + " AND rank="
				+ GlobalArgs.MEMBER_RANK_LEADER);

		Object[] params = new Object[] { groupId, userId, GlobalArgs.INVITE_STATE_ACCPET, GlobalArgs.MEMBER_RANK_LEADER };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public short queryMemberRank(String groupId, String userId)
	{
		int count = GlobalArgs.MEMBER_RANK_NONE;

		try {
			String sql = "SELECT rank FROM cscart_ga_group_member WHERE group_id=? AND user_id=? AND state=?";
			logger.debug("SELECT rank FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND user_id=" + userId
					+ " AND state=" + GlobalArgs.INVITE_STATE_ACCPET);

			Object[] params = new Object[] { groupId, userId, GlobalArgs.INVITE_STATE_ACCPET };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT rank FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND user_id=" + userId
					+ " AND state=" + GlobalArgs.INVITE_STATE_ACCPET);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public short queryMemberState(String groupId, String userId)
	{
		int count = GlobalArgs.INVITE_STATE_QUIT;

		try {
			String sql = "SELECT state FROM cscart_ga_group_member WHERE group_id=? AND user_id=?";
			logger.debug("SELECT state FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND user_id="
					+ userId);

			Object[] params = new Object[] { groupId, userId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT state FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND user_id=" + userId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public List<String> queryMemberIds(final String groupId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT user_id FROM cscart_ga_group_member WHERE group_id=? ORDER BY create_time ASC";
		logger.debug("SELECT user_id FROM cscart_ga_group_member WHERE group_id=" + groupId
				+ " ORDER BY create_time ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
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
	public List<String> queryActiveMemberIds(final String groupId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT user_id FROM cscart_ga_group_member WHERE group_id=? AND state=? ORDER BY create_time ASC";
		logger.debug("SELECT user_id FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND state="
				+ GlobalArgs.INVITE_STATE_ACCPET + " ORDER BY create_time ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
				ps.setShort(2, GlobalArgs.INVITE_STATE_ACCPET);
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
	public List<GroupMemberDetailInfo> queryMembers(final String groupId)
	{
		final List<GroupMemberDetailInfo> array = new ArrayList<GroupMemberDetailInfo>();

		String sql = "SELECT m.user_id,m.state,m.rank FROM cscart_ga_group_member m WHERE m.group_id=? ORDER BY m.create_time ASC";
		logger.debug("SELECT m.user_id,m.state,m.rank FROM cscart_ga_group_member m WHERE m.group_id=" + groupId
				+ " ORDER BY m.create_time ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				GroupMemberDetailInfo member = new GroupMemberDetailInfo();

				member.setAccountId(rs.getString(1));
				member.setState(rs.getShort(2));
				member.setRank(rs.getShort(3));

				array.add(member);
			}
		});

		return array;
	}

	@Override
	public GroupMemberDetailInfo queryMember(final String groupId, final String userId)
	{
		final GroupMemberDetailInfo member = new GroupMemberDetailInfo();

		String sql = "SELECT m.user_id,m.state,m.rank FROM cscart_ga_group_member m WHERE m.group_id=? AND m.user_id=? AND m.state=?";
		logger.debug("SELECT m.user_id,m.state,m.rank FROM cscart_ga_group_member m WHERE m.group_id=" + groupId
				+ " AND m.user_id=" + userId + " AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
				ps.setString(2, userId);
				ps.setShort(3, GlobalArgs.INVITE_STATE_ACCPET);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				member.setAccountId(rs.getString(1));
				member.setState(rs.getShort(2));
				member.setRank(rs.getShort(3));
			}
		});

		return member;
	}

	@Override
	public GroupMemberDetailInfo queryLeader(final String groupId)
	{
		final GroupMemberDetailInfo member = new GroupMemberDetailInfo();

		String sql = "SELECT m.user_id,m.state,m.rank FROM cscart_ga_group_member m WHERE m.group_id=? AND m.rank=?";
		logger.debug("SELECT m.user_id,m.state,m.rank FROM cscart_ga_group_member m WHERE m.group_id=" + groupId
				+ " AND m.rank=" + GlobalArgs.MEMBER_RANK_LEADER);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
				ps.setShort(2, GlobalArgs.MEMBER_RANK_LEADER);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				member.setAccountId(rs.getString(1));
				member.setState(rs.getShort(2));
				member.setRank(rs.getShort(3));
			}
		});

		return member;
	}

	@Override
	public void updateState(final String groupId, final String userId, final short state, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group_member SET state=?,last_update_time=? WHERE group_id=? AND user_id=?";
		logger.debug("UPDATE cscart_ga_group_member SET state=" + state + ",last_update_time=" + timestamp
				+ " WHERE group_id=" + groupId + " AND user_id=" + userId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, state);
				ps.setInt(2, timestamp);
				ps.setString(3, groupId);
				ps.setString(4, userId);
			}
		});
	}

	@Override
	public List<GroupMemberDetailInfo> queryLastChangedMembers(final String groupId, final int lastTryTime)
	{
		final List<GroupMemberDetailInfo> array = new ArrayList<GroupMemberDetailInfo>();

		String sql = "SELECT m.user_id,m.state,m.rank FROM cscart_ga_group_member m WHERE m.group_id=? AND m.last_update_time>=? AND m.state=? ORDER BY m.create_time ASC";
		logger.debug("SELECT m.user_id,m.state,m.rank FROM cscart_ga_group_member m WHERE m.group_id=" + groupId
				+ " AND m.last_update_time>=" + lastTryTime + " AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET
				+ " ORDER BY m.create_time ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
				ps.setInt(2, lastTryTime);
				ps.setShort(3, GlobalArgs.INVITE_STATE_ACCPET);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				GroupMemberDetailInfo member = new GroupMemberDetailInfo();

				member.setGroupId(groupId);
				member.setAccountId(rs.getString(1));
				member.setState(rs.getShort(2));
				member.setRank(rs.getShort(3));

				array.add(member);
			}
		});

		return array;
	}

	public List<GroupMemberMasterInfo> queryLastChangedMembersMasterInfo(final String groupId, final int lastTryTime)
	{
		final List<GroupMemberMasterInfo> array = new ArrayList<GroupMemberMasterInfo>();

		String sql = "SELECT user_id,state,rank FROM cscart_ga_group_member WHERE group_id=? AND last_update_time>=? ORDER BY create_time ASC";
		logger.debug("SELECT user_id,state,rank FROM cscart_ga_group_member WHERE group_id=" + groupId
				+ " AND last_update_time>=" + lastTryTime + " ORDER BY create_time ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
				ps.setInt(2, lastTryTime);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				GroupMemberMasterInfo member = new GroupMemberMasterInfo();

				member.setAccountId(rs.getString(1));
				member.setState(rs.getShort(2));
				member.setRank(rs.getShort(3));

				array.add(member);
			}
		});

		return array;
	}

	@Override
	public String queryLeaderId(final String groupId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT user_id FROM cscart_ga_group_member WHERE group_id=? AND rank=?";
		logger.debug("SELECT user_id FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND rank="
				+ GlobalArgs.MEMBER_RANK_LEADER);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
				ps.setShort(2, GlobalArgs.MEMBER_RANK_LEADER);
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

		if (array.size() > 0)
			return array.get(0);
		else
			return "";

	}

	@Override
	public void updateRank(final String groupId, final String userId, final short rank, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group_member SET rank=?,last_update_time=? WHERE group_id=? AND user_id=?";
		logger.debug("UPDATE cscart_ga_group_member SET rank=" + rank + ",last_update_time=" + timestamp
				+ " WHERE group_id=" + groupId + " AND user_id=" + userId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, rank);
				ps.setInt(2, timestamp);
				ps.setString(3, groupId);
				ps.setString(4, userId);
			}
		});
	}

	@Override
	public short countMemberNum(String groupId)
	{
		String sql = "SELECT count(user_id) FROM cscart_ga_group_member WHERE group_id=? AND (state=? OR state=? OR state=? OR state=?)";
		logger.debug("SELECT count(user_id) FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND (state="
				+ GlobalArgs.INVITE_STATE_ACCPET + " OR state=" + GlobalArgs.INVITE_STATE_DISPATCH + " OR state="
				+ GlobalArgs.INVITE_STATE_REFILL + " OR state=" + GlobalArgs.INVITE_STATE_APPLY + ")");

		Object[] params = new Object[] { groupId, GlobalArgs.INVITE_STATE_ACCPET, GlobalArgs.INVITE_STATE_DISPATCH,
				GlobalArgs.INVITE_STATE_REFILL, GlobalArgs.INVITE_STATE_APPLY };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return (short) count;
	}

	@Override
	public short countMemberAvailableNum(String groupId)
	{
		String sql = "SELECT count(user_id) FROM cscart_ga_group_member WHERE group_id=? AND (state=? OR state=? OR state=?)";
		logger.debug("SELECT count(user_id) FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND (state="
				+ GlobalArgs.INVITE_STATE_ACCPET + " OR state=" + GlobalArgs.INVITE_STATE_DISPATCH + " OR state="
				+ GlobalArgs.INVITE_STATE_REFILL + ")");

		Object[] params = new Object[] { groupId, GlobalArgs.INVITE_STATE_ACCPET, GlobalArgs.INVITE_STATE_DISPATCH,
				GlobalArgs.INVITE_STATE_REFILL };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return (short) count;
	}

	// //////////////////////////////////////////////////////////////////////
	// Do Not Disturb

	@Override
	public void updateDndMode(final String groupId, final String accountId, final short mode, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group_member SET dnd=?,last_update_time=? WHERE group_id=? AND user_id=?";
		logger.debug("UPDATE cscart_ga_group_member SET dnd=" + mode + ",last_update_time=" + timestamp
				+ " WHERE group_id=" + groupId + " AND user_id=" + accountId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, mode);
				ps.setInt(2, timestamp);
				ps.setString(3, groupId);
				ps.setString(4, accountId);
			}
		});
	}

	@Override
	public short selectDndMode(String groupId, String accountId)
	{
		int count = GlobalArgs.DND_NO;

		try {
			String sql = "SELECT dnd FROM cscart_ga_group_member WHERE group_id=? AND user_id=?";
			logger.debug("SELECT dnd FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND user_id="
					+ accountId);

			Object[] params = new Object[] { groupId, accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT dnd FROM cscart_ga_group_member WHERE group_id=" + groupId + " AND user_id="
					+ accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	private final static Logger logger = LoggerFactory.getLogger(GroupMemberDaoImpl.class);

}
