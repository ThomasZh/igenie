package com.oct.ga.activity.dao;

import com.oct.ga.activity.domain.Activity;

public interface ActivityDao {

	void create(Activity activity);

	boolean update(Activity activity);

	boolean delete(String id);

	Activity read(String id);

	boolean updateMemberNum(String id, int memberNum);

}
