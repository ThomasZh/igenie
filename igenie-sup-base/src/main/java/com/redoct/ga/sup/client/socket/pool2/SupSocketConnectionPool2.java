package com.redoct.ga.sup.client.socket.pool2;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupSocketConnectionPool2
{
	/**
	 * key: ip+port
	 */
	private Map<String, Pool2> poolMap = new HashMap<String, Pool2>();

	public IoSession getResource(InetSocketAddress addr)
	{
		String key = addr.getHostName() + addr.getPort();

		try {
			Pool2 pool = poolMap.get(key);
			if (pool == null) {
				SupSocketPooledConnectionFactory2 factory = new SupSocketPooledConnectionFactory2(addr.getHostName(),
						addr.getPort());
				logger.info("create a SupPooledConnectionFactory");

				GenericObjectPoolConfig config = new GenericObjectPoolConfig();
				config.setLifo(false);
				config.setMinIdle(3);
				config.setMaxIdle(5);
				config.setMaxTotal(10);
				config.setMaxWaitMillis(1000);

				pool = new Pool2(factory);
				poolMap.put(key, pool);

				logger.info("create a new GenericObjectPool pool for key:" + key);
			}

			logger.info("got resource=[" + key + "] from pool");

			return pool.getResource();
		} catch (Exception e) {
			throw new RuntimeException("Could not get a resource from the pool", e);
		}
	}

	public void returnResource(InetSocketAddress addr, final IoSession resource)
	{
		String key = addr.getHostName() + addr.getPort();
		logger.info("return resource=[" + key + "] to pool");

		try {
			Pool2 pool = poolMap.get(key);
			if (pool != null) {
				pool.returnResource(resource);
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not return the resource to the pool", e);
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(SupSocketConnectionPool2.class);
}
