package com.oct.ga.comm;

public class SupSocketException
		extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6610212725704426993L;

	public SupSocketException(String message)
	{
		super(message);
	}

	public SupSocketException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SupSocketException(Throwable cause)
	{
		super(cause);
	}
}
