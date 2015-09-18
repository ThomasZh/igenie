package com.oct.ga.apply.dao.spring;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.apply.dao.GaApplicantTemplateDao;
import com.oct.ga.comm.domain.apply.GaApplicantTemplate;

public class ApplicantTemplateDaoImpl
		extends JdbcDaoSupport
		implements GaApplicantTemplateDao
{
	@Override
	public boolean isExist(String activityId)
	{
		String sql = "SELECT count(channel_id) FROM ga_applicant_template WHERE channel_id=?";
		logger.debug("SELECT count(channel_id) FROM ga_applicant_template WHERE channel_id=" + activityId);

		Object[] params = new Object[] { activityId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public void add(final String activityId, final String contactJson, final String participationJson,
			final int timestamp)
	{
		String sql = "INSERT INTO ga_applicant_template (channel_id,contact_json,participation_json,create_time,last_update_time) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO ga_applicant_template (channel_id,contact_json,participation_json,create_time,last_update_time) VALUES ("
				+ activityId + ",contact_json,participation_json," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, activityId);

				Blob bContact = null;
				if (contactJson != null && contactJson.length() > 0) {
					logger.debug(contactJson);
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					bContact = new SerialBlob(contactJson.getBytes());
				}
				ps.setBlob(i++, bContact);// mysql

				Blob bParticipation = null;
				if (participationJson != null && participationJson.length() > 0) {
					logger.debug(participationJson);
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					bParticipation = new SerialBlob(participationJson.getBytes());
				}
				ps.setBlob(i++, bParticipation);// mysql

				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void update(final String activityId, final String contactJson, final String participationJson,
			final int timestamp)
	{
		String sql = "UPDATE ga_applicant_template SET contact_json=?,participation_json=?,last_update_time=? WHERE channel_id=?";
		logger.debug("UPDATE ga_applicant_template SET contact_json=?,participation_json=?,last_update_time="
				+ timestamp + " WHERE channel_id=" + activityId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				Blob bContact = null;
				if (contactJson != null && contactJson.length() > 0) {
					logger.debug(contactJson);
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					bContact = new SerialBlob(contactJson.getBytes());
				}
				ps.setBlob(i++, bContact);// mysql

				Blob bParticipation = null;
				if (participationJson != null && participationJson.length() > 0) {
					logger.debug(participationJson);
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					bParticipation = new SerialBlob(participationJson.getBytes());
				}
				ps.setBlob(i++, bParticipation);// mysql

				ps.setInt(i++, timestamp);
				ps.setString(i++, activityId);
			}
		});
	}

	@Override
	public GaApplicantTemplate query(final String activityId)
	{
		final GaApplicantTemplate data = new GaApplicantTemplate();

		String sql = "SELECT contact_json,participation_json FROM ga_applicant_template WHERE channel_id=?";
		logger.debug("SELECT contact_json,participation_json FROM ga_applicant_template WHERE channel_id=" + activityId);

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
				int i = 1;

				String contactJson = null;
				Blob bContact = rs.getBlob(i++);
				if (bContact != null && bContact.length() > 0) {
					byte[] bytes = bContact.getBytes(1, (int) bContact.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					contactJson = new String(bytes);
				}

				String participationJson = null;
				Blob bParticipation = rs.getBlob(i++);
				if (bParticipation != null && bParticipation.length() > 0) {
					byte[] bytes = bParticipation.getBytes(1, (int) bParticipation.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					participationJson = new String(bytes);
				}

				data.setContactJson(contactJson);
				data.setParticipationJson(participationJson);
			}
		});

		return data;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplicantTemplateDaoImpl.class);

}
