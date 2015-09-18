package com.oct.ga.badgenum.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.badgenum.dao.GaBadgeNumDao;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.AccountBadgeNumJsonBean;

public class BadgeNumDaoImpl
		extends JdbcDaoSupport
		implements GaBadgeNumDao
{
	@Override
	public AccountBadgeNumJsonBean select(final String accountId)
	{
		final AccountBadgeNumJsonBean data = new AccountBadgeNumJsonBean();

		String sql = "SELECT message_num,task_log_num,invite_num,apply_num,moment_log_num FROM ga_badge_num WHERE account_id=?";
		logger.debug("SELECT message_num,task_log_num,invite_num,apply_num,moment_log_num FROM ga_badge_num WHERE account_id="
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
				data.setMessageNum(rs.getShort(1));
				data.setTaskLogNum(rs.getShort(2));
				data.setInviteNum(rs.getShort(3));
				data.setApplyNum(rs.getShort(4));
				data.setMomentLogNum(rs.getShort(5));
			}
		});

		return data;
	}

	@Override
	public boolean isExist(String accountId)
	{
		String sql = "SELECT count(account_id) FROM ga_badge_num WHERE account_id=?";
		logger.debug("SELECT count(account_id) FROM ga_badge_num WHERE account_id=" + accountId);

		Object[] params = new Object[] { accountId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public void add(final String accountId)
	{
		String sql = "INSERT INTO ga_badge_num (account_id) VALUES (?)";
		logger.debug("INSERT INTO ga_badge_num (account_id) VALUES (" + accountId + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, accountId);
			}
		});
	}

	@Override
	public void updateMessageNum(final String accountId, final short num)
	{
		String sql = "UPDATE ga_badge_num SET message_num=? WHERE account_id=?";
		logger.debug("UPDATE ga_badge_num SET message_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public void updateTaskLogNum(final String accountId, final short num)
	{
		String sql = "UPDATE ga_badge_num SET task_log_num=? WHERE account_id=?";
		logger.debug("UPDATE ga_badge_num SET task_log_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public void updateInviteNum(final String accountId, final short num)
	{
		String sql = "UPDATE ga_badge_num SET invite_num=? WHERE account_id=?";
		logger.debug("UPDATE ga_badge_num SET invite_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public void updateApplyNum(final String accountId, final short num)
	{
		String sql = "UPDATE ga_badge_num SET apply_num=? WHERE account_id=?";
		logger.debug("UPDATE ga_badge_num SET apply_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public short selectMessageNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT message_num FROM ga_badge_num WHERE account_id=?";
			logger.debug("SELECT message_num FROM ga_badge_num WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT message_num FROM ga_badge_num WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public short selectTaskLogNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT task_log_num FROM ga_badge_num WHERE account_id=?";
			logger.debug("SELECT task_log_num FROM ga_badge_num WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT task_log_num FROM ga_badge_num WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public short selectInviteNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT invite_num FROM ga_badge_num WHERE account_id=?";
			logger.debug("SELECT invite_num FROM ga_badge_num WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT invite_num FROM ga_badge_num WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public short selectApplyNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT apply_num FROM ga_badge_num WHERE account_id=?";
			logger.debug("SELECT apply_num FROM ga_badge_num WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT apply_num FROM ga_badge_num WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public short countMessageNum(String accountId)
	{
		String sql = "SELECT count(msg_id) FROM ga_message_extend WHERE to_account_id=? AND sync_state=?";
		logger.debug("SELECT count(msg_id) FROM ga_message_extend WHERE to_account_id=" + accountId
				+ " AND sync_state=" + GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		Object[] params = new Object[] { accountId, GlobalArgs.SYNC_STATE_NOT_RECEIVED };
		int count = this.getJdbcTemplate().queryForInt(sql, params);

		return (short) count;
	}

	@Override
	public short countTaskLogNum(String accountId)
	{
		String sql = "SELECT count(ActivityId) FROM cscart_ga_task_activity_extend WHERE ToAccountId=? AND SyncState=?";
		logger.debug("SELECT count(ActivityId) FROM cscart_ga_task_activity_extend WHERE ToAccountId=" + accountId
				+ " AND SyncState=" + GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		Object[] params = new Object[] { accountId, GlobalArgs.SYNC_STATE_NOT_RECEIVED };
		int count = this.getJdbcTemplate().queryForInt(sql, params);

		return (short) count;
	}

	@Override
	public short countInviteNum(String accountId)
	{
		String sql = "SELECT count(invite_id) FROM ga_invite_subscribe_ga WHERE to_account_id =? AND sync_state=?";
		logger.debug("SELECT count(invite_id) FROM ga_invite_subscribe_ga WHERE to_account_id =" + accountId
				+ " AND sync_state=" + GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		Object[] params = new Object[] { accountId, GlobalArgs.SYNC_STATE_NOT_RECEIVED };
		int count = this.getJdbcTemplate().queryForInt(sql, params);

		return (short) count;
	}

	@Override
	public short countInviteNum(short loginType, String loginName)
	{
		String sql = "SELECT count(invite_id) FROM ga_invite_subscribe_external WHERE to_login_type=? AND to_login_name=? AND sync_state=?";
		logger.debug("SELECT count(invite_id) FROM ga_invite_subscribe_external WHERE to_login_type=" + loginType
				+ " AND to_login_name=" + loginName + " AND sync_state=" + GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		Object[] params = new Object[] { loginType, loginName, GlobalArgs.SYNC_STATE_NOT_RECEIVED };
		int count = this.getJdbcTemplate().queryForInt(sql, params);

		return (short) count;
	}

	@Override
	public short countApplyNum(String accountId)
	{
		String sql = "SELECT count(*) FROM ga_apply_state WHERE to_account_id=? AND sync_state=?";
		logger.debug("SELECT count(*) FROM ga_apply_state WHERE to_account_id=" + accountId + " AND sync_state="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		Object[] params = new Object[] { accountId, GlobalArgs.SYNC_STATE_NOT_RECEIVED };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return (short) count;
	}

	@Override
	public short countInviteFeedbackNum(String accountId)
	{
		String sql = "SELECT count(invite_id) FROM ga_invite_feedback WHERE invite_account_id=? AND sync_state=?";
		logger.debug("SELECT count(invite_id) FROM ga_invite_feedback WHERE invite_account_id=" + accountId
				+ " AND sync_state=" + GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		Object[] params = new Object[] { accountId, GlobalArgs.SYNC_STATE_NOT_RECEIVED };
		int count = this.getJdbcTemplate().queryForInt(sql, params);

		return (short) count;
	}

	@Override
	public short selectMomentLogNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT moment_log_num FROM ga_badge_num WHERE account_id=?";
			logger.debug("SELECT moment_log_num FROM ga_badge_num WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT moment_log_num FROM ga_badge_num WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public void updateMomentLogNum(final String accountId, final short num)
	{
		String sql = "UPDATE ga_badge_num SET moment_log_num=? WHERE account_id=?";
		logger.debug("UPDATE ga_badge_num SET moment_log_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public short countMomentLogNum(String accountId)
	{
		String sql = "SELECT count(*) FROM ga_moment_log WHERE to_account_id=? AND sync_state=?";
		logger.debug("SELECT count(*) FROM ga_moment_log WHERE to_account_id=" + accountId + " AND sync_state="
				+ GlobalArgs.SYNC_STATE_NOT_RECEIVED);

		Object[] params = new Object[] { accountId, GlobalArgs.SYNC_STATE_NOT_RECEIVED };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return (short) count;
	}

	private final static Logger logger = LoggerFactory.getLogger(BadgeNumDaoImpl.class);

}
