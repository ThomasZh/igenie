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
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoByLoginNameReq;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoByLoginNameResp;

public class QueryAccountBasicInfoByLoginNameAdapter
		extends SupReqCommand
{
	public QueryAccountBasicInfoByLoginNameAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_ACCOUNT_INFO_BY_LOGIN_NAME_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryAccountBasicInfoByLoginNameReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		short loginType = reqCmd.getLoginType();
		String loginName = reqCmd.getLoginName();
		QueryAccountBasicInfoByLoginNameResp respCmd = null;
		AccountBasic account = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			AccountCacheManager accountCacheManager = GenericSingleton.getInstance(AccountCacheManager.class);

			String accountId = accountCacheManager.getAccountId(loginType, loginName);
			if (accountId == null) {
				account = accountService.queryAccount(loginType, loginName);
				if (account == null || account.getAccountId() == null || account.getAccountId().length() == 0) {
					;
				} else {
					accountCacheManager.putLogin(loginType, loginName, account.getAccountId());
					accountCacheManager.putAccount(account.getAccountId(), account);
				}
			} else {
				account = accountCacheManager.getAccount(accountId);
				if (account == null) {
					account = accountService.queryAccount(accountId);
					accountCacheManager.putAccount(accountId, account);
				}
			}

			respCmd = new QueryAccountBasicInfoByLoginNameResp(this.getSequence(), ErrorCode.SUCCESS, account);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryAccountBasicInfoByLoginNameResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryAccountBasicInfoByLoginNameReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryAccountBasicInfoByLoginNameAdapter.class);

}
