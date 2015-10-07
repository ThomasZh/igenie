package com.oct.ga.stp.http.activity;

import java.util.List;

public class MomentResponse {

	private String accountId;
	private String accountNickName;
	private String accountAvatarUrl;
	private String activityId;
	private String activityName;
	private String desc;
	private long createTime;
	private List<String> imageUrls;
	private int commentNum;
	private int likeNum;

	private List<MomentComment> comments;
	private List<String> likes;
	private boolean like;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountNickName() {
		return accountNickName;
	}

	public void setAccountNickName(String accountNickName) {
		this.accountNickName = accountNickName;
	}

	public String getAccountAvatarUrl() {
		return accountAvatarUrl;
	}

	public void setAccountAvatarUrl(String accountAvatarUrl) {
		this.accountAvatarUrl = accountAvatarUrl;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}

	public List<MomentComment> getComments() {
		return comments;
	}

	public void setComments(List<MomentComment> comments) {
		this.comments = comments;
	}

	public List<String> getLikes() {
		return likes;
	}

	public void setLikes(List<String> likes) {
		this.likes = likes;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

}
