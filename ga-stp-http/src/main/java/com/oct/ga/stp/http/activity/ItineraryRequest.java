package com.oct.ga.stp.http.activity;

import java.util.List;

public class ItineraryRequest {

	private int type;
	private String desc;
	private String title;
	private String originalLocation;
	private String originalGeoX;
	private String originalGeoY;
	private String destLocation;
	private String destGeoX;
	private String destGeoY;
	private Long beginTime;
	private Long endTime;
	private List<String> imageUrls;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginalLocation() {
		return originalLocation;
	}

	public void setOriginalLocation(String originalLocation) {
		this.originalLocation = originalLocation;
	}

	public String getOriginalGeoX() {
		return originalGeoX;
	}

	public void setOriginalGeoX(String originalGeoX) {
		this.originalGeoX = originalGeoX;
	}

	public String getOriginalGeoY() {
		return originalGeoY;
	}

	public void setOriginalGeoY(String originalGeoY) {
		this.originalGeoY = originalGeoY;
	}

	public String getDestLocation() {
		return destLocation;
	}

	public void setDestLocation(String destLocation) {
		this.destLocation = destLocation;
	}

	public String getDestGeoX() {
		return destGeoX;
	}

	public void setDestGeoX(String destGeoX) {
		this.destGeoX = destGeoX;
	}

	public String getDestGeoY() {
		return destGeoY;
	}

	public void setDestGeoY(String destGeoY) {
		this.destGeoY = destGeoY;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

}
