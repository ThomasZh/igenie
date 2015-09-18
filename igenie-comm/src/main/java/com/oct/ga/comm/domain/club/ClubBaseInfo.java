package com.oct.ga.comm.domain.club;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class ClubBaseInfo
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5992541929757343716L;

	private String id;
	private String name;
	private String titleBkImage;
	private int subscriberNum;

	@Override
	public ClubBaseInfo decode(String json)
	{
		Gson gson = new Gson();
		ClubBaseInfo info = gson.fromJson(json, ClubBaseInfo.class);
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

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSubscriberNum()
	{
		return subscriberNum;
	}

	public void setSubscriberNum(int num)
	{
		this.subscriberNum = num;
	}

	public String getTitleBkImage()
	{
		return titleBkImage;
	}

	public void setTitleBkImage(String imageUrl)
	{
		this.titleBkImage = imageUrl;
	}

}
