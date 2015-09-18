package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.account.ResetPwdReq;
import com.oct.ga.comm.cmd.account.ResetPwdResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.domain.LostPwdEkey;

public class ResetPwdAdapter
		extends StpReqCommand
{
	public ResetPwdAdapter()
	{
		super();

		this.setTag(Command.RESET_PASSWORD_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ResetPwdReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String email = reqCmd.getEmail().toLowerCase();
		String ekey = reqCmd.getEkey();
		String md5pwd = reqCmd.getNewPassword();
		String accountId = null;
		ResetPwdResp respCmd = null;

		logger.debug("md5pwd: " + md5pwd);

		try {
			// result for changePassword
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");

			LostPwdEkey ekeyInfo = supAccountService.queryEkey(ekey);
			if (ekeyInfo.getTtl() < currentTimestamp) {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ accountId + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.RESET_PWD_EKEY_EXPIRY_TIME + "]|ekey=[" + ekey + "] expiry time");

				respCmd = new ResetPwdResp(ErrorCode.RESET_PWD_EKEY_EXPIRY_TIME);
				respCmd.setSequence(sequence);
				return respCmd;
			} else {
				supAccountService.resetPwd(ekeyInfo.getLoginType(), ekeyInfo.getLoginName(), md5pwd, currentTimestamp);

				respCmd = new ResetPwdResp(ErrorCode.SUCCESS);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ accountId + "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ResetPwdResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ResetPwdReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ResetPwdAdapter.class);

}
