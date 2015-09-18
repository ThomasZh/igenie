package com.redoct.ga.sup.account;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;

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
public class BaseEventHandler
		extends IoHandlerAdapter
{
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		if (message instanceof TlvObject && message != null) {
			long startTime = System.currentTimeMillis();
			TlvObject pkg = (TlvObject) message;
			short tag = pkg.getTag();

			SupReqCommand reqCmd = null;
			try {
				// decode all the message to request command
				reqCmd = (SupReqCommand) SupAccountCommandParser.decode(pkg);
			} catch (UnsupportedEncodingException ue) {
				logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + tag + "]|ErrorCode=["
						+ ErrorCode.ENCODING_FAILURE
						+ "]|This tlv pkg has no implementation in SupAccountCommandParser.decode() from "
						+ session.getRemoteAddress());

				session.close(true);
				return;// break the logic blow
			} catch (Exception e) {
				logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + tag + "]|ErrorCode=["
						+ ErrorCode.UNKNOWN_FAILURE + "]|" + session.getRemoteAddress() + "|"
						+ LogErrorMessage.getFullInfo(e));

				session.close(true);
				return;// break the logic blow
			}

			SupRespCommand respCmd = null;
			try {
				// init application context for all the command @2014/0415
				ApplicationContext context = ApplicationContextUtil.getContext();
				reqCmd.setIoSession(session);
				respCmd = reqCmd.execute(context);
			} catch (Exception e) {
				logger.error("sessionId=[" + session.getId() + "]|commandTag=[" + tag + "]|ErrorCode=["
						+ ErrorCode.UNKNOWN_FAILURE + "]" + LogErrorMessage.getFullInfo(e));
			}

			if (respCmd != null) {
				TlvObject tResp = null;
				try {
					tResp = SupAccountCommandParser.encode(respCmd);
				} catch (Exception e) {
					logger.error("sessionId=[" + session.getId() + "]|commandTag=[" + tag + "]|ErrorCode=["
							+ ErrorCode.ENCODING_FAILURE + "]" + LogErrorMessage.getFullInfo(e));
				}

				WriteFuture future = session.write(tResp);
				// Wait until the message is completely written out to the
				// O/S buffer.
				future.awaitUninterruptibly();
				if (!future.isWritten()) {
					// The messsage couldn't be written out completely for
					// some reason. (e.g. Connection is closed)
					logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + tag + "]|ErrorCode=["
							+ ErrorCode.CONNECTION_CLOSED
							+ "]|couldn't be written out resp completely for some reason.(e.g. Connection is closed)");

					session.close(true);
				}
			}
			long endTime = System.currentTimeMillis();
			long deltaTime = endTime - startTime;
			logger.info("sessionId=[" + session.getId() + "]|commandTag=[" + reqCmd.getTag() + "]|execute(" + deltaTime
					+ "ms) command end.");
		}// end of if
	}

	@Override
	public void sessionOpened(IoSession session)
			throws Exception
	{
		super.sessionOpened(session);

		SocketAddress rsa = session.getRemoteAddress();
		logger.info("sessionId=[" + session.getId() + "]|remote address=[" + rsa.toString() + "]");
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
		if (rsa != null) {
			logger.info("sessionId=[" + session.getId() + "]|remote address=[" + rsa.toString() + "] disconnect.");
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception
	{
		if (status == IdleStatus.BOTH_IDLE) {
			// SocketAddress rsa = session.getRemoteAddress();
			logger.info("sessionId=[" + session.getId() + "]|session both(in/out) idle!");
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
		// ���������MINA�ڵ�����close()�����󣬲����ٽ���TIME_WAIT״̬�ˣ���ֱ��Close���ˣ�
		// ����Ͳ�������������ЩTIME_WAIT��״̬�ˡ�
		cfg.setSoLinger(0);

		cfg.setTcpNoDelay(true);
		cfg.setUseReadOperation(false);
		cfg.setIdleTime(IdleStatus.BOTH_IDLE, 480);// 8 minutes
		cfg.setReadBufferSize(GlobalArgs.BUFFER_SIZE);
		cfg.setReceiveBufferSize(GlobalArgs.BUFFER_SIZE);
	}

	private final static Logger logger = LoggerFactory.getLogger(BaseEventHandler.class);

}
