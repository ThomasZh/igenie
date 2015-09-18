package com.oct.ga.comm;

public class LogErrorMessage
{
	public static String getFullInfo(Throwable cause)
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
}
