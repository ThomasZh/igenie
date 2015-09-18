package com.oct.ga.following.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.following.FollowingReq;
import com.oct.ga.comm.cmd.following.FollowingResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class FollowingAdpter
		extends StpReqCommand
{
	public FollowingAdpter()
	{
		super();

		this.setTag(Command.FOLLOWING_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new FollowingReq().decode(tlv);
		sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String friendRegisterId = reqCmd.getFriendRegisterId();
		String myUserId = this.getMyAccountId();
		AccountMaster account = new AccountMaster();

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");

			AccountBasic baseAccount = accountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, friendRegisterId);
			if (baseAccount.getAccountId() != null && baseAccount.getAccountId().length() > 0) {
				if (followingService.isExist(myUserId, account.getAccountId())) {
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
							+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
							+ "]|ErrorCode=[" + ErrorCode.CONTACT_ALREADY_EXIST + "]|this friend register id=["
							+ friendRegisterId + "] already exist in user=[" + myUserId + "] contact list");

					FollowingResp respCmd = new FollowingResp(ErrorCode.CONTACT_ALREADY_EXIST, account);
					respCmd.setSequence(sequence);
					return respCmd;
				}

				followingService.follow(myUserId, account.getAccountId(), currentTimestamp);

				FollowingResp respCmd = new FollowingResp(ErrorCode.SUCCESS, account);
				respCmd.setSequence(sequence);
				return respCmd;
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.MEMBER_NOT_GA_ACCOUNT + "]|this friend register id=[" + friendRegisterId
						+ "] is not GA account");

				FollowingResp respCmd = new FollowingResp(ErrorCode.MEMBER_NOT_GA_ACCOUNT, account);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			FollowingResp respCmd = new FollowingResp(ErrorCode.UNKNOWN_FAILURE, account);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private FollowingReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(FollowingAdpter.class);

}
