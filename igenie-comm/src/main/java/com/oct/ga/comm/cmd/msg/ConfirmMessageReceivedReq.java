package com.oct.ga.comm.cmd.msg;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ConfirmMessageReceivedReq
		extends ReqCommand
{
	public ConfirmMessageReceivedReq()
	{
		super();

		this.setTag(Command.CONFIRM_MESSAGE_RECEIVED_REQ);
	}

	@Override
	public ConfirmMessageReceivedReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.CONFIRM_MESSAGE_RECEIVED_REQ + ", child=1) to command");

		TlvParser.decodeChildren(tlv, 1);

		TlvObject tMessageId = tlv.getChild(0);
		messageId = new String(tMessageId.getValue(), "UTF-8");
		logger.debug("messageId: " + messageId);

		return this;
	}

	private String messageId;

	public String getMessageId()
	{
		return messageId;
	}

	public void setMessageId(String messageId)
	{
		this.messageId = messageId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ConfirmMessageReceivedReq.class);

}
