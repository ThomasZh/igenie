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

import com.oct.ga.activity.dao.FriendActivityDao;
import com.oct.ga.activity.domain.FriendActivity;

@Repository
public class FriendActivityDaoImpl extends JdbcDaoSupport implements FriendActivityDao {

	@Override
	public void create(FriendActivity friendActivity) {
		friendActivity.setCreateTime(Util.currentTimeMillis());
		String sql = "insert into APLAN_FRIEND_ACTIVITY (ACTIVITY_ID, ACCOUNT_ID, FRIEND_ACCOUNT_ID, BEGIN_TIME, CREATE_TIME) values (?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, friendActivity.getActivityId(), friendActivity.getAccountId(),
				friendActivity.getFriendAccoundId(), friendActivity.getBeginTime(), friendActivity.getCreateTime());
	}

	@Override
	public int deleteByFriendAccountId(String friendAccountId) {
		String sql = "delete from APLAN_FRIEND_ACTIVITY where FRIEND_ACCOUNT_ID = ?";
		return getJdbcTemplate().update(sql, friendAccountId);
	}

	@Override
	public List<FriendActivity> findByAccountId(String accountId, long beginTime, boolean prev, int pageSize) {
		String compareOperator = prev ? ">" : "<";
		String sql = "select * from APLAN_FRIEND_ACTIVITY where ACCOUNT_ID = ? and BEGIN_TIME " + compareOperator
				+ " order by BEGIN_TIME desc limit 0, ?";
		return getJdbcTemplate().query(sql, new RowMapperImpl(), accountId, beginTime, pageSize);
	}

	@Autowired
	public void setGaDataSource(@Qualifier("gaDataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

	private class RowMapperImpl implements RowMapper<FriendActivity> {

		@Override
		public FriendActivity mapRow(ResultSet rs, int rowNum) throws SQLException {
			FriendActivity friendActivity = new FriendActivity();
			friendActivity.setActivityId(rs.getString("ACTIVITY_ID"));
			friendActivity.setAccountId(rs.getString("ACCOUNT_ID"));
			friendActivity.setFriendAccoundId(rs.getString("FRIEND_ACCOUNT_ID"));
			friendActivity.setBeginTime(rs.getLong("BEGIN_TIME"));
			friendActivity.setCreateTime(rs.getLong("CREATE_TIME"));
			return friendActivity;
		}

	}

}
