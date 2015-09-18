package com.oct.ga.gatekeeper.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.cmd.gatekeeper.GK_ACF;
import com.oct.ga.comm.cmd.gatekeeper.GK_ARQ;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.gatekeeper.ServerListCache;
import com.oct.ga.session.GaSessionInfo;
import com.oct.ga.session.SessionService3MapImpl;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.GateSession;

//deviceId,appId,vendorId
public class GK_ARQ_Adapter
		extends StpReqCommand
{
	public GK_ARQ_Adapter()
	{
		super();

		this.setTag(Command.GK_ARQ);
	}

	@Override
	public StpCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new GK_ARQ().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		SessionService3MapImpl sessionService = GenericSingleton.getInstance(SessionService3MapImpl.class);
		ServerListCache serverListCache = GenericSingleton.getInstance(ServerListCache.class);

		String deviceId = reqCmd.getDeviceId();
		String appId = reqCmd.getAppId();
		String vendorId = reqCmd.getVendorId();
		String clientVersion = reqCmd.getVersion();
		String gateToken = null;
		String stpId = null;
		String stpIp = null;
		int port = 0;
		GK_ACF respCmd = null;

		try {
			SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");
			SupDeviceService supDeviceService = (SupDeviceService) context.getBean("supDeviceService");

			// register device info into igenie system
			supDeviceService.modifyClientVersion(deviceId, clientVersion, appId, vendorId, currentTimestamp);

			StpServerInfoJsonBean stpServerInfo = serverListCache.next(clientVersion);
			if (stpServerInfo != null) {
				stpId = stpServerInfo.getStpId();
				stpIp = stpServerInfo.getServerIp();
				port = stpServerInfo.getPort();

				GateSession gateSession = new GateSession();
				gateSession.setDeviceId(deviceId);
				gateSession.setStpId(stpId);
				gateSession.setStpIp(stpIp);
				gateSession.setStpPort(port);
				gateToken = supSessionService.applyGateToken(gateSession);

				// Save deviceId(key),appId,vendorId,version,sessionToken into
				// memcached.
				GaSessionInfo gaSession = sessionService.getSession(deviceId);
				if (gaSession != null) {
					gaSession.setStpId(stpId);
					gaSession.setGateToken(gateToken);
					gaSession.setClientVersion(clientVersion);
				} else {
					gaSession = new GaSessionInfo();
					gaSession.setGateToken(gateToken);
					gaSession.setStpId(stpId);
					gaSession.setClientVersion(clientVersion);
				}
				sessionService.putSession(deviceId, gaSession);

				logger.info("sessionId=[" + session.getId() + "]deviceId=[" + deviceId + "]|commandTag=["
						+ this.getTag() + "]|appId=[" + appId + "]|verdorId=[" + vendorId + "]|clientVersion=["
						+ clientVersion + "]|redirect to stp=[" + stpIp + ":" + port + "]|gateToken=[" + gateToken
						+ "]");

				respCmd = new GK_ACF(ErrorCode.SUCCESS, gateToken, stpIp, port);
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|commandTag=["
						+ this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|from ip=[" + stpIp
						+ "]|port=[" + port + "]|Not enough (active) stp to dispatch!");

				respCmd = new GK_ACF(ErrorCode.GK_ARQ_NOT_ENOUGH_STP, gateToken, stpIp, port);
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[]|commandTag=[" + this.getTag()
					+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|from ip=[" + stpIp + "]|port=[" + port + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new GK_ACF(ErrorCode.UNKNOWN_FAILURE, gateToken, stpIp, port);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private GK_ARQ reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(GK_ARQ_Adapter.class);
}
