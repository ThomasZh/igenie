package com.oct.ga.gatekeeper;

import java.net.SocketAddress;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.gatekeeper.cmd.GatekeeperCmdParser;
import com.oct.ga.stp.cmd.StpReqCommand;

/**
 * do 5 task on messageReceived:<br>
 * <br>
 * 1.monitor counting;<br>
 * 2.pkg decode to request command;<br>
 * 3.illegal request check;<br>
 * 4.setup command service;<br>
 * 5.do command execute;<br>
 * 
 * @author liwenzhi
 * 
 */
public class GateKeeperEventHandler
		extends IoHandlerAdapter
{
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		if (message instanceof TlvObject && message != null) {
			TlvObject pkg = (TlvObject) message;
			// short tag = pkg.getTag();

			// decode all the message to request command
			StpReqCommand reqCmd = (StpReqCommand) GatekeeperCmdParser.decode(pkg);
			if (reqCmd == null) {
				session.close(true);
				return;// break the logic blow
			}

			// init application context for all the command @2014/0415
			reqCmd.setSession(session);
			ApplicationContext context = ApplicationContextUtil.getContext();
			int currentTimestamp = DatetimeUtil.currentTimestamp();
			reqCmd.setCurrentTimestamp(currentTimestamp);

			RespCommand respCmd = reqCmd.execute(context);
			if (respCmd != null) {
				TlvObject tlv = respCmd.encode();

				WriteFuture future = session.write(tlv);
				// Wait until the message is completely written out to the
				// O/S buffer.
				future.awaitUninterruptibly();
				if (future.isWritten()) {
					logger.debug("Wait until the message is completely written out to the O/S buffer.");
				} else {
					// The messsage couldn't be written out completely for
					// some reason. (e.g. Connection is closed)
					logger.warn("The messsage couldn't be written out completely for some reason. (e.g. Connection is closed)");
				}
			}

			// be sure client received response package,
			// then close this connection.
			Thread.sleep(1000); 
			session.close(true);
		}// end of if
	}

	@Override
	public void sessionOpened(IoSession session)
			throws Exception
	{
		super.sessionOpened(session);

		SocketAddress rsa = session.getRemoteAddress();
		logger.info("remote address=" + rsa.toString());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		super.exceptionCaught(session, cause);

		// if (cause != null)
		// logger.error(LogErrorMessage.getFullInfo(cause));
	}

	@Override
	public void sessionClosed(IoSession session)
			throws Exception
	{
		super.sessionClosed(session);

		SocketAddress rsa = session.getRemoteAddress();
		logger.info("remote address=" + rsa.toString());
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception
	{
		if (status == IdleStatus.BOTH_IDLE) {
			// SocketAddress rsa = session.getRemoteAddress();
			logger.info(">>> session closed!");
			this.sessionClosed(session);// close session
		}
	}

	@Override
	public void sessionCreated(IoSession session)
			throws Exception
	{
		super.sessionCreated(session);
		// Empty handler
		SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
		cfg.setReuseAddress(true);
		cfg.setWriteTimeout(30);
		cfg.setTcpNoDelay(false);
		cfg.setKeepAlive(false);
		// 设置了它后，MINA在调用了close()方法后，不会再进入TIME_WAIT状态了，而直接Close掉了，
		// 这样就不会产生这样的那些TIME_WAIT的状态了。
		cfg.setSoLinger(0);

		logger.info("BUFFER_SIZE:" + GlobalArgs.BUFFER_SIZE);

		cfg.setTcpNoDelay(true);
		cfg.setUseReadOperation(false);
		cfg.setIdleTime(IdleStatus.BOTH_IDLE, 480);// 8 minutes
		cfg.setReadBufferSize(GlobalArgs.BUFFER_SIZE);
		cfg.setReceiveBufferSize(GlobalArgs.BUFFER_SIZE);

		SocketAddress rsa = session.getRemoteAddress();
		logger.info("remote address=" + rsa.toString());
	}

	private final static Logger logger = LoggerFactory.getLogger(GateKeeperEventHandler.class);
}
