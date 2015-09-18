package com.oct.ga.tag.dao;

import java.util.List;

public interface GaTagDao
{
	public void addTag(String tag);

	public int queryTagId(String tag);

	// //////////////////////////////////////////////////////////

	public void addActivityTag(int tagId, String activityId);

	public void removeActivityTag(int tagId, String activityId);

	public List<String> queryActivityTags(String activityId);

	public List<String> queryTagActivityIds(int tagId);

	// //////////////////////////////////////////////////////////

	public boolean isExistActivityTagSummary(int tagId);

	public void addActivityTagSummary(int tagId, int num);

	public void updateActivityTagSummary(int tagId, int num);

	public int queryActivityTagSummary(int tagId);

	// //////////////////////////////////////////////////////////

	public boolean isExistTagRelation(int tagId);

	public void addTagRelation(int tagId, String json);

	public void updateTagRelation(int tagId, String json);

	public String queryTagRelation(int tagId);
}
