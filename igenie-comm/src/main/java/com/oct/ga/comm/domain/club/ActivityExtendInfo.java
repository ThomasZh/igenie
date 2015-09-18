package com.oct.ga.comm.domain.club;

import com.google.gson.Gson;

public class ActivityExtendInfo
		extends ActivityMasterInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1492139890233625850L;
	private int attachmentNum;

	@Override
	public ActivityExtendInfo decode(String json)
	{
		Gson gson = new Gson();
		ActivityExtendInfo info = gson.fromJson(json, ActivityExtendInfo.class);
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

	public int getAttachmentNum()
	{
		return attachmentNum;
	}

	public void setAttachmentNum(int attachmentNum)
	{
		this.attachmentNum = attachmentNum;
	}
}
