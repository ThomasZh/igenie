package com.oct.ga.activity.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.oct.ga.activity.dao.RecommendationDao;
import com.oct.ga.activity.domain.Recommendation;

@Repository
public class RecommendationDaoImpl extends JdbcDaoSupport implements RecommendationDao {

	@Override
	public void create(Recommendation recommendation) {
		recommendation.setId(Util.generateUUID());
		recommendation.setCreateTime(System.currentTimeMillis());
		String sql = "insert into APLAN_RECOMMENDATION"
				+ " (ID_, ACTIVITY_ID, FROM_ACCOUNT_ID, TO_ACCOUNT_ID, CONTENT, CREATE_TIME)"
				+ " values (?, ?, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, recommendation.getId(), recommendation.getActivityId(),
				recommendation.getFromAccountId(), recommendation.getToAccountId(), recommendation.getContent(),
				recommendation.getCreateTime());
	}

	@Override
	public boolean delete(String id) {
		return getJdbcTemplate().update("delete from APLAN_RECOMMENDATION where ID_ = ?", id) > 0;
	}

	@Override
	public List<Recommendation> findByActivityId(String activityId) {
		String sql = "select * from APLAN_RECOMMENDATION where ACTIVITY_ID = ?";
		return getJdbcTemplate().query(sql, new RowMapperImpl(), activityId);
	}

	@Autowired
	public void setGaDataSource(@Qualifier("gaDataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

	private class RowMapperImpl implements RowMapper<Recommendation> {

		@Override
		public Recommendation mapRow(ResultSet rs, int rowNum) throws SQLException {
			Recommendation recommendation = new Recommendation();
			recommendation.setId(rs.getString("ID_"));
			recommendation.setActivityId(rs.getString("ACTIVITY_ID"));
			recommendation.setFromAccountId(rs.getString("FROM_ACCOUNT_ID"));
			recommendation.setToAccountId(rs.getString("TO_ACCOUNT_ID"));
			recommendation.setContent(rs.getString("CONTENT"));
			recommendation.setCreateTime(rs.getLong("CREATE_TIME"));
			return recommendation;
		}

	}

}
