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

import com.oct.ga.activity.dao.MomentDao;
import com.oct.ga.activity.domain.Moment;

@Repository
public class MomentDaoImpl extends JdbcDaoSupport implements MomentDao {

	@Override
	public void create(Moment moment) {
		// TODO use the given id
		moment.setCreateTime(Util.currentTimeMillis());
		String sql = "insert APLAN_MOMENT (ID_, ACTIVITY_ID, ACCOUNT_ID, DESC_, IMAGE_URLS, COMMENT_NUM, LIKE_NUM, CREATE_TIME)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, moment.getId(), moment.getActivityId(), moment.getAccountId(), moment.getDesc(),
				Util.toJson(moment.getImageUrls()), moment.getCommentNum(), moment.getLikeNum(),
				moment.getCreateTime());
	}

	@Override
	public boolean update(Moment moment) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean updateLikeNum(String id, int likeNum) {
		String sql = "update APLAN_MOMENT set LIKE_NUM = ? where ID_ = ?";
		int count = getJdbcTemplate().update(sql, likeNum, id);
		return count > 0;
	}

	@Override
	public boolean updateCommentNum(String id, int commentNum) {
		String sql = "update APLAN_MOMENT set COMMENT_NUM = ? where ID_ = ?";
		int count = getJdbcTemplate().update(sql, commentNum, id);
		return count > 0;
	}

	@Override
	public boolean delete(String id) {
		String sql = "delete from APLAN_MOMENT where ID_ = ?";
		return getJdbcTemplate().update(sql, id) > 0;
	}

	@Override
	public Moment read(String id) {
		String sql = "select * from APLAN_MOMENT where ID_ = ?";
		return getJdbcTemplate().queryForObject(sql, new RowMapperImpl(), id);
	}

	@Override
	public List<Moment> findByActivityId(String activityId, long createTime, int pageSize) {
		String sql = "select * from APLAN_MOMENT where ACTIVITY_ID = ? and CREATE_TIME < ? order by CREATE_TIME desc limit 0, ?";
		return getJdbcTemplate().query(sql, new RowMapperImpl(), activityId, createTime, pageSize);
	}

	@Autowired
	public void setGaDataSource(@Qualifier("gaDataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

	private class RowMapperImpl implements RowMapper<Moment> {

		@Override
		public Moment mapRow(ResultSet rs, int rowNum) throws SQLException {
			Moment moment = new Moment();
			moment.setId(rs.getString("ID_"));
			moment.setActivityId(rs.getString("ACTIVITY_ID"));
			moment.setAccountId(rs.getString("ACCOUNT_ID"));
			moment.setDesc(rs.getString("DESC_"));
			moment.setImageUrls(Util.jsonToList(rs.getString("IMAGE_URLS"), String.class));
			moment.setLikeNum(rs.getInt("LIKE_NUM"));
			moment.setCommentNum(rs.getInt("COMMENT_NUM"));
			moment.setCreateTime(rs.getLong("CREATE_TIME"));
			return moment;
		}

	}
}
