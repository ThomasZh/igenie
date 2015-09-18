package com.oct.ga.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JQGrid JSON Paging Transport Object
 * 
 * @author Thomas.H.Zhang
 * @param <E>
 */
public class JQGridPTO<E>
		implements Serializable
{
	private int page; // current page of the query
	private int total; // total pages for the query
	private int records; // total number of records for the query
	// an array that contains the actual data
	private List<E> rows = new ArrayList<E>();

	public int getPage()
	{
		return page;
	}

	public void setPage(int val)
	{
		this.page = val;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int val)
	{
		this.total = val;
	}

	public void setRecords(int val)
	{
		records = val;
	}

	public int getRecords()
	{
		return records;
	}

	public List<E> getRows()
	{
		return rows;
	}

	public void setRows(List<E> items)
	{
		this.rows = items;
	}
}
