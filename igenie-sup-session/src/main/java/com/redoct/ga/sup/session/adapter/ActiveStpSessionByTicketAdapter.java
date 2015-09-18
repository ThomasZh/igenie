package com.redoct.ga.sup.session.adapter;

import java.io.UnsupportedEncodingException;

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
import com.redoct.ga.sup.session.cmd.ActiveStpSessionByTicketReq;
import com.redoct.ga.sup.session.cmd.ActiveStpSessionByTicketResp;
import com.redoct.ga.sup.session.domain.StpSession;

public class ActiveStpSessionByTicketAdapter
		extends SupReqCommand
{
	public ActiveStpSessionByTicketAdapter()
	{
		super();

		this.setTag(SupCommandTag.ACTIVE_STP_SESSION_BY_TICKET_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActiveStpSessionByTicketReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String sessionTicket = reqCmd.getSessionTicket();
		long ioSessionId = reqCmd.getIoSessionId();
		ActiveStpSessionByTicketResp respCmd = null;

		try {
			SessionCacheManager sessionCacheManager = GenericSingleton.getInstance(SessionCacheManager.class);

			StpSession stpSession = sessionCacheManager.getStpSession(sessionTicket);
			stpSession.setActive(true);
			stpSession.setIoSessionId(ioSessionId);
			// expiry time is 1 week
			stpSession.setExpiryTime(this.getCurrentTimestamp() + 1209600);
			sessionCacheManager.putStpSession(sessionTicket, stpSession);

			respCmd = new ActiveStpSessionByTicketResp(this.getSequence(), ErrorCode.SUCCESS, stpSession);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ActiveStpSessionByTicketResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ActiveStpSessionByTicketReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActiveStpSessionByTicketAdapter.class);

}
