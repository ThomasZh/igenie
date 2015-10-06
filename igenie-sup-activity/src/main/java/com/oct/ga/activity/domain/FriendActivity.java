package com.oct.ga.activity.domain;

public class FriendActivity {
	private String activityId;
	private String accountId;
	private String friendAccoundId;
	private long beginTime;// TODO
	private long createTime;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getFriendAccoundId() {
		return friendAccoundId;
	}

	public void setFriendAccoundId(String friendAccoundId) {
		this.friendAccoundId = friendAccoundId;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
