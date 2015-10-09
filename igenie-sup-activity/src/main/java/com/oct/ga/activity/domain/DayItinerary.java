package com.oct.ga.activity.domain;

public class DayItinerary extends Itinerary {

	private long beginTime;
	private long endTime;

	public DayItinerary() {
		super(ActivityConstants.ITINERARY_TYPE_DAY);
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

}
