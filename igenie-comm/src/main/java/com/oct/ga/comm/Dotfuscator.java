package com.oct.ga.comm;

public class Dotfuscator
{
	private static char decry(char c)
	{
		if (c >= 48 && c <= 123) {
			if (c == '0') {
				c = 'z';
			} else if (c == '1') {
				c = 'y';
			} else if (c == '2') {
				c = 'x';
			} else {
				c = (char) (c - 3);
			}

			return c;
		}

		return c;
	}

	public static String decry(String str)
	{
		StringBuffer sb = new StringBuffer();
		if (str == null) {
			throw new RuntimeException("please input dest string...");
		} else {
			for (char c : str.toCharArray()) {
				sb.append(decry(c));
			}
		}
		return sb.toString();
	}

	private static char encry(char c)
	{
		if (c >= 48 && c <= 123) {
			if (c == 'z') {
				c = '0';
			} else if (c == 'y') {
				c = '1';
			} else if (c == 'x') {
				c = '2';
			} else {
				c = (char) (c + 3);
			}

			return c;
		}

		return c;
	}

	public static String encry(String str)
	{
		StringBuffer sb = new StringBuffer();
		if (str == null) {
			throw new RuntimeException("please input src string...");
		} else {
			for (char c : str.toCharArray()) {
				sb.append(encry(c));
			}
		}
		return sb.toString();
	}
}
