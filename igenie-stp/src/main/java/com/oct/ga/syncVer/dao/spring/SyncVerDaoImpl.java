package com.oct.ga.syncVer.dao.spring;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.syncVer.dao.GaSyncVerDao;

public class SyncVerDaoImpl
		extends JdbcDaoSupport
		implements GaSyncVerDao
{
	@Override
	public int queryLastVer(String oid, short syncType)
	{
		String sql = "SELECT max(ver) FROM cscart_ga_obj_sync_ver_log WHERE oid=? AND sync_type=?";
		logger.debug("SELECT max(ver) FROM cscart_ga_obj_sync_ver_log WHERE oid=" + oid + " AND sync_type=" + syncType);

		Object[] params = new Object[] { oid, syncType };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count;
	}

	@Override
	public int queryUpdateTime(String oid, short syncType, int ver)
	{
		String sql = "SELECT max(timestamp) FROM cscart_ga_obj_sync_ver_log WHERE oid=? AND sync_type=? AND ver=?";
		logger.debug("SELECT max(timestamp) FROM cscart_ga_obj_sync_ver_log WHERE oid=" + oid + " AND sync_type="
				+ syncType + " AND ver=" + ver);

		Object[] params = new Object[] { oid, syncType, ver };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count;
	}

	@Override
	public void add(final String oid, final short syncType, final int ver, final int timestamp, final String userId,
			final short action)
	{
		String sql = "INSERT INTO cscart_ga_obj_sync_ver_log (oid,sync_type,ver,timestamp,user_id,action) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO cscart_ga_obj_sync_ver_log (oid,sync_type,ver,timestamp,user_id,action) VALUES ("
				+ oid + "," + syncType + "," + ver + "," + timestamp + "," + userId + "," + action + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, oid);
				ps.setShort(2, syncType);
				ps.setInt(3, ver);
				ps.setInt(4, timestamp);
				ps.setString(5, userId);
				ps.setShort(6, action);
			}
		});
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncVerDaoImpl.class);

}
