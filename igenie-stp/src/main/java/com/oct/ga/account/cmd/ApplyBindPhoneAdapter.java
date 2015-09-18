package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.account.ApplyBindPhoneReq;
import com.oct.ga.comm.cmd.account.ApplyBindPhoneResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.sms.SupSmsService;

public class ApplyBindPhoneAdapter
		extends StpReqCommand
{
	public ApplyBindPhoneAdapter()
	{
		super();

		this.setTag(Command.APPLY_BIND_PHONE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ApplyBindPhoneReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		short verificationType = GlobalArgs.VERIFICATION_TYPE_BIND_PHONE;
		String phone = reqCmd.getPhone();
		ApplyBindPhoneResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			SupSmsService smsbox = (SupSmsService) context.getBean("supSmsbox");

			String deviceId = (String) session.getAttribute("deviceId");

			String ekey = accountService.applyVerificationCode(verificationType, deviceId, phone,
					this.getCurrentTimestamp());
			logger.debug("ekey: " + ekey);
			if (ekey == null || ekey.length() == 0) {
				respCmd = new ApplyBindPhoneResp(this.getSequence(), ErrorCode.APPLY_VERIFICATON_CODE_TOO_MOUCH_TIMES);
				return respCmd;
			} else {
				// TODO cn/en
				String lang = "cn";
				smsbox.sendVerificationCode(phone, ekey, lang);

				respCmd = new ApplyBindPhoneResp(this.getSequence(), ErrorCode.SUCCESS);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ApplyBindPhoneResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ApplyBindPhoneReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ApplyBindPhoneAdapter.class);

}
