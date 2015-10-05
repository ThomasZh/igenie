package com.oct.ga.activity.domain;

public class Activity {
	private String id;
	private String name;
	private String desc;// FIXME
	private int beginTime;
	private int endTime;
	private String bgImageUrl;
	private int createTime;
	private int lastUpdateTime;
	private int status;
	private int type;
	private String location;
	private String geoX;
	private String geoY;
	private boolean applyInfoRequire;
	private int applyCloseTime;
	private int memberNum;
	private String leaderId;// TODO

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public String getBgImageUrl() {
		return bgImageUrl;
	}

	public void setBgImageUrl(String bgImageUrl) {
		this.bgImageUrl = bgImageUrl;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	public int getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(int lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGeoX() {
		return geoX;
	}

	public void setGeoX(String geoX) {
		this.geoX = geoX;
	}

	public String getGeoY() {
		return geoY;
	}

	public void setGeoY(String geoY) {
		this.geoY = geoY;
	}

	public boolean isApplyInfoRequire() {
		return applyInfoRequire;
	}

	public void setApplyInfoRequire(boolean applyInfoRequire) {
		this.applyInfoRequire = applyInfoRequire;
	}

	public int getApplyCloseTime() {
		return applyCloseTime;
	}

	public void setApplyCloseTime(int applyCloseTime) {
		this.applyCloseTime = applyCloseTime;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

}