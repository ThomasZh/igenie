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
import com.redoct.ga.sup.account.cmd.QueryVerificationCodeReq;
import com.redoct.ga.sup.account.cmd.QueryVerificationCodeResp;
import com.redoct.ga.sup.account.domain.VerificationCode;

public class QueryVerificationCodeAdapter
		extends SupReqCommand
{
	public QueryVerificationCodeAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_VERIFICATION_CODE_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryVerificationCodeReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		short verificationType = reqCmd.getVerificationType();
		String deviceId = reqCmd.getDeviceId();
		QueryVerificationCodeResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			VerificationCode code = accountService.queryVerificationCode(verificationType, deviceId);

			respCmd = new QueryVerificationCodeResp(this.getSequence(), ErrorCode.SUCCESS, code);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryVerificationCodeResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryVerificationCodeReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryVerificationCodeAdapter.class);

}
