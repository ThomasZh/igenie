package com.oct.ga.comm.domain.template;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaTemplateMaster
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5441059499045212683L;
	private String templateId;
	private String templateName;
	private int lastUpdateTime;
	private int copys;
	private String accountName;

	@Override
	public GaTemplateMaster decode(String json)
	{
		Gson gson = new Gson();
		GaTemplateMaster info = gson.fromJson(json, GaTemplateMaster.class);
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

	public int getLastUpdateTime()
	{
		return lastUpdateTime;
	}

	public void setLastUpdateTime(int lastUpdateTime)
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getCopys()
	{
		return copys;
	}

	public void setCopys(int copys)
	{
		this.copys = copys;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getTemplateName()
	{
		return templateName;
	}

	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}

}
