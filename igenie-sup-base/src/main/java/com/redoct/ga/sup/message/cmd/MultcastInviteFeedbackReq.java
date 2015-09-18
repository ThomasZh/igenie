package com.redoct.ga.sup.message.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class MultcastInviteFeedbackReq
		extends SupReqCommand
{
	public MultcastInviteFeedbackReq()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_INVITE_FEEDBACK_REQ);
	}

	public MultcastInviteFeedbackReq(String inviteId, short feedbackState, String fromAccountId,
			String fromAccountName, String fromAccountAvatarUrl)
	{
		this();

		this.setInviteId(inviteId);
		this.setFeedbackState(feedbackState);
		this.setFromAccountId(fromAccountId);
		this.setFromAccountName(fromAccountName);
		this.setFromAccountAvatarUrl(fromAccountAvatarUrl);
	}

	@Override
	public MultcastInviteFeedbackReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 6;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tInviteId = tlv.getChild(i++);
		inviteId = new String(tInviteId.getValue(), "UTF-8");
		logger.debug("inviteId: " + inviteId);

		TlvObject tFeedbackState = tlv.getChild(i++);
		feedbackState = TlvByteUtil.byte2Short(tFeedbackState.getValue());
		logger.debug("feedbackState: " + feedbackState);

		TlvObject tFromAccountId = tlv.getChild(i++);
		fromAccountId = new String(tFromAccountId.getValue(), "UTF-8");
		logger.debug("fromAccountId: " + fromAccountId);

		TlvObject tFromAccountName = tlv.getChild(i++);
		fromAccountName = new String(tFromAccountName.getValue(), "UTF-8");
		logger.debug("fromAccountName: " + fromAccountName);

		TlvObject tFromAccountAvatarUrl = tlv.getChild(i++);
		fromAccountAvatarUrl = new String(tFromAccountAvatarUrl.getValue(), "UTF-8");
		logger.debug("fromAccountAvatarUrl: " + fromAccountAvatarUrl);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tInviteId = new TlvObject(i++, inviteId);
		TlvObject tFeedbackState = new TlvObject(i++, 2, TlvByteUtil.short2Byte(feedbackState));
		TlvObject tFromAccountId = new TlvObject(i++, fromAccountId);
		TlvObject tFromAccountName = new TlvObject(i++, fromAccountName);
		TlvObject tFromAccountAvatarUrl = new TlvObject(i++, fromAccountAvatarUrl);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tInviteId);
		tlv.push(tFeedbackState);
		tlv.push(tFromAccountId);
		tlv.push(tFromAccountName);
		tlv.push(tFromAccountAvatarUrl);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String inviteId;
	private short feedbackState;
	private String fromAccountId;
	private String fromAccountName;
	private String fromAccountAvatarUrl;

	public String getFromAccountId()
	{
		return fromAccountId;
	}

	public void setFromAccountId(String fromAccountId)
	{
		this.fromAccountId = fromAccountId;
	}

	public String getFromAccountName()
	{
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName)
	{
		this.fromAccountName = fromAccountName;
	}

	public String getFromAccountAvatarUrl()
	{
		return fromAccountAvatarUrl;
	}

	public void setFromAccountAvatarUrl(String fromAccountAvatarUrl)
	{
		this.fromAccountAvatarUrl = fromAccountAvatarUrl;
	}

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

	private final static Logger logger = LoggerFactory.getLogger(MultcastInviteFeedbackReq.class);

}
