package com.oct.ga.comm.domain.desc;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaDescCell
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6291613144638456397L;
	private int seq;
	/**
	 * CONTENT_TYPE_TXT = 0; CONTENT_TYPE_PIC = 1;
	 */
	private short type;
	private String txt;

	public int getSeq()
	{
		return seq;
	}

	public void setSeq(int seq)
	{
		this.seq = seq;
	}

	public short getType()
	{
		return type;
	}

	public void setType(short type)
	{
		this.type = type;
	}

	public String getTxt()
	{
		return txt;
	}

	public void setTxt(String txt)
	{
		this.txt = txt;
	}

}
