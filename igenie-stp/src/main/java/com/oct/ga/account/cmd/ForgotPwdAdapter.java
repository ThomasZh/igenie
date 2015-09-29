package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.account.ForgotPasswordReq;
import com.oct.ga.comm.cmd.account.ForgotPasswordResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.mail.SupMailService;

public class ForgotPwdAdapter extends StpReqCommand {
	public ForgotPwdAdapter() {
		super();

		this.setTag(Command.FORGOT_PASSWORD_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv) throws UnsupportedEncodingException {
		reqCmd = new ForgotPasswordReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context) throws Exception {
		String loginName = reqCmd.getEmail();

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");
			SupMailService supMailroom = (SupMailService) context.getBean("supMailroom");

			if (supAccountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, loginName)) {
				logger.info("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]| forgot password");

				String ekey = supAccountService.createEkey(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, loginName,
						currentTimestamp);

				AccountBasic account = supAccountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, loginName);
				logger.debug("store ekey for user(" + account.getAccountId() + ") success!");

				// send email to myself.
				supMailroom.sendForgotPwd(loginName, account.getNickname(), ekey);

				logger.info("Send password recovery email to " + loginName + " success!");

				ForgotPasswordResp respCmd = new ForgotPasswordResp(ErrorCode.SUCCESS);
				respCmd.setSequence(sequence);
				return respCmd;
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.MEMBER_NOT_GA_ACCOUNT + "]| not ga account");

				ForgotPasswordResp respCmd = new ForgotPasswordResp(ErrorCode.MEMBER_NOT_GA_ACCOUNT);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ForgotPasswordResp respCmd = new ForgotPasswordResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ForgotPasswordReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ForgotPwdAdapter.class);

}
