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
import com.redoct.ga.sup.session.cmd.ApplyGateTokenReq;
import com.redoct.ga.sup.session.cmd.ApplyGateTokenResp;
import com.redoct.ga.sup.session.domain.GateSession;

public class ApplyGateTokenAdapter
		extends SupReqCommand
{
	public ApplyGateTokenAdapter()
	{
		super();

		this.setTag(SupCommandTag.APPLY_GATE_TOKEN_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ApplyGateTokenReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		GateSession gateSession = reqCmd.getGateSession();
		ApplyGateTokenResp respCmd = null;

		try {
			SessionCacheManager sessionCacheManager = GenericSingleton.getInstance(SessionCacheManager.class);

			String gateToken = UUID.randomUUID().toString();
			gateSession.setGateToken(gateToken);
			sessionCacheManager.putGateSession(gateSession.getDeviceId(), gateSession);
			logger.info("put gateSesssion=[" + gateSession.getDeviceId() + "," + gateSession.getGateToken() + "] into memcached");

			respCmd = new ApplyGateTokenResp(this.getSequence(), ErrorCode.SUCCESS, gateToken);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ApplyGateTokenResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ApplyGateTokenReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ApplyGateTokenAdapter.class);

}
