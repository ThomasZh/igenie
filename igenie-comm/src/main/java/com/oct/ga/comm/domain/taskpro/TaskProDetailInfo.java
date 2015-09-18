package com.oct.ga.comm.domain.taskpro;

import com.google.gson.Gson;
import com.oct.ga.comm.GlobalArgs;

public class TaskProDetailInfo
		extends TaskProBaseInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7457900065882301529L;
	private String extAttribute;
	private String titleBkImage;
	private String locDesc;
	private String locX;
	private String locY;
	private int executeEnd;
	private short memberAvailableNum;
	/**
	 * yes:1, no:0
	 */
	private short approveType = GlobalArgs.FALSE;
	/**
	 * yes:1, no:0
	 */
	private short applyFormType = GlobalArgs.FALSE;

	@Override
	public TaskProDetailInfo decode(String json)
	{
		Gson gson = new Gson();
		TaskProDetailInfo info = gson.fromJson(json, TaskProDetailInfo.class);
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

	public String getLocDesc()
	{
		return locDesc;
	}

	public void setLocDesc(String locDesc)
	{
		this.locDesc = locDesc;
	}

	public String getLocX()
	{
		return locX;
	}

	public void setLocX(String locX)
	{
		this.locX = locX;
	}

	public String getLocY()
	{
		return locY;
	}

	public void setLocY(String locY)
	{
		this.locY = locY;
	}

	public String getExtAttribute()
	{
		return extAttribute;
	}

	public void setExtAttribute(String extAttribute)
	{
		this.extAttribute = extAttribute;
	}

	public String getTitleBkImage()
	{
		return titleBkImage;
	}

	public void setTitleBkImage(String titleBkImage)
	{
		this.titleBkImage = titleBkImage;
	}

	public int getExecuteEnd()
	{
		return executeEnd;
	}

	public void setExecuteEnd(int executeEnd)
	{
		this.executeEnd = executeEnd;
	}

	public short getMemberAvailableNum()
	{
		return memberAvailableNum;
	}

	public void setMemberAvailableNum(short memberAvailableNum)
	{
		this.memberAvailableNum = memberAvailableNum;
	}

	public short getApproveType()
	{
		return approveType;
	}

	public void setApproveType(short approveType)
	{
		this.approveType = approveType;
	}

	public short getApplyFormType()
	{
		return applyFormType;
	}

	public void setApplyFormType(short applyFormType)
	{
		this.applyFormType = applyFormType;
	}

}
