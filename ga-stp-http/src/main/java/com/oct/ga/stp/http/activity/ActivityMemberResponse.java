package com.oct.ga.stp.http.activity;

public class ActivityMemberResponse {

	private String accountId;
	private String accountNickName;
	private String accountAvatarUrl;
	private int rank;

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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
