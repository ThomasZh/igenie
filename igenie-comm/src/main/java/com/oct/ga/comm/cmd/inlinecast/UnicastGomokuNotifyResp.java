package com.oct.ga.comm.cmd.inlinecast;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.msg.NotifyGomoku;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class UnicastGomokuNotifyResp
		extends RespCommand
{
	public UnicastGomokuNotifyResp()
	{
		this.setTag(Command.MULTICAST_GOMOKU_NOTIFY_RESP);
	}

	public UnicastGomokuNotifyResp(NotifyGomoku notify)
	{
		this();

		this.notify = notify;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tGameId = new TlvObject(1, notify.getChannelId());
		TlvObject tPlayerId = new TlvObject(2, notify.getToAccountId());
		TlvObject tPlayerName = new TlvObject(3, notify.getToAccountName());
		TlvObject tTimestamp = new TlvObject(4, 4, TlvByteUtil.int2Byte(notify.getTimestamp()));

		TlvObject tlv = new TlvObject(Command.MULTICAST_GOMOKU_NOTIFY_RESP);
		tlv.push(tGameId);
		tlv.push(tPlayerId);
		tlv.push(tPlayerName);
		tlv.push(tTimestamp);

		logger.info("from command to tlv package:(tag=" + Command.MULTICAST_GOMOKU_NOTIFY_RESP
				+ ", child=4, length=" + tlv.getLength() + ")");

		return tlv;
	}

	private NotifyGomoku notify;

	private final static Logger logger = LoggerFactory.getLogger(UnicastGomokuNotifyResp.class);

}
