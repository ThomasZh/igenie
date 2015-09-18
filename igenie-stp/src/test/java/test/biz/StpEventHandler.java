package test.biz;

import java.net.SocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.cmd.auth.LoginResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.monitor.agent.cmd.MonitorContextResp;
import com.oct.ga.stp.parser.StpCommandParser;

public class StpEventHandler
		extends IoHandlerAdapter
{
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		if (message instanceof TlvObject && message != null) {
			TlvObject pkg = (TlvObject) message;

			switch (pkg.getTag()) {
			case Command.LOGIN_RESP:
				LoginResp loginResp = new LoginResp().decode(pkg);
				System.out.println("response state: " + loginResp.getRespState());
				if (loginResp.getRespState() == ErrorCode.SUCCESS) {
					System.out.println("account id: " + loginResp.getAccountId());
					System.out.println("session ticket: " + loginResp.getSessionToken());
				}
				break;
			case Command.MONITOR_CONTEXT_RESP: {
				ReqCommand respCmd = StpCommandParser.decode(pkg);
				MonitorContextResp contextResp = (MonitorContextResp) respCmd;
				if (contextResp.isEof())
					System.exit(0);
				else
					System.out.print(contextResp.getContext());
				break;
			}
			default:
				logger.warn("unknown command!");

				session.close(true);
				break;
			}

			return;
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		// logger.error("client start exception");
		super.exceptionCaught(session, cause);

		SocketAddress rsa = session.getRemoteAddress();
		// logger.error("remote address=" + rsa.toString() + " cause="
		// + cause.getLocalizedMessage());
	}

	@Override
	public void messageSent(IoSession session, Object obj)
			throws Exception
	{
		// logger.info("client sent a message");

		super.messageSent(session, obj);
	}

	@Override
	public void sessionClosed(IoSession session)
			throws Exception
	{
		// logger.info("client session close");
		super.sessionClosed(session);

		SocketAddress lsa = session.getLocalAddress();
		// logger.info("local address=" + lsa.toString());
		SocketAddress rsa = session.getRemoteAddress();
		// logger.info("remote address=" + rsa.toString());
	}

	@Override
	public void sessionCreated(IoSession session)
			throws Exception
	{
		// logger.info("client session created.");
		super.sessionCreated(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception
	{
		super.sessionIdle(session, status);

		if (status == IdleStatus.WRITER_IDLE)
			logger.info("client idle");
	}

	@Override
	public void sessionOpened(IoSession session)
			throws Exception
	{
		// logger.info("client session opened.");
		super.sessionOpened(session);

		SocketAddress lsa = session.getLocalAddress();
		// logger.info("local address=" + lsa.toString());
		SocketAddress rsa = session.getRemoteAddress();
		// logger.info("remote address=" + rsa.toString());
	}

	private final static Logger logger = LoggerFactory.getLogger(StpEventHandler.class);

}
