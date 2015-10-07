package com.oct.ga.stp.http.activity;

public class ActivityResponse {
	private String leaderAccountId;
	private String leaderNickName;
	private String leaderAvatarUrl;
	private String activityId;
	private String activityName;
	private long beginTime;
	private long endTime;
	private String bgImageUrl;
	private int status;
	private int myRank;

	public String getLeaderAccountId() {
		return leaderAccountId;
	}

	public void setLeaderAccountId(String leaderAccountId) {
		this.leaderAccountId = leaderAccountId;
	}

	public String getLeaderNickName() {
		return leaderNickName;
	}

	public void setLeaderNickName(String leaderNickName) {
		this.leaderNickName = leaderNickName;
	}

	public String getLeaderAvatarUrl() {
		return leaderAvatarUrl;
	}

	public void setLeaderAvatarUrl(String leaderAvatarUrl) {
		this.leaderAvatarUrl = leaderAvatarUrl;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getBgImageUrl() {
		return bgImageUrl;
	}

	public void setBgImageUrl(String bgImageUrl) {
		this.bgImageUrl = bgImageUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMyRank() {
		return myRank;
	}

	public void setMyRank(int myRank) {
		this.myRank = myRank;
	}

}
