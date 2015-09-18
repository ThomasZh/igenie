package com.oct.ga.invite.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.invite.InviteReq;
import com.oct.ga.comm.cmd.invite.InviteResp;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.message.SupMessageService;

public class InviteAdapter
		extends StpReqCommand
{
	public InviteAdapter()
	{
		super();

		this.setTag(Command.INVITE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new InviteReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		InviteResp respCmd = null;
		String inviteId = null;
		String channelId = reqCmd.getChannelId();
		short inviteType = reqCmd.getInviteType();
		String fromAccountId = this.getMyAccountId();
		String fromAccountName = (String) session.getAttribute("accountName");
		String fromAccountAvatarUrl = (String) session.getAttribute("avatarUrl");
		String toUserSemiId = reqCmd.getToUserSemiId();
		int expiry = currentTimestamp + 604800;// 1 week

		try {
			SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

			GaInvite gaInvite = new GaInvite();
			gaInvite.setInviteType(inviteType);
			gaInvite.setChannelId(channelId);
			gaInvite.setFromAccountId(fromAccountId);
			gaInvite.setFromAccountName(fromAccountName);
			gaInvite.setFromAccountAvatarUrl(fromAccountAvatarUrl);
			gaInvite.setToUserSemiId(toUserSemiId);

			gaInvite = supMessageService.sendInivte(gaInvite, currentTimestamp);
			inviteId = gaInvite.getInviteId();

			logger.debug("send multcast invite=[" + inviteId + "] from=[" + this.getMyAccountId()
					+ "] to toUserSemiId=[" + toUserSemiId + "]");
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new InviteResp(ErrorCode.UNKNOWN_FAILURE, inviteId, expiry);
			respCmd.setSequence(sequence);
			return respCmd;
		}

		respCmd = new InviteResp(ErrorCode.SUCCESS, inviteId, expiry);
		respCmd.setSequence(sequence);
		return respCmd;
	}

	private InviteReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(InviteAdapter.class);

}
