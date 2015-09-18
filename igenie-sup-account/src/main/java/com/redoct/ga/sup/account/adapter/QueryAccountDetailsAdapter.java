package com.redoct.ga.sup.account.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.account.AccountDetail;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.cmd.QueryAccountDetailsReq;
import com.redoct.ga.sup.account.cmd.QueryAccountDetailsResp;

public class QueryAccountDetailsAdapter
		extends SupReqCommand
{
	public QueryAccountDetailsAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_ACCOUNTS_DETAIL_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryAccountDetailsReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		List<String> ids = reqCmd.getIds();
		QueryAccountDetailsResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			//AccountCacheManager accountCacheManager = GenericSingleton.getInstance(AccountCacheManager.class);

			List<AccountDetail> accounts = new ArrayList<AccountDetail>();
			for (String id : ids) {
				AccountMaster account = accountService.queryAccountMaster(id);

				AccountDetail accountDetail = new AccountDetail();
				accountDetail.setAccountId(account.getAccountId());
				accountDetail.setNickname(account.getNickname());
				accountDetail.setAvatarUrl(account.getAvatarUrl());
				accountDetail.setDesc(account.getDesc());
				accountDetail.setEmail(account.getEmail());
				accountDetail.setPhone(account.getPhone());
				accountDetail.setState(GlobalArgs.USER_FOLLOWING);

				accounts.add(accountDetail);
			}

			respCmd = new QueryAccountDetailsResp(this.getSequence(), ErrorCode.SUCCESS, accounts);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryAccountDetailsResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private QueryAccountDetailsReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryAccountDetailsAdapter.class);

}
