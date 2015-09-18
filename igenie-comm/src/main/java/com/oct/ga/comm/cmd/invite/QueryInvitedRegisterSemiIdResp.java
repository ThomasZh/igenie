package com.oct.ga.comm.cmd.invite;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryInvitedRegisterSemiIdResp
		extends RespCommand
{
	public QueryInvitedRegisterSemiIdResp()
	{
		this.setTag(Command.INVITE_QUERY_REGISTER_SEMIID_RESP);
	}

	public QueryInvitedRegisterSemiIdResp(short respState, short inviteType, String id)
	{
		this();

		this.setRespState(respState);
		this.setInviteType(inviteType);
		this.setSemiId(id);
	}

	@Override
	public QueryInvitedRegisterSemiIdResp decode(TlvObject tlv)
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

		TlvObject tResultState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultState.getValue()));
		logger.debug("resultState: " + this.getRespState());

		TlvObject tInviteType = tlv.getChild(i++);
		inviteType = TlvByteUtil.byte2Short(tInviteType.getValue());
		logger.debug("inviteType: " + inviteType);

		TlvObject tSemiId = tlv.getChild(i++);
		semiId = new String(tSemiId.getValue(), "UTF-8");
		logger.debug("semiId: " + semiId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 1;
		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tInviteType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getInviteType()));
		TlvObject tSemiId = new TlvObject(i++, semiId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tInviteType);
		tlv.push(tSemiId);

		logger.info("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private short inviteType;
	private String semiId;

	public String getSemiId()
	{
		return semiId;
	}

	public void setSemiId(String semiId)
	{
		this.semiId = semiId;
	}

	public short getInviteType()
	{
		return inviteType;
	}

	public void setInviteType(short inviteType)
	{
		this.inviteType = inviteType;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryInvitedRegisterSemiIdResp.class);

}
