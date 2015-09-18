package com.oct.ga.comm;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil
{
	public static String filterDash(String s)
	{
		String rs = s.replace("-", "");
		return rs;
	}

	// "http://tripc2c-project-pic.b0.upaiyun.com/2014/12/25/c3e8c697a46f621a134b1d9d868135e3.jpg";
	public static String parserIdFromImageUtrl(String imageUrl)
	{
		String photoName = null;
		Pattern p = Pattern.compile("\\w+\\.\\w+");
		Matcher m = p.matcher(imageUrl);
		while (m.find()) {
			photoName = m.group();
		}

		String photoId = null;
		photoId = photoName.split("[.]")[0];

		return photoId;
	}

	/**
	 * @param x
	 *            (500.000000)
	 * @param y
	 *            (-1.00000)
	 * @return 500.00-1.00
	 */
	public static String locMask(String x, String y)
	{
		if (x != null && x.length() > 7) {
			x = x.substring(0, x.length() - 4);
		} else {
			x = "-1.00";
		}

		if (y != null && y.length() > 7) {
			y = y.substring(0, y.length() - 4);
		} else {
			y = "-1.00";
		}
		return x + y;
	}

	/**
	 * 获得6位随机数验证码
	 * 
	 * @return 验证码
	 */
	public static String random6num()
	{
		String code = "";
		
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			int num = random.nextInt(9);
			code += num;
		}
		
		return code;
	}

	public static void main(String[] args)
			throws UnsupportedEncodingException
	{
		String s = UUID.randomUUID().toString();
		System.out.println(s);
		String rs = StringUtil.filterDash(s);
		System.out.println(rs);

		String imageUrl = "http://tripc2c-project-pic.b0.upaiyun.com/2014/12/25/c3e8c697a46f621a134b1d9d868135e3.jpg";
		System.out.println(imageUrl);
		String photoName = null;

		Pattern p = Pattern.compile("\\w+\\.\\w+");
		Matcher m = p.matcher(imageUrl);
		while (m.find()) {
			System.out.println(m.group());

			photoName = m.group();
		}

		String photoId = null;
		photoId = photoName.split("[.]")[0];
		System.out.println(photoId);

		photoId = StringUtil.parserIdFromImageUtrl(imageUrl);
		System.out.println(photoId);
		
		String code = StringUtil.random6num();
		System.out.println(code);
	}
}
