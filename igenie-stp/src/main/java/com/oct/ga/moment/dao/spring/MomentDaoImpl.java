package com.oct.ga.moment.dao.spring;

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
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.domain.moment.GaMomentPhotoObject;
import com.oct.ga.moment.dao.GaMomentDao;
import com.oct.ga.stp.utility.PaginationHelper;

public class MomentDaoImpl
		extends JdbcDaoSupport
		implements GaMomentDao
{
	@Override
	public void addMoment(final String superId, final String channelId, final String momentId, final String userId,
			final String desc, final int photoNum, final int timestamp)
	{
		String sql = "INSERT INTO ga_moment (super_id,channel_id,moment_id,user_id,moment_desc,photo_num,create_time,last_update_time) VALUES (?,?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_moment (super_id,channel_id,moment_id,user_id,moment_desc,photo_num,create_time,last_update_time) VALUES ("
				+ superId
				+ ","
				+ channelId
				+ ","
				+ momentId
				+ ","
				+ userId
				+ ","
				+ desc
				+ ","
				+ photoNum
				+ ","
				+ timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;
				ps.setString(n++, superId);
				ps.setString(n++, channelId);
				ps.setString(n++, momentId);
				ps.setString(n++, userId);

				Blob blob = null;
				if (desc != null && desc.length() > 0) {
					// byte[] encoded =
					// Base64.encodeBase64(data.getContent().getBytes());
					blob = new SerialBlob(desc.getBytes());
				}
				ps.setBlob(n++, blob);// mysql

				ps.setInt(n++, photoNum);
				ps.setInt(n++, timestamp);
				ps.setInt(n++, timestamp);
			}
		});
	}

	@Override
	public void addMomentPhoto(final String superId, final String channelId, final String momentId, final int seq,
			final String photoId, final String photoUrl, final int timestamp)
	{
		String sql = "INSERT INTO ga_moment_photo (super_id,channel_id,moment_id,seq,photo_id,photo_url,create_time,last_update_time) VALUES (?,?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_moment_photo (super_id,channel_id,moment_id,seq,photo_id,photo_url,create_time,last_update_time) VALUES ("
				+ superId
				+ ","
				+ channelId
				+ ","
				+ momentId
				+ ","
				+ seq
				+ ","
				+ photoId
				+ ","
				+ photoUrl
				+ ","
				+ timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int n = 1;
				ps.setString(n++, superId);
				ps.setString(n++, channelId);
				ps.setString(n++, momentId);
				ps.setInt(n++, seq);
				ps.setString(n++, photoId);
				ps.setString(n++, photoUrl);
				ps.setInt(n++, timestamp);
				ps.setInt(n++, timestamp);
			}
		});
	}

	@Override
	public Page<GaMomentObject> queryPagination(final String channelId, final int pageNum, final int pageSize)
	{
		PaginationHelper<GaMomentObject> ph = new PaginationHelper<GaMomentObject>();
		String countSql = "SELECT count(moment_id) FROM ga_moment WHERE channel_id=?";
		String sql = "SELECT moment_id,user_id,moment_desc,create_time,favorite_num,comment_num FROM ga_moment WHERE channel_id=? ORDER BY create_time DESC";
		logger.debug("SELECT moment_id,user_id,moment_desc,create_time,favorite_num,comment_num FROM ga_moment WHERE channel_id="
				+ channelId + " ORDER BY create_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { channelId }, pageNum, pageSize,
				new ParameterizedRowMapper<GaMomentObject>()
				{
					public GaMomentObject mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GaMomentObject data = new GaMomentObject();
						int n = 1;

						data.setChannelId(channelId);
						data.setMomentId(rs.getString(n++));
						data.setUserId(rs.getString(n++));

						Blob blob = rs.getBlob(n++);
						if (blob != null && blob.length() > 0) {
							byte[] bytes = blob.getBytes(1, (int) blob.length());
							// byte[] decoded = Base64.decodeBase64(bytes);
							data.setDesc(new String(bytes));
						}

						data.setTimestamp(rs.getInt(n++));
						data.setFavoriteNum(rs.getInt(n++));
						data.setCommentNum(rs.getInt(n++));

						return data;
					}
				});
	}

	@Override
	public Page<GaMomentPhotoObject> queryMomentPhotoFlowPagination(String channelId, int pageNum, int pageSize)
	{
		PaginationHelper<GaMomentPhotoObject> ph = new PaginationHelper<GaMomentPhotoObject>();
		String countSql = "SELECT count(moment_id) FROM ga_moment_photo WHERE channel_id=?";
		String sql = "SELECT moment_id,photo_url,create_time FROM ga_moment_photo WHERE channel_id=? ORDER BY create_time DESC";
		logger.debug("SELECT moment_id,photo_url,create_time FROM ga_moment_photo WHERE channel_id=" + channelId
				+ " ORDER BY create_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { channelId }, pageNum, pageSize,
				new ParameterizedRowMapper<GaMomentPhotoObject>()
				{
					public GaMomentPhotoObject mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GaMomentPhotoObject data = new GaMomentPhotoObject();
						int n = 1;

						data.setMomentId(rs.getString(n++));
						data.setPhotoUrl(rs.getString(n++));
						data.setTimestamp(rs.getInt(n++));

						return data;
					}
				});
	}

	@Override
	public Page<GaMomentPhotoObject> queryClubMomentPhotoFlowPagination(String clubId, int pageNum, int pageSize)
	{
		PaginationHelper<GaMomentPhotoObject> ph = new PaginationHelper<GaMomentPhotoObject>();
		String countSql = "SELECT count(moment_id) FROM ga_moment_photo WHERE channel_id=?";
		String sql = "SELECT moment_id,photo_url,create_time FROM ga_moment_photo WHERE super_id=? ORDER BY create_time DESC";
		logger.debug("SELECT moment_id,photo_url,create_time FROM ga_moment_photo WHERE super_id=" + clubId
				+ " ORDER BY create_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { clubId }, pageNum, pageSize,
				new ParameterizedRowMapper<GaMomentPhotoObject>()
				{
					public GaMomentPhotoObject mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GaMomentPhotoObject data = new GaMomentPhotoObject();
						int n = 1;

						data.setMomentId(rs.getString(n++));
						data.setPhotoUrl(rs.getString(n++));
						data.setTimestamp(rs.getInt(n++));

						return data;
					}
				});
	}

	@Override
	public GaMomentObject queryMoment(final String momentId)
	{
		final GaMomentObject data = new GaMomentObject();

		String sql = "SELECT channel_id,moment_desc,user_id,create_time,favorite_num,comment_num FROM ga_moment WHERE moment_id=?";
		logger.debug("SELECT channel_id,moment_desc,user_id,create_time,favorite_num,comment_num FROM ga_moment WHERE moment_id="
				+ momentId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, momentId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int n = 1;

				data.setMomentId(momentId);

				data.setChannelId(rs.getString(n++));
				Blob blob = rs.getBlob(n++);
				if (blob != null && blob.length() > 0) {
					byte[] bytes = blob.getBytes(1, (int) blob.length());
					// byte[] decoded = Base64.decodeBase64(bytes);
					data.setDesc(new String(bytes));
				}

				data.setUserId(rs.getString(n++));
				data.setTimestamp(rs.getInt(n++));
				data.setFavoriteNum(rs.getInt(n++));
				data.setCommentNum(rs.getInt(n++));
			}
		});

		return data;
	}

	@Override
	public List<String> queryMeomentPhotos(final String momentId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT photo_url,seq FROM ga_moment_photo WHERE moment_id=? AND state=? ORDER BY seq";
		logger.debug("SELECT photo_url,seq FROM ga_moment_photo WHERE moment_id=" + momentId + " AND state="
				+ GlobalArgs.FILE_TRANS_STATE_COMPLETE + " ORDER BY seq");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, momentId);
				ps.setShort(2, GlobalArgs.FILE_TRANS_STATE_COMPLETE);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String url = rs.getString(1);

				array.add(url);
			}
		});

		return array;
	}

	@Override
	public int countPhotoNum(String channelId)
	{
		String sql = "SELECT count(channel_id) FROM ga_moment_photo WHERE channel_id=? AND state=?";
		logger.debug("SELECT count(channel_id) FROM ga_moment_photo WHERE channel_id=" + channelId + " AND state="
				+ GlobalArgs.FILE_TRANS_STATE_COMPLETE);

		Object[] params = new Object[] { channelId, GlobalArgs.FILE_TRANS_STATE_COMPLETE };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count;
	}

	@Override
	public void removeMoment(final String momentId)
	{
		String sql = "DELETE FROM ga_moment WHERE moment_id=?";
		logger.debug("DELETE FROM ga_moment WHERE moment_id=" + momentId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, momentId);
			}
		});
	}

	@Override
	public void removeMomentPhoto(final String momentId)
	{
		String sql = "DELETE FROM ga_moment_photo WHERE moment_id=?";
		logger.debug("DELETE FROM ga_moment_photo WHERE moment_id=" + momentId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, momentId);
			}
		});
	}

	@Override
	public int queryFavoriteNum(String momentId)
	{
		int count = 0;
		String sql = "SELECT favorite_num FROM ga_moment WHERE moment_id=?";
		logger.debug("SELECT favorite_num FROM ga_moment WHERE moment_id=" + momentId);

		try {
			Object[] params = new Object[] { momentId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT favorite_num FROM ga_moment WHERE moment_id=" + momentId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}
		return count;
	}

	@Override
	public void modifyFavoriteNum(final String momentId, final int num)
	{
		String sql = "UPDATE ga_moment SET favorite_num=? WHERE moment_id=?";
		logger.debug("UPDATE ga_moment SET favorite_num=" + num + " WHERE moment_id=" + momentId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setString(2, momentId);
			}
		});
	}

	@Override
	public int queryCommentNum(String momentId)
	{
		int count = 0;
		String sql = "SELECT comment_num FROM ga_moment WHERE moment_id=?";
		logger.debug("SELECT comment_num FROM ga_moment WHERE moment_id=" + momentId);

		try {
			Object[] params = new Object[] { momentId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT comment_num FROM ga_moment WHERE moment_id=" + momentId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}
		return count;
	}

	@Override
	public void modifyCommentNum(final String momentId, final int num)
	{
		String sql = "UPDATE ga_moment SET comment_num=? WHERE moment_id=?";
		logger.debug("UPDATE ga_moment SET comment_num=" + num + " WHERE moment_id=" + momentId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setString(2, momentId);
			}
		});
	}

	@Override
	public String queryMomentOwner(final String momentId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT user_id FROM ga_moment WHERE moment_id=?";
		logger.debug("SELECT user_id FROM ga_moment WHERE moment_id=" + momentId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, momentId);
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

	@Override
	public String queryChannelId(final String momentId)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT channel_id FROM ga_moment WHERE moment_id=?";
		logger.debug("SELECT channel_id FROM ga_moment WHERE moment_id=" + momentId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, momentId);
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

	private static final Logger logger = LoggerFactory.getLogger(MomentDaoImpl.class);

}
