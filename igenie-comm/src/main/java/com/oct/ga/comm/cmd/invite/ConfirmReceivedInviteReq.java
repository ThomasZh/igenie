package com.oct.ga.comm.cmd.invite;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ConfirmReceivedInviteReq
		extends ReqCommand
{
	public ConfirmReceivedInviteReq()
	{
		super();

		this.setTag(Command.INVITE_CONFIRM_RECEIVED_REQ);
	}

	public ConfirmReceivedInviteReq(String[] inviteIds, String[] inviteFeedbackIds)
	{
		this();

		this.setInviteIds(inviteIds);
		this.setInviteFeedbackIds(inviteFeedbackIds);
	}

	@Override
	public ConfirmReceivedInviteReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tInviteIds = tlv.getChild(i++);
		String jsonInviteIds = new String(tInviteIds.getValue(), "UTF-8");
		logger.debug("json inviteIds: " + jsonInviteIds);
		Gson gson = new Gson();
		if (jsonInviteIds != null) {
			inviteIds = gson.fromJson(jsonInviteIds, String[].class);
		}

		TlvObject tInviteFeedbackIds = tlv.getChild(i++);
		String jsonInviteFeedbackIds = new String(tInviteFeedbackIds.getValue(), "UTF-8");
		logger.debug("json inviteFeedbackIds: " + jsonInviteFeedbackIds);
		if (jsonInviteFeedbackIds != null) {
			inviteFeedbackIds = gson.fromJson(jsonInviteFeedbackIds, String[].class);
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		Gson gson = new Gson();
		TlvObject tInviteIds = null;
		if (inviteIds != null && inviteIds.length > 0) {
			String jsonInviteIds = gson.toJson(inviteIds);
			tInviteIds = new TlvObject(i++, jsonInviteIds);
		} else {
			tInviteIds = new TlvObject(i++, "");
		}

		TlvObject tInviteFeedbackIds = null;
		if (inviteFeedbackIds != null && inviteFeedbackIds.length > 0) {
			String jsonInviteFeedbackIds = gson.toJson(inviteFeedbackIds);
			tInviteFeedbackIds = new TlvObject(i++, jsonInviteFeedbackIds);
		} else {
			tInviteFeedbackIds = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tInviteIds);
		tlv.push(tInviteFeedbackIds);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String[] inviteIds;
	private String[] inviteFeedbackIds;

	public String[] getInviteIds()
	{
		return inviteIds;
	}

	public void setInviteIds(String[] inviteIds)
	{
		this.inviteIds = inviteIds;
	}

	public String[] getInviteFeedbackIds()
	{
		return inviteFeedbackIds;
	}

	public void setInviteFeedbackIds(String[] inviteFeedbackIds)
	{
		this.inviteFeedbackIds = inviteFeedbackIds;
	}

	private final static Logger logger = LoggerFactory.getLogger(ConfirmReceivedInviteReq.class);

}