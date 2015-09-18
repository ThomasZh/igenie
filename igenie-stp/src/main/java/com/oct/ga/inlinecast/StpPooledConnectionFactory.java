package com.oct.ga.inlinecast;

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

public class StpPooledConnectionFactory
		extends BasePooledObjectFactory<IoSession>
{
	private String hostName;
	private int port;
	private int connectionCount;
	private long connectTimeoutMillis;
	private long writeTimeoutMillis;
	private int idleTime; // second
	private ProtocolCodecFilter protocolCodecFilter;
	private InlinecastEventHandler ioHandler;

	public StpPooledConnectionFactory()
	{
		connectionCount = 10;
		connectTimeoutMillis = 1000;
		writeTimeoutMillis = 1000;
		idleTime = 480;
		protocolCodecFilter = new ProtocolCodecFilter(new TlvPackageCodecFactory());
		ioHandler = new InlinecastEventHandler();

		logger.info("factory init");
	}

	@Override
	public IoSession create()
			throws Exception
	{
		logger.info("connect to stp server=(" + hostName + ":" + port + ") ");

		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(connectTimeoutMillis);
		connector.getSessionConfig().setBothIdleTime(idleTime);
		connector.getSessionConfig().setTcpNoDelay(true);
		connector.getFilterChain().addLast("codec", protocolCodecFilter);
		connector.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(ioHandler);
		ConnectFuture future = connector.connect(new InetSocketAddress(hostName, port));
		boolean completed = future.awaitUninterruptibly(connectTimeoutMillis);
		if (!completed) {
			throw new TimeoutException();
		}
		IoSession ioSession = future.getSession();
		return ioSession;
	}

	/**
	 * When an object is returned to the pool,clear the buffer.
	 */
	@Override
	public void destroyObject(PooledObject<IoSession> po)
			throws Exception
	{
		IoSession ioSession = po.getObject();
		ioSession.close(false);
		ioSession = null;
	}

	@Override
	public PooledObject<IoSession> wrap(IoSession ioSession)
	{
		return new DefaultPooledObject<IoSession>(ioSession);
	}

	public String getHostName()
	{
		return hostName;
	}

	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public int getConnectionCount()
	{
		return connectionCount;
	}

	public void setConnectionCount(int connectionCount)
	{
		this.connectionCount = connectionCount;
	}

	public long getConnectTimeoutMillis()
	{
		return connectTimeoutMillis;
	}

	public void setConnectTimeoutMillis(long connectTimeoutMillis)
	{
		this.connectTimeoutMillis = connectTimeoutMillis;
	}

	public long getWriteTimeoutMillis()
	{
		return writeTimeoutMillis;
	}

	public void setWriteTimeoutMillis(long writeTimeoutMillis)
	{
		this.writeTimeoutMillis = writeTimeoutMillis;
	}

	public int getIdleTime()
	{
		return idleTime;
	}

	public void setIdleTime(int idleTime)
	{
		this.idleTime = idleTime;
	}

	public ProtocolCodecFilter getProtocolCodecFilter()
	{
		return protocolCodecFilter;
	}

	public void setProtocolCodecFilter(ProtocolCodecFilter protocolCodecFilter)
	{
		this.protocolCodecFilter = protocolCodecFilter;
	}

	public InlinecastEventHandler getIoHandler()
	{
		return ioHandler;
	}

	public void setIoHandler(InlinecastEventHandler ioHandler)
	{
		this.ioHandler = ioHandler;
	}

	private final static Logger logger = LoggerFactory.getLogger(StpPooledConnectionFactory.class);
}
