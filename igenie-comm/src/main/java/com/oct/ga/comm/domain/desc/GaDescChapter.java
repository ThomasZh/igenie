package com.oct.ga.comm.domain.desc;

import java.util.List;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaDescChapter
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7550981054873947524L;

	private String title;
	private short seq;
	private List<GaDescCell> cells;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public List<GaDescCell> getCells()
	{
		return cells;
	}

	public void setCells(List<GaDescCell> cells)
	{
		this.cells = cells;
	}

	public short getSeq()
	{
		return seq;
	}

	public void setSeq(short seq)
	{
		this.seq = seq;
	}

}
