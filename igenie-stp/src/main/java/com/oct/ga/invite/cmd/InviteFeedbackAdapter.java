package com.oct.ga.invite.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.invite.InviteFeedbackReq;
import com.oct.ga.comm.cmd.invite.InviteFeedbackResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.message.SupMessageService;

public class InviteFeedbackAdapter
		extends StpReqCommand
{
	public InviteFeedbackAdapter()
	{
		super();

		this.setTag(Command.INVITE_FEEDBACK_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new InviteFeedbackReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		InviteFeedbackResp respCmd = null;
		String inviteId = reqCmd.getInviteId();
		short feedbackState = reqCmd.getFeedbackState();
		String fromAccountId = this.getMyAccountId();
		String fromAccountName = (String) session.getAttribute("accountName");
		String fromAccountAvatarUrl = (String) session.getAttribute("avatarUrl");

		try {
			SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

			supMessageService.sendInivteFeedback(inviteId, feedbackState,fromAccountId,fromAccountName,fromAccountAvatarUrl, currentTimestamp);

			logger.debug("send multcast invite from=[" + this.getMyAccountId() + "]");
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new InviteFeedbackResp(ErrorCode.UNKNOWN_FAILURE, inviteId);
			respCmd.setSequence(sequence);
			return respCmd;
		}

		respCmd = new InviteFeedbackResp(ErrorCode.SUCCESS, inviteId);
		respCmd.setSequence(sequence);
		return respCmd;
	}

	private InviteFeedbackReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(InviteFeedbackAdapter.class);

}
