package com.oct.ga.comm.domain;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class AccountBadgeNumJsonBean
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5065671879774297842L;

	private short messageNum;
	private short taskLogNum;
	private short inviteNum;
	private short applyNum;
	private short momentLogNum;

	public short getMessageNum()
	{
		return messageNum;
	}

	public void setMessageNum(short messageNum)
	{
		this.messageNum = messageNum;
	}

	public short getTaskLogNum()
	{
		return taskLogNum;
	}

	public void setTaskLogNum(short taskLogNum)
	{
		this.taskLogNum = taskLogNum;
	}

	public short getInviteNum()
	{
		return inviteNum;
	}

	public void setInviteNum(short inviteNum)
	{
		this.inviteNum = inviteNum;
	}

	public short getApplyNum()
	{
		return applyNum;
	}

	public void setApplyNum(short applyNum)
	{
		this.applyNum = applyNum;
	}

	public short getMomentLogNum()
	{
		return momentLogNum;
	}

	public void setMomentLogNum(short momentLogNum)
	{
		this.momentLogNum = momentLogNum;
	}

}
