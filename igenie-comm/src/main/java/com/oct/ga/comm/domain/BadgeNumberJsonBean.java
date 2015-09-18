package com.oct.ga.comm.domain;

import java.io.Serializable;

public class BadgeNumberJsonBean
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7386211528341772266L;
	private String id;
	private int number;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

}
