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
import com.redoct.ga.sup.account.cmd.ApplyVerificationCodeReq;
import com.redoct.ga.sup.account.cmd.ApplyVerificationCodeResp;

public class ApplyVerificationCodeAdapter
		extends SupReqCommand
{
	public ApplyVerificationCodeAdapter()
	{
		super();

		this.setTag(SupCommandTag.APPLY_VERIFICATION_CODE_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ApplyVerificationCodeReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		short type = reqCmd.getVerificationType();
		String deviceId = reqCmd.getDeviceId();
		String phone = reqCmd.getPhone();
		ApplyVerificationCodeResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			String ekey = accountService.applyVerificationCode(type, deviceId, phone, this.getCurrentTimestamp());

			respCmd = new ApplyVerificationCodeResp(this.getSequence(), ErrorCode.SUCCESS, ekey);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ApplyVerificationCodeResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ApplyVerificationCodeReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ApplyVerificationCodeAdapter.class);

}
