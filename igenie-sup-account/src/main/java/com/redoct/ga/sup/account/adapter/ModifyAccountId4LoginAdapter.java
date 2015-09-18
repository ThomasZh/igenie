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
import com.redoct.ga.sup.account.cmd.ModifyAccountId4LoginReq;
import com.redoct.ga.sup.account.cmd.ModifyAccountId4LoginResp;

public class ModifyAccountId4LoginAdapter
		extends SupReqCommand
{
	public ModifyAccountId4LoginAdapter()
	{
		super();

		this.setTag(SupCommandTag.MODIFY_ACCOUNT_LOGIN_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ModifyAccountId4LoginReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = reqCmd.getAccountId();
		short loginType = reqCmd.getLoginType();
		String loginName = reqCmd.getLoginName();
		ModifyAccountId4LoginResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			AccountCacheManager accountCacheManager = GenericSingleton.getInstance(AccountCacheManager.class);

			String oldAccountId = accountCacheManager.getAccountId(loginType, loginName);
			accountCacheManager.removeAccount(oldAccountId);
			accountCacheManager.removeLogin(loginType, loginName);

			accountService.modifyAccountId4Login(accountId, loginType, loginName, this.getCurrentTimestamp());

			respCmd = new ModifyAccountId4LoginResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ModifyAccountId4LoginResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ModifyAccountId4LoginReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ModifyAccountId4LoginAdapter.class);

}
