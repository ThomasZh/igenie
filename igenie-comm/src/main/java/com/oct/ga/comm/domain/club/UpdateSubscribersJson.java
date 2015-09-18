package com.oct.ga.comm.domain.club;

import java.io.Serializable;

import com.google.gson.Gson;

public class UpdateSubscribersJson
		implements Serializable
{
	public static UpdateSubscribersJson decode(String json)
	{
		Gson gson = new Gson();
		UpdateSubscribersJson info = gson.fromJson(json, UpdateSubscribersJson.class);
		return info;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5078710849283335201L;
	private String clubId;
	private String[] addIds;
	private String[] removeIds;

	public String getClubId()
	{
		return clubId;
	}

	public void setClubId(String clubId)
	{
		this.clubId = clubId;
	}

	public String[] getAddIds()
	{
		return addIds;
	}

	public void setAddIds(String[] addIds)
	{
		this.addIds = addIds;
	}

	public String[] getRemoveIds()
	{
		return removeIds;
	}

	public void setRemoveIds(String[] removeIds)
	{
		this.removeIds = removeIds;
	}
}
