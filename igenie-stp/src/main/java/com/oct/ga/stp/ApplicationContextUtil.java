package com.oct.ga.stp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.oct.ga.service.GaContactService;
import com.oct.ga.service.GaMessageService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.service.GaTemplateService;

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

	public static GaContactService getContactService()
	{
		return (GaContactService) context.getBean("gaContactService");
	}

	public static GaTaskService getTaskService()
	{
		return (GaTaskService) context.getBean("gaTaskService");
	}

	public static GaTemplateService getTemplateService()
	{
		return (GaTemplateService) context.getBean("gaTemplateService");
	}

	public static GaMessageService getMessageService()
	{
		return (GaMessageService) context.getBean("gaMessageService");
	}

	public static GlobalConfigurationVariables getGlobalConfigurationVariables()
	{
		if (context != null)
			return (GlobalConfigurationVariables) context.getBean("globalConfigurationVariables");
		else {
			logger.debug("context is null");
			return null;
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplicationContextUtil.class);
}
