package com.oct.ga.stp.http.club;

import java.util.List;

public class ItineraryRequest {
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItineraryRequest [beginTime=").append(beginTime).append(", endTime=").append(endTime)
				.append(", title=").append(title).append(", location=").append(location).append(", desc=").append(desc)
				.append(", imageUrls=").append(imageUrls).append(", geoX=").append(geoX).append(", geoY=").append(geoY)
				.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + beginTime;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + endTime;
		result = prime * result + ((geoX == null) ? 0 : geoX.hashCode());
		result = prime * result + ((geoY == null) ? 0 : geoY.hashCode());
		result = prime * result + ((imageUrls == null) ? 0 : imageUrls.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItineraryRequest other = (ItineraryRequest) obj;
		if (beginTime != other.beginTime)
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (endTime != other.endTime)
			return false;
		if (geoX == null) {
			if (other.geoX != null)
				return false;
		} else if (!geoX.equals(other.geoX))
			return false;
		if (geoY == null) {
			if (other.geoY != null)
				return false;
		} else if (!geoY.equals(other.geoY))
			return false;
		if (imageUrls == null) {
			if (other.imageUrls != null)
				return false;
		} else if (!imageUrls.equals(other.imageUrls))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
