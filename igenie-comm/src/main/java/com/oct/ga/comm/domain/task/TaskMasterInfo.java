package com.oct.ga.comm.domain.task;

import com.google.gson.Gson;

public class TaskMasterInfo
		extends TaskBaseInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -771813542750853273L;

	private String desc;
	private String titleBkImage;
	private int endTime;
	private short color;
	private String locDesc;
	private String locX;
	private String locY;
	private short fileNum;

	@Override
	public TaskMasterInfo decode(String json)
	{
		Gson gson = new Gson();
		TaskMasterInfo info = gson.fromJson(json, TaskMasterInfo.class);
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

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getTitleBkImage()
	{
		return titleBkImage;
	}

	public void setTitleBkImage(String titleBkImage)
	{
		this.titleBkImage = titleBkImage;
	}

	public int getEndTime()
	{
		return endTime;
	}

	public void setEndTime(int endTime)
	{
		this.endTime = endTime;
	}

	public short getColor()
	{
		return color;
	}

	public void setColor(short color)
	{
		this.color = color;
	}

	public String getLocDesc()
	{
		return locDesc;
	}

	public void setLocDesc(String locDesc)
	{
		this.locDesc = locDesc;
	}

	public String getLocX()
	{
		return locX;
	}

	public void setLocX(String locX)
	{
		this.locX = locX;
	}

	public String getLocY()
	{
		return locY;
	}

	public void setLocY(String locY)
	{
		this.locY = locY;
	}

	public short getFileNum()
	{
		return fileNum;
	}

	public void setFileNum(short fileNum)
	{
		this.fileNum = fileNum;
	}

}
