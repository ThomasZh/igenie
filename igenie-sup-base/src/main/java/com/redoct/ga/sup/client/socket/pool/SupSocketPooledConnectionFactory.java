package com.redoct.ga.sup.client.socket.pool;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.codec.TlvPackageCodecFactory;
import com.redoct.ga.sup.client.socket.SupSocketEventHandler;

public class SupSocketPooledConnectionFactory
		extends BasePooledObjectFactory<IoSession>
{
	private String hostName;
	private int port;
	private int connectionCount = 10;
	private long connectTimeoutMillis;
	private long writeTimeoutMillis = 1000;
	private int idleTime; // second
	private ProtocolCodecFilter protocolCodecFilter;

	public SupSocketPooledConnectionFactory(String hostname, int port)
	{
		this.hostName = hostname;
		this.port = port;
		this.connectionCount = 10;
		this.connectTimeoutMillis = 1000;
		this.writeTimeoutMillis = 1000;
		this.idleTime = 480;

		logger.info("factory init");
	}

	@Override
	public IoSession create()
			throws Exception
	{
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(connectTimeoutMillis);
		connector.getSessionConfig().setBothIdleTime(idleTime);
		connector.getSessionConfig().setTcpNoDelay(true);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		connector.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(new SupSocketEventHandler());
		ConnectFuture future = connector.connect(new InetSocketAddress(hostName, port));
		logger.info("create a new socket connect to " + hostName + ":" + port);
		boolean completed = future.awaitUninterruptibly(connectTimeoutMillis);
		if (!completed) {
			throw new TimeoutException();
		}
		IoSession ioSession = future.getSession();

		return ioSession;
	}

	@Override
	public PooledObject<IoSession> wrap(IoSession client)
	{
		return new DefaultPooledObject<IoSession>(client);
	}

	private final static Logger logger = LoggerFactory.getLogger(SupSocketPooledConnectionFactory.class);
}
