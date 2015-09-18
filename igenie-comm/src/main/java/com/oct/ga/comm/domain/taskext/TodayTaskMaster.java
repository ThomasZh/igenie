package com.oct.ga.comm.domain.taskext;

import com.google.gson.Gson;

public class TodayTaskMaster
		extends TaskBase
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7450727143031793944L;

	private String pid;
	private String pname;
	private int executeEnd;
	private short pState;
	private int depth;

	@Override
	public TodayTaskMaster decode(String json)
	{
		Gson gson = new Gson();
		TodayTaskMaster info = gson.fromJson(json, TodayTaskMaster.class);
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

	public String getPid()
	{
		return pid;
	}

	public void setPid(String pid)
	{
		this.pid = pid;
	}

	public String getPname()
	{
		return pname;
	}

	public void setPname(String pname)
	{
		this.pname = pname;
	}

	public int getExecuteEnd()
	{
		return executeEnd;
	}

	public void setExecuteEnd(int executeEnd)
	{
		this.executeEnd = executeEnd;
	}

	public short getpState()
	{
		return pState;
	}

	public void setpState(short pState)
	{
		this.pState = pState;
	}

	public int getDepth()
	{
		return depth;
	}

	public void setDepth(int depth)
	{
		this.depth = depth;
	}

}
