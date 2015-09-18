package com.oct.ga.inlinecast.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.inlinecast.InlinecastInviteFeedbackReq;
import com.oct.ga.comm.cmd.invite.SyncInviteResp;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;

public class InlinecastInviteFeedbackAdapter
		extends StpReqCommand
{
	public InlinecastInviteFeedbackAdapter()
	{
		super();

		this.setTag(Command.INLINECAST_INVITE_FEEDBACK_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new InlinecastInviteFeedbackReq().decode(tlv);
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
		GaInviteFeedback feedback = reqCmd.getFeedback();

		// session.getService().getManagedSessions();
		IoService ioSerive = session.getService();

		IoSession ioSession = ioSerive.getManagedSessions().get(reqCmd.getIoSessionId());
		if (ioSession == null) { // this session is not online now!
			logger.warn("ioSession is null!");
			return null;
		}

		List<GaInviteFeedback> array = new ArrayList<GaInviteFeedback>();
		array.add(feedback);
		SyncInviteResp syncInviteResp = new SyncInviteResp(ErrorCode.SUCCESS, null, array);
		TlvObject tlvSyncInviteResp = CommandParser.encode(syncInviteResp);

		WriteFuture future = ioSession.write(tlvSyncInviteResp);
		// Wait until the message is completely written out to the
		// O/S buffer.
		future.awaitUninterruptibly();
		if (!future.isWritten()) {
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

	private InlinecastInviteFeedbackReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(InlinecastInviteFeedbackAdapter.class);

}