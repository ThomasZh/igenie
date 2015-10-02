package com.redoct.ga.sup.account.domain;

import java.util.Set;

public class AccountDetail extends AccountMaster {
	private static final long serialVersionUID = 5876976436044298201L;
	private short state;
	private int fans_num;
	private int followingNum;
	private int friendNum;
	private int createActNum;
	private int joinedActNum;
	private int unpublishActNum;
	private int favoriteNum;
	private int momentNum;
	private Set<String> tags;

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public int getFans_num() {
		return fans_num;
	}

	public void setFans_num(int fans_num) {
		this.fans_num = fans_num;
	}

	public int getFollowingNum() {
		return followingNum;
	}

	public void setFollowingNum(int followingNum) {
		this.followingNum = followingNum;
	}

	public int getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
	}

	public int getCreateActNum() {
		return createActNum;
	}

	public void setCreateActNum(int createActNum) {
		this.createActNum = createActNum;
	}

	public int getJoinedActNum() {
		return joinedActNum;
	}

	public void setJoinedActNum(int joinedActNum) {
		this.joinedActNum = joinedActNum;
	}

	public int getUnpublishActNum() {
		return unpublishActNum;
	}

	public void setUnpublishActNum(int unpublishActNum) {
		this.unpublishActNum = unpublishActNum;
	}

	public int getFavoriteNum() {
		return favoriteNum;
	}

	public void setFavoriteNum(int favoriteNum) {
		this.favoriteNum = favoriteNum;
	}

	public int getMomentNum() {
		return momentNum;
	}

	public void setMomentNum(int momentNum) {
		this.momentNum = momentNum;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

}
