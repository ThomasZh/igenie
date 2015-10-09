package com.oct.ga.activity.domain;

import java.util.List;

public class EquipmentItinerary extends Itinerary {

	private List<String> imageUrls;

	public EquipmentItinerary() {
		super(ActivityConstants.ITINERARY_TYPE_EQUIPMENT);
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

}
