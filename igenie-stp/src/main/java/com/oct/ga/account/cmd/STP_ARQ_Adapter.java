package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.auth.STP_ACF;
import com.oct.ga.comm.cmd.auth.STP_ARQ;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

public class STP_ARQ_Adapter extends StpReqCommand {
	public STP_ARQ_Adapter() {
		super();

		this.setTag(Command.STP_ARQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv) throws UnsupportedEncodingException {
		reqCmd = new STP_ARQ().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context) throws Exception {
		String deviceId = reqCmd.getDeviceId();
		String sessionTicket = reqCmd.getSessionToken();
		STP_ACF respCmd = null;

		try {
			SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");

			StpSession stpSession = supSessionService.activeStpSession(sessionTicket, session.getId());
			if (stpSession != null) {
				String accountId = stpSession.getAccountId();

				if (stpSession.getExpiryTime() > currentTimestamp) {
					if (deviceId.equals(stpSession.getDeviceId())) {
						AccountBasic account = supAccountService.queryAccount(accountId);
						session.setAttribute("deviceId", deviceId);
						session.setAttribute("accountId", accountId);
						session.setAttribute("accountName", account.getNickname());
						session.setAttribute("avatarUrl", account.getAvatarUrl());

						logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=["
								+ accountId + "]|commandTag=[" + this.getTag() + "]|nickname=[" + account.getNickname()
								+ "]|osVersion=[" + stpSession.getDeviceOsVersion() + "]| StpARQ success");

						respCmd = new STP_ACF(ErrorCode.SUCCESS);
					} else {
						logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=["
								+ accountId + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
								+ ErrorCode.STP_ARQ_NOT_YOUR_SESSION + "]|SessionTicket=[" + sessionTicket
								+ "] not equal deviceId=[" + stpSession.getDeviceId() + "] in stpSession");

						respCmd = new STP_ACF(ErrorCode.STP_ARQ_NOT_YOUR_SESSION);
					}
				} else {
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + null
							+ "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.STP_ARQ_SESSION_TIMEOUT
							+ "]| session time out, expiryTime=[" + stpSession.getExpiryTime() + "]");

					respCmd = new STP_ACF(ErrorCode.STP_ARQ_SESSION_TIMEOUT);
					respCmd.setSequence(sequence);
					return respCmd;
				}
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + null
						+ "|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.STP_ARQ_NULL_SESSION
						+ "]| has no gaSession");

				respCmd = new STP_ACF(ErrorCode.STP_ARQ_NULL_SESSION);
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + null
					+ "|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new STP_ACF(ErrorCode.UNKNOWN_FAILURE);
		}
		respCmd.setSequence(sequence);
		return respCmd;
	}

	private STP_ARQ reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(STP_ARQ_Adapter.class);
}
