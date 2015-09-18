package com.oct.ga.comm.domain.msg;

import java.util.HashMap;
import java.util.Map;

public class JPushMessage
		implements Comparable<JPushMessage>
{
	private boolean isOnline = false;
	private String title;
	private String msgContent;
	private Map<String, String> extras = new HashMap<String, String>();
	private String alias;
	private int timestamp;

	public JPushMessage()
	{
	}

	public String toString()
	{
		return "jpush message";
	}

	@Override
	public int compareTo(JPushMessage o)
	{
		return this.timestamp - o.timestamp;
	}

	// /////////////////////////////////////////////////

	public void putAttr(String key, String value)
	{
		extras.put(key, value);
	}

	public boolean isOnline()
	{
		return isOnline;
	}

	public void setOnline(boolean isOnline)
	{
		this.isOnline = isOnline;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getMsgContent()
	{
		return msgContent;
	}

	public void setMsgContent(String msgContent)
	{
		this.msgContent = msgContent;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public Map<String, String> getExtras()
	{
		return extras;
	}

	public void setExtras(Map<String, String> extras)
	{
		this.extras = extras;
	}

}
