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
import com.oct.ga.group.dao.GaGroupDao;

public class GroupDaoImpl
		extends JdbcDaoSupport
		implements GaGroupDao
{
	@Override
	public void add(final String groupId, final String groupName, final short channelType, final int timestamp,
			final String creatorId)
	{
		String sql = "INSERT INTO cscart_ga_group (group_id,group_name,channel_type,create_time,last_update_time,creator_id) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_ga_group (group_id,group_name,channel_type,create_time,last_update_time,creator_id) VALUES ("
				+ groupId
				+ ","
				+ groupName
				+ ","
				+ channelType
				+ ","
				+ timestamp
				+ ","
				+ timestamp
				+ ","
				+ creatorId
				+ ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
				ps.setString(2, groupName);
				ps.setInt(3, channelType);
				ps.setInt(4, timestamp);
				ps.setInt(5, timestamp);
				ps.setString(6, creatorId);
			}
		});
	}

	@Override
	public void add(final String groupId, final String groupName, final short channelType, final int timestamp,
			final String creatorId, final short depth)
	{
		String sql = "INSERT INTO cscart_ga_group (group_id,group_name,channel_type,create_time,last_update_time,creator_id,depth) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_ga_group (group_id,group_name,channel_type,create_time,last_update_time,creator_id,depth) VALUES ("
				+ groupId
				+ ","
				+ groupName
				+ ","
				+ channelType
				+ ","
				+ timestamp
				+ ","
				+ timestamp
				+ ","
				+ creatorId
				+ "," + depth + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
				ps.setString(2, groupName);
				ps.setInt(3, channelType);
				ps.setInt(4, timestamp);
				ps.setInt(5, timestamp);
				ps.setString(6, creatorId);
				ps.setShort(7, depth);
			}
		});
	}

	@Override
	public void update(final String groupId, final String groupName, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group SET group_name=?,last_update_time=? WHERE group_id=?";
		logger.debug("UPDATE cscart_ga_group SET group_name=" + groupName + ",last_update_time=" + groupId
				+ " WHERE group_id=" + timestamp);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupName);
				ps.setInt(2, timestamp);
				ps.setString(3, groupId);
			}
		});
	}

	@Override
	public String queryGroupName(final String groupId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT group_name FROM cscart_ga_group WHERE group_id=?";
		logger.debug("SELECT group_name FROM cscart_ga_group WHERE group_id=" + groupId);

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

		if (array.size() > 0)
			return array.get(0);
		else
			return "";
	}

	public short queryChannelType(String groupId)
	{
		int count = 0;

		try {
			String sql = "SELECT channel_type FROM cscart_ga_group WHERE group_id=?";
			logger.debug("SELECT channel_type FROM cscart_ga_group WHERE group_id=" + groupId);

			Object[] params = new Object[] { groupId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT channel_type FROM cscart_ga_group WHERE group_id=" + groupId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	public short queryDepth(String groupId)
	{
		int count = -1;

		try {
			String sql = "SELECT depth FROM cscart_ga_group WHERE group_id=?";
			logger.debug("SELECT depth FROM cscart_ga_group WHERE group_id=" + groupId);

			Object[] params = new Object[] { groupId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT depth FROM cscart_ga_group WHERE group_id=" + groupId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public short queryState(String groupId)
	{
		int count = -1;

		try {
			String sql = "SELECT state FROM cscart_ga_group WHERE group_id=?";
			logger.debug("SELECT state FROM cscart_ga_group WHERE group_id=" + groupId);

			Object[] params = new Object[] { groupId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT state FROM cscart_ga_group WHERE group_id=" + groupId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	// //////////////////////////////////////////////////////////////////////
	// Group member summary information

	@Override
	public short queryMemberAvailableNum(String groupId)
	{
		int count = 0;

		try {
			String sql = "SELECT member_available_num FROM cscart_ga_group WHERE group_id=?";
			logger.debug("SELECT member_available_num FROM cscart_ga_group WHERE group_id=" + groupId);

			Object[] params = new Object[] { groupId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT member_available_num FROM cscart_ga_group WHERE group_id=" + groupId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short)count;
	}

	@Override
	public void updateMemberAvailableNum(final String groupId, final short num, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group SET member_available_num=?,last_update_time=? WHERE group_id=?";
		logger.debug("UPDATE cscart_ga_group SET member_available_num=" + num + ",last_update_time=" + timestamp
				+ " WHERE group_id=" + groupId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, num);
				ps.setInt(2, timestamp);
				ps.setString(3, groupId);
			}
		});
	}

	// //////////////////////////////////////////////////////////////////////
	// Group member summary information

	@Override
	public int queryMemberNum(String groupId)
	{
		int count = 0;

		try {
			String sql = "SELECT member_num FROM cscart_ga_group WHERE group_id=?";
			logger.debug("SELECT member_num FROM cscart_ga_group WHERE group_id=" + groupId);

			Object[] params = new Object[] { groupId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT member_num FROM cscart_ga_group WHERE group_id=" + groupId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return count;
	}

	@Override
	public void updateMemberNum(final String groupId, final int num, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group SET member_num=?,last_update_time=? WHERE group_id=?";
		logger.debug("UPDATE cscart_ga_group SET member_num=" + num + ",last_update_time=" + timestamp
				+ " WHERE group_id=" + groupId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setInt(2, timestamp);
				ps.setString(3, groupId);
			}
		});
	}

	// //////////////////////////////////////////////////////////////////////
	// Children summary information

	@Override
	public int queryChildNum(String groupId)
	{
		int count = 0;

		try {
			String sql = "SELECT child_num FROM cscart_ga_group WHERE group_id=?";
			logger.debug("SELECT child_num FROM cscart_ga_group WHERE group_id=" + groupId);

			Object[] params = new Object[] { groupId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT child_num FROM cscart_ga_group WHERE group_id=" + groupId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return count;
	}

	@Override
	public void updateChildNum(final String groupId, final int num, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group SET child_num=?,last_update_time=? WHERE group_id=?";
		logger.debug("UPDATE cscart_ga_group SET child_num=" + num + ",last_update_time=" + timestamp
				+ " WHERE group_id=" + groupId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setInt(2, timestamp);
				ps.setString(3, groupId);
			}
		});
	}

	// //////////////////////////////////////////////////////////////////////
	// Attachment summary information

	@Override
	public int queryAttachmentNum(String groupId)
	{
		int count = 0;

		try {
			String sql = "SELECT attachment_num FROM cscart_ga_group WHERE group_id=?";
			logger.debug("SELECT attachment_num FROM cscart_ga_group WHERE group_id=" + groupId);

			Object[] params = new Object[] { groupId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT attachment_num FROM cscart_ga_group WHERE group_id=" + groupId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return count;
	}

	@Override
	public void updateAttachmentNum(final String groupId, final int num, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group SET attachment_num=?,last_update_time=? WHERE group_id=?";
		logger.debug("UPDATE cscart_ga_group SET attachment_num=" + num + ",last_update_time=" + timestamp
				+ " WHERE group_id=" + groupId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setInt(2, timestamp);
				ps.setString(3, groupId);
			}
		});
	}

	// //////////////////////////////////////////////////////////////////////
	// Note summary information

	@Override
	public int queryNoteNum(String groupId)
	{
		int count = 0;

		try {
			String sql = "SELECT note_num FROM cscart_ga_group WHERE group_id=?";
			logger.debug("SELECT note_num FROM cscart_ga_group WHERE group_id=" + groupId);

			Object[] params = new Object[] { groupId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT note_num FROM cscart_ga_group WHERE group_id=" + groupId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return count;
	}

	@Override
	public void updateNoteNum(final String groupId, final int num, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group SET note_num=?,last_update_time=? WHERE group_id=?";
		logger.debug("UPDATE cscart_ga_group SET note_num=" + num + ",last_update_time=" + timestamp
				+ " WHERE group_id=" + groupId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setInt(2, timestamp);
				ps.setString(3, groupId);
			}
		});
	}

	@Override
	public void updateSate(final String groupId, final short state, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group SET state=?,last_update_time=? WHERE group_id=?";
		logger.debug("UPDATE cscart_ga_group SET state=" + state + ",last_update_time=" + timestamp
				+ " WHERE group_id=" + groupId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, state);
				ps.setInt(2, timestamp);
				ps.setString(3, groupId);
			}
		});
	}

	@Override
	public boolean isActive(String groupId)
	{
		String sql = "SELECT count(group_id) FROM cscart_ga_group WHERE group_id=? AND state=?";
		logger.debug("SELECT count(group_id) FROM cscart_ga_group WHERE group_id=" + groupId + " AND state="
				+ GlobalArgs.CLUB_ACTIVITY_STATE_OPENING);

		Object[] params = new Object[] { groupId, GlobalArgs.CLUB_ACTIVITY_STATE_OPENING };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public void remove(final String groupId)
	{
		String sql = "DELETE FROM cscart_ga_group WHERE group_id=?";
		logger.debug("DELETE FROM cscart_ga_group WHERE group_id=" + groupId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, groupId);
			}
		});
	}

	private final static Logger logger = LoggerFactory.getLogger(GroupDaoImpl.class);

}
