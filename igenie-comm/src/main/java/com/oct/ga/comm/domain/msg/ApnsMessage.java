package com.oct.ga.comm.domain.msg;

public class ApnsMessage
		implements Comparable<ApnsMessage>
{
	private String payload;
	private String token;
	private int timestamp;

	public ApnsMessage()
	{
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getPayload()
	{
		return payload;
	}

	public void setPayload(String content)
	{
		this.payload = content;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String toString()
	{
		return "apn message";
	}

	@Override
	public int compareTo(ApnsMessage o)
	{
		return this.timestamp - o.timestamp;
	}

}
