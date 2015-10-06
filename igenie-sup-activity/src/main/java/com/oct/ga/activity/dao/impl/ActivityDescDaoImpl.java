package com.oct.ga.activity.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.activity.dao.ActivityDescDao;
import com.oct.ga.activity.domain.ActivityDesc;
import com.oct.ga.activity.domain.DescContent;

public class ActivityDescDaoImpl extends JdbcDaoSupport implements ActivityDescDao {

	@Override
	public void create(ActivityDesc desc) {
		String sql = "insert into APLAN_ACTIVITY_DESC (ID_, ACTIVITY_ID, TITLE, CONTENTS, IDX, CREATE_TIME) values"
				+ " (?, ?, ?, ?, ?, ?)";
		desc.setId(Util.generateUUID());
		desc.setCreateTime(Util.currentTimeSeconds());
		getJdbcTemplate().update(sql, desc.getId(), desc.getActivityId(), desc.getTitle(),
				Util.toJson(desc.getContents()), desc.getIdx(), desc.getCreateTime());
	}

	@Override
	public int deleteByActivityId(String activityId) {
		return getJdbcTemplate().update("delete from APLAN_ACTIVITY_DESC where ACTIVITY_ID = ?", activityId);
	}

	@Override
	public List<ActivityDesc> findByActivityId(String activityId) {
		String sql = "select * from APLAN_ACTIVITY_DESC where ACTIVITY_ID = ? order by IDX asc";
		return getJdbcTemplate().query(sql, new RowMapperImpl());
	}

	@Autowired
	public void setGaDataSource(@Qualifier("gaDataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

	private class RowMapperImpl implements RowMapper<ActivityDesc> {

		@Override
		public ActivityDesc mapRow(ResultSet rs, int rowNum) throws SQLException {
			ActivityDesc desc = new ActivityDesc();
			desc.setId(rs.getString("ID_"));
			desc.setActivityId(rs.getString("ACTIVITY_ID"));
			desc.setTitle(rs.getString("TITLE"));
			desc.setContents(Util.jsonToList(rs.getString("CONTENTS"), DescContent.class));
			desc.setIdx(rs.getInt("IDX"));
			desc.setCreateTime(rs.getLong("CREATE_TIME"));
			return desc;
		}

	}
}
