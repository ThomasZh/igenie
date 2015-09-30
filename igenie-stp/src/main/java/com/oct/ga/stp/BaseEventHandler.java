package com.oct.ga.stp;

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
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.oct.ga.stp.parser.StpCommandParser;
import com.redoct.ga.sup.session.SupSessionService;

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
public class BaseEventHandler extends IoHandlerAdapter {
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message instanceof TlvObject && message != null) {
			long startTime = System.currentTimeMillis();
			TlvObject pkg = (TlvObject) message;
			short tag = pkg.getTag();

			StpReqCommand reqCmd = null;
			try {
				// decode all the message to request command
				reqCmd = (StpReqCommand) StpCommandParser.decode(pkg);
			} catch (UnsupportedEncodingException ue) {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[]|accountId=[]|commandTag=[" + tag
						+ "]|ErrorCode=[" + ErrorCode.ENCODING_FAILURE
						+ "]|This tlv pkg has no implementation in StpCommandParser.decode() from "
						+ session.getRemoteAddress());

				session.close(true);
				return;// break the logic blow
			} catch (Exception e) {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[]|accountId=[]|commandTag=[" + tag
						+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|" + session.getRemoteAddress() + "|"
						+ LogErrorMessage.getFullInfo(e));

				session.close(true);
				return;// break the logic blow
			}

			// init application context for all the command @2014/0415
			reqCmd.setSession(session);

			// String deviceId = (String) session.getAttribute("deviceId");
			String deviceId = null;
			String accountId = null;
			if (!(session instanceof IoSessionAdapter)) {
				deviceId = reqCmd.getMyDeviceId();
				accountId = reqCmd.getMyAccountId();
			}
			/**
			 * Illegal request!!! logic place here is waiting for command above
			 * set account id
			 */
			if (!(session instanceof IoSessionAdapter) && tag != Command.REGISTER_REQ && tag != Command.LOGIN_REQ
					&& tag != Command.SSO_LOGIN_REQ && tag != Command.REGISTER_LOGIN_REQ && tag != Command.STP_ARQ
					&& tag != Command.QUERY_FORGOT_PASSWORD_EMAIL_REQ && tag != Command.RESET_PASSWORD_REQ
					&& tag != Command.FORGOT_PASSWORD_REQ && tag != Command.INVITE_QUERY_REGISTER_SEMIID_REQ
					&& tag != Command.CHECK_VERSION_UPGRADE_REQ && tag != Command.MODIFY_SUP_STATE_REQ
					&& tag != Command.HEARTBIT_REQ && tag != Command.APPLY_PHONE_REGISTER_VERIFICATION_CODE_REQ
					&& tag != Command.PHONE_REGISTER_LOGIN_REQ && tag != Command.DEVICE_REGISTER_LOGIN_REQ
					&& tag != Command.INLINECAST_ACTIVITY_JOIN_REQ && tag != Command.INLINECAST_APPLY_STATE_REQ
					&& tag != Command.INLINECAST_INVITE_FEEDBACK_REQ && tag != Command.INLINECAST_INVITE_REQ
					&& tag != Command.INLINECAST_MESSAGE_REQ && tag != Command.INLINECAST_TASK_ACTIVITY_REQ
					&& tag != Command.INLINECAST_TASK_LOG_REQ && deviceId == null) {
				long endTime = System.currentTimeMillis();
				long deltaTime = endTime - startTime;

				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[]|commandTag=["
						+ tag + "]|ErrorCode=[" + ErrorCode.NOT_ALLOW + "]|execute(" + deltaTime
						+ "ms). Illegal request from " + session.getRemoteAddress());

				session.close(true);
				return;// break the logic blow
			}

			ApplicationContext context = ApplicationContextUtil.getContext();
			StpCommand respCmd = null;
			try {
				respCmd = reqCmd.execute(context);
			} catch (Exception e) {
				logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
						+ "]|commandTag=[" + tag + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]"
						+ LogErrorMessage.getFullInfo(e));
			}

			if (respCmd != null) {
				TlvObject tResp = null;
				try {
					tResp = StpCommandParser.encode(respCmd);
				} catch (Exception e) {
					logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=["
							+ accountId + "]|commandTag=[" + tag + "]|ErrorCode=[" + ErrorCode.ENCODING_FAILURE + "]"
							+ LogErrorMessage.getFullInfo(e));
				}

				WriteFuture future = session.write(tResp);
				// Wait until the message is completely written out to the
				// O/S buffer.
				future.awaitUninterruptibly();
				if (!future.isWritten()) {
					// The messsage couldn't be written out completely for
					// some reason. (e.g. Connection is closed)
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=["
							+ accountId + "]|commandTag=[" + tag + "]|ErrorCode=[" + ErrorCode.CONNECTION_CLOSED
							+ "]|couldn't be written out resp completely for some reason.(e.g. Connection is closed)");

					session.close(true);
				}
			}
			long endTime = System.currentTimeMillis();
			long deltaTime = endTime - startTime;
			logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
					+ "]|commandTag=[" + reqCmd.getTag() + "]|execute(" + deltaTime + "ms) command end.");
		} // end of if
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);

		SocketAddress rsa = session.getRemoteAddress();
		logger.info("sessionId=[" + session.getId() + "]|remote address=[" + rsa.toString() + "]");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);

		// if (cause != null)
		// logger.error(LogErrorMessage.getFullInfo(cause));
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);

		String accountId = (String) session.getAttribute("accountId");
		String deviceId = (String) session.getAttribute("deviceId");
		SocketAddress rsa = session.getRemoteAddress();
		if (rsa != null) {
			logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
					+ "]|remote address=[" + rsa.toString() + "] disconnect.");
		}

		if (accountId != null) {
			ApplicationContext context = ApplicationContextUtil.getContext();
			SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");
			supSessionService.inactiveStpSession(accountId);
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		if (status == IdleStatus.BOTH_IDLE) {
			// SocketAddress rsa = session.getRemoteAddress();
			logger.info("sessionId=[" + session.getId() + "]|session both(in/out) idle!");
			this.sessionClosed(session);// close session
		}
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
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

		cfg.setTcpNoDelay(true);
		cfg.setUseReadOperation(false);
		cfg.setIdleTime(IdleStatus.BOTH_IDLE, 480);// 8 minutes
		cfg.setReadBufferSize(GlobalArgs.BUFFER_SIZE);
		cfg.setReceiveBufferSize(GlobalArgs.BUFFER_SIZE);
	}

	private final static Logger logger = LoggerFactory.getLogger(BaseEventHandler.class);

}
