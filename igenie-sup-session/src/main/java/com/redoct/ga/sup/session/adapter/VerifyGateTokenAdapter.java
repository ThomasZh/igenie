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
import com.redoct.ga.sup.session.cmd.VerifyGateTokenReq;
import com.redoct.ga.sup.session.cmd.VerifyGateTokenResp;
import com.redoct.ga.sup.session.domain.GateSession;

public class VerifyGateTokenAdapter
		extends SupReqCommand
{
	public VerifyGateTokenAdapter()
	{
		super();

		this.setTag(SupCommandTag.VERIFY_GATE_TOKEN_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new VerifyGateTokenReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String deviceId = reqCmd.getDeviceId();
		String gateToken = reqCmd.getGateToken();
		VerifyGateTokenResp respCmd = null;

		try {
			SessionCacheManager sessionCacheManager = GenericSingleton.getInstance(SessionCacheManager.class);

			GateSession gateSession = sessionCacheManager.getGateSession(deviceId);
			logger.debug("get gateSesssion=[" + deviceId + "," + gateSession.getGateToken() + "] from memcached");
			if (gateToken.equals(gateSession.getGateToken())) {
				respCmd = new VerifyGateTokenResp(this.getSequence(), ErrorCode.SUCCESS);
				return respCmd;
			} else {
				respCmd = new VerifyGateTokenResp(this.getSequence(), ErrorCode.GATE_TOKEN_NOT_ON_DEVICE);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new VerifyGateTokenResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private VerifyGateTokenReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(VerifyGateTokenAdapter.class);

}
