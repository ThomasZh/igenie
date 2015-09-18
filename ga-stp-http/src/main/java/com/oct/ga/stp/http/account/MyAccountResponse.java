package com.oct.ga.stp.http.account;

import com.oct.ga.comm.domain.account.AccountMaster;

public class MyAccountResponse {

	private AccountMaster accountMaster;
	private int currentTimestamp;

	public AccountMaster getAccountMaster() {
		return accountMaster;
	}

	public int getCurrentTimestamp() {
		return currentTimestamp;
	}

	public void setAccountMaster(AccountMaster accountMaster) {
		this.accountMaster = accountMaster;
	}

	public void setCurrentTimestamp(int currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}

}
