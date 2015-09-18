package com.oct.ga.comm.cmd.inlinecast;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class InlinecastInviteReq
		extends ReqCommand
{
	public InlinecastInviteReq()
	{
		this.setTag(Command.INLINECAST_INVITE_REQ);
	}

	public InlinecastInviteReq(long ioSessionId, GaInvite invite)
	{
		this();

		this.setIoSessionId(ioSessionId);
		this.setInvite(invite);
	}

	@Override
	public InlinecastInviteReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tIoSessionId = tlv.getChild(i++);
		ioSessionId = TlvByteUtil.byte2Long(tIoSessionId.getValue());
		logger.debug("ioSessionId: " + ioSessionId);

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			invite = gson.fromJson(json, GaInvite.class);
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tIoSessionId = new TlvObject(i++, 8, TlvByteUtil.long2Byte(ioSessionId));
		Gson gson = new Gson();
		String json = gson.toJson(invite);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tIoSessionId);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private long ioSessionId;
	private GaInvite invite;

	public long getIoSessionId()
	{
		return ioSessionId;
	}

	public void setIoSessionId(long ioSessionId)
	{
		this.ioSessionId = ioSessionId;
	}

	public GaInvite getInvite()
	{
		return invite;
	}

	public void setInvite(GaInvite invite)
	{
		this.invite = invite;
	}

	private final static Logger logger = LoggerFactory.getLogger(InlinecastInviteReq.class);

}
