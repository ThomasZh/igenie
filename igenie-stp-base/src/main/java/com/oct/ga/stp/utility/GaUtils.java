package com.oct.ga.stp.utility;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GaUtils
{
	private static int counter = 0;

	public static String generateTimeId()
	{
		counter = (counter++) % 100;
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssms");
		return sdf.format(date) + counter;
	}

	public static int generateTimeIntId()
	{
		String timeId = generateTimeId();
		int length = timeId.length();
		String time = timeId.substring(length - 9);
		int intTimeId = Integer.parseInt(time);

		return intTimeId;
	}

	public static long getTime()
	{
		return new Date().getTime();
	}

	public static long formatLong(Object o)
	{
		if (o == null)
			return 0;
		if (o instanceof BigDecimal) {
			return ((BigDecimal) o).intValue();
		} else if (o instanceof String) {
			return Long.valueOf((String) o);
		} else {
			String s = String.valueOf(o);
			return Long.valueOf(s);
		}
	}

	public static float toFloat(Object o)
	{
		if (o == null)
			return 0;
		if (o instanceof BigDecimal) {
			return ((BigDecimal) o).floatValue();
		} else if (o instanceof String) {
			return Float.valueOf((String) o);
		} else {
			String s = String.valueOf(o);
			return Float.valueOf(s);
		}
	}

	public static Object toString(Object s)
	{
		if (s == null)
			return "";
		return String.valueOf(s);
	}

	public static String fullErrorMessage(Throwable cause)
	{
		String ss = String.format("Exception in thread '%s' %s\n", Thread.currentThread().getName(), cause);
		for (StackTraceElement te : cause.getStackTrace()) {
			if (te.getFileName() == null) {
				ss += String.format("\t|- %s.%s(Unknown Source)\n", te.getClassName(), te.getMethodName());
			} else {
				ss += String.format("\t|- %s.%s(%s:%d)\n", te.getClassName(), te.getMethodName(), te.getFileName(),
						te.getLineNumber());
			}
		}
		return ss;
	}

	public static String formatShortTime(long timestamp)
	{
		Date date = new Date(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		return sdf.format(date);
	}

}
