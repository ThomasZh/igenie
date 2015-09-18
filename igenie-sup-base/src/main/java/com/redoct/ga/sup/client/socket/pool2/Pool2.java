package com.redoct.ga.sup.client.socket.pool2;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pool2
{
	private BasePooledObjectFactory factory;
	private List<IoSession> sessions = new ArrayList<IoSession>();

	public Pool2(BasePooledObjectFactory factory)
	{
		logger.debug("resuorce pool init.");
		this.factory = factory;
	}

	public IoSession getResource()
			throws Exception
	{
		IoSession session = null;
		if (sessions.size() > 0) {
			logger.debug("resuorce pool size=[" + sessions.size() + "]");
			session = sessions.get(0);
			sessions.remove(0);
		} else {
			session = (IoSession) factory.create();
		}
		return session;
	}

	public void returnResource(final IoSession resource)
	{
		logger.debug("return resuorce=[" + resource.getId() + "]");
		sessions.add(resource);
	}

	private final static Logger logger = LoggerFactory.getLogger(Pool2.class);
}
