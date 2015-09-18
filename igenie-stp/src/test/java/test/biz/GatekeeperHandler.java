package test.biz;

import java.net.SocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.gatekeeper.GK_ACF;
import com.oct.ga.comm.tlv.TlvObject;

public class GatekeeperHandler
		extends IoHandlerAdapter
{
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		if (message instanceof TlvObject && message != null) {
			TlvObject pkg = (TlvObject) message;

			switch (pkg.getTag()) {
			case Command.GK_ACF:
				GK_ACF acf = new GK_ACF().decode(pkg);
				System.out.println("response state: " + acf.getRespState());
				if (acf.getRespState() == ErrorCode.SUCCESS) {
					System.out.println("gateToken: " + acf.getGateToken());
					System.out.println("stp ip: " + acf.getServerIp());
					System.out.println("port: " + acf.getPort());

					GatekeeperArgs gkArgs = GenericSingleton.getInstance(GatekeeperArgs.class);
					gkArgs.setToken(acf.getGateToken());
					gkArgs.setIp(acf.getServerIp());
					gkArgs.setPort(acf.getPort());
				}
				break;
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

	private final static Logger logger = LoggerFactory.getLogger(GatekeeperHandler.class);
}
