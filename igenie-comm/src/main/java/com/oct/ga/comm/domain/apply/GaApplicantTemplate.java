package com.oct.ga.comm.domain.apply;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaApplicantTemplate
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5327000297484191987L;

	private String participationJson;
	private String contactJson;

	public String getParticipationJson()
	{
		return participationJson;
	}

	public void setParticipationJson(String participationJson)
	{
		this.participationJson = participationJson;
	}

	public String getContactJson()
	{
		return contactJson;
	}

	public void setContactJson(String contactJson)
	{
		this.contactJson = contactJson;
	}

}
