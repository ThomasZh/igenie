package com.oct.ga.tag;

import java.util.List;

import com.oct.ga.service.GaTagService;
import com.oct.ga.tag.dao.GaTagDao;

public class TagServiceImpl
		implements GaTagService
{
	@Override
	public void addTag(String tag)
	{
		tagDao.addTag(tag);
	}

	@Override
	public int queryTagId(String tag)
	{
		return tagDao.queryTagId(tag);
	}

	@Override
	public void addActivityTag(int tagId, String activityId)
	{
		tagDao.addActivityTag(tagId, activityId);
	}

	@Override
	public void removeActivityTag(int tagId, String activityId)
	{
		tagDao.removeActivityTag(tagId, activityId);
	}

	@Override
	public List<String> queryActivityTags(String activityId)
	{
		return tagDao.queryActivityTags(activityId);
	}

	@Override
	public List<String> queryTagActivityIds(int tagId)
	{
		return tagDao.queryTagActivityIds(tagId);
	}

	@Override
	public void modifyActivityTagSummary(int tagId, int num)
	{
		if (tagDao.isExistActivityTagSummary(tagId)) {
			tagDao.updateActivityTagSummary(tagId, num);
		} else {
			tagDao.addActivityTagSummary(tagId, num);
		}
	}

	@Override
	public int queryActivityTagSummary(int tagId)
	{
		return tagDao.queryActivityTagSummary(tagId);
	}

	@Override
	public void modifyTagRelation(int tagId, String json)
	{
		if (tagDao.isExistTagRelation(tagId)) {
			tagDao.updateTagRelation(tagId, json);
		} else {
			tagDao.addTagRelation(tagId, json);
		}
	}

	@Override
	public String queryTagRelation(int tagId)
	{
		return tagDao.queryTagRelation(tagId);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////

	private GaTagDao tagDao;

	public GaTagDao getTagDao()
	{
		return tagDao;
	}

	public void setTagDao(GaTagDao tagDao)
	{
		this.tagDao = tagDao;
	}

}
