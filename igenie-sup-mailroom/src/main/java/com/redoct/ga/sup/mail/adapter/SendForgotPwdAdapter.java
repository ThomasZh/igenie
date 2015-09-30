package com.redoct.ga.sup.mail.adapter;

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
import com.redoct.ga.sup.mail.SupMailService;
import com.redoct.ga.sup.mail.cmd.SendForgotPwdEmailReq;
import com.redoct.ga.sup.mail.cmd.SendForgotPwdEmailResp;

public class SendForgotPwdAdapter
		extends SupReqCommand
{
	public SendForgotPwdAdapter()
	{
		super();

		this.setTag(SupCommandTag.SEND_FORGOT_PWD_EMAIL_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SendForgotPwdEmailReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String toEmail = reqCmd.getToEmail();
		String toName = reqCmd.getToName();
		String ekey = reqCmd.getEkey();
		SendForgotPwdEmailResp respCmd = null;

		try {
			SupMailService mailService = (SupMailService) context.getBean("supMailService");

			mailService.sendForgotPwd(toEmail, toName, ekey);

			respCmd = new SendForgotPwdEmailResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new SendForgotPwdEmailResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private SendForgotPwdEmailReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SendForgotPwdAdapter.class);

}
