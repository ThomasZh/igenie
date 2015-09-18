package com.oct.ga.stp.http.account;

public class LoginResponse {
	private String accountId;
	private String sessionToken;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginResponse [accountId=").append(accountId).append(", sessionToken=").append(sessionToken)
				.append("]");
		return builder.toString();
	}

}
