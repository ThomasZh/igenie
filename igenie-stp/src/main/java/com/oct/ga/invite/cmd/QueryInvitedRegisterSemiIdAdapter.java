package com.oct.ga.invite.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.invite.QueryInvitedRegisterSemiIdReq;
import com.oct.ga.comm.cmd.invite.QueryInvitedRegisterSemiIdResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.invite.domain.GaInviteMasterInfo;
import com.oct.ga.service.GaInviteService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class QueryInvitedRegisterSemiIdAdapter
		extends StpReqCommand
{
	public QueryInvitedRegisterSemiIdAdapter()
	{
		super();

		this.setTag(Command.INVITE_QUERY_REGISTER_SEMIID_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryInvitedRegisterSemiIdReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		QueryInvitedRegisterSemiIdResp respCmd = null;
		String inviteId = reqCmd.getInviteId();
		String toUserSemiId = null;
		short inviteType = GlobalArgs.INVITE_TYPE_REGISTER_BY_EMAIL;

		GaInviteService inviteService = (GaInviteService) context.getBean("gaInviteService");

		try {
			GaInviteMasterInfo invite = inviteService.queryMaster(inviteId);
			List<String> loginNames = inviteService.queryExternalSubscribeIds(inviteId);
			if (loginNames.size() > 0) {
				toUserSemiId = loginNames.get(0);
			}
			inviteType = invite.getInviteType();

			// exist
			if (invite.getInviteId() != null && invite.getInviteId().length() > 0) {
				respCmd = new QueryInvitedRegisterSemiIdResp(ErrorCode.SUCCESS, inviteType, toUserSemiId);
				respCmd.setSequence(sequence);
				return respCmd;
			} else { // not exist
				respCmd = new QueryInvitedRegisterSemiIdResp(ErrorCode.INVITE_NOT_EXIST, inviteType, toUserSemiId);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new QueryInvitedRegisterSemiIdResp(ErrorCode.UNKNOWN_FAILURE, inviteType, toUserSemiId);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private QueryInvitedRegisterSemiIdReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryInvitedRegisterSemiIdAdapter.class);

}
