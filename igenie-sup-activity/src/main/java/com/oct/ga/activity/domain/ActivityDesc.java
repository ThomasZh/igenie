package com.oct.ga.activity.domain;

import java.util.List;

public class ActivityDesc {
	private String id;
	private String activityId;
	private String title;
	private List<DescContent> contents;
	private long createTime;
	private int idx;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<DescContent> getContents() {
		return contents;
	}

	public void setContents(List<DescContent> contents) {
		this.contents = contents;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

}