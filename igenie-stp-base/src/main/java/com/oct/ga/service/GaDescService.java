package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.desc.GaDescChapter;

public interface GaDescService
{
	public void modify(String activityId, short seq, GaDescChapter chapter, int timestamp);

	public void remove(String activityId, short seq);

	public void removeAll(String activityId);

	public List<GaDescChapter> query(String activityId);
}
