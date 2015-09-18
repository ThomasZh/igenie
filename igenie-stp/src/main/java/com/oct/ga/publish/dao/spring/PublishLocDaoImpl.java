package com.oct.ga.publish.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.publish.GaPublishHotLoc;
import com.oct.ga.comm.domain.publish.GaPublishLoc;
import com.oct.ga.comm.domain.publish.IdAndTimestamp;
import com.oct.ga.publish.dao.GaPublishLocDao;
import com.oct.ga.stp.utility.PaginationHelper;

public class PublishLocDaoImpl
		extends JdbcDaoSupport
		implements GaPublishLocDao
{
	@Override
	public void remove(final String activityId)
	{
		String sql = "DELETE FROM ga_activity_loc_publish WHERE activity_id=?";
		logger.debug("DELETE FROM ga_activity_loc_publish WHERE activity_id=" + activityId);

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
	public void add(final String activityId, final GaPublishLoc location, final int timestamp)
	{
		String sql = "INSERT INTO ga_activity_loc_publish (activity_id,seq,loc_x,loc_y,loc_desc,loc_mask,create_time,last_update_time) VALUES (?,?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_activity_loc_publish (activity_id,seq,loc_x,loc_y,loc_desc,loc_mask,create_time,last_update_time) VALUES ("
				+ activityId
				+ ","
				+ location.getSeq()
				+ ","
				+ location.getLocX()
				+ ","
				+ location.getLocY()
				+ ","
				+ location.getLocDesc() + "," + location.getLocMask() + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, activityId);
				ps.setShort(i++, location.getSeq());
				ps.setString(i++, location.getLocX());
				ps.setString(i++, location.getLocY());
				ps.setString(i++, location.getLocDesc());
				ps.setString(i++, location.getLocMask());
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public List<GaPublishLoc> selectPublish(String channelId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<IdAndTimestamp> selectChannelIds(String locMask, int pageNum, int pageSize)
	{
		PaginationHelper<IdAndTimestamp> ph = new PaginationHelper<IdAndTimestamp>();

		String countSql = "SELECT count(activity_id) FROM ga_activity_loc_publish WHERE loc_mask=?";
		String sql = "SELECT activity_id,create_time FROM ga_activity_loc_publish WHERE loc_mask=? ORDER BY create_time DESC";
		logger.debug("SELECT activity_id,create_time FROM ga_activity_loc_publish WHERE loc_mask=" + locMask
				+ " ORDER BY create_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { locMask }, pageNum, pageSize,
				new ParameterizedRowMapper<IdAndTimestamp>()
				{
					public IdAndTimestamp mapRow(ResultSet rs, int i)
							throws SQLException
					{
						IdAndTimestamp it = new IdAndTimestamp();

						it.setId(rs.getString(1));
						it.setTimstamp(rs.getInt(2));

						return it;
					}
				});
	}

	@Override
	public List<String> selectAllChannelIds()
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT DISTINCT activity_id FROM ga_activity_loc_publish";
		logger.debug("SELECT DISTINCT activity_id FROM ga_activity_loc_publish");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
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
	public Page<GaPublishHotLoc> selectHot(int pageNum, int pageSize)
	{
		PaginationHelper<GaPublishHotLoc> ph = new PaginationHelper<GaPublishHotLoc>();

		String countSql = "SELECT count(loc_id) FROM ga_activity_loc_publish_hot";
		String sql = "SELECT loc_x,loc_y,loc_desc,url,loc_mask,weight FROM ga_activity_loc_publish_hot ORDER BY weight DESC";
		logger.debug("SELECT loc_x,loc_y,loc_desc,url,loc_mask,weight FROM ga_activity_loc_publish_hot ORDER BY weight DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] {}, pageNum, pageSize,
				new ParameterizedRowMapper<GaPublishHotLoc>()
				{
					public GaPublishHotLoc mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GaPublishHotLoc data = new GaPublishHotLoc();
						int n = 1;

						data.setLocX(rs.getString(n++));
						data.setLocY(rs.getString(n++));
						data.setLocDesc(rs.getString(n++));
						data.setUrl(rs.getString(n++));
						data.setLocMask(rs.getString(n++));
						data.setWeight(rs.getShort(n++));

						return data;
					}
				});
	}

	private final static Logger logger = LoggerFactory.getLogger(PublishLocDaoImpl.class);

}
