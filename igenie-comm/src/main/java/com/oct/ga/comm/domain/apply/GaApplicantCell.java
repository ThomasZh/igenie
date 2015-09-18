package com.oct.ga.comm.domain.apply;

import com.oct.ga.comm.domain.JsonBeanAdapter;

/**
 * 申请人信息单元
 * 
 * @author thomas
 */
public class GaApplicantCell
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5296326384714303881L;

	private int seq;
	private String name;
	private String val;

	public int getSeq()
	{
		return seq;
	}

	public void setSeq(int seq)
	{
		this.seq = seq;
	}

	public String getVal()
	{
		return val;
	}

	public void setVal(String val)
	{
		this.val = val;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
