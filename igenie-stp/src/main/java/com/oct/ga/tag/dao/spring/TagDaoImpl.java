package com.oct.ga.tag.dao.spring;

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
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.tag.dao.GaTagDao;

public class TagDaoImpl
		extends JdbcDaoSupport
		implements GaTagDao
{
	@Override
	public void addTag(final String tag)
	{
		String sql = "INSERT INTO ga_tag (tag_name) VALUES (?)";
		logger.debug("INSERT INTO ga_tag (tag_name) VALUES (" + tag + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, tag);
			}
		});
	}

	@Override
	public void addActivityTag(final int tagId, final String activityId)
	{
		String sql = "INSERT INTO ga_tag_activity (tag_id,acitivity_id) VALUES (?,?)";
		logger.debug("INSERT INTO ga_tag_activity (tag_id,acitivity_id) VALUES (" + tagId + "," + activityId + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, tagId);
				ps.setString(2, activityId);
			}
		});
	}

	@Override
	public void removeActivityTag(final int tagId, final String activityId)
	{
		String sql = "DELETE FROM ga_tag_activity WHERE tag_id=? AND acitivity_id=?";
		logger.debug("DELETE FROM ga_tag_activity WHERE tag_id=" + tagId + " AND acitivity_id=" + activityId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, tagId);
				ps.setString(2, activityId);
			}
		});
	}

	@Override
	public List<String> queryActivityTags(final String activityId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT t.tag_name FROM ga_tag t, ga_tag_activity a WHERE a.tag_id=t.tag_id AND a.acitivity_id=?";
		logger.debug("SELECT t.tag_name FROM ga_tag t, ga_tag_activity a WHERE a.tag_id=t.tag_id AND a.acitivity_id="
				+ activityId);

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
				String tagName = rs.getString(1);

				array.add(tagName);
			}
		});

		return array;
	}

	@Override
	public List<String> queryTagActivityIds(final int tagId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT acitivity_id FROM ga_tag_activity WHERE tag_id=?";
		logger.debug("SELECT acitivity_id FROM ga_tag_activity WHERE tag_id=" + tagId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, tagId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String acitivityId = rs.getString(1);

				array.add(acitivityId);
			}
		});

		return array;
	}

	@Override
	public int queryTagId(String tag)
	{
		int count = 0;

		try {
			String sql = "SELECT tag_id FROM ga_tag WHERE tag_name=?";
			logger.debug("SELECT tag_id FROM ga_tag WHERE tag_name=" + tag);

			Object[] params = new Object[] { tag };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT tag_id FROM ga_tag WHERE tag_name=" + tag);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public boolean isExistActivityTagSummary(int tagId)
	{
		String sql = "SELECT count(tag_id) FROM ga_summary_tag_activity WHERE tag_id=?";
		logger.debug("SELECT count(tag_id) FROM ga_summary_tag_activity WHERE tag_id=" + tagId);

		Object[] params = new Object[] { tagId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public void addActivityTagSummary(final int tagId, final int num)
	{
		String sql = "INSERT INTO ga_summary_tag_activity (tag_id,activity_num) VALUES (?,?)";
		logger.debug("INSERT INTO ga_summary_tag_activity (tag_id,activity_num) VALUES (" + tagId + "," + num + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, tagId);
				ps.setInt(2, num);
			}
		});
	}

	@Override
	public void updateActivityTagSummary(final int tagId, final int num)
	{
		String sql = "UPDATE ga_summary_tag_activity SET activity_num=? WHERE tag_id=?";
		logger.debug("UPDATE ga_summary_tag_activity SET activity_num=" + num + " WHERE tag_id=" + tagId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, tagId);
				ps.setInt(2, num);
			}
		});
	}

	@Override
	public int queryActivityTagSummary(int tagId)
	{
		String sql = "SELECT count(tag_id) FROM ga_summary_tag_activity WHERE tag_id=?";
		logger.debug("SELECT count(tag_id) FROM ga_summary_tag_activity WHERE tag_id=" + tagId);

		Object[] params = new Object[] { tagId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count;
	}

	@Override
	public boolean isExistTagRelation(int tagId)
	{
		String sql = "SELECT count(tag_id) FROM ga_tag_relation WHERE tag_id=?";
		logger.debug("SELECT count(tag_id) FROM ga_tag_relation WHERE tag_id=" + tagId);

		Object[] params = new Object[] { tagId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public void addTagRelation(final int tagId, final String json)
	{
		String sql = "INSERT INTO ga_tag_relation (tag_id,relation_ids) VALUES (?,?)";
		logger.debug("INSERT INTO ga_tag_relation (tag_id,relation_ids) VALUES (" + tagId + ",?)");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, tagId);

				Blob blob = null;
				if (json != null && json.length() > 0) {
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					blob = new SerialBlob(json.getBytes());
				}
				ps.setBlob(2, blob);// mysql

			}
		});
	}

	@Override
	public void updateTagRelation(final int tagId, final String json)
	{
		String sql = "UPDATE ga_tag_relation SET relation_ids=? WHERE tag_id=?";
		logger.debug("UPDATE ga_tag_relation SET relation_ids=" + json + " WHERE tag_id=" + tagId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, tagId);

				Blob blob = null;
				if (json != null && json.length() > 0) {
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					blob = new SerialBlob(json.getBytes());
				}
				ps.setBlob(2, blob);// mysql

			}
		});
	}

	@Override
	public String queryTagRelation(final int tagId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT relation_ids FROM ga_tag_relation WHERE tag_id=?";
		logger.debug("SELECT relation_ids FROM ga_tag_relation WHERE tag_id=" + tagId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, tagId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String json = null;

				Blob blob = rs.getBlob(1);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					json = new String(bytes);
				}

				array.add(json);
			}
		});

		if (array.size() > 0)
			return array.get(0);
		else
			return null;
	}

	private static final Logger logger = LoggerFactory.getLogger(TagDaoImpl.class);

}
