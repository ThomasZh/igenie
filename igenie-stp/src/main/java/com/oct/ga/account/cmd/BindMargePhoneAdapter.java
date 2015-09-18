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
import com.oct.ga.comm.cmd.account.BindMargePhoneReq;
import com.oct.ga.comm.cmd.account.BindMargePhoneResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.domain.VerificationCode;
import com.redoct.ga.sup.session.SupSessionService;

public class BindMargePhoneAdapter
		extends StpReqCommand
{
	public BindMargePhoneAdapter()
	{
		super();

		this.setTag(Command.BIND_MARGE_PHONE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new BindMargePhoneReq().decode(tlv);
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
		BindMargePhoneResp respCmd = null;
		String accountId = this.getMyAccountId();

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");
			SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");

			String deviceId = (String) session.getAttribute("deviceId");

			VerificationCode code = supAccountService.queryVerificationCode(verificationType, deviceId);
			if (code != null) {
				if (verificationCode.equals(code.getEkey())) {
					if (code.getTtl() < this.getCurrentTimestamp()) {
						logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
								+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
								+ ErrorCode.VERIFICATON_CODE_TIMEOUT + "]|This verificationCode(" + verificationCode
								+ ") is time out.");

						respCmd = new BindMargePhoneResp(sequence, ErrorCode.VERIFICATON_CODE_TIMEOUT);
						return respCmd;
					} else {
						if (supAccountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone)) {
							logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
									+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
									+ ErrorCode.REGISTER_PHONE_EXIST + "]|This phone(" + phone + ") already exist.");

							// clean old account session
							AccountBasic oldAccount = supAccountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE,
									phone);
							String oldAccountId = oldAccount.getAccountId();
							supSessionService.removeStpSession(oldAccountId);
							
							supAccountService.modifyAccountId4Login(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_PHONE,
									phone, this.getCurrentTimestamp());

							respCmd = new BindMargePhoneResp(sequence, ErrorCode.SUCCESS);
							return respCmd;
						} else { // this login(phone) not exist
							supAccountService.createLogin(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone,
									currentTimestamp);
							supAccountService.resetPwd(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone, md5pwd,
									currentTimestamp);

							logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=["
									+ accountId + "]|commandTag=[" + this.getTag() + "]| bind phone success)");

							respCmd = new BindMargePhoneResp(sequence, ErrorCode.SUCCESS);
							return respCmd;
						}
					}
				} else {
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
							+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
							+ ErrorCode.NOT_MATCH_VERIFICATION_CODE + "]|This verificationCode(" + verificationCode
							+ ") not match to (" + code.getEkey() + ")");

					respCmd = new BindMargePhoneResp(this.getSequence(), ErrorCode.NOT_MATCH_VERIFICATION_CODE);
					return respCmd;
				}
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[]|commandTag=["
						+ this.getTag() + "]|ErrorCode=[" + ErrorCode.NOT_MATCH_VERIFICATION_CODE
						+ "]|No this verificationCode(" + verificationCode + ") for apply bind phone");

				respCmd = new BindMargePhoneResp(this.getSequence(), ErrorCode.NOT_MATCH_VERIFICATION_CODE);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new BindMargePhoneResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private BindMargePhoneReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(BindMargePhoneAdapter.class);

}
