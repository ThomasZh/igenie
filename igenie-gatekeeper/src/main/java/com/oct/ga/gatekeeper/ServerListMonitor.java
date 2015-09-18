package com.oct.ga.gatekeeper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.session.SessionService3MapImpl;

public class ServerListMonitor
		implements Runnable
{
	public ServerListMonitor()
	{
	}

	public void run()
	{
		SessionService3MapImpl sessionService = GenericSingleton.getInstance(SessionService3MapImpl.class);
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();

		try {
			while (true) {
				ServerListCache serverListCache = GenericSingleton.getInstance(ServerListCache.class);
				String pathname = gcv.getStpServerListPath();
				List<StpServerInfoJsonBean> newStpArray = serverListCache.loadFromFile(pathname);
				logger.info("Load server list from " + pathname);

				long currentTimestamp = System.currentTimeMillis();
				for (StpServerInfoJsonBean newStp : newStpArray) {
					long lastTryTimestamp = serverListCache.getLastTryTimestamp(newStp.getStpId());
					if (lastTryTimestamp == 0) {
						newStp.setActive(true);
					} else {
						long deltaTime = currentTimestamp - lastTryTimestamp;
						newStp.setActive(deltaTime < 2 * gcv.getHeartbitInterval() ? true : false); // sec
					}
				}

				synchronized (serverListCache) {
					serverListCache.clear();
					if (newStpArray != null)
						for (StpServerInfoJsonBean newStp : newStpArray) {
							serverListCache.add(newStp);

							sessionService.putStp(newStp.getStpId(), newStp);
						}
				}

				Thread.sleep(gcv.getCheckInterval()); // 1 minute
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(ServerListMonitor.class);
}
