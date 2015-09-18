package com.oct.ga.stp.http.account;

public class ResetPwdRequest {

	private String email;
	private String ekey;
	private String newPassword;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEkey() {
		return ekey;
	}

	public void setEkey(String ekey) {
		this.ekey = ekey;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
