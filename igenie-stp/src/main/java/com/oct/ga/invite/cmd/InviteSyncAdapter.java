package com.oct.ga.invite.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.invite.SyncInviteReq;
import com.oct.ga.comm.cmd.invite.SyncInviteResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.invite.domain.GaFeedbackMasterInfo;
import com.oct.ga.invite.domain.GaInviteMasterInfo;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaInviteService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class InviteSyncAdapter
		extends StpReqCommand
{
	public InviteSyncAdapter()
	{
		super();

		this.setTag(Command.INVITE_SYNC_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncInviteReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String inviteId = reqCmd.getInviteId();
		logger.debug("inviteId: " + inviteId);
		String myAccountId = this.getMyAccountId();

		try {
			GaInviteService inviteService = (GaInviteService) context.getBean("gaInviteService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			// got invite code from anyway
			if (inviteId != null && inviteId.length() > 0) {
				GaInviteMasterInfo invite = inviteService.queryMaster(inviteId);
				String fromAccountId = invite.getFromAccountId();
				logger.debug("fromAccountId: " + fromAccountId);

				switch (invite.getInviteType()) {
				case GlobalArgs.INVITE_TYPE_REGISTER_AND_FOLLOW_BY_KEY:
				case GlobalArgs.INVITE_TYPE_REGISTER_AND_JOIN_ACTIVITY_BY_KEY:
					if (invite.getExpiry() > currentTimestamp) { // notExpiry
						if (!myAccountId.equals(invite.getFromAccountId())) {
							if (!inviteService.isExistSubscribeInvite(inviteId, myAccountId)) {
								inviteService.subscribeInvite(inviteId, myAccountId, currentTimestamp);
							} else {
								logger.warn("invite(" + inviteId + "," + myAccountId + ") already exist");
							}
						} else {
							logger.warn("invite yourself: " + myAccountId);
						}
					} else {
						logger.warn("invite expiry: " + invite.getExpiry());
					}
					break;
				}
			}

			List<GaInvite> inviteList = new ArrayList<GaInvite>();

			List<GaInviteMasterInfo> accountInvites = inviteService.queryNotReceivedInvite(this.getMyAccountId());
			for (GaInviteMasterInfo accountInvite : accountInvites) {
				String fromAccountId = accountInvite.getFromAccountId();
				logger.debug("fromAccountId: " + fromAccountId);
				AccountBasic fromAccount = accountService.queryAccount(fromAccountId);
				if (fromAccount != null && fromAccount.getAccountId() != null) {
					GaInvite gaInvite = new GaInvite();
					gaInvite.setInviteId(accountInvite.getInviteId());
					gaInvite.setInviteType(accountInvite.getInviteType());

					gaInvite.setFromAccountId(fromAccountId);
					gaInvite.setFromAccountName(fromAccount.getNickname());
					gaInvite.setFromAccountAvatarUrl(fromAccount.getAvatarUrl());

					gaInvite.setChannelId(accountInvite.getChannelId());
					String groupName = groupService.queryGroupName(accountInvite.getChannelId());
					gaInvite.setChannelName(groupName);

					switch (accountInvite.getInviteType()) {
					case GlobalArgs.INVITE_TYPE_FOLLOW_ME:
					case GlobalArgs.INVITE_TYPE_REGISTER_AND_FOLLOW_BY_KEY:
					case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL:
						gaInvite.setChannelType(GlobalArgs.CHANNEL_TYPE_NONE);
						break;
					case GlobalArgs.INVITE_TYPE_JOIN_ACTIVITY:
					case GlobalArgs.INVITE_TYPE_REGISTER_AND_JOIN_ACTIVITY_BY_KEY:
					case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL_AND_JOIN_ACTIVITY:
						gaInvite.setChannelType(GlobalArgs.CHANNEL_TYPE_ACTIVITY);
						break;
					}

					gaInvite.setExpiry(accountInvite.getExpiry());
					gaInvite.setTimestamp(accountInvite.getTimestamp());

					inviteList.add(gaInvite);
				} else {
					logger.warn("can't got fromAccount(invite) info");
				}
			}

			// TODO only email login
			String email = accountService.queryLoginName(this.getMyAccountId(), GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL);
			List<GaInviteMasterInfo> userInvites = inviteService.queryNotReceivedInvite(
					GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, email);
			for (GaInviteMasterInfo userInvite : userInvites) {
				AccountBasic fromAccount = accountService.queryAccount(userInvite.getFromAccountId());

				GaInvite gaInvite = new GaInvite();
				gaInvite.setInviteId(userInvite.getInviteId());
				gaInvite.setInviteType(userInvite.getInviteType());

				gaInvite.setFromAccountId(userInvite.getFromAccountId());
				gaInvite.setFromAccountName(fromAccount.getNickname());
				gaInvite.setFromAccountAvatarUrl(fromAccount.getAvatarUrl());

				gaInvite.setChannelId(userInvite.getChannelId());
				String groupName = groupService.queryGroupName(userInvite.getChannelId());
				gaInvite.setChannelName(groupName);

				switch (userInvite.getInviteType()) {
				case GlobalArgs.INVITE_TYPE_FOLLOW_ME:
				case GlobalArgs.INVITE_TYPE_REGISTER_AND_FOLLOW_BY_KEY:
				case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL:
					gaInvite.setChannelType(GlobalArgs.CHANNEL_TYPE_NONE);
					break;
				case GlobalArgs.INVITE_TYPE_JOIN_ACTIVITY:
				case GlobalArgs.INVITE_TYPE_REGISTER_AND_JOIN_ACTIVITY_BY_KEY:
				case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL_AND_JOIN_ACTIVITY:
					gaInvite.setChannelType(GlobalArgs.CHANNEL_TYPE_ACTIVITY);
					break;
				}

				gaInvite.setExpiry(userInvite.getExpiry());
				gaInvite.setTimestamp(userInvite.getTimestamp());

				inviteList.add(gaInvite);
			}

			List<GaInviteFeedback> inviteFeedbackList = new ArrayList<GaInviteFeedback>();
			List<GaFeedbackMasterInfo> feedbackList = inviteService.queryNotReceivedFeedback(this.getMyAccountId());
			for (GaFeedbackMasterInfo feedback : feedbackList) {
				GaInviteFeedback gaInviteFeedback = new GaInviteFeedback();

				gaInviteFeedback.setInviteId(feedback.getInviteId());
				gaInviteFeedback.setFeedbackState(feedback.getFeedbackState());

				AccountBasic toAccount = accountService.queryAccount(feedback.getFeedbackAccountId());
				gaInviteFeedback.setFeedbackUserId(feedback.getFeedbackAccountId());
				gaInviteFeedback.setFeedbackUserName(toAccount.getNickname());
				gaInviteFeedback.setFeedbackUserAvatarUrl(toAccount.getAvatarUrl());

				GaInviteMasterInfo gaInvite = inviteService.queryMaster(feedback.getInviteId());
				gaInviteFeedback.setInviteType(gaInvite.getInviteType());
				gaInviteFeedback.setFeedbackChannelId(gaInvite.getChannelId());

				switch (gaInvite.getInviteType()) {
				case GlobalArgs.INVITE_TYPE_FOLLOW_ME:
				case GlobalArgs.INVITE_TYPE_REGISTER_AND_FOLLOW_BY_KEY:
				case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL:
					gaInviteFeedback.setFeedbackChannelType(GlobalArgs.CHANNEL_TYPE_NONE);
					break;
				case GlobalArgs.INVITE_TYPE_JOIN_ACTIVITY:
				case GlobalArgs.INVITE_TYPE_REGISTER_AND_JOIN_ACTIVITY_BY_KEY:
				case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL_AND_JOIN_ACTIVITY:
					gaInviteFeedback.setFeedbackChannelType(GlobalArgs.CHANNEL_TYPE_ACTIVITY);
					break;
				}

				String groupName = groupService.queryGroupName(gaInvite.getChannelId());
				gaInviteFeedback.setFeedbackChannelName(groupName);
				gaInviteFeedback.setTimestamp(feedback.getTimestamp());

				inviteFeedbackList.add(gaInviteFeedback);
			}

			SyncInviteResp respCmd = new SyncInviteResp(ErrorCode.SUCCESS, inviteList, inviteFeedbackList);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncInviteResp respCmd = new SyncInviteResp(ErrorCode.UNKNOWN_FAILURE, null, null);
			return respCmd;
		}
	}

	private SyncInviteReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(InviteSyncAdapter.class);

}
