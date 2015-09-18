package com.oct.ga.comm.domain.taskext;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class TaskBase
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1982104506520999822L;

	private String id;
	private String name;
	private short state;
	private short color;
	private int start;
	private int end;

	@Override
	public TaskBase decode(String json)
	{
		Gson gson = new Gson();
		TaskBase info = gson.fromJson(json, TaskBase.class);
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

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

	public short getColor()
	{
		return color;
	}

	public void setColor(short color)
	{
		this.color = color;
	}

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

	public int getEnd()
	{
		return end;
	}

	public void setEnd(int end)
	{
		this.end = end;
	}

}
