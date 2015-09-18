package com.oct.ga.comm.domain.template;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class AttachmentJsonBean
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5336505925802927637L;
	private String name;
	private String filename;

	@Override
	public AttachmentJsonBean decode(String json)
	{
		Gson gson = new Gson();
		AttachmentJsonBean info = gson.fromJson(json, AttachmentJsonBean.class);
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}
}
