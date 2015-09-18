package com.oct.ga.gatekeeper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.oct.ga.session.GaSessionService;

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

	public static GaSessionService getSessionService()
	{
		return (GaSessionService) context.getBean("gaSessionService");
	}

	public static GlobalConfigurationVariables getGlobalConfigurationVariables()
	{
		return (GlobalConfigurationVariables) context.getBean("globalConfigurationVariables");
	}
}
