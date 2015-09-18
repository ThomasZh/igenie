package com.oct.ga.comm.domain.vote;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class Vote
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6159521198585737435L;

	@Override
	public Vote decode(String json)
	{
		Gson gson = new Gson();
		Vote info = gson.fromJson(json, Vote.class);
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

	private String _id;
	private String voteName;
	private String description;
	private String selectionList;
	private int timestamp;
	private String createId;
	private int startTime;
	private int endTime;

	public String get_id()
	{
		return _id;
	}

	public void set_id(String _id)
	{
		this._id = _id;
	}

	public String getVoteName()
	{
		return voteName;
	}

	public void setVoteName(String voteName)
	{
		this.voteName = voteName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getSelectionList()
	{
		return selectionList;
	}

	public void setSelectionList(String selectionList)
	{
		this.selectionList = selectionList;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getCreateId()
	{
		return createId;
	}

	public void setCreateId(String createId)
	{
		this.createId = createId;
	}

	public int getStartTime()
	{
		return startTime;
	}

	public void setStartTime(int startTime)
	{
		this.startTime = startTime;
	}

	public int getEndTime()
	{
		return endTime;
	}

	public void setEndTime(int endTime)
	{
		this.endTime = endTime;
	}

}
