package com.redoct.ga.sup.account.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.account.AccountCacheManager;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.cmd.VerifyLoginExistReq;
import com.redoct.ga.sup.account.cmd.VerifyLoginExistResp;

public class VerifyLoginExistAdapter
		extends SupReqCommand
{
	public VerifyLoginExistAdapter()
	{
		super();

		this.setTag(SupCommandTag.VERIFY_LOGIN_EXIST_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new VerifyLoginExistReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		short loginType = reqCmd.getLoginType();
		String loginName = reqCmd.getLoginName();
		VerifyLoginExistResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			AccountCacheManager accountCacheManager = GenericSingleton.getInstance(AccountCacheManager.class);

			String accountId = accountCacheManager.getAccountId(loginType, loginName);
			if (accountId == null) {
				if (accountService.verifyExist(loginType, loginName)) {
					respCmd = new VerifyLoginExistResp(this.getSequence(), ErrorCode.SUCCESS);
					return respCmd;
				} else {
					respCmd = new VerifyLoginExistResp(this.getSequence(), ErrorCode.LOGIN_NOT_EXIST);
					return respCmd;
				}
			} else {
				respCmd = new VerifyLoginExistResp(this.getSequence(), ErrorCode.SUCCESS);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new VerifyLoginExistResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private VerifyLoginExistReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(VerifyLoginExistAdapter.class);

}
