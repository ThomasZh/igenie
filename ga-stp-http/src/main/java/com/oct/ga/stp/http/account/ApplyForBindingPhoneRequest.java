package com.oct.ga.stp.http.account;

public class ApplyForBindingPhoneRequest {
	private short verificationType;
	private String phone;
	private String deviceId;

	public short getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(short verificationType) {
		this.verificationType = verificationType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
