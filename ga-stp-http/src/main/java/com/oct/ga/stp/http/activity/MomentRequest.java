package com.oct.ga.stp.http.activity;

import java.util.List;

public class MomentRequest {
	private String desc;
	private List<String> imageUrls;

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

}
