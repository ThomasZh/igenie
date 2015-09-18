package com.oct.ga.invite.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.invite.ConfirmReceivedInviteReq;
import com.oct.ga.comm.cmd.invite.ConfirmReceivedInviteResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.invite.domain.GaInviteMasterInfo;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaInviteService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class InviteConfirmReceivedAdapter
		extends StpReqCommand
{
	public InviteConfirmReceivedAdapter()
	{
		super();

		this.setTag(Command.INVITE_CONFIRM_RECEIVED_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ConfirmReceivedInviteReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String[] inviteIds = reqCmd.getInviteIds();
		String[] inviteFeedbackIds = reqCmd.getInviteFeedbackIds();

		try {
			GaInviteService inviteService = (GaInviteService) context.getBean("gaInviteService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

			String email = accountService.queryLoginName(this.getMyAccountId(), GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL);

			short count = 0;
			if (inviteIds != null)
				for (String inviteId : inviteIds) {
					GaInviteMasterInfo invite = inviteService.queryMaster(inviteId);
					switch (invite.getInviteType()) {
					case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL:
					case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL_AND_JOIN_ACTIVITY:
						inviteService.confirmReceivedInvite(inviteId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, email,
								currentTimestamp);
						break;
					case GlobalArgs.INVITE_TYPE_FOLLOW_ME:
					case GlobalArgs.INVITE_TYPE_JOIN_ACTIVITY:
					case GlobalArgs.INVITE_TYPE_REGISTER_AND_FOLLOW_BY_KEY:
					case GlobalArgs.INVITE_TYPE_REGISTER_AND_JOIN_ACTIVITY_BY_KEY:
					default:
						inviteService.confirmReceivedInvite(inviteId, this.getMyAccountId(), currentTimestamp);
						break;
					}

					count++;
				}

			if (inviteFeedbackIds != null)
				for (String inviteId : inviteFeedbackIds) {
					GaInviteMasterInfo invite = inviteService.queryMaster(inviteId);
					String inviteAccountId = invite.getFromAccountId();
					String feedbackAccountId = null; // TODO whom?
					inviteService.confirmReceivedFeedback(inviteId, feedbackAccountId, inviteAccountId,
							currentTimestamp);
					count++;
				}

			short num = badgeNumService.queryInviteNum(this.getMyAccountId());
			num -= count;
			badgeNumService.modifyInviteNum(this.getMyAccountId(), num);

			ConfirmReceivedInviteResp respCmd = new ConfirmReceivedInviteResp(ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ConfirmReceivedInviteResp respCmd = new ConfirmReceivedInviteResp(ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ConfirmReceivedInviteReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(InviteConfirmReceivedAdapter.class);

}
