package com.oct.ga.desc.dao.spring;

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

import com.google.gson.Gson;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.desc.dao.GaDescDao;

public class DescDaoImpl
		extends JdbcDaoSupport
		implements GaDescDao
{
	@Override
	public void insert(final String activityId, final short seq, final String title, final String json,
			final int timestamp)
	{
		String sql = "INSERT INTO ga_activity_desc (activity_id,seq,title,json,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_activity_desc (activity_id,seq,title,json,create_time,last_update_time) VALUES ("
				+ activityId + "," + seq + "," + title + "," + json + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, activityId);
				ps.setInt(i++, seq);
				ps.setString(i++, title);

				Blob blob = null;
				if (json != null && json.length() > 0) {
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					blob = new SerialBlob(json.getBytes());
				}
				ps.setBlob(i++, blob);// mysql

				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void update(final String activityId, final short seq, final String title, final String json,
			final int timestamp)
	{
		String sql = "UPDATE ga_activity_desc SET title=?,json=?,last_update_time=? WHERE activity_id=? AND seq=?";
		logger.debug("UPDATE ga_activity_desc SET title=" + title + ",json" + json + ",last_update_time=" + timestamp
				+ " WHERE AND activity_id=" + activityId + " AND seq=" + seq);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, title);

				Blob blob = null;
				if (json != null && json.length() > 0) {
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					blob = new SerialBlob(json.getBytes());
				}
				ps.setBlob(i++, blob);// mysql

				ps.setInt(i++, timestamp);
				ps.setString(i++, activityId);
				ps.setInt(i++, seq);
			}
		});
	}

	@Override
	public void delete(final String activityId, final short seq)
	{
		String sql = "DELETE FROM ga_activity_desc WHERE activity_id=? AND seq=?";
		logger.debug("DELETE FROM ga_activity_desc WHERE activity_id=" + activityId + " AND seq=" + seq);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, activityId);
				ps.setShort(2, seq);
			}
		});
	}

	@Override
	public void deleteAll(final String activityId)
	{
		String sql = "DELETE FROM ga_activity_desc WHERE activity_id=?";
		logger.debug("DELETE FROM ga_activity_desc WHERE activity_id=" + activityId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, activityId);
			}
		});
	}

	@Override
	public boolean isExist(String activityId, short seq)
	{
		String sql = "SELECT count(activity_id) FROM ga_activity_desc WHERE activity_id=? AND seq=?";
		logger.debug("SELECT count(activity_id) FROM ga_activity_desc WHERE activity_id=" + activityId + " AND seq="
				+ seq);

		Object[] params = new Object[] { activityId, seq };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public List<GaDescChapter> select(final String activityId)
	{
		final List<GaDescChapter> array = new ArrayList<GaDescChapter>();

		String sql = "SELECT seq,title,json FROM ga_activity_desc WHERE activity_id=? ORDER BY seq ASC";
		logger.debug("SELECT seq,title,json FROM ga_activity_desc WHERE activity_id=" + activityId
				+ " ORDER BY seq ASC");

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
				GaDescChapter data = new GaDescChapter();
				int i = 1;

				data.setSeq(rs.getShort(i++));
				data.setTitle(rs.getString(i++));

				Blob blob = rs.getBlob(i++);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					String json = new String(bytes);

					Gson gson = new Gson();
					data = gson.fromJson(json, GaDescChapter.class);
				}

				array.add(data);
			}
		});

		return array;
	}

	private final static Logger logger = LoggerFactory.getLogger(DescDaoImpl.class);

}
