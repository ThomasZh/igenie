package com.oct.ga.admin;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.oct.ga.admin.mvc.ApplicationContextProvider;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.stp.ApplicationContextUtil;
import com.oct.ga.stp.GlobalConfigurationVariables;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;

public class MyServletContextListener
		implements ServletContextListener
{
	private ServletContext context = null;

	public void contextInitialized(ServletContextEvent event)
	{
		System.out.println("start MyServletContextListener...");
		// context = event.getServletContext();
		// context.setAttribute("user1", null);

		logger.info("start loadSupServerState...");
		System.out.println("start loadSupServerState...");
		try {
			loadSupServerState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("loadSupServerState success!");
		System.out.println("loadSupServerState success!");
	}

	public void contextDestroyed(ServletContextEvent event)
	{
		// User user = (User) context.getAttribute("user1");
		// this.context = null;
	}

	private void loadSupServerState()
			throws IOException
	{
		String springXmlFile = "/Users/thomas/git/igenie-web/WebContent/WEB-INF/mvcDispather-servlet.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		ApplicationContextUtil.setApplicationContext(applicationContext);
		logger.info("init ApplicationContext");

		GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext().getBean(
				"globalConfigurationVariables");

		SupSocketConnectionManager socketConnectionManager = GenericSingleton
				.getInstance(SupSocketConnectionManager.class);
		String pathname = gcv.getSupServerListPath();
		socketConnectionManager.loadFromFile(pathname);
		logger.info("Load sup server list from " + pathname);
	}

	private ApplicationContextProvider applicationContextProvider;
	private GlobalConfigurationVariables globalConfigurationVariables;

	public GlobalConfigurationVariables getGlobalConfigurationVariables()
	{
		return globalConfigurationVariables;
	}

	public void setGlobalConfigurationVariables(GlobalConfigurationVariables globalConfigurationVariables)
	{
		this.globalConfigurationVariables = globalConfigurationVariables;
	}

	public ApplicationContextProvider getApplicationContextProvider()
	{
		return applicationContextProvider;
	}

	public void setApplicationContextProvider(ApplicationContextProvider applicationContextProvider)
	{
		this.applicationContextProvider = applicationContextProvider;
	}

	private final static Logger logger = LoggerFactory.getLogger(MyServletContextListener.class);
}
