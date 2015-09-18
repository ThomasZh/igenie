package com.oct.ga.comm.cmd.msg;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ConfirmMessageReadReq
		extends ReqCommand
{
	public ConfirmMessageReadReq()
	{
		super();

		this.setTag(Command.CONFIRM_MESSAGE_READ_REQ);
	}

	public ConfirmMessageReadReq(int sequence, String chatId)
	{
		this();

		this.setSequence(sequence);
		this.setChatId(chatId);
	}

	@Override
	public ConfirmMessageReadReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tChatId = tlv.getChild(i++);
		chatId = new String(tChatId.getValue(), "UTF-8");
		logger.debug("chatId: " + chatId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tChatId = new TlvObject(i++, chatId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tChatId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String chatId;

	public String getChatId()
	{
		return chatId;
	}

	public void setChatId(String chatId)
	{
		this.chatId = chatId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ConfirmMessageReadReq.class);

}
