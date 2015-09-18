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
import com.redoct.ga.sup.account.cmd.QueryLostPwdEkeyInfoReq;
import com.redoct.ga.sup.account.cmd.QueryLostPwdEkeyInfoResp;
import com.redoct.ga.sup.account.domain.LostPwdEkey;

public class QueryLostPwdEkeyInfoAdapter
		extends SupReqCommand
{
	public QueryLostPwdEkeyInfoAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_LOST_PWD_EKEY_INFO_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryLostPwdEkeyInfoReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String ekey = reqCmd.getEkey();
		QueryLostPwdEkeyInfoResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			LostPwdEkey ekeyInfo = accountService.queryEkey(ekey);

			respCmd = new QueryLostPwdEkeyInfoResp(this.getSequence(), ErrorCode.SUCCESS, ekeyInfo);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryLostPwdEkeyInfoResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryLostPwdEkeyInfoReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryLostPwdEkeyInfoAdapter.class);

}
