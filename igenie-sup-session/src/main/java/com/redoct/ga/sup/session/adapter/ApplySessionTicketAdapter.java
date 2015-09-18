package com.redoct.ga.sup.session.adapter;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.session.SessionCacheManager;
import com.redoct.ga.sup.session.cmd.ApplySessionTicketReq;
import com.redoct.ga.sup.session.cmd.ApplySessionTicketResp;
import com.redoct.ga.sup.session.domain.StpSession;

public class ApplySessionTicketAdapter
		extends SupReqCommand
{
	public ApplySessionTicketAdapter()
	{
		super();

		this.setTag(SupCommandTag.APPLY_SESSION_TICKET_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ApplySessionTicketReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		StpSession stpSession = reqCmd.getStpSession();
		String accountId = stpSession.getAccountId();
		String sessionTicket = null;
		ApplySessionTicketResp respCmd = null;

		try {
			SessionCacheManager sessionCacheManager = GenericSingleton.getInstance(SessionCacheManager.class);

			String oldSessionTicket = sessionCacheManager.getSessionTicket(accountId);
			if (oldSessionTicket == null) {
				sessionTicket = UUID.randomUUID().toString();
				// expiry time is 1 week
				stpSession.setExpiryTime(this.getCurrentTimestamp() + 1209600);

				sessionCacheManager.putSessionTicket(accountId, sessionTicket);
				sessionCacheManager.putStpSession(sessionTicket, stpSession);

				respCmd = new ApplySessionTicketResp(this.getSequence(), ErrorCode.SUCCESS, sessionTicket);
				return respCmd;
			} else {
				StpSession oldStpSession = sessionCacheManager.getStpSession(oldSessionTicket);

				if (oldStpSession == null) {
					sessionTicket = UUID.randomUUID().toString();
					// expiry time is 1 week
					stpSession.setExpiryTime(this.getCurrentTimestamp() + 1209600);

					sessionCacheManager.putSessionTicket(accountId, sessionTicket);
					sessionCacheManager.putStpSession(sessionTicket, stpSession);
				} else {
					if (oldStpSession.getExpiryTime() > this.getCurrentTimestamp()) {
						sessionTicket = oldSessionTicket;

						sessionCacheManager.putStpSession(sessionTicket, stpSession);
					} else {
						sessionTicket = UUID.randomUUID().toString();
						// expiry time is 1 week
						stpSession.setExpiryTime(this.getCurrentTimestamp() + 1209600);

						sessionCacheManager.removeStpSession(oldSessionTicket);

						sessionCacheManager.putSessionTicket(accountId, sessionTicket);
						sessionCacheManager.putStpSession(sessionTicket, stpSession);
					}
				}

				respCmd = new ApplySessionTicketResp(this.getSequence(), ErrorCode.SUCCESS, sessionTicket);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ApplySessionTicketResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ApplySessionTicketReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ApplySessionTicketAdapter.class);

}
