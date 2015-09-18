package com.oct.ga.comm.domain.talent;

public class TalentScore
{
	private String accountId;
	private int votedNum;
	private int position;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public int getVotedNum()
	{
		return votedNum;
	}

	public void setVotedNum(int votedNum)
	{
		this.votedNum = votedNum;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int postion)
	{
		this.position = postion;
	}

}
