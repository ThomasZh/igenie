package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.account.QueryForgotPwdEmailReq;
import com.oct.ga.comm.cmd.account.QueryForgotPwdEmailResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.domain.LostPwdEkey;

public class QueryForgotPwdEmailAdapter
		extends StpReqCommand
{
	public QueryForgotPwdEmailAdapter()
	{
		super();

		this.setTag(Command.QUERY_FORGOT_PASSWORD_EMAIL_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryForgotPwdEmailReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		QueryForgotPwdEmailResp respCmd = null;
		String ekey = reqCmd.getEkey();
		String email = null;

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");

			LostPwdEkey ekeyInfo = supAccountService.queryEkey(ekey);
			String accountId = ekeyInfo.getAccountId();
			// exist
			if (accountId != null && accountId.length() > 0) {
				if (ekeyInfo.getTtl() < currentTimestamp) {
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
							+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
							+ "]|ErrorCode=[" + ErrorCode.RESET_PWD_EKEY_EXPIRY_TIME + "]|ekey=[" + ekey
							+ "] expiry time");

					respCmd = new QueryForgotPwdEmailResp(ErrorCode.RESET_PWD_EKEY_EXPIRY_TIME, email);
					respCmd.setSequence(sequence);
					return respCmd;
				} else {
					email = ekeyInfo.getLoginName();

					respCmd = new QueryForgotPwdEmailResp(ErrorCode.SUCCESS, email);
					respCmd.setSequence(sequence);
					return respCmd;
				}
			} else { // not exist
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ accountId + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.RESET_PWD_HAS_NO_EKEY + "]|has no ekey=[" + ekey + "]");

				respCmd = new QueryForgotPwdEmailResp(ErrorCode.RESET_PWD_HAS_NO_EKEY, email);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new QueryForgotPwdEmailResp(ErrorCode.UNKNOWN_FAILURE, email);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private QueryForgotPwdEmailReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryForgotPwdEmailAdapter.class);

}
