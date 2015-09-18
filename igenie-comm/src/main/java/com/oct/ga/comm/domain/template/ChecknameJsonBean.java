package com.oct.ga.comm.domain.template;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class ChecknameJsonBean
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6469078797772027270L;
	private String checkname;

	@Override
	public ChecknameJsonBean decode(String json)
	{
		Gson gson = new Gson();
		ChecknameJsonBean info = gson.fromJson(json, ChecknameJsonBean.class);
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

	public String getCheckname()
	{
		return checkname;
	}

	public void setCheckname(String checkname)
	{
		this.checkname = checkname;
	}

	public String toString()
	{
		return checkname;
	}

}
