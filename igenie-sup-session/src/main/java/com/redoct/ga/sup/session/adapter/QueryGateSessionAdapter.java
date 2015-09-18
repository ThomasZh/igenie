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
import com.redoct.ga.sup.session.cmd.QueryGateSessionReq;
import com.redoct.ga.sup.session.cmd.QueryGateSessionResp;
import com.redoct.ga.sup.session.domain.GateSession;

public class QueryGateSessionAdapter
		extends SupReqCommand
{
	public QueryGateSessionAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_GATE_SESSION_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryGateSessionReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String deviceId = reqCmd.getDeviceId();
		QueryGateSessionResp respCmd = null;

		try {
			SessionCacheManager sessionCacheManager = GenericSingleton.getInstance(SessionCacheManager.class);

			logger.debug("deviceId: " + deviceId);
			GateSession gateSession = sessionCacheManager.getGateSession(deviceId);
			if (gateSession != null) {
				logger.debug("deviceId: " + gateSession.getDeviceId());
				logger.debug("gateToken: " + gateSession.getGateToken());
				logger.debug("stpId: " + gateSession.getStpId());
				logger.debug("stpIp: " + gateSession.getStpIp());
				logger.debug("stpPort: " + gateSession.getStpPort());
			} else {
				logger.warn("can't get gateSession: " + gateSession);
			}

			respCmd = new QueryGateSessionResp(this.getSequence(), ErrorCode.SUCCESS, gateSession);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryGateSessionResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryGateSessionReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryGateSessionAdapter.class);

}
