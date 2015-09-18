package com.oct.ga.inlinecast.cmd;

import java.io.UnsupportedEncodingException;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.inlinecast.InlinecastMessageReq;
import com.oct.ga.comm.cmd.msg.SyncMessageResp;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaMessageService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class InlinecastMessageAdapter
		extends StpReqCommand
{
	public InlinecastMessageAdapter()
	{
		super();

		this.setTag(Command.INLINECAST_MESSAGE_REQ);
	}

	public InlinecastMessageAdapter(MessageInlinecast msg)
	{
		this();

		reqCmd = new InlinecastMessageReq();
		reqCmd.setMessage(msg);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new InlinecastMessageReq().decode(tlv);
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
		GaMessageService messageService = (GaMessageService) context.getBean("gaMessageService");
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

		MessageInlinecast msg = reqCmd.getMessage();

		// session.getService().getManagedSessions();
		IoService ioSerive = session.getService();

		IoSession ioSession = ioSerive.getManagedSessions().get(msg.getReciverIoSessionId());
		if (ioSession == null) { // this session is not online now!
			logger.warn("ioSession is null!");
			return null;
		}

		SyncMessageResp syncMessageResp = new SyncMessageResp(msg);
		TlvObject tlvSyncMessageResp = CommandParser.encode(syncMessageResp);

		WriteFuture future = ioSession.write(tlvSyncMessageResp);
		// Wait until the message is completely written out to the
		// O/S buffer.
		future.awaitUninterruptibly();
		if (future.isWritten()) {
			messageService.updateState(msg.get_id(), msg.getToAccountId(), msg.getCurrentTimestamp(),
					GlobalArgs.SYNC_STATE_RECEIVED);

			short num = badgeNumService.queryMessageNum(msg.getToAccountId());
			badgeNumService.modifyMessageNum(msg.getToAccountId(), --num);

			short channelBadgeNum = messageService.countCacheBadgeNum(msg.getChatId(), msg.getToAccountId());
			messageService
					.updateCacheBadgeNum(msg.getChatId(), msg.getToAccountId(), channelBadgeNum, currentTimestamp);
		} else {
			// The messsage couldn't be written out completely for
			// some
			// reason.
			// (e.g. Connection is closed)
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

	private InlinecastMessageReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(InlinecastMessageAdapter.class);

}
