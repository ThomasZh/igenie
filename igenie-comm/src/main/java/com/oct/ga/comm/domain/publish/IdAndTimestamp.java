package com.oct.ga.comm.domain.publish;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class IdAndTimestamp
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1732319628963137791L;
	private String id;
	private int timstamp;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getTimstamp()
	{
		return timstamp;
	}

	public void setTimstamp(int timstamp)
	{
		this.timstamp = timstamp;
	}

}
