package com.oct.ga.stp.http.account;

public class PhoneRegisterLoginRequest {

	private String osVersion;
	private String gateToken;
	private String deviceId;
	private String apnsToken;
	private String phone;
	private String md5pwd;
	private String verificationCode;
	private String lang;

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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getApnsToken() {
		return apnsToken;
	}

	public void setApnsToken(String apnsToken) {
		this.apnsToken = apnsToken;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMd5pwd() {
		return md5pwd;
	}

	public void setMd5pwd(String md5pwd) {
		this.md5pwd = md5pwd;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
