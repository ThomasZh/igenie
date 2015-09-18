package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.account.ChangePasswordReq;
import com.oct.ga.comm.cmd.account.ChangePasswordResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

public class ChangePwdAdapter
		extends StpReqCommand
{
	public ChangePwdAdapter()
	{
		super();

		this.setTag(Command.CHANGE_PASSWORD_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ChangePasswordReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String loginName = reqCmd.getLoginName();
		String oldPassword = reqCmd.getOldPassword();
		String newPassword = reqCmd.getNewPassword();
		ChangePasswordResp respCmd = null;

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");
			SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");

			StpSession stpSession = supSessionService.queryStpSession(this.getMyAccountId());
			short loginType = stpSession.getLoginType();

			String accountId = supAccountService.verifyLogin(loginType, loginName, oldPassword);
			if (accountId != null) {
				supAccountService.resetPwd(loginType, loginName, newPassword, currentTimestamp);

				logger.debug("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|loginName(" + loginName
						+ ") change password success!");

				respCmd = new ChangePasswordResp(ErrorCode.SUCCESS);
				respCmd.setSequence(sequence);
				return respCmd;
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.NOT_MATCH_OLD_PASSWORD + "]|Email(" + loginName + ") old password not matched!");

				respCmd = new ChangePasswordResp(ErrorCode.NOT_MATCH_OLD_PASSWORD);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ChangePasswordResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ChangePasswordReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ChangePwdAdapter.class);

}
