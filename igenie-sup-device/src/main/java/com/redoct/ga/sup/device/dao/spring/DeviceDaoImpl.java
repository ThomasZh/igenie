package com.redoct.ga.sup.device.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.redoct.ga.sup.device.dao.SupDeviceDao;
import com.redoct.ga.sup.device.domain.DeviceBasicInfo;

public class DeviceDaoImpl
		extends JdbcDaoSupport
		implements SupDeviceDao
{
	@Override
	public DeviceBasicInfo select(final String deviceId)
	{
		final DeviceBasicInfo data = new DeviceBasicInfo();

		String sql = "SELECT client_version,app_id,vendor_id,os_version,notify_token FROM ga_device_base WHERE device_id=?";
		logger.debug("SELECT client_version,app_id,vendor_id,os_version,notify_token FROM ga_device_base WHERE device_id="
				+ deviceId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, deviceId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int i = 1;

				data.setDeviceId(deviceId);
				data.setClientVersion(rs.getString(i++));
				data.setAppId(rs.getString(i++));
				data.setVendorId(rs.getString(i++));
				data.setOsVersion(rs.getString(i++));
				data.setNotifyToken(rs.getString(i++));
			}
		});

		return data;
	}

	@Override
	public boolean isExist(String deviceId)
	{
		String sql = "SELECT count(device_id) FROM ga_device_base WHERE device_id=?";
		logger.debug("SELECT count(device_id) FROM ga_device_base WHERE group_id=" + deviceId);

		Object[] params = new Object[] { deviceId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public void insert(final String deviceId, final String clientVersion, final String appId, final String vendorId,
			final int timestamp)
	{
		String sql = "INSERT INTO ga_device_base (device_id,client_version,app_id,vendor_id,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO ga_device_base (device_id,client_version,app_id,vendor_id,create_time,last_update_time) VALUES ("
				+ deviceId
				+ ","
				+ clientVersion
				+ ","
				+ appId
				+ ","
				+ vendorId
				+ ","
				+ timestamp
				+ ","
				+ timestamp
				+ ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setString(i++, deviceId);
				ps.setString(i++, clientVersion);
				ps.setString(i++, appId);
				ps.setString(i++, vendorId);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void update(final String deviceId, final String clientVersion, final String appId, final String vendorId,
			final int timestamp)
	{
		String sql = "UPDATE ga_device_base SET client_version=?,app_id=?,vendor_id=?,last_update_time=? WHERE device_id=?";
		logger.debug("UPDATE ga_device_base SET client_version=" + clientVersion + ",app_id=" + appId + ",vendor_id="
				+ vendorId + ",last_update_time=" + timestamp + " WHERE device_id=" + deviceId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, clientVersion);
				ps.setString(i++, appId);
				ps.setString(i++, vendorId);
				ps.setInt(i++, timestamp);
				ps.setString(i++, deviceId);
			}
		});
	}

	@Override
	public void update(final String deviceId, final String osVersion, final String notifyToken, final int timestamp)
	{
		String sql = "UPDATE ga_device_base SET os_version=?,notify_token=?,last_update_time=? WHERE device_id=?";
		logger.debug("UPDATE ga_device_base SET os_version=" + osVersion + ",notify_token=" + notifyToken
				+ ",last_update_time=" + timestamp + " WHERE device_id=" + deviceId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, osVersion);
				ps.setString(i++, notifyToken);
				ps.setInt(i++, timestamp);
				ps.setString(i++, deviceId);
			}
		});
	}

	private final static Logger logger = LoggerFactory.getLogger(DeviceDaoImpl.class);
}
