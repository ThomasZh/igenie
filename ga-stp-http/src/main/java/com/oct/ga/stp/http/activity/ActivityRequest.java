package com.oct.ga.stp.http.activity;

public class ActivityRequest {
	private String name;
	private long beginTime;
	private long endTime;
	private String bgImageUrl;
	private int type;
	private String location;
	private String geoX;
	private String geoY;
	private boolean applyInfoRequire;
	private long applyCloseTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getBgImageUrl() {
		return bgImageUrl;
	}

	public void setBgImageUrl(String bgImageUrl) {
		this.bgImageUrl = bgImageUrl;
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

	public long getApplyCloseTime() {
		return applyCloseTime;
	}

	public void setApplyCloseTime(long applyCloseTime) {
		this.applyCloseTime = applyCloseTime;
	}

}
