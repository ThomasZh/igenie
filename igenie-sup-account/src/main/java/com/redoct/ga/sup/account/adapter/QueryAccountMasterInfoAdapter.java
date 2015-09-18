package com.redoct.ga.sup.account.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.cmd.QueryAccountMasterInfoReq;
import com.redoct.ga.sup.account.cmd.QueryAccountMasterInfoResp;

public class QueryAccountMasterInfoAdapter
		extends SupReqCommand
{
	public QueryAccountMasterInfoAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_ACCOUNT_MASTER_INFO_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryAccountMasterInfoReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = reqCmd.getAccountId();
		QueryAccountMasterInfoResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			AccountMaster account = accountService.queryAccountMaster(accountId);

			respCmd = new QueryAccountMasterInfoResp(this.getSequence(), ErrorCode.SUCCESS, account);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryAccountMasterInfoResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryAccountMasterInfoReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryAccountMasterInfoAdapter.class);

}
