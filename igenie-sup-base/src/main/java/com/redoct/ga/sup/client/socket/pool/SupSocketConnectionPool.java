package com.redoct.ga.sup.client.socket.pool;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupSocketConnectionPool
{
	/**
	 * key: ip+port
	 */
	private Map<String, GenericObjectPool> poolMap = new HashMap<String, GenericObjectPool>();

	@SuppressWarnings("unchecked")
	public IoSession getResource(InetSocketAddress addr)
	{
		String key = addr.getHostName() + addr.getPort();

		try {
			GenericObjectPool pool = poolMap.get(key);
			if (pool == null) {
				SupSocketPooledConnectionFactory factory = new SupSocketPooledConnectionFactory(addr.getHostName(),
						addr.getPort());
				logger.info("create a SupPooledConnectionFactory");

				GenericObjectPoolConfig config = new GenericObjectPoolConfig();
				config.setLifo(false);
				config.setMinIdle(3);
				config.setMaxIdle(5);
				config.setMaxTotal(10);
				config.setMaxWaitMillis(1000);

				pool = new GenericObjectPool(factory, config);
				poolMap.put(key, pool);

				logger.info("create a new GenericObjectPool pool for key:" + key);
			}

			IoSession ioSession = (IoSession) pool.borrowObject();
			logger.debug("getCreatedCount in pool:" + pool.getCreatedCount());
			logger.debug("getBorrowedCount in pool:" + pool.getBorrowedCount());
			logger.debug("getDestroyedCount in pool:" + pool.getDestroyedCount());

			logger.info("got resource=[" + key + "] from pool");

			return ioSession;
		} catch (Exception e) {
			throw new RuntimeException("Could not get a resource from the pool", e);
		}
	}

	public void returnResourceObject(InetSocketAddress addr, final IoSession resource)
	{
		String key = addr.getHostName() + addr.getPort();
		logger.info("return resource=[" + key + "] to pool");

		try {
			GenericObjectPool pool = poolMap.get(key);
			if (pool != null) {
				pool.returnObject(resource);
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not return the resource to the pool", e);
		}
	}

	public void returnBrokenResource(InetSocketAddress addr, final IoSession resource)
	{
		String key = addr.getHostName() + addr.getPort();
		logger.warn("return broken resource=[" + key + "] to pool");

		try {
			GenericObjectPool pool = poolMap.get(key);
			if (pool != null) {
				logger.debug("getCreatedCount in pool:" + pool.getCreatedCount());
				logger.debug("getBorrowedCount in pool:" + pool.getBorrowedCount());
				logger.debug("getDestroyedCount in pool:" + pool.getDestroyedCount());

				pool.invalidateObject(resource);
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not return the resource to the pool", e);
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(SupSocketConnectionPool.class);
}
