package com.oct.ga.stp.http.account;

public class ChangePwdRequest {
	private String oldPassword;
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChangePwdRequest [oldPassword=").append(oldPassword).append(", newPassword=")
				.append(newPassword).append("]");
		return builder.toString();
	}

}
