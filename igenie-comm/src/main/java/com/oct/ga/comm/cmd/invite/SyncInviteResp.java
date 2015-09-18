package com.oct.ga.comm.cmd.invite;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncInviteResp
		extends RespCommand
{
	public SyncInviteResp()
	{
		this.setTag(Command.INVITE_SYNC_RESP);
	}

	public SyncInviteResp(short respState, List<GaInvite> inviteList, List<GaInviteFeedback> inviteFeedbackList)
	{
		this();

		this.setRespState(respState);
		this.setInviteList(inviteList);
		this.setInviteFeedbackList(inviteFeedbackList);
	}

	@Override
	public SyncInviteResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tResultState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultState.getValue()));
		logger.debug("resultState: " + this.getRespState());

		TlvObject tInvites = tlv.getChild(i++);
		String jsonInviteList = new String(tInvites.getValue(), "UTF-8");
		logger.debug("invites json: " + jsonInviteList);
		Gson gson = new Gson();
		if (jsonInviteList != null && jsonInviteList.length() > 0) {
			inviteList = gson.fromJson(jsonInviteList, new TypeToken<List<GaInvite>>()
			{
			}.getType());
		}

		TlvObject tInviteFeedbacks = tlv.getChild(i++);
		String jsonInviteFeedbacks = new String(tInviteFeedbacks.getValue(), "UTF-8");
		logger.debug("inviteFeedbacks json: " + jsonInviteFeedbacks);
		if (jsonInviteFeedbacks != null && jsonInviteFeedbacks.length() > 0) {
			inviteFeedbackList = gson.fromJson(jsonInviteFeedbacks, new TypeToken<List<GaInviteFeedback>>()
			{
			}.getType());
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));

		Gson gson = new Gson();
		TlvObject tInviteList = null;
		if (inviteList != null && inviteList.size() > 0) {
			String jsonInviteList = gson.toJson(inviteList);
			logger.debug("invites json: " + jsonInviteList);
			tInviteList = new TlvObject(i++, jsonInviteList);
		} else {
			tInviteList = new TlvObject(i++, "");
		}

		TlvObject tInviteFeedbackList = null;
		if (inviteFeedbackList != null && inviteFeedbackList.size() > 0) {
			String jsonInviteFeedbackList = gson.toJson(inviteFeedbackList);
			logger.debug("inviteFeedbacks json: " + jsonInviteFeedbackList);
			tInviteFeedbackList = new TlvObject(i++, jsonInviteFeedbackList);
		} else {
			tInviteFeedbackList = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tResultFlag);
		tlv.push(tInviteList);
		tlv.push(tInviteFeedbackList);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private List<GaInvite> inviteList;
	private List<GaInviteFeedback> inviteFeedbackList;

	public List<GaInvite> getInviteList()
	{
		return inviteList;
	}

	public void setInviteList(List<GaInvite> inviteList)
	{
		this.inviteList = inviteList;
	}

	public List<GaInviteFeedback> getInviteFeedbackList()
	{
		return inviteFeedbackList;
	}

	public void setInviteFeedbackList(List<GaInviteFeedback> inviteFeedbackList)
	{
		this.inviteFeedbackList = inviteFeedbackList;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncInviteResp.class);

}
