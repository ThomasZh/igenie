package com.oct.ga.comm.cmd.msg;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.msg.NotifyGomoku;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class MulticastMessageReadNotifyResp
		extends RespCommand
{
	public MulticastMessageReadNotifyResp()
	{
		this.setTag(Command.MULTICAST_MESSAGE_READ_RESP);
	}

	public MulticastMessageReadNotifyResp(NotifyGomoku notify)
	{
		this();

		this.notify = notify;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tMessageId = new TlvObject(1, notify.getChannelId());
		TlvObject tAccountId = new TlvObject(2, notify.getToAccountId());
		TlvObject tAccountName = new TlvObject(3, notify.getToAccountName());
		TlvObject tTimestamp = new TlvObject(4, 4, TlvByteUtil.int2Byte(notify.getTimestamp()));

		TlvObject tlv = new TlvObject(Command.MULTICAST_MESSAGE_READ_RESP);
		tlv.push(tMessageId);
		tlv.push(tAccountId);
		tlv.push(tAccountName);
		tlv.push(tTimestamp);

		logger.info("from command to tlv package:(tag=" + Command.MULTICAST_MESSAGE_READ_RESP + ", child=4, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private NotifyGomoku notify;

	private final static Logger logger = LoggerFactory.getLogger(MulticastMessageReadNotifyResp.class);

}
