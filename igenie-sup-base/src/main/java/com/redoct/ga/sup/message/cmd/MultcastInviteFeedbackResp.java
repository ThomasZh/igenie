package com.redoct.ga.sup.message.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupRespCommand;

public class MultcastInviteFeedbackResp
		extends SupRespCommand
{
	public MultcastInviteFeedbackResp()
	{
		this.setTag(SupCommandTag.MULTCAST_INVITE_FEEDBACK_RESP);
	}

	public MultcastInviteFeedbackResp(String inviteId)
	{
		this();

		this.setInviteId(inviteId);
	}

	public MultcastInviteFeedbackResp(short respState, String inviteId)
	{
		this(inviteId);

		this.setRespState(respState);
	}

	public MultcastInviteFeedbackResp(long sequence, short respState, String inviteId)
	{
		this(respState, inviteId);

		this.setSequence(sequence);
	}

	@Override
	public MultcastInviteFeedbackResp decode(TlvObject tlv)
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

		TlvObject tResultState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultState.getValue()));
		logger.debug("resultState: " + this.getRespState());

		TlvObject tInviteId = tlv.getChild(i++);
		inviteId = new String(tInviteId.getValue(), "UTF-8");
		logger.debug("inviteId: " + inviteId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tInviteId = new TlvObject(i++, inviteId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tInviteId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String inviteId;

	public String getInviteId()
	{
		return inviteId;
	}

	public void setInviteId(String inviteId)
	{
		this.inviteId = inviteId;
	}

	private final static Logger logger = LoggerFactory.getLogger(MultcastInviteFeedbackResp.class);

}
