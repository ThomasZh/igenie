package com.oct.ga.cscart;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8523163982224947058L;
	private String id;
	private String venderId;
	private String name;
	private String description;
	private String note;
	private String address;
	private String location;
	private String price;
	private String priceUnit;
	private int favorite;
	private int grade;
	private List<String> pictures;// picture name list
	private List<Itinerary> itineraries;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictureArray) {
		this.pictures = pictureArray;
	}

	public List<Itinerary> getItineraries() {
		return itineraries;
	}

	public void setItineraries(List<Itinerary> itineraryArray) {
		this.itineraries = itineraryArray;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVenderId() {
		return venderId;
	}

	public void setVenderId(String venderId) {
		this.venderId = venderId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

}
