package com.oct.ga.invite.dao.spring;

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

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.invite.dao.GaInviteFeedbackDao;
import com.oct.ga.invite.domain.GaFeedbackMasterInfo;

public class InviteFeedbackDaoImpl
		extends JdbcDaoSupport
		implements GaInviteFeedbackDao
{
	@Override
	public void add(final String inviteId, final String feedbackAccountId, final String inviteAccountId,
			final short feedbackState, final int timestamp)
	{
		String sql = "INSERT INTO ga_invite_feedback (invite_id,feedback_account_id,invite_account_id,feedback_state,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_invite_feedback (invite_id,feedback_account_id,invite_account_id,feedback_state,create_time,last_update_time) VALUES ("
				+ inviteId
				+ ","
				+ feedbackAccountId
				+ ","
				+ inviteAccountId
				+ ","
				+ feedbackState
				+ ","
				+ timestamp
				+ "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, inviteId);
				ps.setString(i++, feedbackAccountId);
				ps.setString(i++, inviteAccountId);
				ps.setShort(i++, feedbackState);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void updateSyncState(final String inviteId, final String feedbackAccountId, final String inviteAccountId,
			final short syncState, final int timestamp)
	{
		String sql = "UPDATE ga_invite_feedback SET sync_state=?,last_update_time=? WHERE invite_id=? AND feedback_account_id=? AND invite_account_id=?";
		logger.debug("UPDATE ga_invite_feedback SET sync_state=" + syncState + ",last_update_time=" + timestamp
				+ " WHERE invite_id=" + inviteId + " AND feedback_account_id=" + feedbackAccountId
				+ " AND invite_account_id=" + inviteAccountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setInt(i++, syncState);
				ps.setInt(i++, timestamp);
				ps.setString(i++, inviteId);
				ps.setString(i++, feedbackAccountId);
				ps.setString(i++, inviteAccountId);
			}
		});
	}

	@Override
	public void updateSyncState(final String inviteId, final String inviteAccountId, final short syncState,
			final int timestamp)
	{
		String sql = "UPDATE ga_invite_feedback SET sync_state=?,last_update_time=? WHERE invite_id=? AND invite_account_id=?";
		logger.debug("UPDATE ga_invite_feedback SET sync_state=" + syncState + ",last_update_time=" + timestamp
				+ " WHERE invite_id=" + inviteId + " AND invite_account_id=" + inviteAccountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setInt(i++, syncState);
				ps.setInt(i++, timestamp);
				ps.setString(i++, inviteId);
				ps.setString(i++, inviteAccountId);
			}
		});
	}

	@Override
	public List<GaFeedbackMasterInfo> queryNotReceived(final String inviteAccountId)
	{
		final List<GaFeedbackMasterInfo> array = new ArrayList<GaFeedbackMasterInfo>();

		String sql = "SELECT invite_id,feedback_account_id,feedback_state,last_update_time FROM ga_invite_feedback WHERE invite_account_id=? AND sync_state=?";
		logger.debug("SELECT invite_id,feedback_account_id,feedback_state,last_update_time FROM ga_invite_feedback WHERE invite_account_id="
				+ inviteAccountId + " AND sync_state=" + GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, inviteAccountId);
				ps.setShort(2, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				GaFeedbackMasterInfo data = new GaFeedbackMasterInfo();
				int i = 1;

				data.setInviteId(rs.getString(i++));
				data.setFeedbackAccountId(rs.getString(i++));
				data.setInviteAccountId(inviteAccountId);
				data.setFeedbackState(rs.getShort(i++));
				data.setTimestamp(rs.getInt(i++));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public boolean isExist(String inviteId, String feedbackAccountId, String inviteAccountId)
	{
		String sql = "SELECT count(invite_id) FROM ga_invite_feedback WHERE invite_id=? AND feedback_account_id=? AND invite_account_id=?";
		logger.debug("SELECT count(invite_id) FROM ga_invite_feedback WHERE invite_id=" + inviteId
				+ " AND feedback_account_id=" + feedbackAccountId + " AND invite_account_id=" + inviteAccountId);

		Object[] params = new Object[] { inviteId, feedbackAccountId, inviteAccountId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	private final static Logger logger = LoggerFactory.getLogger(InviteFeedbackDaoImpl.class);
}
