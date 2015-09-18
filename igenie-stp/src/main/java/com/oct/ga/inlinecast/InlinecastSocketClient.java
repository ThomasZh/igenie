package com.oct.ga.inlinecast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvObject;

public class InlinecastSocketClient
{
	/**
	 * key:ip+port
	 */
	private Map<String, ObjectPool<IoSession>> poolMap = new HashMap<String, ObjectPool<IoSession>>();

	public void sendto(String stpIp, int port, TlvObject tlv)
			throws NoSuchElementException, IllegalStateException, Exception
	{
		IoSession ioSession = null;
		String key = stpIp + port;
		ObjectPool<IoSession> pool = poolMap.get(key);
		if (pool == null) {
			StpPooledConnectionFactory factory = new StpPooledConnectionFactory();
			factory.setHostName(stpIp);
			factory.setPort(port);
			logger.info("create a StpPooledConnectionFactory");

			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			config.setLifo(false);
			config.setMinIdle(1);
			config.setMaxIdle(10);
			config.setMaxTotal(10);
			config.setMaxWaitMillis(5000);

			pool = new GenericObjectPool<IoSession>(factory, config);
			logger.info("create a connection pool");
		}

		try {
			ioSession = pool.borrowObject();
			if (ioSession.isConnected()) {
				WriteFuture writeFuture = ioSession.write(tlv);
				writeFuture.awaitUninterruptibly();

				if (writeFuture.getException() != null) {
					ioSession.getConfig().setUseReadOperation(false);
					return;
				}

				ioSession.getConfig().setUseReadOperation(true);

				final ReadFuture readFuture = ioSession.read();
				readFuture.awaitUninterruptibly();// read response message

				if (readFuture.getException() != null) {
					ioSession.getConfig().setUseReadOperation(false);
					return;
				}
				// stop blocking inbound messages
				ioSession.getConfig().setUseReadOperation(false);

				return;
			}
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Unable to borrow ioSession from pool" + e.toString());
		} finally {
			try {
				if (null != ioSession) {
					pool.returnObject(ioSession);
				}
			} catch (Exception e) {
				// ignored
			}
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(InlinecastSocketClient.class);
}
