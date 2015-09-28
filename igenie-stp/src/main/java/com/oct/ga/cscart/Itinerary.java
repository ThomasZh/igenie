package com.oct.ga.cscart;

import java.io.Serializable;
import java.util.List;

public class Itinerary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3658948906187306491L;
	private String id;
	private String pid;
	private String description;
	private String timeLabel;
	private String address;
	private String location;
	private short type;
	private short status;
	private int timestamp;
	private List<String> pictures;// picture name list

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimeLabel() {
		return timeLabel;
	}

	public void setTimeLabel(String timeLabel) {
		this.timeLabel = timeLabel;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictureArray) {
		this.pictures = pictureArray;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

}
