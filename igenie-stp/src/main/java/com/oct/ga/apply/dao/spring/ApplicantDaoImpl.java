package com.oct.ga.apply.dao.spring;

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
import com.google.gson.reflect.TypeToken;
import com.oct.ga.apply.dao.GaApplicantDao;
import com.oct.ga.comm.domain.apply.GaApplicantCell;
import com.oct.ga.comm.domain.apply.GaApplicantDetailInfo;
import com.oct.ga.comm.domain.apply.GaApplicantInfo;

public class ApplicantDaoImpl
		extends JdbcDaoSupport
		implements GaApplicantDao
{
	@Override
	public boolean isExistContact(String activityId, String accountId)
	{
		String sql = "SELECT count(channel_id) FROM ga_applicant_contact WHERE channel_id=? AND account_id=?";
		logger.debug("SELECT count(channel_id) FROM ga_applicant_contact WHERE channel_id=" + activityId
				+ " AND account_id=" + accountId);

		Object[] params = new Object[] { activityId, accountId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public void addContact(final String activityId, final String accountId, final String contactInfoJson,
			final int timestamp)
	{
		String sql = "INSERT INTO ga_applicant_contact (channel_id,account_id,json,create_time,last_update_time) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO ga_applicant_contact (channel_id,account_id,json,create_time,last_update_time) VALUES ("
				+ activityId + ",account_id=" + accountId + ",json," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, activityId);
				ps.setString(i++, accountId);

				Blob blob = null;
				if (contactInfoJson != null && contactInfoJson.length() > 0) {
					logger.debug(contactInfoJson);
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					blob = new SerialBlob(contactInfoJson.getBytes());
				}
				ps.setBlob(i++, blob);// mysql

				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void updateContact(final String activityId, final String accountId, final String contactInfoJson,
			final int timestamp)
	{
		String sql = "UPDATE ga_applicant_contact SET json=?,last_update_time=? WHERE channel_id=? AND account_id=?";
		logger.debug("UPDATE ga_applicant_contact SET json=?,last_update_time=" + timestamp + " WHERE channel_id="
				+ activityId + " AND account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				Blob blob = null;
				if (contactInfoJson != null && contactInfoJson.length() > 0) {
					logger.debug(contactInfoJson);
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					blob = new SerialBlob(contactInfoJson.getBytes());
				}
				ps.setBlob(i++, blob);// mysql

				ps.setInt(i++, timestamp);
				ps.setString(i++, activityId);
				ps.setString(i++, accountId);
			}
		});
	}

	@Override
	public void removeAll(final String activityId, final String accountId)
	{
		String sql = "DELETE FROM ga_applicant WHERE channel_id=? AND account_id=?";
		logger.debug("DELETE FROM ga_applicant WHERE channel_id=" + activityId + " AND account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, activityId);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public void add(final String activityId, final String accountId, final int seq, final String json,
			final int timestamp)
	{
		String sql = "INSERT INTO ga_applicant (channel_id,account_id,seq,json,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_applicant (channel_id,account_id,seq,json,create_time,last_update_time) VALUES ("
				+ activityId + "," + accountId + "," + seq + ",json," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, activityId);
				ps.setString(i++, accountId);
				ps.setInt(i++, seq);

				Blob blob = null;
				if (json != null && json.length() > 0) {
					logger.debug(json);
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
	public List<GaApplicantInfo> query(final String activityId, final String accountId)
	{
		final List<GaApplicantInfo> array = new ArrayList<GaApplicantInfo>();

		String sql = "SELECT seq,json FROM ga_applicant WHERE channel_id=? AND account_id=?";
		logger.debug("SELECT seq,json FROM ga_applicant WHERE channel_id=" + activityId + " AND account_id="
				+ accountId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, activityId);
				ps.setString(2, accountId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				GaApplicantInfo data = new GaApplicantInfo();
				int i = 1;

				data.setSeq(rs.getInt(i++));

				String json = null;
				Blob blob = rs.getBlob(i++);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					json = new String(bytes);
				}
				logger.debug("json: " + json);
				if (json != null && json.length() > 0) {
					Gson gson = new Gson();
					List<GaApplicantCell> applicantCells = gson.fromJson(json, new TypeToken<List<GaApplicantCell>>()
					{
					}.getType());
					data.setApplicant(applicantCells);
				}

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public List<GaApplicantDetailInfo> query(final String activityId)
	{
		final List<GaApplicantDetailInfo> array = new ArrayList<GaApplicantDetailInfo>();

		String sql = "SELECT account_id,seq,json FROM ga_applicant WHERE channel_id=?";
		logger.debug("SELECT account_id,seq,json FROM ga_applicant WHERE channel_id=" + activityId);

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
				GaApplicantDetailInfo data = new GaApplicantDetailInfo();
				int i = 1;

				data.setAccountId(rs.getString(i++));
				data.setSeq(rs.getInt(i++));

				String json = null;
				Blob blob = rs.getBlob(i++);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					json = new String(bytes);
				}
				logger.debug("json: " + json);
				if (json != null && json.length() > 0) {
					Gson gson = new Gson();
					List<GaApplicantCell> applicantCells = gson.fromJson(json, new TypeToken<List<GaApplicantCell>>()
					{
					}.getType());
					data.setApplicant(applicantCells);
				}

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public String queryApplicantContact(final String activityId, final String accountId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT json FROM ga_applicant_contact WHERE channel_id=? AND account_id=?";
		logger.debug("SELECT json FROM ga_applicant_contact WHERE channel_id=" + activityId + " AND account_id="
				+ accountId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, activityId);
				ps.setString(2, accountId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int i = 1;

				String json = null;
				Blob blob = rs.getBlob(i++);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					json = new String(bytes);
				}
				logger.debug("json: " + json);

				array.add(json);
			}
		});

		if (array.size() > 0)
			return array.get(0);
		else
			return "";
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplicantDaoImpl.class);

}
