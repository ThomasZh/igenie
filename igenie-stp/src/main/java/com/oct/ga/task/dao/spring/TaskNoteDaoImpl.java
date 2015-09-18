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
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.task.TaskNote;
import com.oct.ga.task.dao.GaTaskNoteDao;

/**
 * (TaskNote) Data Access Object.
 * 
 * @author Thomas.Zhang
 */
public class TaskNoteDaoImpl
		extends JdbcDaoSupport
		implements GaTaskNoteDao
{
	@Override
	public List<TaskNote> queryLastUpdate(final String taskId, final int timestamp)
	{
		final List<TaskNote> array = new ArrayList<TaskNote>();

		String sql = "SELECT note_id,channel_id,last_update_time,user_id,txt,state FROM cscart_ga_task_note WHERE channel_id=? AND last_update_time>=? ORDER BY last_update_time DESC";
		logger.debug("SELECT note_id,channel_id,last_update_time,user_id,txt,state FROM cscart_ga_task_note WHERE channel_id="
				+ taskId + " AND last_update_time>=" + timestamp + " ORDER BY last_update_time DESC");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, taskId);
				ps.setInt(2, timestamp);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				TaskNote data = new TaskNote();

				data.setNoteId(rs.getString(1));
				data.setTaskId(rs.getString(2));
				data.setTimestamp(rs.getInt(3));
				data.setAccountId(rs.getString(4));

				Blob blob = rs.getBlob(5);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setTxt(new String(bytes));
				}

				data.setState(rs.getShort(6));

				array.add(data);
			}
		});

		return array;
	}

	/**
	 * insert or update
	 */
	@Override
	public void add(final TaskNote data, final int timestamp)
	{
		String sql = "INSERT INTO cscart_ga_task_note (note_id,channel_id,create_time,last_update_time,user_id,txt) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_ga_task_note (note_id,channel_id,create_time,last_update_time,user_id,txt) VALUES ("
				+ data.getNoteId()
				+ ","
				+ data.getTaskId()
				+ ","
				+ timestamp
				+ ","
				+ timestamp
				+ ","
				+ data.getAccountId() + ",?)");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, data.getNoteId());
				ps.setString(2, data.getTaskId());
				ps.setInt(3, timestamp);
				ps.setInt(4, timestamp);
				ps.setString(5, data.getAccountId());

				Blob blob = null;
				if (data.getTxt() != null && data.getTxt().length() > 0) {
					// byte[] encoded =
					// Base64.encodeBase64(data.getExtAttribute().getBytes());
					blob = new SerialBlob(data.getTxt().getBytes());
				}
				ps.setBlob(6, blob);// mysql

			}
		});
	}

	@Override
	public void update(final TaskNote data, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_task_note SET last_update_time=?,user_id=?,txt=? WHERE note_id=?";
		logger.debug("UPDATE cscart_ga_task_note SET last_update_time=" + timestamp + ",user_id=" + data.getAccountId()
				+ ",txt=? WHERE note_id=" + data.getNoteId());

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, timestamp);
				ps.setString(2, data.getAccountId());

				Blob blob = null;
				if (data.getTxt() != null && data.getTxt().length() > 0) {
					// byte[] encoded =
					// Base64.encodeBase64(data.getExtAttribute().getBytes());
					blob = new SerialBlob(data.getTxt().getBytes());
				}
				ps.setBlob(3, blob);// mysql

				ps.setString(4, data.getNoteId());
			}
		});
	}

	@Override
	public void updateState(final String noteId, final short state, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_task_note SET last_update_time=?,state=? WHERE note_id=?";
		logger.debug("UPDATE cscart_ga_task_note SET last_update_time=" + timestamp + ",state=" + state
				+ " WHERE note_id=" + noteId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, timestamp);
				ps.setShort(2, state);
				ps.setString(3, noteId);
			}
		});
	}

	@Override
	public int countTaskNoteNumber(final String taskId)
	{
		String sql = "SELECT count(note_id) FROM cscart_ga_task_note WHERE channel_id=? AND state=?";
		logger.debug("SELECT count(note_id) FROM cscart_ga_task_note WHERE channel_id=" + taskId + " AND state="
				+ GlobalArgs.NOTE_STATE_NORMAL);

		Object[] params = new Object[] { taskId, GlobalArgs.NOTE_STATE_NORMAL };
		return getJdbcTemplate().queryForInt(sql, params);
	}

	@Override
	public boolean isExist(String noteId)
	{
		String sql = "SELECT count(note_id) FROM cscart_ga_task_note WHERE note_id=?";
		logger.debug("SELECT count(note_id) FROM cscart_ga_task_note WHERE note_id=" + noteId);

		Object[] params = new Object[] { noteId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	private static final Logger logger = LoggerFactory.getLogger(TaskNoteDaoImpl.class);

}
