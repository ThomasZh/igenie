package com.redoct.ga.sup.sms;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class ApplicationContextUtil
{
	private static ApplicationContext context;

	public static void setApplicationContext(ApplicationContext contex)
			throws BeansException
	{
		ApplicationContextUtil.context = contex;
	}

	public static ApplicationContext getContext()
	{
		return context;
	}

	public static SupSmsService getDeviceService()
	{
		return (SupSmsService) context.getBean("supSmsService");
	}

	public static GlobalConfigurationVariables getGlobalConfigurationVariables()
	{
		return (GlobalConfigurationVariables) context.getBean("globalConfigurationVariables");
	}
}
