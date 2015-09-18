package com.redoct.ga.sup.client.socket;

import java.net.SocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;

public class StpSocketEventHandler
		extends IoHandlerAdapter
{
	/**
	 * currently received command mapped by message
	 */
	private RespCommand response;

	/**
	 * for outer use to call execute method
	 */
	public RespCommand getResponse()
	{
		return response;
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		// logger.debug(">>> server response received!");

		if (message == null)
			return;

		TlvObject tlv = (TlvObject) message;

		// decode all the message to response command
		response = (RespCommand) CommandParser.decode(tlv);
		if (response == null) {
			logger.warn(">>> current pkg decode has no implementation in SupCommandParser.decode()!");
			session.close(true);
			return;// break the logic blow
		} else {
		}

		logger.info("receive pkg=[" + tlv.getTag() + "], parse=[" + response.getTag() + "], state=["
				+ response.getRespState() + "]");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		// logger.error("client exception!");
		super.exceptionCaught(session, cause);

		SocketAddress rsa = session.getRemoteAddress();
		logger.error("remote address=" + rsa.toString() + " cause=" + cause.getLocalizedMessage());
	}

	@Override
	public void messageSent(IoSession session, Object obj)
			throws Exception
	{
		// logger.info(">>> messge sent!");

		super.messageSent(session, obj);
	}

	@Override
	public void sessionClosed(IoSession session)
			throws Exception
	{
		// logger.info(">>> session closed!");
		super.sessionClosed(session);

		// SocketAddress lsa = session.getLocalAddress();
		// logger.info("local address=" + lsa.toString());
		// SocketAddress rsa = session.getRemoteAddress();
		// logger.info("remote address=" + rsa.toString());
	}

	@Override
	public void sessionCreated(IoSession session)
			throws Exception
	{
		// logger.info(">>> session created!");
		super.sessionCreated(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception
	{
		super.sessionIdle(session, status);

		if (status == IdleStatus.WRITER_IDLE)
			logger.info(">>> session idle!");
	}

	@Override
	public void sessionOpened(IoSession session)
			throws Exception
	{
		// logger.info(">>> session opened!");
		super.sessionOpened(session);

		// SocketAddress lsa = session.getLocalAddress();
		// logger.info("local address=" + lsa.toString());
		// SocketAddress rsa = session.getRemoteAddress();
		// logger.info("remote address=" + rsa.toString());
	}

	private final static Logger logger = LoggerFactory.getLogger(SupSocketEventHandler.class);

}
