package com.redoct.ga.sup.message.adapter;

import java.io.UnsupportedEncodingException;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.invite.domain.GaFeedbackBaseInfo;
import com.oct.ga.invite.domain.GaInviteMasterInfo;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaInviteService;
import com.oct.ga.service.InlinecastMessageServiceIf;
import com.redoct.ga.sup.SupCommandParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.message.cmd.MultcastInviteFeedbackReq;
import com.redoct.ga.sup.message.cmd.MultcastInviteFeedbackResp;

public class MultcastInviteFeedbackAdapter
		extends SupReqCommand
{
	public MultcastInviteFeedbackAdapter()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_INVITE_FEEDBACK_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new MultcastInviteFeedbackReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		MultcastInviteFeedbackResp respCmd = null;
		String inviteId = reqCmd.getInviteId();
		short feedbackState = reqCmd.getFeedbackState();
		String fromAccountId = reqCmd.getFromAccountId();
		String fromAccountName = reqCmd.getFromAccountName();
		String fromAccountAvatarUrl = reqCmd.getFromAccountAvatarUrl();
		int currentTimestamp = DatetimeUtil.currentTimestamp();

		// LOGIC: Send a invite message(accept/reject) to inviter.
		try {
			GaInviteService inviteService = (GaInviteService) context.getBean("gaInviteService");
			GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			InlinecastMessageServiceIf inlinecastMessageService = (InlinecastMessageServiceIf) context
					.getBean("inlinecastMessageService");

			GaInviteMasterInfo invite = inviteService.queryMaster(inviteId);
			if (invite.getInviteId() != null && invite.getInviteId().length() > 0) { // exist
				// not expiry
				if (invite.getExpiry() > currentTimestamp) {
					String inviteAccountId = invite.getFromAccountId();
					String feedbackAccountId = fromAccountId;
					String email = accountService.queryLoginName(fromAccountId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL);

					GaFeedbackBaseInfo feedback = new GaFeedbackBaseInfo();
					feedback.setInviteId(inviteId);
					feedback.setInviteAccountId(inviteAccountId);
					feedback.setFeedbackAccountId(feedbackAccountId);
					feedback.setFeedbackState(feedbackState);

					inviteService.feedback(feedback, currentTimestamp);

					switch (invite.getInviteType()) {
					case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL:
					case GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL_AND_JOIN_ACTIVITY:
						inviteService.confirmReadInvite(inviteId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, email,
								currentTimestamp);
						break;
					case GlobalArgs.INVITE_TYPE_REGISTER_AND_FOLLOW_BY_KEY:
					case GlobalArgs.INVITE_TYPE_REGISTER_AND_JOIN_ACTIVITY_BY_KEY:
					case GlobalArgs.INVITE_TYPE_FOLLOW_ME:
					case GlobalArgs.INVITE_TYPE_JOIN_ACTIVITY:
						inviteService.confirmReadInvite(inviteId, feedbackAccountId, currentTimestamp);
						break;
					default:
						// TODO add feedback user into task/activity/...
					}

					IoSession session = this.getIoSession();
					respCmd = new MultcastInviteFeedbackResp(this.getSequence(), ErrorCode.SUCCESS, inviteId);
					TlvObject tResp = null;
					try {
						tResp = SupCommandParser.encode(respCmd);
					} catch (Exception e) {
						logger.error("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag()
								+ "]|ErrorCode=[" + ErrorCode.ENCODING_FAILURE + "]" + LogErrorMessage.getFullInfo(e));
						respCmd = new MultcastInviteFeedbackResp(this.getSequence(), ErrorCode.ENCODING_FAILURE,
								inviteId);
						return respCmd;
					}

					WriteFuture future = session.write(tResp);
					// Wait until the message is completely written out to the
					// O/S buffer.
					future.awaitUninterruptibly();
					if (!future.isWritten()) {
						// The messsage couldn't be written out completely for
						// some reason. (e.g. Connection is closed)
						logger.warn("sessionId=["
								+ session.getId()
								+ "]|commandTag=["
								+ this.getTag()
								+ "]|ErrorCode=["
								+ ErrorCode.CONNECTION_CLOSED
								+ "]|couldn't be written out resp completely for some reason.(e.g. Connection is closed)");

						session.close(true);
					}

					if (feedbackState == GlobalArgs.INVITE_STATE_ACCPET) {
						logger.info("commandTag=[" + this.getTag() + "]|accept invite=[" + inviteId + "]");

						// Logic: follow to each other.
						followingService.follow(inviteAccountId, feedbackAccountId, currentTimestamp);
						followingService.follow(feedbackAccountId, inviteAccountId, currentTimestamp);

						logger.info("commandTag=[" + this.getTag() + "]|follow to each=[" + feedbackAccountId
								+ "] other=[" + inviteAccountId + "]");
					} else if (feedbackState == GlobalArgs.INVITE_STATE_REJECT) {
						logger.info("commandTag=[" + this.getTag() + "]|reject invite=[" + inviteId + "]");
					}

					GaInviteFeedback gaFeedback = new GaInviteFeedback();
					gaFeedback.setInviteId(inviteId);
					gaFeedback.setInviteType(invite.getInviteType());
					gaFeedback.setFromUserId(inviteAccountId);
					gaFeedback.setFeedbackUserId(feedbackAccountId);
					gaFeedback.setFeedbackUserName(fromAccountName);
					gaFeedback.setFeedbackUserAvatarUrl(fromAccountAvatarUrl);
					gaFeedback.setFeedbackState(feedbackState);
					gaFeedback.setFeedbackChannelType(GlobalArgs.CHANNEL_TYPE_ACTIVITY);
					gaFeedback.setFeedbackChannelId(invite.getChannelId());
					String groupName = groupService.queryGroupName(invite.getChannelId());
					gaFeedback.setFeedbackChannelName(groupName);
					gaFeedback.setTimestamp(currentTimestamp);

					try {
						inlinecastMessageService.multicast(context, gaFeedback);
					} catch (Exception e) {
						logger.warn("send invite message error: " + LogErrorMessage.getFullInfo(e));
					}

					short num = badgeNumService.queryInviteNum(inviteAccountId);
					badgeNumService.modifyInviteNum(inviteAccountId, --num);

					return null;
				} else { // expiry
					respCmd = new MultcastInviteFeedbackResp(this.getSequence(), ErrorCode.INVITE_EXPIRY_TIME, inviteId);
					return respCmd;
				}
			} else { // not exist
				respCmd = new MultcastInviteFeedbackResp(this.getSequence(), ErrorCode.INVITE_NOT_EXIST, inviteId);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new MultcastInviteFeedbackResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE, inviteId);
			return respCmd;
		}
	}

	private MultcastInviteFeedbackReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MultcastInviteFeedbackAdapter.class);

}
