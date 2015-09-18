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
import com.redoct.ga.sup.session.cmd.InactiveStpSessionReq;
import com.redoct.ga.sup.session.cmd.InactiveStpSessionResp;
import com.redoct.ga.sup.session.domain.StpSession;

public class InactiveStpSessionAdapter
		extends SupReqCommand
{
	public InactiveStpSessionAdapter()
	{
		super();

		this.setTag(SupCommandTag.INACTIVE_STP_SESSION_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new InactiveStpSessionReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = reqCmd.getAccountId();
		InactiveStpSessionResp respCmd = null;

		try {
			SessionCacheManager sessionCacheManager = GenericSingleton.getInstance(SessionCacheManager.class);

			String sessionTicket = sessionCacheManager.getSessionTicket(accountId);
			if (sessionTicket != null) {
				StpSession stpSession = sessionCacheManager.getStpSession(sessionTicket);
				if (stpSession != null) {
					stpSession.setActive(false);
					sessionCacheManager.putStpSession(sessionTicket, stpSession);
				}
			}

			respCmd = new InactiveStpSessionResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new InactiveStpSessionResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private InactiveStpSessionReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(InactiveStpSessionAdapter.class);

}
