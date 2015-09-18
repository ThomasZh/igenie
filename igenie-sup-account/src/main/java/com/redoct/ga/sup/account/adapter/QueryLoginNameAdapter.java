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
import com.redoct.ga.sup.account.cmd.QueryLoginNameReq;
import com.redoct.ga.sup.account.cmd.QueryLoginNameResp;

public class QueryLoginNameAdapter
		extends SupReqCommand
{
	public QueryLoginNameAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_LOGIN_NAME_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryLoginNameReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = reqCmd.getAccountId();
		short loginType = reqCmd.getLoginType();
		QueryLoginNameResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			String loginName = accountService.queryLoginName(accountId, loginType);

			respCmd = new QueryLoginNameResp(this.getSequence(), ErrorCode.SUCCESS, loginName);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryLoginNameResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryLoginNameReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryLoginNameAdapter.class);

}
