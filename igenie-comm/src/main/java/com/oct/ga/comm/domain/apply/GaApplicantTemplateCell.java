package com.oct.ga.comm.domain.apply;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaApplicantTemplateCell
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5962729906702827454L;
	private int seq;
	private String name;
	/**
	 * 1:must;0:not
	 */
	private int required;

	public int getSeq()
	{
		return seq;
	}

	public void setSeq(int seq)
	{
		this.seq = seq;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getRequired()
	{
		return required;
	}

	public void setRequired(int required)
	{
		this.required = required;
	}



}
