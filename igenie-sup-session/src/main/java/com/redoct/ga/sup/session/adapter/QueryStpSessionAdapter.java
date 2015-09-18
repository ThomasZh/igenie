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
import com.redoct.ga.sup.session.cmd.QueryStpSessionReq;
import com.redoct.ga.sup.session.cmd.QueryStpSessionResp;
import com.redoct.ga.sup.session.domain.StpSession;

public class QueryStpSessionAdapter
		extends SupReqCommand
{
	public QueryStpSessionAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_STP_SESSION_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryStpSessionReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = reqCmd.getAccountId();
		QueryStpSessionResp respCmd = null;
		StpSession stpSession = null;

		try {
			SessionCacheManager sessionCacheManager = GenericSingleton.getInstance(SessionCacheManager.class);

			logger.debug("accountId: " + accountId);
			String sessionTicket = sessionCacheManager.getSessionTicket(accountId);
			if (sessionTicket == null) {
				logger.warn("can't get sessionTicket: " + sessionTicket);
			} else {
				logger.debug("sessionTicket: " + sessionTicket);
				stpSession = sessionCacheManager.getStpSession(sessionTicket);
				if (stpSession != null) {
					logger.debug("accountId: " + stpSession.getAccountId());
					logger.debug("deviceId: " + stpSession.getDeviceId());
					logger.debug("deviceOsVersion: " + stpSession.getDeviceOsVersion());
					logger.debug("expiryTime: " + stpSession.getExpiryTime());
					logger.debug("gateToken: " + stpSession.getGateToken());
					logger.debug("ioSessionId: " + stpSession.getIoSessionId());
					logger.debug("loginName: " + stpSession.getLoginName());
					logger.debug("loginType: " + stpSession.getLoginType());
					logger.debug("notifyToken: " + stpSession.getNotifyToken());
				} else {
					logger.warn("can't get stpSession: " + stpSession);
				}
			}

			respCmd = new QueryStpSessionResp(this.getSequence(), ErrorCode.SUCCESS, stpSession);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryStpSessionResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryStpSessionReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryStpSessionAdapter.class);

}
