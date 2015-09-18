package com.oct.ga.gatekeeper.client;

import java.net.SocketAddress;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.gatekeeper.QueryStpStatesResp;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.comm.parser.JsonParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.gatekeeper.cmd.GatekeeperCmdParser;

/**
 * only handle server response
 * 
 * @author liwenzhi
 * 
 */
public class StpStateHandler
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
		logger.debug(">>> server response received!");

		if (message == null)
			return;

		TlvObject tlv = (TlvObject) message;

		// decode all the message to response command
		response = (RespCommand) GatekeeperCmdParser.decode(tlv);
		if (response == null) {
			logger.warn(">>> current pkg decode has no implementation in GatekeeperCmdParser.decode()!");
			session.close(true);
			return;// break the logic blow
		} else {
			if (response.getTag() == Command.GK_QUERY_STP_STATES_RESP) {
				QueryStpStatesResp respCmd = (QueryStpStatesResp) response;
				String json = respCmd.getJson();
				logger.debug(json);

				List<StpServerInfoJsonBean> serverList = JsonParser.json2ServerList(json);
				System.out.println("|------------------------------------|--------------------------|-------|");
				System.out.println("|stp                                 |ip:port                   |State  |");
				System.out.println("|------------------------------------|--------------------------|-------|");
				for (StpServerInfoJsonBean stp : serverList) {
					System.out.println("|" + stp.getStpId() + "|" + stp.getServerIp() + ":" + stp.getPort() + "\t|"
							+ stp.isActive()+"\t|");
				}
				System.out.println("|------------------------------------|--------------------------|-------|");
			}
		}

		logger.debug(">>> messageReceived!");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		logger.error("client exception!");
		super.exceptionCaught(session, cause);

		SocketAddress rsa = session.getRemoteAddress();
		logger.error("remote address=" + rsa.toString() + " cause=" + cause.getLocalizedMessage());
	}

	@Override
	public void messageSent(IoSession session, Object obj)
			throws Exception
	{
		logger.info(">>> messge sent!");

		super.messageSent(session, obj);
	}

	@Override
	public void sessionClosed(IoSession session)
			throws Exception
	{
		logger.info(">>> session closed!");
		super.sessionClosed(session);

		SocketAddress lsa = session.getLocalAddress();
		logger.info("local address=" + lsa.toString());
		SocketAddress rsa = session.getRemoteAddress();
		logger.info("remote address=" + rsa.toString());
	}

	@Override
	public void sessionCreated(IoSession session)
			throws Exception
	{
		logger.info(">>> session created!");
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
		logger.info(">>> session opened!");
		super.sessionOpened(session);

		SocketAddress lsa = session.getLocalAddress();
		logger.info("local address=" + lsa.toString());
		SocketAddress rsa = session.getRemoteAddress();
		logger.info("remote address=" + rsa.toString());
	}

	private final static Logger logger = LoggerFactory.getLogger(StpStateHandler.class);
}
