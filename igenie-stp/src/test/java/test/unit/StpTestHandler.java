package test.unit;

import java.net.SocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.tlv.TlvObject;

public class StpTestHandler extends IoHandlerAdapter {
	private final static Logger logger = LoggerFactory.getLogger(StpTestHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		if (message instanceof TlvObject && message != null) {
			TlvObject pkg = (TlvObject) message;

			switch (pkg.getTag()) {
			case Command.UPLOAD_TASK_NOTE_RESP: {
				// UploadTaskNoteResp contextResp = (UploadTaskNoteResp)
				// StpCommandParser.decode(pkg);
				// if (contextResp.getRespState() == 0){
				// System.out.println(">>> upload task note success!");
				// }else{
				// System.out.println(">>> upload task note failed!");
				// }
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
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);

		SocketAddress rsa = session.getRemoteAddress();
		logger.error("remote address=" + rsa.toString() + " cause=" + cause.getLocalizedMessage());
	}

	@Override
	public void messageSent(IoSession session, Object obj) throws Exception {

		super.messageSent(session, obj);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);

		SocketAddress lsa = session.getLocalAddress();
		logger.info("local address=" + lsa.toString());
		SocketAddress rsa = session.getRemoteAddress();
		logger.info("remote address=" + rsa.toString());
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);

		if (status == IdleStatus.WRITER_IDLE)
			logger.info("status: writer_idle");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);

		SocketAddress lsa = session.getLocalAddress();
		logger.info("local address=" + lsa.toString());
		SocketAddress rsa = session.getRemoteAddress();
		logger.info("remote address=" + rsa.toString());
	}

}
