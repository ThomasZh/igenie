package com.oct.ga.stp.http.account;

public class EmailRegisterLoginRequest {

	private String osVersion;
	private String gateToken;
	private String deviceId;
	private String firstName;
	private String lastName;
	private String email;
	private String md5pwd;
	private String facePhoto;
	private String apnsToken;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMd5pwd() {
		return md5pwd;
	}

	public void setMd5pwd(String md5pwd) {
		this.md5pwd = md5pwd;
	}

	public String getFacePhoto() {
		return facePhoto;
	}

	public void setFacePhoto(String facePhoto) {
		this.facePhoto = facePhoto;
	}

	public String getApnsToken() {
		return apnsToken;
	}

	public void setApnsToken(String apnsToken) {
		this.apnsToken = apnsToken;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
