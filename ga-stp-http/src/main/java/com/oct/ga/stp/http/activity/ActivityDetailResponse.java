package com.oct.ga.stp.http.activity;

public class ActivityDetailResponse {

	private String leaderAccountId;
	private String leaderNickName;
	private String leaderAvatarUrl;
	private String activityId;
	private String activityName;
	private long beginTime;
	private long endTime;
	private String bgImageUrl;
	private String location;
	private String geoX;
	private String geoY;
	private boolean applyInfoRequire;
	private long applyCloseTime;
	private int status;
	private int memberNum;
	private int myRank;
	private boolean favorite;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGeoX() {
		return geoX;
	}

	public void setGeoX(String geoX) {
		this.geoX = geoX;
	}

	public String getGeoY() {
		return geoY;
	}

	public void setGeoY(String geoY) {
		this.geoY = geoY;
	}

	public boolean isApplyInfoRequire() {
		return applyInfoRequire;
	}

	public void setApplyInfoRequire(boolean applyInfoRequire) {
		this.applyInfoRequire = applyInfoRequire;
	}

	public long getApplyCloseTime() {
		return applyCloseTime;
	}

	public void setApplyCloseTime(long applyCloseTime) {
		this.applyCloseTime = applyCloseTime;
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

	public int getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

}
