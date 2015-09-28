package com.oct.ga.admin;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.stp.ApplicationContextUtil;
import com.oct.ga.stp.GlobalConfigurationVariables;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;

public class GaContextLoaderListener
		implements ApplicationListener<ContextStartedEvent>
{
	/**
	 * 启动加载执行: spring中已经内置的几种事件
	 * 
	 * ContextClosedEvent 、ContextRefreshedEvent 、ContextStartedEvent
	 * 、ContextStoppedEvent 、RequestHandleEvent
	 */
	@Override
	public void onApplicationEvent(ContextStartedEvent event)
	{
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

	private void loadSupServerState()
			throws IOException
	{
		GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext().getBean(
				"globalConfigurationVariables");
		SupSocketConnectionManager socketConnectionManager = GenericSingleton
				.getInstance(SupSocketConnectionManager.class);
		String pathname = gcv.getSupServerListPath();
		socketConnectionManager.loadFromFile(pathname);
		logger.info("Load sup server list from " + pathname);
	}

	private final static Logger logger = LoggerFactory.getLogger(GaContextLoaderListener.class);
}
