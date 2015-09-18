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
import com.oct.ga.comm.cmd.account.BindPhoneReq;
import com.oct.ga.comm.cmd.account.BindPhoneResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.domain.VerificationCode;

public class BindPhoneAdapter
		extends StpReqCommand
{
	public BindPhoneAdapter()
	{
		super();

		this.setTag(Command.BIND_PHONE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new BindPhoneReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		short verificationType = GlobalArgs.VERIFICATION_TYPE_BIND_PHONE;
		String phone = reqCmd.getPhone();
		String md5pwd = reqCmd.getMd5pwd();
		String verificationCode = reqCmd.getVerificationCode();
		BindPhoneResp respCmd = null;

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");

			String deviceId = (String) session.getAttribute("deviceId");

			VerificationCode code = supAccountService.queryVerificationCode(verificationType, deviceId);
			if (code != null) {
				if (verificationCode.equals(code.getEkey())) {
					if (code.getTtl() < this.getCurrentTimestamp()) {
						logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
								+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
								+ ErrorCode.VERIFICATON_CODE_TIMEOUT + "]|This verificationCode(" + verificationCode
								+ ") is time out.");

						respCmd = new BindPhoneResp(sequence, ErrorCode.VERIFICATON_CODE_TIMEOUT);
						return respCmd;
					} else {
						if (supAccountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone)) {
							AccountBasic oldAccount = supAccountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE,
									phone);
							String oldAccountId = oldAccount.getAccountId();
							
							String email = supAccountService.queryLoginName(oldAccountId,
									GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL);
							if (email != null && email.length() > 0) {
								logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
										+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
										+ ErrorCode.PHONE_ALREADY_BIND + "]|This phone(" + phone
										+ ") already bind email(" + email + ")");

								respCmd = new BindPhoneResp(sequence, ErrorCode.PHONE_ALREADY_BIND);
								return respCmd;
							}
							
							String wechat = supAccountService.queryLoginName(oldAccountId,
									GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT);
							if (wechat != null && wechat.length() > 0) {
								logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
										+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
										+ ErrorCode.PHONE_ALREADY_BIND + "]|This phone(" + phone
										+ ") already bind wechat(" + wechat + ")");

								respCmd = new BindPhoneResp(sequence, ErrorCode.PHONE_ALREADY_BIND);
								return respCmd;
							}

							logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
									+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
									+ ErrorCode.REGISTER_PHONE_EXIST + "]|This phone(" + phone + ") already exist.");

							respCmd = new BindPhoneResp(sequence, ErrorCode.REGISTER_PHONE_EXIST);
							return respCmd;
						} else { // this login(phone) not exist
							String accountId = this.getMyAccountId();
							supAccountService.createLogin(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone,
									currentTimestamp);
							supAccountService.resetPwd(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone, md5pwd,
									currentTimestamp);

							logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=["
									+ accountId + "]|commandTag=[" + this.getTag() + "]| bind phone success)");

							respCmd = new BindPhoneResp(sequence, ErrorCode.SUCCESS);
							return respCmd;
						}
					}
				} else {
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
							+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
							+ ErrorCode.NOT_MATCH_VERIFICATION_CODE + "]|This verificationCode(" + verificationCode
							+ ") not match to (" + code.getEkey() + ")");

					respCmd = new BindPhoneResp(this.getSequence(), ErrorCode.NOT_MATCH_VERIFICATION_CODE);
					return respCmd;
				}
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[]|commandTag=["
						+ this.getTag() + "]|ErrorCode=[" + ErrorCode.NOT_MATCH_VERIFICATION_CODE
						+ "]|No this verificationCode(" + verificationCode + ") for apply bind phone");

				respCmd = new BindPhoneResp(this.getSequence(), ErrorCode.NOT_MATCH_VERIFICATION_CODE);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new BindPhoneResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private BindPhoneReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(BindPhoneAdapter.class);

}
