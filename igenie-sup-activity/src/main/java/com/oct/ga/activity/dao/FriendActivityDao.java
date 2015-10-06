package com.oct.ga.activity.dao;

import java.util.List;

import com.oct.ga.activity.domain.FriendActivity;

public interface FriendActivityDao {
	void create(FriendActivity friendActivity);

	int deleteByFriendAccountId(String firendAccountId);

	List<FriendActivity> findByAccountId(String accountId, long beginTime, boolean prev, int pageSize);
}
