package com.oct.ga.activity.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.activity.dao.DescDao;
import com.oct.ga.activity.domain.Desc;
import com.oct.ga.activity.domain.DescContent;

public class DescDaoImpl extends JdbcDaoSupport implements DescDao {
	private static final RowMapper<Desc> ROW_MAPPER = new RowMapper<Desc>() {

		@Override
		public Desc mapRow(ResultSet rs, int rowNum) throws SQLException {
			Desc desc = new Desc();
			desc.setId(rs.getString("ID_"));
			desc.setActivityId(rs.getString("ACTIVITY_ID"));
			desc.setTitle(rs.getString("TITLE"));
			desc.setContents(Util.jsonToList(rs.getString("CONTENTS"), DescContent.class));
			desc.setIdx(rs.getInt("IDX"));
			desc.setCreateTime(rs.getInt("CREATE_TIME"));
			return desc;
		}
	};

	@Override
	public void create(Desc desc) {
		String sql = "insert into DESC_ (ID_, ACTIVITY_ID, TITLE, CONTENTS, IDX, CREATE_TIME) values"
				+ " (?, ?, ?, ?, ?, ?)";
		desc.setId(Util.generateUUID());
		desc.setCreateTime(Util.currentTimeSeconds());
		getJdbcTemplate().update(sql, desc.getId(), desc.getActivityId(), desc.getTitle(),
				Util.toJson(desc.getContents()), desc.getIdx(), desc.getCreateTime());
	}

	@Override
	public int deleteByActivityId(String activityId) {
		return getJdbcTemplate().update("delete from DESC_ where ACTIVITY_ID = ?", activityId);
	}

	@Override
	public List<Desc> findByActivityId(String activityId) {
		String sql = "select * from DESC where ACTIVITY_ID = ? order by IDX asc";
		return getJdbcTemplate().query(sql, ROW_MAPPER);
	}

}
