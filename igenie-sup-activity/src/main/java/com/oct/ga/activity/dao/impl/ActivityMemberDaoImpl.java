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

import com.oct.ga.activity.dao.ActivityMemberDao;
import com.oct.ga.activity.domain.ActivityMember;

@Repository
public class ActivityMemberDaoImpl extends JdbcDaoSupport implements ActivityMemberDao {

	@Override
	public void create(ActivityMember activityMember) {
		activityMember.setCreateTime(Util.currentTimeMillis());
		String sql = "insert into APLAN_ACTIVITY_MEMBER (ACTIVITY_ID, ACCOUNT_ID, BEGIN_TIME, RANK, CREATE_TIME) values (?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, activityMember.getActivityId(), activityMember.getAccountId(),
				activityMember.getBeginTime(), activityMember.getRank(), activityMember.getCreateTime());
	}

	@Override
	public boolean delete(String activityId, String accountId) {
		String sql = "delete from APLAN_ACTIVITY_MEMBER where ACTIVITY_ID = ? and ACCOUNT_ID = ?";
		return getJdbcTemplate().update(sql, activityId, accountId) > 0;
	}

	@Override
	public List<ActivityMember> findByAccountId(String accountId, long beginTime, boolean prev, int pageSize) {
		String compareOperator = prev ? ">" : "<";
		String sql = "select * from APLAN_ACTIVITY_MEMBER where ACCOUNT_ID = ? and BEGIN_TIME " + compareOperator
				+ " ? order by BEGIN_TIME desc limit 0, ?";
		return getJdbcTemplate().query(sql, new RowMapperImpl(), accountId, beginTime, pageSize);
	}

	@Override
	public List<ActivityMember> findByActivityId(String activityId, long createTime, int pageSize) {
		String sql = "select * from APLAN_ACTIVITY_MEMBER where ACTIVITY_ID = ? and CREATE_TIME > ? order by CREATE_TIME asc limit 0, ?";
		return getJdbcTemplate().query(sql, new RowMapperImpl(), activityId, createTime, pageSize);
	}

	@Override
	public int countActivityMembersByActivityId(String activityId) {
		String sql = "select count(1) from APLAN_ACTIVITY_MEMBER where ACTIVITY_ID = ?";
		return getJdbcTemplate().queryForObject(sql, Integer.class, activityId);
	}

	@Autowired
	public void setGaDataSource(@Qualifier("gaDataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

	private class RowMapperImpl implements RowMapper<ActivityMember> {

		@Override
		public ActivityMember mapRow(ResultSet rs, int rowNum) throws SQLException {
			ActivityMember activityMember = new ActivityMember();
			activityMember.setAccountId(rs.getString("ACCOUNT_ID"));
			activityMember.setActivityId(rs.getString("ACTIVITY_ID"));
			activityMember.setBeginTime(rs.getLong("BEGIN_TIME"));
			activityMember.setRank(rs.getInt("RANK"));
			activityMember.setCreateTime(rs.getLong("CREATE_TIME"));
			return activityMember;
		}

	}
}
