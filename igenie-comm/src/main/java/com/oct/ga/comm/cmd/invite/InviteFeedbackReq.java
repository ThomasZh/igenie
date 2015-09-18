package com.oct.ga.comm.cmd.invite;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class InviteFeedbackReq
		extends ReqCommand
{
	public InviteFeedbackReq()
	{
		super();

		this.setTag(Command.INVITE_FEEDBACK_REQ);
	}

	public InviteFeedbackReq(String inviteId, short feedbackState)
	{
		this();

		this.setInviteId(inviteId);
		this.setFeedbackState(feedbackState);
	}

	@Override
	public InviteFeedbackReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tInviteId = tlv.getChild(i++);
		inviteId = new String(tInviteId.getValue(), "UTF-8");
		logger.debug("inviteId: " + inviteId);

		TlvObject tFeedbackState = tlv.getChild(i++);
		feedbackState = TlvByteUtil.byte2Short(tFeedbackState.getValue());
		logger.debug("feedbackState: " + feedbackState);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tInviteId = new TlvObject(i++, inviteId);
		TlvObject tFeedbackState = new TlvObject(i++, 2, TlvByteUtil.short2Byte(feedbackState));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tInviteId);
		tlv.push(tFeedbackState);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String inviteId;
	private short feedbackState;

	public String getInviteId()
	{
		return inviteId;
	}

	public void setInviteId(String inviteId)
	{
		this.inviteId = inviteId;
	}

	public short getFeedbackState()
	{
		return feedbackState;
	}

	public void setFeedbackState(short feedbackState)
	{
		this.feedbackState = feedbackState;
	}

	private final static Logger logger = LoggerFactory.getLogger(InviteFeedbackReq.class);

}
