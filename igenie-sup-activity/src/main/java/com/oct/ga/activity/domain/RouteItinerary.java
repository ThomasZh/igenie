package com.oct.ga.activity.domain;

import java.util.List;

public class RouteItinerary extends Itinerary {

	private String originalLocation;
	private String originalGeoX;
	private String originalGeoY;
	private String destLocation;
	private String destGeoX;
	private String destGeoY;
	private long beginTime;
	private long endTime;
	private List<String> imageUrls;

	public RouteItinerary() {
		super(ActivityConstants.ITINERARY_TYPE_ROUTE);
	}

	public String getOriginalLocation() {
		return originalLocation;
	}

	public void setOriginalLocation(String orignalLocation) {
		this.originalLocation = orignalLocation;
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

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

}
