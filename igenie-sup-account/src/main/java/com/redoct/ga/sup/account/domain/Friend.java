package com.redoct.ga.sup.account.domain;

public class Friend {

	private String accountId;
	private String friendAccountId;
	private int createTime;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getFriendAccountId() {
		return friendAccountId;
	}

	public void setFriendAccountId(String friendAccountId) {
		this.friendAccountId = friendAccountId;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

}
