package com.oct.ga.comm.domain.template;

import com.google.gson.Gson;

public class ClwcJsonBean
		extends ChecknameJsonBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1591354238676262970L;
	private boolean isDisplayStepper;
	private int defaultNum;
	private String unit;

	@Override
	public ClwcJsonBean decode(String json)
	{
		Gson gson = new Gson();
		ClwcJsonBean info = gson.fromJson(json, ClwcJsonBean.class);
		return info;
	}

	@Override
	public String encode()
	{
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}

//	@Override
//	public String toString()
//	{
//		return this.encode();
//	}

	public boolean isDisplayStepper()
	{
		return isDisplayStepper;
	}

	public void setDisplayStepper(boolean isDisplayStepper)
	{
		this.isDisplayStepper = isDisplayStepper;
	}

	public String toString()
	{
		return "'checkname':'" + this.getCheckname() + "','displayStepper':" + isDisplayStepper;
	}

	public int getDefaultNum()
	{
		return defaultNum;
	}

	public void setDefaultNum(int defaultNum)
	{
		this.defaultNum = defaultNum;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}
}
