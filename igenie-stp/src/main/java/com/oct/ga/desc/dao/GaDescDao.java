package com.oct.ga.desc.dao;

import java.util.List;

import com.oct.ga.comm.domain.desc.GaDescChapter;

public interface GaDescDao
{
	public void insert(String activityId, short seq, String title, String json, int timestamp);

	public void update(String activityId, short seq, String title, String json, int timestamp);

	public void delete(String activityId, short seq);

	public void deleteAll(String activityId);

	public boolean isExist(String activityId, short seq);

	public List<GaDescChapter> select(String activityId);
}
