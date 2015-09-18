package com.redoct.ga.sup.message.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupRespCommand;

public class MultcastInviteResp
		extends SupRespCommand
{
	public MultcastInviteResp()
	{
		this.setTag(SupCommandTag.MULTCAST_INVITE_RESP);
	}

	public MultcastInviteResp(GaInvite invite)
	{
		this();

		this.setInvite(invite);
	}

	public MultcastInviteResp(short respState, GaInvite invite)
	{
		this(invite);

		this.setRespState(respState);
	}

	public MultcastInviteResp(long sequence, short respState, GaInvite invite)
	{
		this(respState, invite);

		this.setSequence(sequence);
	}

	@Override
	public MultcastInviteResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));
		logger.debug("respState: " + this.getRespState());

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

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		Gson gson = new Gson();
		String json = gson.toJson(invite);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());

		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private GaInvite invite;

	public GaInvite getInvite()
	{
		return invite;
	}

	public void setInvite(GaInvite invite)
	{
		this.invite = invite;
	}

	private final static Logger logger = LoggerFactory.getLogger(MultcastInviteResp.class);

}
