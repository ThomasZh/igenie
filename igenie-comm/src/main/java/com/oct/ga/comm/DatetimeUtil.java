package com.oct.ga.comm;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeUtil
{
	public static int currentTimestamp()
	{
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static int nextDayTimestamp()
	{
		return (int) (System.currentTimeMillis() / 1000) + 24 * 3600;
	}

	public static int genSequence()
	{
		return new Long(System.currentTimeMillis() / 1000).intValue();
	}

	public static String time2Str(int timestamp)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		java.util.Date date = new java.util.Date((long) timestamp * 1000);
		String str = sdf.format(date);
		return str;
	}
	
	public static String today2Str()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
		String str = sdf.format(new Date());
		return str;
	}
}
