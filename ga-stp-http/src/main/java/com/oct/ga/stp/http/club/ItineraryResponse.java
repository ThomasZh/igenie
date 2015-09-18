package com.oct.ga.stp.http.club;

import java.util.List;

public class ItineraryResponse {
	private int beginTime;
	private int endTime;
	private String title;
	private String location;
	private String desc;
	private List<String> imageUrls;
	private String geoX;
	private String geoY;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
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

}
