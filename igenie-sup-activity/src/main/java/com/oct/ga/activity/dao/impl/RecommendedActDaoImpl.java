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

import com.oct.ga.activity.dao.RecommendedActDao;
import com.oct.ga.activity.domain.RecommendedAct;

@Repository
public class RecommendedActDaoImpl extends JdbcDaoSupport implements RecommendedActDao {

	@Override
	public void create(RecommendedAct recommendedAct) {
		String sql = "insert into APLAN_RECOMMENDED_ACT (ACTIVITY_ID, ACCOUNT_ID, BEGIN_TIME, CREATE_TIME, LAST_UPDATE_TIME)"
				+ " values (?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, recommendedAct.getActivityId(), recommendedAct.getAccountId(),
				recommendedAct.getBeginTime(), recommendedAct.getCreateTime(), recommendedAct.getLastUpdateTime());
	}

	@Override
	public boolean delete(String accountId, String activityId) {
		String sql = "delete from APLAN_RECOMMENDED_ACT where ACCOUNT_ID = ? and ACTIVITY_ID = ?";
		int count = getJdbcTemplate().update(sql, accountId, activityId);
		return count > 0;
	}

	@Override
	public boolean update(String accountId, String activityId, long lastUpdateTime) {
		String sql = "update APLAN_RECOMMENDED_ACT set LAST_UPDATE_TIME = ? where ACCOUNT_ID = ? and ACTIVITY_ID = ?";
		return getJdbcTemplate().update(sql, lastUpdateTime, accountId, activityId) > 0;
	}

	@Override
	public List<RecommendedAct> find(String accountId, long beginTime, boolean prev, int pageSize) {
		String compareOperator = prev ? ">" : "<";
		String sql = "select * from APLAN_RECOMMENDED_ACT where ACCOUNT_ID = ? and BEGIN_TIME " + compareOperator
				+ " ? order by BEGIN_TIME desc limit 0, ?";
		return getJdbcTemplate().query(sql, new RowMapperImpl(), accountId, beginTime, pageSize);
	}

	@Autowired
	public void setGaDataSource(@Qualifier("gaDataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

	private class RowMapperImpl implements RowMapper<RecommendedAct> {

		@Override
		public RecommendedAct mapRow(ResultSet rs, int rowNum) throws SQLException {
			RecommendedAct recommendedAct = new RecommendedAct();
			recommendedAct.setAccountId(rs.getString("ACCOUNT_ID"));
			recommendedAct.setActivityId(rs.getString("ACTIVITY_ID"));
			recommendedAct.setBeginTime(rs.getLong("BEGIN_TIME"));
			recommendedAct.setCreateTime(rs.getLong("CREATE_TIME"));
			recommendedAct.setLastUpdateTime(rs.getLong("LAST_UPDATE_TIME"));
			return recommendedAct;
		}
	}
}
