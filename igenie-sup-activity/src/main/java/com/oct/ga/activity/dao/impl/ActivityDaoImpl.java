package com.oct.ga.activity.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.activity.dao.ActivityDao;
import com.oct.ga.activity.domain.Activity;

public class ActivityDaoImpl extends JdbcDaoSupport implements ActivityDao {

	@Override
	public void create(Activity activity) {
		String sql = "insert into APLAN_ACTIVITY (ID_, NAME_, BEGIN_TIME, END_TIME, BG_IMAGE_URL, STATUS_, TYPE_,"
				+ " LOCATION, GEO_X, GEO_Y, APPLY_INFO_REQUIRE, APPLY_CLOSE_TIME, MEMBER_NUM,"
				+ " CREATE_TIME, LAST_UPDATE_TIME)";
		activity.setId(Util.generateUUID());
		int currentTimeSeconds = Util.currentTimeSeconds();
		activity.setCreateTime(currentTimeSeconds);
		activity.setLastUpdateTime(currentTimeSeconds);
		getJdbcTemplate().update(sql, activity.getId(), activity.getName(), activity.getBeginTime(),
				activity.getEndTime(), activity.getBgImageUrl(), activity.getStatus(), activity.getType(),
				activity.getLocation(), activity.getGeoX(), activity.getGeoY(), activity.isApplyInfoRequire(),
				activity.getApplyCloseTime(), activity.getMemberNum(), activity.getCreateTime(),
				activity.getLastUpdateTime());
	}

	@Override
	public boolean update(Activity activity) {
		String sql = "update APLAN_ACTIVITY (NAME_, BEGIN_TIME, END_TIME, BG_IMAGE_URL,"
				+ " LOCATION, GEO_X, GEO_Y, APPLY_INFO_REQUIRE, APPLY_CLOSE_TIME) where ID_ = ?";
		int count = getJdbcTemplate().update(sql, activity.getName(), activity.getBeginTime(), activity.getEndTime(),
				activity.getBgImageUrl(), activity.getLocation(), activity.getGeoX(), activity.getGeoY(),
				activity.isApplyInfoRequire(), activity.getApplyCloseTime(), activity.getId());
		return count > 0;
	}

	@Override
	public boolean delete(String id) {
		String sql = "delete APLAN_ACTIVITY where ID_ = ?";
		return getJdbcTemplate().update(sql, id) > 0;
	}

	@Override
	public Activity read(String id) {
		return getJdbcTemplate().queryForObject("select * from APLAN_ACTIVITY where ID_ = ?", new RowMapperImpl());
	}

	@Autowired
	public void setGaDataSource(@Qualifier("gaDataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

	private class RowMapperImpl implements RowMapper<Activity> {

		@Override
		public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
			Activity activity = new Activity();
			activity.setId(rs.getString("ID_"));
			activity.setName(rs.getString("NAME_"));
			activity.setBeginTime(rs.getLong("BEGIN_TIME"));
			activity.setEndTime(rs.getLong("END_TIME"));
			activity.setBgImageUrl(rs.getString("BG_IMAGE_URL"));
			activity.setStatus(rs.getInt("STATUS_"));
			activity.setType(rs.getInt("TYPE_"));
			activity.setLocation(rs.getString("LOCATION"));
			activity.setGeoX(rs.getString("GEO_X"));
			activity.setGeoY(rs.getString("GEO_Y"));
			activity.setApplyInfoRequire(rs.getBoolean("APPLY_INFO_REQUIRE"));
			activity.setApplyCloseTime(rs.getInt("APPLY_CLOSE_TIME"));
			activity.setMemberNum(rs.getInt("MEMBER_NUM"));
			activity.setCreateTime(rs.getLong("CREATE_TIME"));
			activity.setLastUpdateTime(rs.getLong("LAST_UPDATE_TIME"));
			return activity;
		}

	}
}
