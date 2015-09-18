package com.oct.ga.stp.http.account;

public class LoginRequest {

	private String osVersion;
	private String gateToken;
	private String deviceId;
	private String apnsToken;
	private String password;
	private String email;

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getGateToken() {
		return gateToken;
	}

	public void setGateToken(String gateToken) {
		this.gateToken = gateToken;
	}

	public String getMyDeviceId() {
		return deviceId;
	}

	public void setMyDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getApnsToken() {
		return apnsToken;
	}

	public void setApnsToken(String apnsToken) {
		this.apnsToken = apnsToken;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginRequest [osVersion=").append(osVersion).append(", gateToken=").append(gateToken)
				.append(", deviceId=").append(deviceId).append(", apnsToken=").append(apnsToken).append(", password=")
				.append(password).append(", email=").append(email).append("]");
		return builder.toString();
	}

}
