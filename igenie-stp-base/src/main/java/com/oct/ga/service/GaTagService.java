package com.oct.ga.service;

import java.util.List;

public interface GaTagService
{
	public void addTag(String tag);

	public int queryTagId(String tag);

	// //////////////////////////////////////////////////////////

	public void addActivityTag(int tagId, String activityId);

	public void removeActivityTag(int tagId, String activityId);

	public List<String> queryActivityTags(String activityId);

	public List<String> queryTagActivityIds(int tagId);

	// //////////////////////////////////////////////////////////

	public void modifyActivityTagSummary(int tagId, int num);

	public int queryActivityTagSummary(int tagId);

	// //////////////////////////////////////////////////////////

	public void modifyTagRelation(int tagId, String json);

	public String queryTagRelation(int tagId);
}
