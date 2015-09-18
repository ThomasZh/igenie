package com.oct.ga.comm.domain.moment;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaMomentPhotoObject
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1931550455876673501L;
	private String momentId;
	private String userId;
	private String userName;
	private String userPhotoUrl;
	private String desc;
	private int timestamp;
	private String photoUrl;

	@Override
	public GaMomentPhotoObject decode(String json)
	{
		Gson gson = new Gson();
		GaMomentPhotoObject info = gson.fromJson(json, GaMomentPhotoObject.class);
		return info;
	}

	@Override
	public String encode()
	{
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}

	@Override
	public String toString()
	{
		return this.encode();
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getPhotoUrl()
	{
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl)
	{
		this.photoUrl = photoUrl;
	}

	public String getMomentId()
	{
		return momentId;
	}

	public void setMomentId(String momentId)
	{
		this.momentId = momentId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserPhotoUrl()
	{
		return userPhotoUrl;
	}

	public void setUserPhotoUrl(String userPhotoUrl)
	{
		this.userPhotoUrl = userPhotoUrl;
	}

}
