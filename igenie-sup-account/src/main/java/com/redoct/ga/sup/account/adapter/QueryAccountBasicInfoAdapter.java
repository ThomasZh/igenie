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
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoReq;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoResp;

public class QueryAccountBasicInfoAdapter
		extends SupReqCommand
{
	public QueryAccountBasicInfoAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_ACCOUNT_INFO_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryAccountBasicInfoReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = reqCmd.getAccountId();
		QueryAccountBasicInfoResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			AccountCacheManager accountCacheManager = GenericSingleton.getInstance(AccountCacheManager.class);

			AccountBasic account = accountCacheManager.getAccount(accountId);
			if (account == null) {
				account = accountService.queryAccount(accountId);
				accountCacheManager.putAccount(accountId, account);
			}

			respCmd = new QueryAccountBasicInfoResp(this.getSequence(), ErrorCode.SUCCESS, account);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryAccountBasicInfoResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryAccountBasicInfoReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryAccountBasicInfoAdapter.class);

}
