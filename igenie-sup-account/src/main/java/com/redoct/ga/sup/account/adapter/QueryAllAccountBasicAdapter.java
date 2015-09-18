package com.redoct.ga.sup.account.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.cmd.QueryAllAccountBasicReq;
import com.redoct.ga.sup.account.cmd.QueryAllAccountBasicResp;

public class QueryAllAccountBasicAdapter
		extends SupReqCommand
{
	public QueryAllAccountBasicAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_ALL_ACCOUNT_BASIC_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryAllAccountBasicReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		QueryAllAccountBasicResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			List<AccountBasic> array = accountService.queryAllAccountBasic();

			respCmd = new QueryAllAccountBasicResp(this.getSequence(), ErrorCode.SUCCESS, array);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryAllAccountBasicResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryAllAccountBasicReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryAllAccountBasicAdapter.class);

}
