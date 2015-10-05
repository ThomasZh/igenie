package com.oct.ga.activity.domain;

public class Application {
	private String activiityId;
	private String accountId;
	private String contactInfo;
	private String companionsInfo;
	private int createTime;

	public String getActiviityId() {
		return activiityId;
	}

	public void setActiviityId(String activiityId) {
		this.activiityId = activiityId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getCompanionsInfo() {
		return companionsInfo;
	}

	public void setCompanionsInfo(String companionsInfo) {
		this.companionsInfo = companionsInfo;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

}