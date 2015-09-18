package com.oct.ga.inlinecast.cmd;

import java.io.UnsupportedEncodingException;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.inlinecast.InlinecastTaskLogReq;
import com.oct.ga.comm.cmd.inlinecast.InlinecastTaskLogResp;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;

public class InlinecastTaskLogAdapter
		extends StpReqCommand
{
	public InlinecastTaskLogAdapter()
	{
		super();

		this.setTag(Command.INLINECAST_TASK_LOG_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new InlinecastTaskLogReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		return reqCmd.encode();
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		MsgFlowBasicInfo notify = reqCmd.getNotify();
		long toSessionId = reqCmd.getIoSessionId();

		// session.getService().getManagedSessions();
		IoService ioService = session.getService();
		IoSession ioSession = ioService.getManagedSessions().get(toSessionId);
		if (ioSession == null) { // this session is not online now!
			logger.warn("ioSession=[" + toSessionId + "] is null!");
			return null;
		}

		InlinecastTaskLogResp taskLogResp = new InlinecastTaskLogResp(currentTimestamp, ErrorCode.SUCCESS, notify);
		TlvObject tlvTaskLogResp = CommandParser.encode(taskLogResp);

		WriteFuture future = ioSession.write(tlvTaskLogResp);
		// Wait until the message is completely written out to the
		// O/S buffer.
		future.awaitUninterruptibly();
		if (future.isWritten()) {
			// TODO sync state received

		} else {
			// The messsage couldn't be written out completely for
			// some reason. (e.g. Connection is closed)
			logger.warn("sessionId=["
					+ session.getId()
					+ "]|deviceId=["
					+ this.getMyDeviceId()
					+ "]|accountId=["
					+ this.getMyAccountId()
					+ "]|commandTag=["
					+ this.getTag()
					+ "]|ErrorCode=["
					+ ErrorCode.CONNECTION_CLOSED
					+ "]|couldn't be written out SyncMessageResp completely for some reason.(e.g. Connection is closed)");
		}

		// Warning: OldStpEventHandler do not response anything.
		return null;
	}

	private InlinecastTaskLogReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(InlinecastTaskLogAdapter.class);

}
