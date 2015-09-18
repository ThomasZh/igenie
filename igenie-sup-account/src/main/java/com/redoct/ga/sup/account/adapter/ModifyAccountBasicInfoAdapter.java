package com.redoct.ga.sup.account.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.account.AccountCacheManager;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.cmd.ModifyAccountBasicInfoReq;
import com.redoct.ga.sup.account.cmd.ModifyAccountBasicInfoResp;

public class ModifyAccountBasicInfoAdapter
		extends SupReqCommand
{
	public ModifyAccountBasicInfoAdapter()
	{
		super();

		this.setTag(SupCommandTag.MODIFY_ACCOUNT_INFO_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ModifyAccountBasicInfoReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		AccountBasic account = reqCmd.getAccount();
		ModifyAccountBasicInfoResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			AccountCacheManager accountCacheManager = GenericSingleton.getInstance(AccountCacheManager.class);

			// update in db
			accountService.modifyAccountBasicInfo(account, this.getCurrentTimestamp());

			// update in memcached
			account = accountService.queryAccount(account.getAccountId());
			logger.debug("accountId: " + account.getAccountId());
			logger.debug("nickname: " + account.getNickname());
			logger.debug("avatarUrl: " + account.getAvatarUrl());
			logger.debug("desc: " + account.getDesc());
			accountCacheManager.putAccount(account.getAccountId(), account);

			respCmd = new ModifyAccountBasicInfoResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ModifyAccountBasicInfoResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ModifyAccountBasicInfoReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ModifyAccountBasicInfoAdapter.class);

}
