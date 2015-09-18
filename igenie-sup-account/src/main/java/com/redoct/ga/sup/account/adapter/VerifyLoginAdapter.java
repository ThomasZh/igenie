package com.redoct.ga.sup.account.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.cmd.VerifyLoginReq;
import com.redoct.ga.sup.account.cmd.VerifyLoginResp;

public class VerifyLoginAdapter
		extends SupReqCommand
{
	public VerifyLoginAdapter()
	{
		super();

		this.setTag(SupCommandTag.VERIFY_LOGIN_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new VerifyLoginReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		short loginType = reqCmd.getLoginType();
		String loginName = reqCmd.getLoginName();
		String md5pwd = reqCmd.getMd5pwd();

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			String accountId = accountService.verifyLogin(loginType, loginName, md5pwd);
			if (accountId != null) {
				logger.info("loginName=[" + loginName + "] & pwd pair are correct, accountId=[" + accountId + "]");

				VerifyLoginResp respCmd = new VerifyLoginResp(this.getSequence(), ErrorCode.SUCCESS, accountId);
				return respCmd;
			} else {
				logger.warn("loginName=[" + loginName + "] & pwd pair are wrong!");

				VerifyLoginResp respCmd = new VerifyLoginResp(this.getSequence(), ErrorCode.LOGIN_WRONG_PWD);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			VerifyLoginResp respCmd = new VerifyLoginResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private VerifyLoginReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(VerifyLoginAdapter.class);

}
