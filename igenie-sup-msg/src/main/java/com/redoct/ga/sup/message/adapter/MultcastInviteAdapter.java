package com.redoct.ga.sup.message.adapter;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.invite.domain.GaInviteBaseInfo;
import com.oct.ga.invite.domain.GaInviteMasterInfo;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaInviteService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.service.InlinecastMessageServiceIf;
import com.redoct.ga.sup.SupCommandParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.mail.SupMailService;
import com.redoct.ga.sup.message.cmd.MultcastInviteReq;
import com.redoct.ga.sup.message.cmd.MultcastInviteResp;
import com.redoct.ga.sup.message.cmd.MultcastMessageResp;

public class MultcastInviteAdapter
		extends SupReqCommand
{
	public MultcastInviteAdapter()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_INVITE_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new MultcastInviteReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		MultcastInviteResp respCmd = null;
		GaInvite gaInvite = reqCmd.getInvite();
		String inviteId = null;
		String channelId = gaInvite.getChannelId();
		short inviteType = gaInvite.getInviteType();
		String fromAccountId = gaInvite.getFromAccountId();
		String fromAccountName = gaInvite.getFromAccountName();
		String toUserSemiId = gaInvite.getToUserSemiId();
		int currentTimestamp = DatetimeUtil.currentTimestamp();
		int expiry = currentTimestamp + 604800;// 1 week
		gaInvite.setExpiry(expiry);

		// LOGIC: Send a invite message(accept/reject) to inviter.
		try {
			GaInviteService inviteService = (GaInviteService) context.getBean("gaInviteService");
			SupMailService supMailroom = (SupMailService) context.getBean("supMailroom");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			InlinecastMessageServiceIf inlinecastMessageService = (InlinecastMessageServiceIf) context
					.getBean("inlinecastMessageService");

			try {
				GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

				String activityId = taskService.modifyExerciseProject2Completed(fromAccountId,
						GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_2, currentTimestamp);

				if (activityId != null) {
					GaTaskLog log = new GaTaskLog();
					log.setLogId(UUID.randomUUID().toString());
					log.setChannelId(activityId);
					log.setFromAccountId(fromAccountId);
					log.setActionTag(GlobalArgs.TASK_ACTION_COMPLETED);
					log.setToActionId(activityId);
					taskService.addLog(log, currentTimestamp);

					// demo task completed
					taskService.addLogExtend(log.getLogId(), fromAccountId, activityId,
							GlobalArgs.TASK_ACTION_COMPLETED, GlobalArgs.SYNC_STATE_READ, currentTimestamp);
				}
			} catch (Exception e) {
				logger.warn("accountId=[" + fromAccountId + "]|add task log error: " + LogErrorMessage.getFullInfo(e));
			}

			String toLoginName = null;
			String toAccountId = null;
			if (toUserSemiId != null) {
				switch (inviteType) {
				case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL:
				case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL_AND_JOIN_ACTIVITY:
					toLoginName = toUserSemiId.toLowerCase();
					if (accountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, toLoginName)) {
						AccountBasic account = accountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL,
								toLoginName);
						toAccountId = account.getAccountId();
					}
					break;
				case GlobalArgs.INVITE_TYPE_FOLLOW_ME:
				case GlobalArgs.INVITE_TYPE_JOIN_ACTIVITY:
					toAccountId = toUserSemiId;
					break;
				case GlobalArgs.INVITE_TYPE_REGISTER_AND_FOLLOW_BY_KEY:
				case GlobalArgs.INVITE_TYPE_REGISTER_AND_JOIN_ACTIVITY_BY_KEY:
				default:
					break;
				}
			}

			switch (inviteType) {
			case GlobalArgs.INVITE_TYPE_REGISTER_AND_FOLLOW_BY_KEY: { // noLoginName
				// Logic: add invite original
				GaInviteBaseInfo invite = new GaInviteBaseInfo();
				invite.setInviteType(inviteType);
				invite.setChannelId(GlobalArgs.ID_DEFAULT_NONE);
				invite.setFromAccountId(fromAccountId);
				invite.setExpiry(expiry);

				inviteId = inviteService.create(invite, currentTimestamp);

				gaInvite.setInviteId(inviteId);
				gaInvite.setChannelId(GlobalArgs.ID_DEFAULT_NONE);

				respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.SUCCESS, gaInvite);
				return respCmd;
			}
			case GlobalArgs.INVITE_TYPE_REGISTER_AND_JOIN_ACTIVITY_BY_KEY: { // noLoginName
				// Logic: add invite original
				GaInviteBaseInfo invite = new GaInviteBaseInfo();
				invite.setInviteType(inviteType);
				invite.setChannelId(channelId);
				invite.setFromAccountId(fromAccountId);
				invite.setExpiry(expiry);

				inviteId = inviteService.create(invite, currentTimestamp);
				gaInvite.setInviteId(inviteId);

				respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.SUCCESS, gaInvite);
				return respCmd;
			}
			case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL: { // loginName
				if (accountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, toLoginName)) {
					GaInviteMasterInfo invite = inviteService.queryMaster(GlobalArgs.INVITE_TYPE_FOLLOW_ME,
							fromAccountId, toAccountId);
					inviteId = invite.getInviteId();

					if (inviteId != null && inviteId.length() > 0) { // exist
						inviteService.modifyExpiryTime(inviteId, expiry, currentTimestamp);
						inviteService.modifySyncStateToNotReceived(inviteId, toAccountId, currentTimestamp);
					} else { // not exist
						invite = new GaInviteMasterInfo();
						invite.setInviteType(GlobalArgs.INVITE_TYPE_FOLLOW_ME);
						invite.setChannelId(GlobalArgs.ID_DEFAULT_NONE);
						invite.setFromAccountId(fromAccountId);
						invite.setExpiry(expiry);

						inviteId = inviteService.create(invite, currentTimestamp);
						gaInvite.setInviteId(inviteId);
						inviteService.subscribeInvite(inviteId, toAccountId, currentTimestamp);
					}
				} else {
					String toName = toUserSemiId.split("@")[0];
					String toEmail = toLoginName;
					String fromEmail = accountService.queryLoginName(fromAccountId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL);

					GaInviteMasterInfo invite = inviteService.queryMaster(GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL,
							fromAccountId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, toLoginName);
					inviteId = invite.getInviteId();

					if (inviteId != null && inviteId.length() > 0) { // exist
						inviteService.modifyExpiryTime(inviteId, expiry, currentTimestamp);
						inviteService.modifySyncStateToNotReceived(inviteId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL,
								toLoginName, currentTimestamp);
					} else { // not exist
						invite = new GaInviteMasterInfo();
						invite.setInviteType(GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL);
						invite.setChannelId(GlobalArgs.ID_DEFAULT_NONE);
						invite.setFromAccountId(fromAccountId);
						invite.setExpiry(expiry);

						inviteId = inviteService.create(invite, currentTimestamp);
						gaInvite.setInviteId(inviteId);
						inviteService.subscribeInvite(inviteId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, toLoginName,
								currentTimestamp);

						IoSession session = this.getIoSession();
						respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.SUCCESS, gaInvite);
						TlvObject tResp = null;
						try {
							tResp = SupCommandParser.encode(respCmd);
						} catch (Exception e) {
							logger.error("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
									+ ErrorCode.ENCODING_FAILURE + "]" + LogErrorMessage.getFullInfo(e));
							respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.ENCODING_FAILURE, gaInvite);
							return respCmd;
						}

						WriteFuture future = session.write(tResp);
						// Wait until the message is completely written out to the
						// O/S buffer.
						future.awaitUninterruptibly();
						if (!future.isWritten()) {
							// The messsage couldn't be written out completely for
							// some reason. (e.g. Connection is closed)
							logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
									+ ErrorCode.CONNECTION_CLOSED
									+ "]|couldn't be written out resp completely for some reason.(e.g. Connection is closed)");

							session.close(true);
							return null;
						}
						
						// Logic: send email
						try {
							supMailroom.sendFriendInvite(fromEmail, fromAccountName, toEmail, toName, inviteId);
						} catch (Exception e) {
							logger.warn("send invite email error: " + LogErrorMessage.getFullInfo(e));
						}
						
						return null;
					}
				}
				
				respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.SUCCESS, gaInvite);
				return respCmd;
			}
			case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL_AND_JOIN_ACTIVITY: { // loginName
				GaInviteMasterInfo invite = inviteService.queryMaster(inviteType, fromAccountId,
						GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, toLoginName);
				inviteId = invite.getInviteId();

				if (inviteId != null && inviteId.length() > 0) { // exist
					inviteService.modifyExpiryTime(inviteId, expiry, currentTimestamp);
					inviteService.modifySyncStateToNotReceived(inviteId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL,
							toLoginName, currentTimestamp);
				} else { // not exist
					invite = new GaInviteMasterInfo();
					invite.setInviteType(inviteType);
					invite.setChannelId(channelId);
					invite.setFromAccountId(fromAccountId);
					invite.setExpiry(expiry);

					inviteId = inviteService.create(invite, currentTimestamp);
					gaInvite.setInviteId(inviteId);
					inviteService.subscribeInvite(inviteId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, toLoginName,
							currentTimestamp);
				}
				
				respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.SUCCESS, gaInvite);
				return respCmd;
			}
			case GlobalArgs.INVITE_TYPE_FOLLOW_ME: { // accountId
				GaInviteMasterInfo invite = inviteService.queryMaster(inviteType, fromAccountId, toAccountId);
				inviteId = invite.getInviteId();

				if (inviteId != null && inviteId.length() > 0) { // exist
					inviteService.modifyExpiryTime(inviteId, expiry, currentTimestamp);
					inviteService.modifySyncStateToNotReceived(inviteId, toAccountId, currentTimestamp);
				} else { // not exist
					invite = new GaInviteMasterInfo();
					invite.setInviteType(inviteType);
					invite.setChannelId(channelId);
					invite.setFromAccountId(fromAccountId);
					invite.setExpiry(expiry);

					inviteId = inviteService.create(invite, currentTimestamp);
					gaInvite.setInviteId(inviteId);
					inviteService.subscribeInvite(inviteId, toAccountId, currentTimestamp);
				}

				short num = badgeNumService.queryInviteNum(toAccountId);
				badgeNumService.modifyInviteNum(toAccountId, ++num);

				gaInvite.setInviteId(inviteId);
				gaInvite.setTimestamp(currentTimestamp);
				gaInvite.setToUserSemiId(toUserSemiId);

				IoSession session = this.getIoSession();
				respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.SUCCESS, gaInvite);
				TlvObject tResp = null;
				try {
					tResp = SupCommandParser.encode(respCmd);
				} catch (Exception e) {
					logger.error("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
							+ ErrorCode.ENCODING_FAILURE + "]" + LogErrorMessage.getFullInfo(e));
					respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.ENCODING_FAILURE, gaInvite);
					return respCmd;
				}

				WriteFuture future = session.write(tResp);
				// Wait until the message is completely written out to the
				// O/S buffer.
				future.awaitUninterruptibly();
				if (!future.isWritten()) {
					// The messsage couldn't be written out completely for
					// some reason. (e.g. Connection is closed)
					logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
							+ ErrorCode.CONNECTION_CLOSED
							+ "]|couldn't be written out resp completely for some reason.(e.g. Connection is closed)");

					session.close(true);
				}
				
				try {
					inlinecastMessageService.multicast(context, gaInvite);
				} catch (Exception e) {
					logger.warn("send invite message error: " + LogErrorMessage.getFullInfo(e));
				}
				
				return null;
			}
			case GlobalArgs.INVITE_TYPE_JOIN_ACTIVITY: { // acountId
				GaInviteMasterInfo invite = inviteService.queryMaster(inviteType, fromAccountId, toAccountId);
				inviteId = invite.getInviteId();

				if (inviteId != null && inviteId.length() > 0) { // exist
					inviteService.modifyExpiryTime(inviteId, expiry, currentTimestamp);
					inviteService.modifySyncStateToNotReceived(inviteId, toAccountId, currentTimestamp);
				} else { // not exist
					invite = new GaInviteMasterInfo();
					invite.setInviteType(inviteType);
					invite.setChannelId(channelId);
					invite.setFromAccountId(fromAccountId);
					invite.setExpiry(expiry);

					inviteId = inviteService.create(invite, currentTimestamp);
					gaInvite.setInviteId(inviteId);
					inviteService.subscribeInvite(inviteId, toAccountId, currentTimestamp);

					short num = badgeNumService.queryInviteNum(toAccountId);
					badgeNumService.modifyInviteNum(toAccountId, ++num);
				}

				gaInvite.setInviteId(inviteId);
				gaInvite.setChannelType(GlobalArgs.CHANNEL_TYPE_ACTIVITY);
				String channelName = groupService.queryGroupName(channelId);
				gaInvite.setChannelName(channelName);
				gaInvite.setTimestamp(currentTimestamp);
				gaInvite.setToUserSemiId(toUserSemiId);

				IoSession session = this.getIoSession();
				respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.SUCCESS, gaInvite);
				TlvObject tResp = null;
				try {
					tResp = SupCommandParser.encode(respCmd);
				} catch (Exception e) {
					logger.error("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
							+ ErrorCode.ENCODING_FAILURE + "]" + LogErrorMessage.getFullInfo(e));
					respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.ENCODING_FAILURE, gaInvite);
					return respCmd;
				}

				WriteFuture future = session.write(tResp);
				// Wait until the message is completely written out to the
				// O/S buffer.
				future.awaitUninterruptibly();
				if (!future.isWritten()) {
					// The messsage couldn't be written out completely for
					// some reason. (e.g. Connection is closed)
					logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
							+ ErrorCode.CONNECTION_CLOSED
							+ "]|couldn't be written out resp completely for some reason.(e.g. Connection is closed)");

					session.close(true);
				}
				
				try {
					inlinecastMessageService.multicast(context, gaInvite);
				} catch (Exception e) {
					logger.warn("send invite message error: " + LogErrorMessage.getFullInfo(e));
				}
				
				return null;
			}
			default:
				break;
			}

			respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.SUCCESS, gaInvite);
			return respCmd;
		} catch (Exception e) {
			logger.error("accountId=[" + fromAccountId + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new MultcastInviteResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE, gaInvite);
			return respCmd;
		}
	}

	private MultcastInviteReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MultcastInviteAdapter.class);

}
