package com.redoct.ga.sup.message.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.msg.MessageOriginalMulticast;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.account.cmd.QueryAllAccountBasicResp;

public class MultcastMessageReq
		extends SupReqCommand
{
	public MultcastMessageReq()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_MESSAGE_REQ);
	}

	public MultcastMessageReq(long sequence, MessageOriginalMulticast msg)
	{
		this();

		this.setSequence(sequence);
		this.setMsg(msg);
	}

	@Override
	public MultcastMessageReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			msg = gson.fromJson(json, MessageOriginalMulticast.class);
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		Gson gson = new Gson();
		String json = gson.toJson(msg);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private MessageOriginalMulticast msg;

	public MessageOriginalMulticast getMsg()
	{
		return msg;
	}

	public void setMsg(MessageOriginalMulticast msg)
	{
		this.msg = msg;
	}

	private final static Logger logger = LoggerFactory.getLogger(MultcastMessageReq.class);

}
