package com.redoct.ga.sup.sms.adapter;

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
import com.redoct.ga.sup.sms.SupSmsService;
import com.redoct.ga.sup.sms.cmd.SendVerificationCodeReq;
import com.redoct.ga.sup.sms.cmd.SendVerificationCodeResp;

public class SendVerificationCodeAdapter
		extends SupReqCommand
{
	public SendVerificationCodeAdapter()
	{
		super();

		this.setTag(SupCommandTag.SEND_SMS_VERIFICATION_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SendVerificationCodeReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String phone = reqCmd.getPhone();
		String ekey = reqCmd.getEkey();
		String lang = reqCmd.getLang();
		SendVerificationCodeResp respCmd = null;

		try {
			SupSmsService smsService = (SupSmsService) context.getBean("supSmsService");

			smsService.sendVerificationCode(phone, ekey, lang);

			respCmd = new SendVerificationCodeResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new SendVerificationCodeResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private SendVerificationCodeReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SendVerificationCodeAdapter.class);

}
