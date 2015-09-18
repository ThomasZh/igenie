package com.oct.ga.task.dao.spring;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

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
import com.oct.ga.comm.domain.taskext.ChildTaskMaster;
import com.oct.ga.comm.domain.taskext.ProjectMaster;
import com.oct.ga.comm.domain.taskext.TodayTaskMaster;
import com.oct.ga.comm.domain.taskpro.TaskProBaseInfo;
import com.oct.ga.comm.domain.taskpro.TaskProDetailInfo;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.task.dao.GaTaskInfoDao;

/**
 * (Task) Data Access Object.
 * 
 * @author Thomas.Zhang
 */
public class TaskInfoDaoImpl
		extends JdbcDaoSupport
		implements GaTaskInfoDao
{
	@Override
	public void updateProjectId(final String taskId, final String projectId, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_task SET TaskPid=?,LastUpdateTime=? WHERE TaskID=?";
		logger.debug("UPDATE cscart_ga_task SET TaskPid=" + projectId + ",LastUpdateTime=" + timestamp
				+ " WHERE TaskID=" + taskId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, projectId);
				ps.setInt(2, timestamp);
				ps.setString(3, taskId);
			}
		});
	}

	@Override
	public void updateCompletedTime(final String taskId, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_task SET execute_end_time=?,LastUpdateTime=? WHERE TaskID=?";
		logger.debug("UPDATE cscart_ga_task SET execute_end_time=" + timestamp + ",LastUpdateTime=" + timestamp
				+ " WHERE TaskID=" + taskId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, timestamp);
				ps.setInt(2, timestamp);
				ps.setString(3, taskId);
			}
		});
	}

	@Override
	public ProjectMaster queryProjectMaster(final String projectId)
	{
		final ProjectMaster data = new ProjectMaster();

		String sql = "SELECT g.group_id,t.TaskName,t.StartTime,g.state,t.Color "
				+ " FROM cscart_ga_group g, cscart_ga_task t " + " WHERE g.group_id=t.TaskId AND g.group_id=?";
		logger.debug("SELECT g.group_id,t.TaskName,t.StartTime,g.state,t.Color "
				+ " FROM cscart_ga_group g, cscart_ga_task t " + " WHERE g.group_id=t.TaskId AND g.group_id="
				+ projectId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, projectId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setId(rs.getString(1));
				data.setName(rs.getString(2));
				data.setStart(rs.getInt(3));
				data.setState(rs.getShort(4));
				data.setColor(rs.getShort(5));
			}
		});

		return data;
	}

	@Override
	public List<ChildTaskMaster> queryLastModifyChildTask(final String projectId, final int lastTryTime)
	{
		final List<ChildTaskMaster> array = new ArrayList<ChildTaskMaster>();

		String sql = "SELECT g.group_id,t.TaskName,t.StartTime,t.EndTime,g.state,g.member_num,t.Color,t.TaskDesc "
				+ " FROM cscart_ga_group g, cscart_ga_task t "
				+ " WHERE g.group_id=t.TaskId AND t.TaskPid=? AND g.last_update_time>=?";
		logger.debug("SELECT g.group_id,t.TaskName,t.StartTime,t.EndTime,g.state,g.member_num,t.Color,t.TaskDesc "
				+ " FROM cscart_ga_group g, cscart_ga_task t " + " WHERE g.group_id=t.TaskId AND t.TaskPid="
				+ projectId + " AND g.last_update_time>=" + lastTryTime);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, projectId);
				ps.setInt(2, lastTryTime);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int n = 1;
				ChildTaskMaster data = new ChildTaskMaster();

				data.setId(rs.getString(n++));
				data.setName(rs.getString(n++));
				data.setStart(rs.getInt(n++));
				data.setEnd(rs.getInt(n++));
				data.setState(rs.getShort(n++));
				data.setMember(rs.getShort(n++));
				data.setColor(rs.getShort(n++));
				data.setDesc(rs.getString(n++));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public TodayTaskMaster queryTodayTaskMaster(final String taskId)
	{
		final TodayTaskMaster data = new TodayTaskMaster();

		String sql = "SELECT g.group_id,t.TaskName,t.StartTime,g.state,t.Color,t.TaskPid,t.execute_end_time,g.depth "
				+ " FROM cscart_ga_group g, cscart_ga_task t " + " WHERE g.group_id=t.TaskId AND g.group_id=?";
		logger.debug("SELECT g.group_id,t.TaskName,t.StartTime,g.state,t.Color,t.TaskPid,t.execute_end_time,g.depth "
				+ " FROM cscart_ga_group g, cscart_ga_task t " + " WHERE g.group_id=t.TaskId AND g.group_id=" + taskId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, taskId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setId(rs.getString(1));
				data.setName(rs.getString(2));
				data.setStart(rs.getInt(3));
				data.setState(rs.getShort(4));
				data.setColor(rs.getShort(5));
				data.setPid(rs.getString(6));
				data.setExecuteEnd(rs.getInt(7));
				data.setDepth(rs.getInt(8));
			}
		});

		return data;
	}

	@Override
	public short queryChannelType(String taskId)
	{
		try {
			String sql = "SELECT channel_type FROM cscart_ga_task WHERE TaskId=?";
			logger.debug("SELECT channel_type FROM cscart_ga_task WHERE TaskId=" + taskId);

			Object[] params = new Object[] { taskId };
			int count = this.getJdbcTemplate().queryForInt(sql, params);
			return (short) count;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}

	@Override
	public List<String> queryCompletedProjectIds(final String userId, final int lastTryTime)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT g.group_id "
				+ " FROM cscart_ga_group g, cscart_ga_group_member m "
				+ " WHERE g.group_id=m.group_id AND g.depth=0 AND m.user_id=? AND g.last_update_time>=? AND g.state=? AND m.state=?";
		logger.debug("SELECT g.group_id " + " FROM cscart_ga_group g, cscart_ga_group_member m "
				+ " WHERE g.group_id=m.group_id AND g.depth=0 AND m.user_id=" + userId + " AND g.last_update_time>="
				+ lastTryTime + " AND g.state=" + GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED + " AND m.state="
				+ GlobalArgs.INVITE_STATE_ACCPET);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, userId);
				ps.setInt(2, lastTryTime);
				ps.setShort(3, GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED);
				ps.setShort(4, GlobalArgs.INVITE_STATE_ACCPET);
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
	public TaskProBaseInfo queryTaskBaseInfo(final String taskId)
	{
		final TaskProBaseInfo data = new TaskProBaseInfo();

		String sql = "SELECT g.group_id,t.TaskPid,t.TaskName,t.StartTime,g.state,g.child_num,g.member_num,g.attachment_num,g.note_num,g.depth,t.Color "
				+ " FROM cscart_ga_group g, cscart_ga_task t " + " WHERE g.group_id=t.TaskId AND g.group_id=?";
		logger.debug("SELECT g.group_id,t.TaskPid,t.TaskName,t.StartTime,g.state,g.child_num,g.member_num,g.attachment_num,g.note_num,g.depth,t.Color "
				+ " FROM cscart_ga_group g, cscart_ga_task t " + " WHERE g.group_id=t.TaskId AND g.group_id=" + taskId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, taskId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setId(rs.getString(1));
				data.setPid(rs.getString(2));
				data.setName(rs.getString(3));
				data.setStartTime(rs.getInt(4));
				data.setState(rs.getShort(5));
				data.setChildNum(rs.getShort(6));
				data.setMemberNum(rs.getShort(7));
				data.setFileNum(rs.getShort(8));
				data.setNoteNum(rs.getShort(9));
				data.setDepth(rs.getShort(10));
				data.setColor(rs.getShort(11));
			}
		});

		return data;
	}

	@Override
	public List<String> queryUncompletedProjectIds(final String userId, final int lastTryTime)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT g.group_id "
				+ " FROM cscart_ga_group g, cscart_ga_group_member m "
				+ " WHERE g.group_id=m.group_id AND g.depth=0 AND m.user_id=? AND g.last_update_time>=? AND g.state=? AND m.state=?";
		logger.debug("SELECT g.group_id " + " FROM cscart_ga_group g, cscart_ga_group_member m "
				+ " WHERE g.group_id=m.group_id AND g.depth=0 AND m.user_id=" + userId + " AND g.last_update_time>"
				+ lastTryTime + " AND g.state=" + GlobalArgs.CLUB_ACTIVITY_STATE_OPENING + " AND m.state="
				+ GlobalArgs.INVITE_STATE_ACCPET);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, userId);
				ps.setInt(2, lastTryTime);
				ps.setShort(3, GlobalArgs.CLUB_ACTIVITY_STATE_OPENING);
				ps.setShort(4, GlobalArgs.INVITE_STATE_ACCPET);
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
	public List<String> queryCompletedTaskIds(final String userId, final int lastTryTime, final int startTime,
			final int endTime)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT g.group_id "
				+ " FROM cscart_ga_group g, cscart_ga_task t, cscart_ga_group_member m "
				+ " WHERE g.group_id=m.group_id AND g.group_id=t.TaskId AND g.child_num=0 AND m.user_id=? AND g.last_update_time>=? AND g.state=? AND m.state=? AND t.execute_end_time>? AND t.execute_end_time<?";
		logger.debug("SELECT g.group_id " + " FROM cscart_ga_group g, cscart_ga_task t, cscart_ga_group_member m "
				+ " WHERE g.group_id=m.group_id AND g.group_id=t.TaskId AND g.child_num=0 AND m.user_id=" + userId
				+ " AND g.last_update_time>" + lastTryTime + " AND g.state=" + GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED
				+ " AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET + "AND t.execute_end_time>" + startTime
				+ " AND t.execute_end_time<" + endTime);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, userId);
				ps.setInt(2, lastTryTime);
				ps.setShort(3, GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED);
				ps.setShort(4, GlobalArgs.INVITE_STATE_ACCPET);
				ps.setInt(5, startTime);
				ps.setInt(6, endTime);
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
	public List<String> queryUncompletedTaskIds(final String userId, final int lastTryTime, final int timestamp)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT g.group_id "
				+ " FROM cscart_ga_group g, cscart_ga_task t, cscart_ga_group_member m "
				+ " WHERE g.group_id=m.group_id AND g.group_id=t.TaskId AND g.child_num=0 AND m.user_id=? AND g.last_update_time>=? AND t.StartTime<? AND g.state=? AND m.state=?";
		logger.debug("SELECT g.group_id " + " FROM cscart_ga_group g, cscart_ga_task t, cscart_ga_group_member m "
				+ " WHERE g.group_id=m.group_id AND g.group_id=t.TaskId AND g.child_num=0 AND m.user_id=" + userId
				+ " AND g.create_time>=" + lastTryTime + " AND t.StartTime<" + timestamp + " AND g.state="
				+ GlobalArgs.CLUB_ACTIVITY_STATE_OPENING + " AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, userId);
				ps.setInt(2, lastTryTime);
				ps.setInt(3, timestamp);
				ps.setShort(4, GlobalArgs.CLUB_ACTIVITY_STATE_OPENING);
				ps.setShort(5, GlobalArgs.INVITE_STATE_ACCPET);
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
	public List<String> queryUncompletedTaskIds(final String userId, final int lastTryTime, final int startTime,
			final int endTime)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT g.group_id "
				+ " FROM cscart_ga_group g, cscart_ga_task t, cscart_ga_group_member m "
				+ " WHERE g.group_id=m.group_id AND g.group_id=t.TaskId AND g.child_num=0 AND m.user_id=? AND g.last_update_time>=? AND t.StartTime>? AND t.StartTime<? AND g.state=? AND m.state=?";
		logger.debug("SELECT g.group_id " + " FROM cscart_ga_group g, cscart_ga_task t, cscart_ga_group_member m "
				+ " WHERE g.group_id=m.group_id AND g.group_id=t.TaskId AND g.child_num=0 AND m.user_id=" + userId
				+ " AND g.last_update_time>=" + lastTryTime + " AND t.StartTime>" + startTime + " AND t.StartTime<"
				+ endTime + " AND g.state=" + GlobalArgs.CLUB_ACTIVITY_STATE_OPENING + " AND m.state="
				+ GlobalArgs.INVITE_STATE_ACCPET);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, userId);
				ps.setInt(2, lastTryTime);
				ps.setInt(3, startTime);
				ps.setInt(4, endTime);
				ps.setShort(5, GlobalArgs.CLUB_ACTIVITY_STATE_OPENING);
				ps.setShort(6, GlobalArgs.INVITE_STATE_ACCPET);
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
	public List<TaskProBaseInfo> queryLastUpdateChildTask(final String taskId, final int lastTryTime)
	{
		final List<TaskProBaseInfo> array = new ArrayList<TaskProBaseInfo>();

		String sql = "SELECT g.group_id,t.TaskPid,t.TaskName,t.StartTime,t.EndTime,g.state,g.child_num,g.member_num,g.attachment_num,g.note_num,g.depth,t.Color,t.TaskDesc "
				+ " FROM cscart_ga_group g, cscart_ga_task t "
				+ " WHERE g.group_id=t.TaskId AND t.TaskPid=? AND g.last_update_time>=?";
		logger.debug("SELECT g.group_id,t.TaskPid,t.TaskName,t.StartTime,t.EndTime,g.state,g.child_num,g.member_num,g.attachment_num,g.note_num,g.depth,t.Color,t.TaskDesc "
				+ " FROM cscart_ga_group g, cscart_ga_task t "
				+ " WHERE g.group_id=t.TaskId AND t.TaskPid="
				+ taskId
				+ " AND g.last_update_time>=" + lastTryTime);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, taskId);
				ps.setInt(2, lastTryTime);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int n = 1;
				TaskProBaseInfo data = new TaskProBaseInfo();

				data.setId(rs.getString(n++));
				data.setPid(rs.getString(n++));
				data.setName(rs.getString(n++));
				data.setStartTime(rs.getInt(n++));
				data.setEndTime(rs.getInt(n++));
				data.setState(rs.getShort(n++));
				data.setChildNum(rs.getShort(n++));
				data.setMemberNum(rs.getShort(n++));
				data.setFileNum(rs.getShort(n++));
				data.setNoteNum(rs.getShort(n++));
				data.setDepth(rs.getShort(n++));
				data.setColor(rs.getShort(n++));
				data.setDesc(rs.getString(n++));

				array.add(data);
			}
		});

		return array;
	}

	public TaskProDetailInfo queryTaskDetailLastUpdate(final String taskId, final int lastTryTime)
	{
		final TaskProDetailInfo data = new TaskProDetailInfo();

		String sql = "SELECT t.TaskPid,t.TaskName,t.StartTime,g.state,g.child_num,g.member_num,g.attachment_num,g.note_num,g.depth,t.Color,t.TaskDesc,t.EndTime,t.ExtAttribute,t.loc_desc,t.loc_x,t.loc_y,t.execute_end_time "
				+ " FROM cscart_ga_group g, cscart_ga_task t "
				+ " WHERE g.group_id=t.TaskId AND t.TaskId=? AND g.last_update_time>=?";
		logger.debug("SELECT t.TaskPid,t.TaskName,t.StartTime,g.state,g.child_num,g.member_num,g.attachment_num,g.note_num,g.depth,t.Color,t.TaskDesc,t.EndTime,t.ExtAttribute,t.loc_desc,t.loc_x,t.loc_y,t.execute_end_time "
				+ " FROM cscart_ga_group g, cscart_ga_task t "
				+ " WHERE g.group_id=t.TaskId AND t.TaskId="
				+ taskId
				+ " AND g.last_update_time>=" + lastTryTime);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, taskId);
				ps.setInt(2, lastTryTime);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setId(taskId);
				data.setPid(rs.getString(1));
				data.setName(rs.getString(2));
				data.setStartTime(rs.getInt(3));
				data.setState(rs.getShort(4));
				data.setChildNum(rs.getShort(5));
				data.setMemberNum(rs.getShort(6));
				data.setFileNum(rs.getShort(7));
				data.setNoteNum(rs.getShort(8));
				data.setDepth(rs.getShort(9));
				data.setColor(rs.getShort(10));
				data.setDesc(rs.getString(11));
				data.setEndTime(rs.getInt(12));

				Blob blob = rs.getBlob(13);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setExtAttribute(new String(bytes));
				}

				data.setLocDesc(rs.getString(14));
				data.setLocX(rs.getString(15));
				data.setLocY(rs.getString(16));
				data.setExecuteEnd(rs.getInt(17));
			}
		});

		return data;
	}

	// ////////////////////////////////////////////////////////

	@Override
	public List<String> queryChildrenTaskList(final String taskId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT TaskId FROM cscart_ga_task WHERE TaskPid=?";
		logger.debug("SELECT TaskId FROM cscart_ga_task WHERE TaskPid=" + taskId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, taskId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				array.add(rs.getString(1));
			}
		});

		return array;
	}

	@Override
	public boolean isExist(final String taskId)
	{
		String sql = "SELECT count(TaskId) FROM cscart_ga_task WHERE TaskId=?";
		logger.debug("SELECT count(TaskId) FROM cscart_ga_task WHERE TaskId=" + taskId);

		Object[] params = new Object[] { taskId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public TaskProExtInfo query(final String taskId)
	{
		final TaskProExtInfo data = new TaskProExtInfo();

		String sql = "SELECT t.TaskID,t.TaskPid,t.TaskName,t.TaskDesc,t.ExtAttribute,t.TemplateID,t.CreateAccountID,t.Color,g.member_num,g.attachment_num,g.child_num,g.state,t.StartTime,t.EndTime,t.ImagePath,t.Permission,g.depth,g.channel_type "
				+ " FROM cscart_ga_task t, cscart_ga_group g " + " WHERE t.TaskID=g.group_id AND t.TaskID=?";
		logger.debug("SELECT t.TaskID,t.TaskPid,t.TaskName,t.TaskDesc,t.ExtAttribute,t.TemplateID,t.CreateAccountID,t.Color,g.member_num,g.attachment_num,g.child_num,g.state,t.StartTime,t.EndTime,t.ImagePath,t.Permission,g.depth,g.channel_type "
				+ " FROM cscart_ga_task t, cscart_ga_group g " + " WHERE t.TaskID=g.group_id AND t.TaskID=" + taskId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, taskId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setId(rs.getString(1));
				data.setPid(rs.getString(2));
				data.setName(rs.getString(3));
				data.setDesc(rs.getString(4));

				Blob blob = rs.getBlob(5);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setExtAttribute(new String(bytes));
				}

				data.setTemplateId(rs.getString(6));
				data.setCreateAccountId(rs.getString(7));
				data.setColor(rs.getShort(8));
				data.setMemberNum(rs.getShort(9));
				data.setFileNum(rs.getShort(10));
				data.setChildNum(rs.getShort(11));
				data.setState(rs.getShort(12));
				data.setStartTime(rs.getInt(13));
				data.setEndTime(rs.getInt(14));
				data.setTitleBkImage(rs.getString(15));
				data.setPermission(rs.getInt(16));
				data.setDepth(rs.getShort(17));
				data.setChannelType(rs.getShort(18));
			}
		});

		return data;
	}

	@Override
	public TaskProExtInfo queryLastUpdate(final String taskId, final int timestamp)
	{
		final TaskProExtInfo data = new TaskProExtInfo();

		String sql = "SELECT t.TaskID,t.TaskPid,t.TaskName,t.TaskDesc,t.ExtAttribute,t.TemplateID,t.CreateAccountID,t.Color,g.member_num,g.attachment_num,g.child_num,g.state,t.StartTime,t.EndTime,t.ImagePath,t.Permission,g.depth "
				+ " FROM cscart_ga_task t,cscart_ga_group g "
				+ " WHERE t.TaskID=g.group_id AND t.TaskID=? AND t.LastUpdateTime>=?";
		logger.debug("SELECT t.TaskID,t.TaskPid,t.TaskName,t.TaskDesc,t.ExtAttribute,t.TemplateID,t.CreateAccountID,t.Color,g.member_num,g.attachment_num,g.child_num,g.state,t.StartTime,t.EndTime,t.ImagePath,t.Permission,g.depth "
				+ " FROM cscart_ga_task t,cscart_ga_group g "
				+ " WHERE t.TaskID=g.group_id AND t.TaskID="
				+ taskId
				+ " AND t.LastUpdateTime>=" + timestamp);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, taskId);
				ps.setLong(2, timestamp);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setId(rs.getString(1));
				data.setPid(rs.getString(2));
				data.setName(rs.getString(3));
				data.setDesc(rs.getString(4));

				Blob blob = rs.getBlob(5);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setExtAttribute(new String(bytes));
				}

				data.setTemplateId(rs.getString(6));
				data.setCreateAccountId(rs.getString(7));
				data.setColor(rs.getShort(8));
				data.setMemberNum(rs.getShort(9));
				data.setFileNum(rs.getShort(10));
				data.setChildNum(rs.getShort(11));
				data.setState(rs.getShort(12));
				data.setStartTime(rs.getInt(13));
				data.setEndTime(rs.getInt(14));
				data.setTitleBkImage(rs.getString(15));
				data.setPermission(rs.getInt(16));
				data.setDepth(rs.getShort(17));
			}
		});

		return data;
	}

	@Override
	public void add(final TaskProExtInfo data, final int timestamp)
	{
		String sql = "INSERT INTO cscart_ga_task (TaskID,TaskPid,TaskName,TaskDesc,ExtAttribute,TemplateID,TemplateVersion,CreateAccountID,Color,StartTime,EndTime,CreateTime,LastUpdateTime,ImagePath,Permission) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		logger.debug(sql);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, data.getId());
				ps.setString(2, data.getPid());
				ps.setString(3, data.getName());
				ps.setString(4, data.getDesc());

				Blob blob = null;
				if (data.getExtAttribute() != null && data.getExtAttribute().length() > 0) {
					// byte[] encoded =
					// Base64.encodeBase64(data.getExtAttribute().getBytes());
					blob = new SerialBlob(data.getExtAttribute().getBytes());
				}
				ps.setBlob(5, blob);// mysql

				ps.setString(6, data.getTemplateId());
				ps.setInt(7, data.getTemplateVersion());
				ps.setString(8, data.getCreateAccountId());
				ps.setShort(9, data.getColor());
				ps.setInt(10, data.getStartTime());
				ps.setInt(11, data.getEndTime());
				ps.setInt(12, timestamp);
				ps.setInt(13, timestamp);
				ps.setString(14, data.getTitleBkImage());
				ps.setInt(15, data.getPermission());
			}
		});
	}

	@Override
	public void update(final TaskProExtInfo data, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_task SET TaskName=?,TaskDesc=?,ExtAttribute=?,TemplateID=?,Color=?,StartTime=?,EndTime=?,loc_desc=?,loc_x=?,loc_y=?,LastUpdateTime=? "
				+ " WHERE TaskID=?";
		logger.debug(sql);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, data.getName());
				ps.setString(i++, data.getDesc());

				Blob blob = null;
				if (data.getExtAttribute() != null && data.getExtAttribute().length() > 0) {
					// blob = new
					// SerialBlob(data.getExtAttribute().getBytes("UTF-8"));
					// byte[] encoded =
					// Base64.encodeBase64(data.getExtAttribute().getBytes());
					blob = new SerialBlob(data.getExtAttribute().getBytes());
				}
				ps.setBlob(i++, blob);// mysql

				ps.setString(i++, data.getTemplateId());
				ps.setShort(i++, data.getColor());
				ps.setInt(i++, data.getStartTime());
				ps.setInt(i++, data.getEndTime());
				ps.setString(i++, data.getLocDesc());
				ps.setString(i++, data.getLocX());
				ps.setString(i++, data.getLocY());
				ps.setInt(i++, timestamp);
				ps.setString(i++, data.getId());
			}
		});
	}

	@Override
	public void remove(final String taskId)
	{
		String sql = "DELETE FROM cscart_ga_task WHERE TaskID=?";
		logger.debug("DELETE FROM cscart_ga_task WHERE TaskID=" + taskId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, taskId);
			}
		});
	}

	@Override
	public List<TaskProExtInfo> queryLastUpdateProjectByAccount(final String accountId, final int timestamp)
	{
		final List<TaskProExtInfo> array = new ArrayList<TaskProExtInfo>();

		String sql = "SELECT t.TaskID,t.TaskPid,t.TaskName,t.TaskDesc,t.ExtAttribute,t.TemplateID,t.CreateAccountID,t.Color,g.member_num,g.attachment_num,g.child_num,g.state,t.StartTime,t.EndTime,t.ImagePath,t.Permission,g.depth "
				+ " FROM cscart_ga_task t,cscart_ga_group g, cscart_ga_group_member m "
				+ " WHERE m.user_id=? AND t.TaskId=g.group_id AND t.TaskId=m.group_id AND t.LastUpdateTime>=? AND t.depth=0 AND m.state="
				+ GlobalArgs.INVITE_STATE_ACCPET;
		logger.debug("SELECT t.TaskID,t.TaskPid,t.TaskName,t.TaskDesc,t.ExtAttribute,t.TemplateID,t.CreateAccountID,t.Color,g.member_num,g.attachment_num,g.child_num,g.state,t.StartTime,t.EndTime,t.ImagePath,t.Permission,g.depth "
				+ " FROM cscart_ga_task t,cscart_ga_group g, cscart_ga_group_member m "
				+ " WHERE m.user_id="
				+ accountId
				+ " AND t.TaskId=g.group_id AND t.TaskId=m.group_id AND t.LastUpdateTime>="
				+ timestamp
				+ " AND t.depth=0 AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, accountId);
				ps.setInt(2, timestamp);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				TaskProExtInfo data = new TaskProExtInfo();

				data.setId(rs.getString(1));
				data.setPid(rs.getString(2));
				data.setName(rs.getString(3));
				data.setDesc(rs.getString(4));

				Blob blob = rs.getBlob(5);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setExtAttribute(new String(bytes));
				}

				data.setTemplateId(rs.getString(6));
				data.setCreateAccountId(rs.getString(7));
				data.setColor(rs.getShort(8));
				data.setMemberNum(rs.getShort(9));
				data.setFileNum(rs.getShort(10));
				data.setChildNum(rs.getShort(11));
				data.setState(rs.getShort(12));
				data.setStartTime(rs.getInt(13));
				data.setEndTime(rs.getInt(14));
				data.setTitleBkImage(rs.getString(15));
				data.setPermission(rs.getInt(16));
				data.setDepth(rs.getShort(17));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public List<TaskProExtInfo> queryLastUpdateTaskByAccount(final String accountId, final int timestamp)
	{
		final List<TaskProExtInfo> array = new ArrayList<TaskProExtInfo>();

		String sql = "SELECT t.TaskID,t.TaskPid,t.TaskName,t.TaskDesc,t.ExtAttribute,t.TemplateID,t.CreateAccountID,t.Color,g.member_num,g.attachment_num,g.child_num,g.state,t.StartTime,t.EndTime,t.ImagePath,t.Permission,g.depth "
				+ " FROM cscart_ga_task t,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE m.user_id=? AND t.TaskId=g.group_id AND t.TaskId=m.group_id AND t.LastUpdateTime>=? AND t.depth>0 AND m.state="
				+ GlobalArgs.INVITE_STATE_ACCPET;
		logger.debug("SELECT t.TaskID,t.TaskPid,t.TaskName,t.TaskDesc,t.ExtAttribute,t.TemplateID,t.CreateAccountID,t.Color,g.member_num,g.attachment_num,g.child_num,g.state,t.StartTime,t.EndTime,t.ImagePath,t.Permission,g.depth "
				+ " FROM cscart_ga_task t,cscart_ga_group g,cscart_ga_group_member m "
				+ " WHERE m.user_id="
				+ accountId
				+ " AND t.TaskId=g.group_id AND t.TaskId=m.group_id AND t.LastUpdateTime>="
				+ timestamp
				+ " AND t.depth>0 AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, accountId);
				ps.setInt(2, timestamp);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				TaskProExtInfo data = new TaskProExtInfo();

				data.setId(rs.getString(1));
				data.setPid(rs.getString(2));
				data.setName(rs.getString(3));
				data.setDesc(rs.getString(4));

				Blob blob = rs.getBlob(5);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setExtAttribute(new String(bytes));
				}

				data.setTemplateId(rs.getString(6));
				data.setCreateAccountId(rs.getString(7));
				data.setColor(rs.getShort(8));
				data.setMemberNum(rs.getShort(9));
				data.setFileNum(rs.getShort(10));
				data.setChildNum(rs.getShort(11));
				data.setState(rs.getShort(12));
				data.setStartTime(rs.getInt(13));
				data.setEndTime(rs.getInt(14));
				data.setTitleBkImage(rs.getString(15));
				data.setPermission(rs.getInt(16));
				data.setDepth(rs.getShort(17));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public List<TaskProExtInfo> queryLastUpdateByProject(final String projectId, final int timestamp)
	{
		final List<TaskProExtInfo> array = new ArrayList<TaskProExtInfo>();

		String sql = "SELECT t.TaskID,t.TaskPid,t.TaskName,t.TaskDesc,t.ExtAttribute,t.TemplateID,t.CreateAccountID,t.Color,g.member_num,g.attachment_num,g.child_num,g.state,t.StartTime,t.EndTime,t.ImagePath,t.Permission,g.depth "
				+ " FROM cscart_ga_task t,cscart_ga_group g "
				+ " WHERE t.TaskPid=? AND t.TaskId=g.group_id AND t.LastUpdateTime>=?";
		logger.debug("SELECT t.TaskID,t.TaskPid,t.TaskName,t.TaskDesc,t.ExtAttribute,t.TemplateID,t.CreateAccountID,t.Color,g.member_num,g.attachment_num,g.child_num,g.state,t.StartTime,t.EndTime,t.ImagePath,t.Permission,g.depth "
				+ " FROM cscart_ga_task t,cscart_ga_group g "
				+ " WHERE t.TaskPid="
				+ projectId
				+ " AND t.TaskId=g.group_id AND t.LastUpdateTime>=" + timestamp);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, projectId);
				ps.setInt(2, timestamp);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				TaskProExtInfo data = new TaskProExtInfo();

				data.setId(rs.getString(1));
				data.setPid(rs.getString(2));
				data.setName(rs.getString(3));
				data.setDesc(rs.getString(4));

				Blob blob = rs.getBlob(5);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setExtAttribute(new String(bytes));
				}

				data.setTemplateId(rs.getString(6));
				data.setCreateAccountId(rs.getString(7));
				data.setColor(rs.getShort(8));
				data.setMemberNum(rs.getShort(9));
				data.setFileNum(rs.getShort(10));
				data.setChildNum(rs.getShort(11));
				data.setState(rs.getShort(12));
				data.setStartTime(rs.getInt(13));
				data.setEndTime(rs.getInt(14));
				data.setTitleBkImage(rs.getString(15));
				data.setPermission(rs.getInt(16));
				data.setDepth(rs.getShort(17));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public int countTaskChildNumber(final String taskId)
	{
		String sql = "SELECT count(TaskId) FROM cscart_ga_task WHERE TaskPid=?";
		logger.debug("SELECT count(TaskId) FROM cscart_ga_task WHERE TaskPid=" + taskId);

		Object[] params = new Object[] { taskId };
		return getJdbcTemplate().queryForInt(sql, params);
	}

	@Override
	public int queryProjectNumberByAccount(String accountId)
	{
		String sql = "SELECT count(m.group_id) FROM cscart_ga_group_member m,cscart_ga_task i WHERE m.user_id=? AND m.group_id=i.TaskId AND i.depth=0 AND m.state="
				+ GlobalArgs.INVITE_STATE_ACCPET;
		logger.debug("SELECT count(m.group_id) FROM cscart_ga_group_member m,cscart_ga_task i WHERE m.user_id="
				+ accountId + " AND m.group_id=i.TaskId AND i.depth=0 AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET);

		Object[] params = new Object[] { accountId };
		return getJdbcTemplate().queryForInt(sql, params);
	}

	@Override
	public List<String> queryUncompleteProjectIdsByAccount(final String accountId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT m.group_id FROM cscart_ga_group_member m,cscart_ga_group g WHERE m.user_id=? AND m.group_id=g.group_id AND g.depth=0 AND g.state="
				+ GlobalArgs.CLUB_ACTIVITY_STATE_OPENING + " AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET;
		logger.debug("SELECT m.group_id FROM cscart_ga_group_member m,cscart_ga_group g WHERE m.user_id=" + accountId
				+ " AND m.group_id=g.group_id AND g.depth=0 AND g.state=" + GlobalArgs.CLUB_ACTIVITY_STATE_OPENING
				+ " AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET);

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
				array.add(rs.getString(1));
			}
		});

		return array;
	}

	@Override
	public List<String> queryProjectIdsByAccount(final String accountId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT m.group_id FROM cscart_ga_group_member m,cscart_ga_task i WHERE m.user_id=? AND m.group_id=i.TaskId AND i.depth=0 AND m.state="
				+ GlobalArgs.INVITE_STATE_ACCPET;
		logger.debug("SELECT m.group_id FROM cscart_ga_group_member m,cscart_ga_task i WHERE m.user_id=" + accountId
				+ " AND m.group_id=i.TaskId AND i.depth=0 AND m.state=" + GlobalArgs.INVITE_STATE_ACCPET);

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
				array.add(rs.getString(1));
			}
		});

		return array;
	}

	private static final Logger logger = LoggerFactory.getLogger(TaskInfoDaoImpl.class);

	@Override
	public String queryExerciseProjectId(final String accountId, final short publishType)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT m.group_id FROM cscart_ga_task t,cscart_ga_group_member m WHERE m.group_id=t.TaskID AND t.publish_type=? AND m.user_id=?";
		logger.debug("SELECT m.group_id FROM cscart_ga_task t,cscart_ga_group_member m WHERE m.group_id=t.TaskID AND t.publish_type="
				+ publishType + " AND m.user_id=" + accountId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, publishType);
				ps.setString(2, accountId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String groupId = rs.getString(1);

				array.add(groupId);
			}
		});

		if (array.size() > 0)
			return array.get(0);
		else
			return null;
	}

	@Override
	public short queryProjectState(String projectId)
	{
		int count = GlobalArgs.CLUB_ACTIVITY_STATE_NOT_EXIST;

		try {
			String sql = "SELECT state FROM cscart_ga_group WHERE group_id=?";
			logger.debug("SELECT state FROM cscart_ga_group WHERE group_id=" + projectId);

			Object[] params = new Object[] { projectId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT state FROM cscart_ga_group WHERE group_id=" + projectId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public void updateState(final String projectId, final short state, final int timestamp)
	{
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE cscart_ga_group SET state=?,last_update_time=? WHERE group_id=?";
		logger.debug("UPDATE cscart_ga_group SET state=" + state + ",LastUpdateTime=" + timestamp + " WHERE group_id="
				+ projectId);

		jdbcTemplate.update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, state);
				ps.setInt(2, timestamp);
				ps.setString(3, projectId);
			}
		});
	}

}