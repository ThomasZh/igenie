package com.oct.ga.comm.cmd.invite;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class InviteResp
		extends RespCommand
{
	public InviteResp()
	{
		this.setTag(Command.INVITE_RESP);
	}

	public InviteResp(String inviteId, int expiry)
	{
		this();

		this.inviteId = inviteId;
		this.expiry = expiry;
	}

	public InviteResp(short respState, String inviteId, int expiry)
	{
		this(inviteId, expiry);

		this.setRespState(respState);
	}

	public InviteResp(int sequence, short respState, String inviteId, int expiry)
	{
		this(respState, inviteId, expiry);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tInviteId = new TlvObject(i++, inviteId);
		TlvObject tExpiry = new TlvObject(i++, 4, TlvByteUtil.int2Byte(expiry));

		TlvObject tlv = new TlvObject(this.getTag());

		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tInviteId);
		tlv.push(tExpiry);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public InviteResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tInviteId = tlv.getChild(i++);
		inviteId = new String(tInviteId.getValue(), "UTF-8");
		logger.debug("inviteId: " + inviteId);

		TlvObject tExpiry = tlv.getChild(i++);
		expiry = TlvByteUtil.byte2Int(tExpiry.getValue());
		logger.debug("expiry: " + expiry);

		return this;
	}

	private String inviteId;
	private int expiry;

	public int getExpiry()
	{
		return expiry;
	}

	public void setExpiry(int expiry)
	{
		this.expiry = expiry;
	}

	public String getInviteId()
	{
		return inviteId;
	}

	public void setInviteId(String inviteId)
	{
		this.inviteId = inviteId;
	}

	private final static Logger logger = LoggerFactory.getLogger(InviteResp.class);

}
