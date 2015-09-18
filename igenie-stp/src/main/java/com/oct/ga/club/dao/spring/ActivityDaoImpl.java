package com.oct.ga.club.dao.spring;

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
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.club.dao.ActivityDao;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ActivityCreateInfo;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.oct.ga.comm.domain.club.ActivityExtendInfo;
import com.oct.ga.comm.domain.club.ActivityMasterInfo;
import com.oct.ga.comm.domain.club.ActivityNameListInfo;
import com.oct.ga.comm.domain.club.ActivityRecommend;
import com.oct.ga.comm.domain.club.ActivitySubscribeDetailInfo;
import com.oct.ga.comm.domain.club.ActivitySubscribeInfo;
import com.oct.ga.comm.domain.club.ActivityUpdateInfo;
import com.oct.ga.stp.utility.PaginationHelper;

public class ActivityDaoImpl
		extends JdbcDaoSupport
		implements ActivityDao
{
	// ///////////////////////////////////////////////////////////

	@Override
	public void addSubscribe(final String clubId, final String activityId, final String userId, final short syncState,
			final int timestamp)
	{
		String sql = "INSERT INTO cscart_club_activity_subscribe (club_id,activity_id,user_id,sync_state,timestamp) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_club_activity_subscribe (club_id,activity_id,user_id,sync_state,timestamp) VALUES ("
				+ clubId + "," + activityId + "," + userId + "," + syncState + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, clubId);
				ps.setString(2, activityId);
				ps.setString(3, userId);
				ps.setShort(4, syncState);
				ps.setInt(5, timestamp);
			}
		});
	}

	@Override
	public void updateSubscribeState(final String clubId, final String activityId, final String userId,
			final short syncState, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_club_activity_subscribe SET timestamp=?,sync_state=? WHERE club_id=? AND activity_id=? AND user_id=?";
		logger.debug("UPDATE cscart_club_activity_subscribe SET timestamp=" + timestamp + ",sync_state=" + syncState
				+ " WHERE club_id=" + clubId + " AND activity_id=" + activityId + " AND user_id=" + userId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, timestamp);
				ps.setShort(2, syncState);
				ps.setString(3, clubId);
				ps.setString(4, activityId);
				ps.setString(5, userId);
			}
		});
	}

	// ///////////////////////////////////////////////////////////

	@Override
	public boolean isExistSubscribe(String activityId, String userId)
	{
		String sql = "SELECT count(user_id) FROM cscart_club_activity_subscribe WHERE activity_id=? AND user_id=?";
		logger.debug("SELECT count(user_id) FROM cscart_club_activity_subscribe WHERE activity_id=" + activityId
				+ " AND user_id=" + userId);

		Object[] params = new Object[] { activityId, userId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public void deleteSubscriber(final String activityId, final String userId)
	{
		String sql = "DELETE FROM cscart_club_activity_subscribe WHERE activity_id=? AND user_id=?";
		logger.debug("DELETE FROM cscart_club_activity_subscribe WHERE activity_id=" + activityId + " AND user_id="
				+ userId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, activityId);
				ps.setString(2, userId);
			}
		});
	}

	@Override
	public void add(final ActivityRecommend recommend)
	{
		String sql = "INSERT INTO cscart_club_activity_recommend (activity_id,to_user_id,from_user_id,from_user_name,timestamp,txt,sync_state) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_club_activity_recommend (activity_id,to_user_id,from_user_id,from_user_name,timestamp,txt,sync_state) VALUES ("
				+ recommend.getActivityId()
				+ ","
				+ recommend.getToUserId()
				+ ","
				+ recommend.getFromUserId()
				+ ","
				+ recommend.getFromUserName()
				+ ","
				+ recommend.getTimestamp()
				+ ","
				+ recommend.getText()
				+ ","
				+ recommend.getSyncState() + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, recommend.getActivityId());
				ps.setString(2, recommend.getToUserId());
				ps.setString(3, recommend.getFromUserId());
				ps.setString(4, recommend.getFromUserName());
				ps.setInt(5, recommend.getTimestamp());
				ps.setString(6, recommend.getText());
				ps.setInt(7, recommend.getSyncState());
			}
		});
	}

	@Override
	public void update(final ActivityRecommend recommend)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_club_activity_recommend SET timestamp=?,txt=? WHERE activity_id=? AND from_user_id=? AND to_user_id=?";
		logger.debug("UPDATE cscart_club_activity_recommend SET timestamp=" + recommend.getTimestamp() + ",txt="
				+ recommend.getText() + " WHERE activity_id=" + recommend.getActivityId() + " AND from_user_id="
				+ recommend.getFromUserId() + " AND to_user_id=" + recommend.getToUserId());

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, recommend.getTimestamp());
				ps.setString(2, recommend.getText());
				ps.setString(3, recommend.getActivityId());
				ps.setString(4, recommend.getFromUserId());
				ps.setString(5, recommend.getToUserId());
			}
		});
	}

	@Override
	public boolean isExistRecommend(String activityId, String fromUserId, String toUserId)
	{
		String sql = "SELECT count(to_user_id) FROM cscart_club_activity_recommend WHERE activity_id=? AND from_user_id=? AND to_user_id=?";
		logger.debug("SELECT count(to_user_id) FROM cscart_club_activity_recommend WHERE activity_id=" + activityId
				+ " AND from_user_id=" + fromUserId + " AND to_user_id=" + toUserId);

		Object[] params = new Object[] { activityId, fromUserId, toUserId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public int queryRecommendNum(String activityId, String toUserId)
	{
		String sql = "SELECT count(to_user_id) FROM cscart_club_activity_recommend WHERE activity_id=? AND to_user_id=?";
		logger.debug("SELECT count(to_user_id) FROM cscart_club_activity_recommend WHERE activity_id=" + activityId
				+ " AND to_user_id=" + toUserId);

		Object[] params = new Object[] { activityId, toUserId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count;
	}

	@Override
	public List<ActivityRecommend> queryRecommends(final String activityId, final String toUserId)
	{
		final List<ActivityRecommend> array = new ArrayList<ActivityRecommend>();

		String sql = "SELECT a.from_user_id,a.from_user_name,a.txt,a.timestamp,a.sync_state FROM cscart_club_activity_recommend a WHERE a.activity_id=? AND a.to_user_id=? ORDER BY a.timestamp DESC";
		logger.debug("SELECT a.from_user_id,a.from_user_name,a.txt,a.timestamp,a.sync_state FROM cscart_club_activity_recommend a WHERE a.activity_id="
				+ activityId + " AND a.to_user_id=" + toUserId + " ORDER BY a.timestamp DESC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, activityId);
				ps.setString(2, toUserId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				ActivityRecommend recommend = new ActivityRecommend();

				recommend.setActivityId(activityId);
				recommend.setFromUserId(rs.getString(1));
				recommend.setFromUserName(rs.getString(2));
				recommend.setText(rs.getString(3));
				recommend.setTimestamp(rs.getInt(4));
				recommend.setSyncState(rs.getShort(5));
				recommend.setToUserId(toUserId);

				array.add(recommend);
			}
		});

		return array;
	}

	// ///////////////////////////////////////////////////////

	@Override
	public void add(final String id, final ActivityCreateInfo activity, final String locMask, final String userId,
			final int timestamp)
	{
		final String pid = GlobalArgs.ID_DEFAULT_NONE;
		String sql = "INSERT INTO cscart_ga_task (TaskID,TaskPid,TaskName,TaskDesc,StartTime,EndTime,CreateTime,LastUpdateTime,CreateAccountID,channel_type,loc_desc,loc_x,loc_y,title_bk_image,publish_type,loc_mask) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_ga_task (TaskID,TaskPid,TaskName,TaskDesc,StartTime,EndTime,CreateTime,LastUpdateTime,CreateAccountID,channel_type,loc_desc,loc_x,loc_y,title_bk_image,publish_type,loc_mask) VALUES ("
				+ id
				+ ","
				+ pid
				+ ","
				+ activity.getName()
				+ ","
				+ activity.getDesc()
				+ ","
				+ activity.getStartTime()
				+ ","
				+ activity.getEndTime()
				+ ","
				+ timestamp
				+ ","
				+ timestamp
				+ ","
				+ userId
				+ ","
				+ GlobalArgs.CHANNEL_TYPE_ACTIVITY
				+ ","
				+ activity.getLocDesc()
				+ ","
				+ activity.getLocX()
				+ ","
				+ activity.getLocY()
				+ ","
				+ activity.getTitleBkImage()
				+ ","
				+ activity.getPublishType()
				+ ","
				+ locMask + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;

				ps.setString(n++, id);
				ps.setString(n++, pid);
				ps.setString(n++, activity.getName());
				ps.setString(n++, activity.getDesc());
				ps.setInt(n++, activity.getStartTime());
				ps.setInt(n++, activity.getEndTime());
				ps.setInt(n++, timestamp);
				ps.setInt(n++, timestamp);
				ps.setString(n++, userId);
				ps.setShort(n++, GlobalArgs.CHANNEL_TYPE_ACTIVITY);
				ps.setString(n++, activity.getLocDesc());
				ps.setString(n++, activity.getLocX());
				ps.setString(n++, activity.getLocY());
				ps.setString(n++, activity.getTitleBkImage());
				ps.setShort(n++, activity.getPublishType());
				ps.setString(n++, locMask);
			}
		});
	}

	@Override
	public void updateApproveType(final String activityId, final short type, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_task SET approve_type=?,LastUpdateTime=? WHERE TaskID=?";
		logger.debug("UPDATE cscart_ga_task SET approve_type=" + type + ",LastUpdateTime=" + timestamp
				+ " WHERE TaskID=" + activityId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;

				ps.setShort(n++, type);
				ps.setInt(n++, timestamp);
				ps.setString(n++, activityId);
			}
		});
	}

	@Override
	public void updateApplyFormType(final String activityId, final short type, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_task SET apply_form_type=?,LastUpdateTime=? WHERE TaskID=?";
		logger.debug("UPDATE cscart_ga_task SET apply_form_type=" + type + ",LastUpdateTime=" + timestamp
				+ " WHERE TaskID=" + activityId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;

				ps.setShort(n++, type);
				ps.setInt(n++, timestamp);
				ps.setString(n++, activityId);
			}
		});
	}

	@Override
	public short queryApproveType(String activityId)
	{
		short count = GlobalArgs.FALSE;

		try {
			String sql = "SELECT approve_type FROM cscart_ga_task WHERE TaskID=?";
			logger.debug("SELECT approve_type FROM cscart_ga_task WHERE TaskID=" + activityId);

			Object[] params = new Object[] { activityId };
			count = (short) this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT approve_type FROM cscart_ga_task WHERE TaskID=" + activityId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return count;
	}

	@Override
	public short queryApplyFormType(String activityId)
	{
		short count = GlobalArgs.FALSE;

		try {
			String sql = "SELECT apply_form_type FROM cscart_ga_task WHERE TaskID=?";
			logger.debug("SELECT apply_form_type FROM cscart_ga_task WHERE TaskID=" + activityId);

			Object[] params = new Object[] { activityId };
			count = (short) this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT apply_form_type FROM cscart_ga_task WHERE TaskID=" + activityId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return count;
	}

	@Override
	public void update(final ActivityUpdateInfo activity, final String locMask, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_task SET TaskName=?,TaskDesc=?,StartTime=?,EndTime=?,LastUpdateTime=?,loc_desc=?,loc_x=?,loc_y=?,title_bk_image=?,loc_mask=? WHERE TaskID=?";
		logger.debug("UPDATE cscart_ga_task SET TaskName=" + activity.getName() + ",TaskDesc=" + activity.getDesc()
				+ ",StartTime=" + activity.getStartTime() + ",EndTime=" + activity.getEndTime() + ",LastUpdateTime="
				+ timestamp + ",loc_desc=" + activity.getLocDesc() + ",loc_x=" + activity.getLocX() + ",loc_y="
				+ activity.getLocY() + ",title_bk_image=" + activity.getTitleBkImage() + ",loc_mask=" + locMask
				+ " WHERE TaskID=" + activity.getId());

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;
				ps.setString(n++, activity.getName());
				ps.setString(n++, activity.getDesc());
				ps.setInt(n++, activity.getStartTime());
				ps.setInt(n++, activity.getEndTime());
				ps.setInt(n++, timestamp);
				ps.setString(n++, activity.getLocDesc());
				ps.setString(n++, activity.getLocX());
				ps.setString(n++, activity.getLocY());
				ps.setString(n++, activity.getTitleBkImage());
				ps.setString(n++, locMask);
				ps.setString(n++, activity.getId());
			}
		});
	}

	@Override
	public ActivityDetailInfo queryDetailInfo(final String activityId)
	{
		final ActivityDetailInfo activity = new ActivityDetailInfo();

		String sql = "SELECT a.TaskID,a.TaskName,a.TaskDesc,a.StartTime,a.EndTime,g.state,a.TaskPid,a.loc_desc,a.loc_x,a.loc_y,a.title_bk_image,g.member_num,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g WHERE a.TaskID=? AND g.group_id=a.TaskID";
		logger.debug("SELECT a.TaskID,a.TaskName,a.TaskDesc,a.StartTime,a.EndTime,g.state,a.TaskPid,a.loc_desc,a.loc_x,a.loc_y,a.title_bk_image,g.member_num,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID="
				+ activityId
				+ " AND g.group_id=a.TaskID");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, activityId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int n = 1;

				activity.setId(rs.getString(n++));
				activity.setName(rs.getString(n++));
				activity.setDesc(rs.getString(n++));
				activity.setStartTime(rs.getInt(n++));
				activity.setEndTime(rs.getInt(n++));
				activity.setState(rs.getShort(n++));
				activity.setPid(rs.getString(n++));
				activity.setLocDesc(rs.getString(n++));
				activity.setLocX(rs.getString(n++));
				activity.setLocY(rs.getString(n++));
				activity.setTitleBkImage(rs.getString(n++));
				activity.setMemberNum(rs.getShort(n++));
				activity.setPublishType(rs.getShort(n++));
			}
		});

		return activity;
	}

	@Override
	public Page<ActivityMasterInfo> queryHistoryPagination(String clubId, int pageNum, int pageSize)
	{
		PaginationHelper<ActivityMasterInfo> ph = new PaginationHelper<ActivityMasterInfo>();

		String countSql = "SELECT count(a.TaskID) FROM cscart_ga_task a WHERE a.TaskPid=?";
		String sql = "SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID=g.group_id AND a.TaskPid=? AND g.depth=0 ORDER BY a.StartTime DESC";
		logger.debug("SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g " + " WHERE a.TaskID=g.group_id AND a.TaskPid=" + clubId
				+ " AND g.depth=0 ORDER BY a.StartTime DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { clubId }, pageNum, pageSize,
				new ParameterizedRowMapper<ActivityMasterInfo>()
				{
					public ActivityMasterInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						ActivityMasterInfo data = new ActivityMasterInfo();

						data.setId(rs.getString(1));
						data.setName(rs.getString(2));
						data.setStartTime(rs.getInt(3));
						data.setState(rs.getShort(4));
						data.setMemberNum(rs.getShort(5));
						data.setPublishType(rs.getShort(6));

						return data;
					}
				});
	}

	@Override
	public Page<ActivitySubscribeInfo> queryFuturePagination(String accountId, int timestamp, int pageNum, int pageSize)
	{
		PaginationHelper<ActivitySubscribeInfo> ph = new PaginationHelper<ActivitySubscribeInfo>();

//		String countSql = "SELECT count(*) FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
//				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.rank<>? AND m.state=? AND a.StartTime>? AND g.depth=0";
//		String sql = "SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.publish_type "
//				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
//				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.rank<>? AND m.state=? AND a.StartTime>? AND g.depth=0"
//				+ " ORDER BY a.StartTime ASC";
//		logger.debug("SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.publish_type "
//				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
//				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=" + accountId + " AND m.rank<>"
//				+ GlobalArgs.MEMBER_RANK_NONE + " AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET + " AND a.StartTime>"
//				+ timestamp + " AND g.depth=0 ORDER BY a.StartTime ASC");
		
		String countSql = "SELECT count(*) FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.state=? AND g.state=? AND g.depth=0 AND a.endTime>?";
		String sql = "SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.state=? AND g.state=? AND g.depth=0 AND a.endTime>?"
				+ " ORDER BY a.StartTime ASC";
		logger.debug("SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=" + accountId 
				+ " AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET + " AND g.state=?"
				+ GlobalArgs.CLUB_ACTIVITY_STATE_OPENING + " AND g.depth=0 AND a.endTime>" + timestamp + " ORDER BY a.StartTime ASC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { accountId,
				 GlobalArgs.INVITE_STATE_ACCPET, GlobalArgs.CLUB_ACTIVITY_STATE_OPENING, timestamp}, pageNum, pageSize,
				new ParameterizedRowMapper<ActivitySubscribeInfo>()
				{
					public ActivitySubscribeInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						ActivitySubscribeInfo data = new ActivitySubscribeInfo();

						data.setId(rs.getString(1));
						data.setName(rs.getString(2));
						data.setStartTime(rs.getInt(3));
						data.setState(rs.getShort(4));
						data.setMemberNum(rs.getShort(5));
						data.setPublishType(rs.getShort(6));

						return data;
					}
				});
	}

	@Override
	public Page<ActivitySubscribeInfo> queryHistoryPagination(String accountId, int timestamp, int pageNum, int pageSize)
	{
		PaginationHelper<ActivitySubscribeInfo> ph = new PaginationHelper<ActivitySubscribeInfo>();

		String countSql = "SELECT count(*) FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.rank<>? AND a.StartTime<? AND g.depth=0";
		String sql = "SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.rank<>? AND a.StartTime<? AND g.depth=0"
				+ " ORDER BY a.StartTime DESC";
		logger.debug("SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=" + accountId + " AND m.rank<>"
				+ GlobalArgs.MEMBER_RANK_NONE + " AND a.StartTime<" + timestamp
				+ " AND g.depth=0 ORDER BY a.StartTime DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { accountId,
				GlobalArgs.MEMBER_RANK_NONE, timestamp }, pageNum, pageSize,
				new ParameterizedRowMapper<ActivitySubscribeInfo>()
				{
					public ActivitySubscribeInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						ActivitySubscribeInfo data = new ActivitySubscribeInfo();

						data.setId(rs.getString(1));
						data.setName(rs.getString(2));
						data.setStartTime(rs.getInt(3));
						data.setState(rs.getShort(4));
						data.setMemberNum(rs.getShort(5));
						data.setPublishType(rs.getShort(6));

						return data;
					}
				});
	}

	@Override
	public Page<ActivitySubscribeInfo> queryCreateFuturePagination(String accountId, int timestamp, int pageNum,
			int pageSize)
	{
		PaginationHelper<ActivitySubscribeInfo> ph = new PaginationHelper<ActivitySubscribeInfo>();

		String countSql = "SELECT count(*) FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.rank=? AND a.StartTime>? AND g.depth=0 AND a.publish_type=?";
		String sql = "SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.loc_desc,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.rank=? AND a.StartTime>? AND g.depth=0 AND a.publish_type=?"
				+ " ORDER BY a.StartTime ASC";
		logger.debug("SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.loc_desc,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=" + accountId + " AND m.rank="
				+ GlobalArgs.MEMBER_RANK_LEADER + " AND a.StartTime>" + timestamp
				+ " AND g.depth=0 AND a.publish_type=" + GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC
				+ " ORDER BY a.StartTime ASC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { accountId,
				GlobalArgs.MEMBER_RANK_LEADER, timestamp, GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC }, pageNum,
				pageSize, new ParameterizedRowMapper<ActivitySubscribeInfo>()
				{
					public ActivitySubscribeInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						ActivitySubscribeInfo data = new ActivitySubscribeInfo();

						data.setId(rs.getString(1));
						data.setName(rs.getString(2));
						data.setStartTime(rs.getInt(3));
						data.setState(rs.getShort(4));
						data.setMemberNum(rs.getShort(5));
						data.setLocDesc(rs.getString(6));
						data.setPublishType(rs.getShort(7));

						return data;
					}
				});
	}

	@Override
	public Page<ActivitySubscribeInfo> queryFuturePagination(int endTime, int pageNum, int pageSize)
	{
		PaginationHelper<ActivitySubscribeInfo> ph = new PaginationHelper<ActivitySubscribeInfo>();

		String countSql = "SELECT count(a.TaskID) " + " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID=g.group_id AND a.EndTime>? AND g.state<>? AND a.publish_type=? AND g.depth=0";
		String sql = "SELECT a.TaskID,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_x,a.loc_y,a.loc_desc,a.LastUpdateTime "
				+ " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID=g.group_id AND a.EndTime>? AND g.state<>? AND a.publish_type=? AND g.depth=0"
				+ " ORDER BY a.LastUpdateTime DESC";
		logger.debug("SELECT a.TaskID,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_x,a.loc_y,a.loc_desc,a.LastUpdateTime "
				+ " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID=g.group_id AND a.EndTime>"
				+ endTime
				+ " AND g.state<>"
				+ GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED
				+ " AND a.publish_type="
				+ GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC + " AND g.depth=0 ORDER BY a.LastUpdateTime DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { endTime,
				GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED, GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC }, pageNum,
				pageSize, new ParameterizedRowMapper<ActivitySubscribeInfo>()
				{
					public ActivitySubscribeDetailInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						int n = 1;
						ActivitySubscribeDetailInfo data = new ActivitySubscribeDetailInfo();

						data.setId(rs.getString(n++));
						data.setName(rs.getString(n++));
						data.setPid(rs.getString(n++));
						data.setStartTime(rs.getInt(n++));
						data.setMemberNum(rs.getShort(n++));
						data.setState(rs.getShort(n++));
						data.setLocX(rs.getString(n++));
						data.setLocY(rs.getString(n++));
						data.setLocDesc(rs.getString(n++));
						data.setCreateTime(rs.getInt(n++));
						data.setPublishType(GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC);

						return data;
					}
				});
	}

	@Override
	public Page<ActivitySubscribeInfo> queryCreateHistoryPagination(String accountId, int timestamp, int pageNum,
			int pageSize)
	{
		PaginationHelper<ActivitySubscribeInfo> ph = new PaginationHelper<ActivitySubscribeInfo>();

		String countSql = "SELECT count(*) FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.rank=? AND a.StartTime<? AND g.depth=0 AND a.publish_type=?";
		String sql = "SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.loc_desc,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.rank=? AND a.StartTime<? AND g.depth=0 AND a.publish_type=?"
				+ " ORDER BY a.StartTime DESC";
		logger.debug("SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,a.loc_desc,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=" + accountId + " AND m.rank="
				+ GlobalArgs.MEMBER_RANK_LEADER + " AND a.StartTime<" + timestamp
				+ " AND g.depth=0 AND a.publish_type=" + GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC
				+ " ORDER BY a.StartTime DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { accountId,
				GlobalArgs.MEMBER_RANK_LEADER, timestamp, GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC }, pageNum,
				pageSize, new ParameterizedRowMapper<ActivitySubscribeInfo>()
				{
					public ActivitySubscribeInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						ActivitySubscribeInfo data = new ActivitySubscribeInfo();

						data.setId(rs.getString(1));
						data.setName(rs.getString(2));
						data.setStartTime(rs.getInt(3));
						data.setState(rs.getShort(4));
						data.setMemberNum(rs.getShort(5));
						data.setLocDesc(rs.getString(6));
						data.setPublishType(rs.getShort(7));

						return data;
					}
				});
	}

	@Override
	public Page<ActivityExtendInfo> queryImagesPagination(String clubId, int pageNum, int pageSize)
	{
		PaginationHelper<ActivityExtendInfo> ph = new PaginationHelper<ActivityExtendInfo>();

		String countSql = "SELECT count(a.TaskID) FROM cscart_ga_task a WHERE a.TaskPid=?";
		String sql = "SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,g.attachment_num,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID=g.group_id AND a.TaskPid=? AND g.attachment_num>0 ORDER BY a.StartTime DESC";
		logger.debug("SELECT a.TaskID,a.TaskName,a.StartTime,g.state,g.member_num,g.attachment_num,a.publish_type "
				+ " FROM cscart_ga_task a,cscart_ga_group g " + " WHERE a.TaskID=g.group_id AND a.TaskPid=" + clubId
				+ " AND g.attachment_num>0 ORDER BY a.StartTime DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { clubId }, pageNum, pageSize,
				new ParameterizedRowMapper<ActivityExtendInfo>()
				{
					public ActivityExtendInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						ActivityExtendInfo data = new ActivityExtendInfo();

						data.setId(rs.getString(1));
						data.setName(rs.getString(2));
						data.setStartTime(rs.getInt(3));
						data.setState(rs.getShort(4));
						data.setMemberNum(rs.getShort(5));
						data.setAttachmentNum(rs.getShort(6));
						data.setPublishType(rs.getShort(7));

						return data;
					}
				});
	}

	@Override
	public List<ActivityMasterInfo> querySubscribe(final String userId, final int startTime, final int endTime)
	{
		final List<ActivityMasterInfo> array = new ArrayList<ActivityMasterInfo>();

		String sql = "SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.publish_type "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id=? AND a.StartTime<? AND a.EndTime>? AND (g.state=? OR g.state=?) AND g.depth=0"
				+ " ORDER BY a.StartTime ASC";
		logger.debug("SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.publish_type "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id=" + userId
				+ " AND a.StartTime<" + startTime + " AND a.EndTime>" + endTime + "AND (g.state="
				+ GlobalArgs.CLUB_ACTIVITY_STATE_OPENING + " OR g.state=" + GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED
				+ ") AND g.depth=0 ORDER BY a.StartTime ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, userId);
				ps.setInt(2, startTime);
				ps.setInt(3, endTime);
				ps.setShort(4, GlobalArgs.CLUB_ACTIVITY_STATE_OPENING);
				ps.setShort(5, GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				ActivityMasterInfo data = new ActivityMasterInfo();

				data.setId(rs.getString(1));
				data.setName(rs.getString(2));
				data.setPid(rs.getString(3));
				data.setStartTime(rs.getInt(4));
				data.setMemberNum(rs.getShort(5));
				data.setState(rs.getShort(6));
				data.setPublishType(rs.getShort(7));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public List<AccountBasic> querySubscribers(final String activityId)
	{
		final List<AccountBasic> array = new ArrayList<AccountBasic>();

		String sql = "SELECT user_id FROM cscart_club_activity_subscribe WHERE activity_id=? ORDER BY timestamp ASC";
		logger.debug("SELECT user_id FROM cscart_club_activity_subscribe WHERE activity_id=" + activityId
				+ " ORDER BY timestamp ASC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, activityId);
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
	public Page<ActivitySubscribeInfo> querySubscribePagination(String userId, int endTime, int pageNum, int pageSize)
	{
		PaginationHelper<ActivitySubscribeInfo> ph = new PaginationHelper<ActivitySubscribeInfo>();

		String countSql = "SELECT count(s.activity_id) "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id=? AND a.EndTime>? AND (g.state=? OR g.state=?) AND g.depth=0";
		String sql = "SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.publish_type "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id=? AND a.EndTime>? AND (g.state=? OR g.state=?) AND g.depth=0"
				+ " ORDER BY a.StartTime ASC";
		logger.debug("SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.publish_type "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id="
				+ userId
				+ " AND a.EndTime>"
				+ endTime
				+ " AND (g.state="
				+ GlobalArgs.CLUB_ACTIVITY_STATE_OPENING
				+ " OR g.state=" + GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED + ") AND g.depth=0 ORDER BY a.StartTime ASC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { userId, endTime,
				GlobalArgs.CLUB_ACTIVITY_STATE_OPENING, GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED }, pageNum, pageSize,
				new ParameterizedRowMapper<ActivitySubscribeInfo>()
				{
					public ActivitySubscribeInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						int n = 1;
						ActivitySubscribeInfo data = new ActivitySubscribeInfo();

						data.setId(rs.getString(n++));
						data.setName(rs.getString(n++));
						data.setPid(rs.getString(n++));
						data.setStartTime(rs.getInt(n++));
						data.setMemberNum(rs.getShort(n++));
						data.setState(rs.getShort(n++));
						data.setLocDesc(rs.getString(n++));
						data.setPublishType(rs.getShort(n++));

						return data;
					}
				});
	}

	@Override
	public Page<ActivitySubscribeInfo> querySubscribePagination(String userId, int startTime, int endTime, int pageNum,
			int pageSize)
	{
		PaginationHelper<ActivitySubscribeInfo> ph = new PaginationHelper<ActivitySubscribeInfo>();

		String countSql = "SELECT count(s.activity_id) "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id=? AND a.StartTime<? AND a.EndTime>? AND (g.state=? OR g.state=?) AND g.depth=0";
		String sql = "SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.publish_type "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id=? AND a.StartTime<? AND a.EndTime>? AND (g.state=? OR g.state=?) AND g.depth=0"
				+ " ORDER BY a.StartTime ASC";
		logger.debug("SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.publish_type "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id="
				+ userId
				+ " AND a.StartTime<"
				+ endTime
				+ " AND a.EndTime>"
				+ startTime
				+ " AND (g.state="
				+ GlobalArgs.CLUB_ACTIVITY_STATE_OPENING
				+ " OR g.state="
				+ GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED
				+ ") AND g.depth=0 ORDER BY a.StartTime ASC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { userId, endTime, startTime,
				GlobalArgs.CLUB_ACTIVITY_STATE_OPENING, GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED }, pageNum, pageSize,
				new ParameterizedRowMapper<ActivitySubscribeInfo>()
				{
					public ActivitySubscribeInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						int n = 1;
						ActivitySubscribeInfo data = new ActivitySubscribeInfo();

						data.setId(rs.getString(n++));
						data.setName(rs.getString(n++));
						data.setPid(rs.getString(n++));
						data.setStartTime(rs.getInt(n++));
						data.setMemberNum(rs.getShort(n++));
						data.setState(rs.getShort(n++));
						data.setLocDesc(rs.getString(n++));
						data.setPublishType(rs.getShort(n++));

						return data;
					}
				});
	}

	@Override
	public Page<ActivitySubscribeDetailInfo> querySubscribeOrderByCreateTimePagination(String userId, int endTime,
			int pageNum, int pageSize)
	{
		PaginationHelper<ActivitySubscribeDetailInfo> ph = new PaginationHelper<ActivitySubscribeDetailInfo>();

		String countSql = "SELECT count(s.activity_id) "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id=? AND a.EndTime>? AND (g.state=? OR g.state=?) AND g.depth=0 ";
		String sql = "SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.CreateTime,a.publish_type "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id=? AND a.EndTime>? AND (g.state=? OR g.state=?) AND g.depth=0 "
				+ " ORDER BY a.CreateTime DESC";
		logger.debug("SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.CreateTime,a.publish_type "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id="
				+ userId
				+ " AND a.EndTime>"
				+ endTime
				+ " AND (g.state="
				+ GlobalArgs.CLUB_ACTIVITY_STATE_OPENING
				+ " OR g.state="
				+ GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED
				+ ") AND g.depth=0 ORDER BY a.CreateTime DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { userId, endTime,
				GlobalArgs.CLUB_ACTIVITY_STATE_OPENING, GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED }, pageNum, pageSize,
				new ParameterizedRowMapper<ActivitySubscribeDetailInfo>()
				{
					public ActivitySubscribeDetailInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						int n = 1;
						ActivitySubscribeDetailInfo data = new ActivitySubscribeDetailInfo();

						data.setId(rs.getString(n++));
						data.setName(rs.getString(n++));
						data.setPid(rs.getString(n++));
						data.setStartTime(rs.getInt(n++));
						data.setMemberNum(rs.getShort(n++));
						data.setState(rs.getShort(n++));
						data.setLocDesc(rs.getString(n++));
						data.setCreateTime(rs.getInt(n++));
						data.setPublishType(rs.getShort(n++));

						return data;
					}
				});
	}

	@Override
	public Page<ActivitySubscribeInfo> queryHistoryOrderByLastUpdateTimePagination(String accountId, int endTime,
			int pageNum, int pageSize)
	{
		PaginationHelper<ActivitySubscribeInfo> ph = new PaginationHelper<ActivitySubscribeInfo>();

		String countSql = "SELECT count(s.activity_id) "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND a.TaskId=m.group_id AND s.user_id=? AND a.EndTime<? AND g.state<>? AND m.state=? AND g.depth=0";
		String sql = "SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.LastUpdateTime,a.publish_type "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND a.TaskId=m.group_id AND s.user_id=? AND a.EndTime<? AND g.state<>? AND m.state=? AND g.depth=0"
				+ " ORDER BY a.LastUpdateTime DESC";
		logger.debug("SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.LastUpdateTime,a.publish_type "
				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND a.TaskId=m.group_id AND s.user_id="
				+ accountId
				+ " AND a.EndTime<"
				+ endTime
				+ " AND g.state<>"
				+ GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED
				+ " AND m.state="
				+ GlobalArgs.INVITE_STATE_ACCPET
				+ " AND g.depth=0 ORDER BY a.LastUpdateTime DESC");

//		String countSql = "SELECT count(s.activity_id) "
//				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
//				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id=? AND g.state=? AND g.depth=0";
//		String sql = "SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.LastUpdateTime,a.publish_type "
//				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
//				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id=? AND g.state=? AND g.depth=0"
//				+ " ORDER BY a.LastUpdateTime DESC";
//		logger.debug("SELECT s.activity_id,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.LastUpdateTime,a.publish_type "
//				+ " FROM cscart_club_activity_subscribe s,cscart_ga_task a,cscart_ga_group g "
//				+ " WHERE s.activity_id=a.TaskID AND s.activity_id=g.group_id AND s.user_id="
//				+ userId
//				+ " AND g.state="
//				+ GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED
//				+ " AND g.depth=0 ORDER BY a.LastUpdateTime DESC");
		
//		String countSql = "SELECT count(*) FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
//				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.state=? AND g.state=? AND g.depth=0";
//		String sql = "SELECT a.TaskID,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.LastUpdateTime,a.publish_type "
//				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
//				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=? AND m.state=? AND g.state=? AND g.depth=0"
//				+ " ORDER BY a.StartTime ASC";
//		logger.debug("SELECT a.TaskID,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_desc,a.LastUpdateTime,a.publish_type "
//				+ " FROM cscart_ga_task a,cscart_ga_group g,cscart_ga_group_member m "
//				+ " WHERE a.TaskID=g.group_id AND a.TaskId=m.group_id AND m.user_id=" + accountId 
//				+ " AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET + " AND g.state=?"
//				+ GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED + " AND g.depth=0 ORDER BY a.StartTime ASC");
		
		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { accountId, endTime,
				GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED,GlobalArgs.INVITE_STATE_ACCPET }, pageNum, pageSize,
//		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { accountId,GlobalArgs.INVITE_STATE_ACCPET,
//			GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED }, pageNum, pageSize,
			new ParameterizedRowMapper<ActivitySubscribeInfo>()
				{
					public ActivitySubscribeDetailInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						int n = 1;
						ActivitySubscribeDetailInfo data = new ActivitySubscribeDetailInfo();

						data.setId(rs.getString(n++));
						data.setName(rs.getString(n++));
						data.setPid(rs.getString(n++));
						data.setStartTime(rs.getInt(n++));
						data.setMemberNum(rs.getShort(n++));
						data.setState(rs.getShort(n++));
						data.setLocDesc(rs.getString(n++));
						data.setCreateTime(rs.getInt(n++));
						data.setPublishType(rs.getShort(n++));

						return data;
					}
				});
	}

	@Override
	public Page<ActivitySubscribeInfo> queryFutureFilterByLocPagination(String locMask, int endTime, int pageNum,
			int pageSize)
	{
		PaginationHelper<ActivitySubscribeInfo> ph = new PaginationHelper<ActivitySubscribeInfo>();

		String countSql = "SELECT count(a.TaskID) "
				+ " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID=g.group_id AND a.loc_mask=? AND a.EndTime>? AND g.state<>? AND a.publish_type=? AND g.depth=0";
		String sql = "SELECT a.TaskID,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_x,a.loc_y,a.loc_desc,a.LastUpdateTime "
				+ " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID=g.group_id AND a.loc_mask=? AND a.EndTime>? AND g.state<>? AND a.publish_type=? AND g.depth=0"
				+ " ORDER BY a.LastUpdateTime DESC";
		logger.debug("SELECT a.TaskID,a.TaskName,a.TaskPid,a.StartTime,g.member_num,g.state,a.loc_x,a.loc_y,a.loc_desc,a.LastUpdateTime "
				+ " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID=g.group_id AND a.loc_mask="
				+ locMask
				+ " AND a.EndTime>"
				+ endTime
				+ " AND g.state<>"
				+ GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED
				+ " AND a.publish_type="
				+ GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC
				+ " AND g.depth=0 ORDER BY a.LastUpdateTime DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { locMask, endTime,
				GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED, GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC }, pageNum,
				pageSize, new ParameterizedRowMapper<ActivitySubscribeInfo>()
				{
					public ActivitySubscribeDetailInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						int n = 1;
						ActivitySubscribeDetailInfo data = new ActivitySubscribeDetailInfo();

						data.setId(rs.getString(n++));
						data.setName(rs.getString(n++));
						data.setPid(rs.getString(n++));
						data.setStartTime(rs.getInt(n++));
						data.setMemberNum(rs.getShort(n++));
						data.setState(rs.getShort(n++));
						data.setLocX(rs.getString(n++));
						data.setLocY(rs.getString(n++));
						data.setLocDesc(rs.getString(n++));
						data.setCreateTime(rs.getInt(n++));
						data.setPublishType(GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC);

						return data;
					}
				});
	}

	@Override
	public List<ActivityNameListInfo> queryMyList(final String userId)
	{
		final List<ActivityNameListInfo> array = new ArrayList<ActivityNameListInfo>();

		String sql = "SELECT a.TaskID,a.TaskName,g.member_available_num "
				+ " FROM cscart_ga_task a,cscart_ga_group_member m,cscart_ga_group g "
				+ " WHERE a.TaskID=m.group_id AND a.TaskID=g.group_id AND m.user_id=? AND g.channel_type=?";
		logger.debug("SELECT a.TaskID,a.TaskName,g.member_available_num "
				+ " FROM cscart_ga_task a,cscart_ga_group_member m,cscart_ga_group g "
				+ " WHERE a.TaskID=m.group_id AND a.TaskID=g.group_id AND m.user_id=" + userId + " AND g.channel_type="
				+ GlobalArgs.CHANNEL_TYPE_ACTIVITY);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, userId);
				ps.setShort(2, GlobalArgs.CHANNEL_TYPE_ACTIVITY);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				ActivityNameListInfo activity = new ActivityNameListInfo();

				activity.setId(rs.getString(1));
				activity.setName(rs.getString(2));
				activity.setMemberNum(rs.getInt(3));

				array.add(activity);
			}
		});

		return array;
	}

	@Override
	public List<ActivityNameListInfo> queryNotCompletedNameList(final String clubId, final int timestamp)
	{
		final List<ActivityNameListInfo> array = new ArrayList<ActivityNameListInfo>();

		String sql = "SELECT a.TaskID,a.TaskName,g.member_num " + " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID=g.group_id AND a.TaskPid=? AND a.EndTime>? AND g.channel_type=?";
		logger.debug("SELECT a.TaskID,a.TaskName,g.member_num " + " FROM cscart_ga_task a,cscart_ga_group g "
				+ " WHERE a.TaskID=g.group_id AND a.TaskPid=" + clubId + " AND a.EndTime>" + timestamp
				+ " AND g.channel_type=" + GlobalArgs.CHANNEL_TYPE_ACTIVITY);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, clubId);
				ps.setInt(2, timestamp);
				ps.setShort(3, GlobalArgs.CHANNEL_TYPE_ACTIVITY);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				ActivityNameListInfo activity = new ActivityNameListInfo();

				activity.setId(rs.getString(1));
				activity.setName(rs.getString(2));
				activity.setMemberNum(rs.getInt(3));

				array.add(activity);
			}
		});

		return array;
	}

	@Override
	public List<ActivityDetailInfo> queryByUser(final String userId)
	{
		final List<ActivityDetailInfo> array = new ArrayList<ActivityDetailInfo>();

		String sql = "SELECT a.TaskID,a.TaskName,a.TaskDesc,a.StartTime,g.state,a.publish_type "
				+ " FROM cscart_club_group g, cscart_club_group_member m, cscart_ga_task a "
				+ " WHERE g.group_id=a.TaskID AND m.group_id=a.TaskID AND m.user_id=? AND g.channel_type=?";
		logger.debug("SELECT a.TaskID,a.TaskName,a.TaskDesc,a.StartTime,g.state,a.publish_type "
				+ " FROM cscart_club_group g, cscart_club_group_member m, cscart_ga_task a "
				+ " WHERE g.group_id=a.TaskID AND m.group_id=a.TaskID AND m.user_id=" + userId + " AND g.channel_type="
				+ GlobalArgs.CHANNEL_TYPE_ACTIVITY);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, userId);
				ps.setShort(2, GlobalArgs.CHANNEL_TYPE_ACTIVITY);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				ActivityDetailInfo data = new ActivityDetailInfo();
				int n = 1;

				data.setId(rs.getString(n++));
				data.setName(rs.getString(n++));
				data.setDesc(rs.getString(n++));
				data.setStartTime(rs.getInt(n++));
				data.setPublishType(rs.getShort(n++));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public int countTotalJoinNum(String clubId)
	{
		String sql = "SELECT sum(member_num) from cscart_ga_group "
				+ " WHERE group_id in (SELECT TaskID FROM cscart_ga_task where TaskPid=?)";
		logger.debug("SELECT sum(member_num) from cscart_ga_group "
				+ " WHERE group_id in (SELECT TaskID FROM cscart_ga_task where TaskPid=" + clubId + ")");

		Object[] params = new Object[] { clubId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityDaoImpl.class);

}
