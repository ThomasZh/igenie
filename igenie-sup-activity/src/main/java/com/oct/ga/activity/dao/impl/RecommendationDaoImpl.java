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
	private static final RowMapper<Recommendation> ROW_MAPPER = new RowMapper<Recommendation>() {

		@Override
		public Recommendation mapRow(ResultSet rs, int rowNum) throws SQLException {
			Recommendation recommendation = new Recommendation();
			recommendation.setId(rs.getString("ID_"));
			recommendation.setActivityId(rs.getString("ACTIVITY_ID"));
			recommendation.setFromAccountId(rs.getString("FROM_ACCOUNT_ID"));
			recommendation.setToAccountId(rs.getString("TO_ACCOUNT_ID"));
			recommendation.setRank(rs.getInt("RANK"));
			recommendation.setContent(rs.getString("CONTENT"));
			recommendation.setCreateTime(rs.getInt("CREATE_TIME"));
			return recommendation;
		}
	};

	@Override
	public void create(Recommendation recommendation) {
		recommendation.setId(Util.generateUUID());
		recommendation.setCreateTime(Util.currentTimeSeconds());
		String sql = "insert into RECOMMENDATION"
				+ " (ID_, ACTIVITY_ID, FROM_ACCOUNT_ID, TO_ACCOUNT_ID, CONTENT, RANK, CREATE_TIME)"
				+ " values (?, ?, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, recommendation.getId(), recommendation.getActivityId(),
				recommendation.getFromAccountId(), recommendation.getToAccountId(), recommendation.getContent(),
				recommendation.getRank(), recommendation.getCreateTime());
	}

	@Override
	public boolean delete(String id) {
		return getJdbcTemplate().update("delete from RECOMMENDATION where ID_ = ?", id) > 0;
	}

	@Override
	public List<Recommendation> find(String toAccountId, int rank, int createTime, boolean prev, int pageSize) {
		String sql = "select * from RECOMMENDATION where TO_ACCOUNT_ID = ? and RANK = ? and CREATE_TIME"
				+ (prev ? " >" : " <") + " ? order by CREATE_TIME desc limit 0, ?";
		return getJdbcTemplate().query(sql, ROW_MAPPER, toAccountId, rank, createTime, pageSize);
	}

	@Autowired
	public void setGaDataSource(@Qualifier("gaDataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

}
