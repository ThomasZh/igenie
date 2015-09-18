package com.oct.ga.comm.cmd.msg;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class UploadMessageResp
		extends RespCommand
{
	public UploadMessageResp()
	{
		this.setTag(Command.UPLOAD_MESSAGE_RESP);
	}

	public UploadMessageResp(short respState, String messageId, int timestamp)
	{
		this();

		this.setRespState(respState);
		this.setMessageId(messageId);
		this.setTimestamp(timestamp);
	}

	@Override
	public UploadMessageResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tRespState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tRespState.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tMessageId = tlv.getChild(i++);
		messageId = new String(tMessageId.getValue(), "UTF-8");
		logger.debug("messageId: " + messageId);

		TlvObject tTimestamp = tlv.getChild(i++);
		timestamp = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("timestamp: " + timestamp);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tRespState = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tMessageId = new TlvObject(i++, this.getMessageId());
		TlvObject tTimestamp = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getTimestamp()));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tRespState);
		tlv.push(tMessageId);
		tlv.push(tTimestamp);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String messageId;
	private int timestamp;

	public String getMessageId()
	{
		return messageId;
	}

	public void setMessageId(String messageId)
	{
		this.messageId = messageId;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	private final static Logger logger = LoggerFactory.getLogger(UploadMessageResp.class);

}
