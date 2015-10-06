package com.oct.ga.activity.dao;

import java.util.List;

import com.oct.ga.activity.domain.Moment;

public interface MomentDao {
	void create(Moment moment);

	boolean update(Moment moment);

	boolean updateLikeNum(String id, int likeNum);

	boolean updateCommentNum(String id, int commentNum);

	boolean delete(String id);

	Moment read(String id);

	List<Moment> findByActivityId(String activityId, long createTime, int pageSize);
}
